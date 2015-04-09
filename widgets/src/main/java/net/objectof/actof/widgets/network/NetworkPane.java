package net.objectof.actof.widgets.network;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.collections.SetChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Path;
import net.objectof.actof.widgets.network.edgestyles.LineEdgeStyle;


public class NetworkPane extends Pane {

    public interface EdgeStyle {

        Path createReferenceLine(NetworkEdge edge);
    }

    private ObjectProperty<EdgeStyle> edgeStyle = new SimpleObjectProperty<>(new LineEdgeStyle());
    private boolean performingLayout = false;
    private Map<NetworkVertex, SetChangeListener<NetworkEdge>> edgeListeners = new HashMap<>();

    private ObservableList<NetworkVertex> vertices = FXCollections.observableArrayList();

    public NetworkPane() {
        setStyle("-fx-background-color: #ffffff;");
        vertices.addListener(this::verticesChanged);
        edgeStyle.addListener(change -> {
            regenerateChildren();
            requestLayout();
        });
        regenerateChildren();
    }

    private void verticesChanged(Change<? extends NetworkVertex> c) {

        regenerateChildren();
        requestLayout();

        while (c.next()) {
            if (!c.wasAdded() && !c.wasRemoved()) {
                continue;
            }

            for (NetworkVertex v : c.getAddedSubList()) {
                SetChangeListener<NetworkEdge> listener = this::edgesChanged;
                v.getEdges().addListener(listener);
                edgeListeners.put(v, listener);
            }

            for (NetworkVertex v : c.getRemoved()) {
                v.getEdges().removeListener(edgeListeners.remove(v));
            }
        }
    }

    private void edgesChanged(SetChangeListener.Change<? extends NetworkEdge> change) {
        regenerateChildren();
        requestLayout();
    }

    public ObservableList<NetworkVertex> getVertices() {
        return vertices;
    }

    public final ObjectProperty<EdgeStyle> edgeStyleProperty() {
        return this.edgeStyle;
    }

    public final EdgeStyle getEdgeStyle() {
        return this.edgeStyleProperty().get();
    }

    public final void setEdgeStyle(final EdgeStyle edgeStyle) {
        this.edgeStyleProperty().set(edgeStyle);
    }

    @Override
    public void requestLayout() {
        if (performingLayout) { return; }
        super.requestLayout();
    }

    @Override
    protected void layoutChildren() {

        performingLayout = true;

        // make a copy of all managed children, removing them if they match a
        // vertex's node. Any remaining must be connector lines
        List<Node> fxNodes = new ArrayList<>(getManagedChildren());

        for (NetworkVertex vertex : getVertices()) {

            if (!fxNodes.contains(vertex.getFXNode())) {
                continue;
            }

            Node fxNode = vertex.getFXNode();
            // calculate position/size of node
            double height = nodePrefHeight(fxNode);
            double width = nodePrefWidth(fxNode);

            // position the node
            fxNode.resize(snapSize(width), snapSize(height));

            // bind the node layout position to the Coordinate returned by
            // the position function
            NumberBinding px = vertex.xProperty().add(getInsets().getLeft());
            NumberBinding py = vertex.yProperty().add(getInsets().getLeft());

            px = px.subtract(fxNode.getLayoutBounds().getMinX());
            py = py.subtract(fxNode.getLayoutBounds().getMinY());

            px = Bindings.min(Bindings.max(0, px), widthProperty());
            py = Bindings.min(Bindings.max(0, py), heightProperty());

            fxNode.layoutXProperty().bind(px);
            fxNode.layoutYProperty().bind(py);

            // remove this node from the list of nodes remaining to lay out.
            fxNodes.remove(fxNode);

        }

        for (Node fxNode : fxNodes) {
            fxNode.setTranslateX(getInsets().getLeft());
            fxNode.setTranslateY(getInsets().getTop());
        }

        performingLayout = false;

    }

    // scrub the list of children and rebuild it. This includes adding the nodes
    // for all vertices, as well an generating all lines representing edges
    private void regenerateChildren() {
        getChildren().clear();

        // build a list of all edges so that undirected graphs don't repeat
        // themselves
        Set<NetworkEdge> allEdges = new HashSet<>();
        for (NetworkVertex v : vertices) {
            allEdges.addAll(v.getEdges());
        }

        // create + add lines
        // lines.getChildren().clear();
        for (NetworkEdge e : allEdges) {
            Path line = getEdgeStyle().createReferenceLine(e);
            getChildren().add(line);
        }

        // add nodes
        for (NetworkVertex v : getVertices()) {
            getChildren().add(v.getFXNode());
        }

    }

    private double nodePrefHeight(Node child) {
        return nodePrefHeight(child, -1, VBox.getMargin(child), getWidth());
    }

    private double nodePrefWidth(Node child) {
        return nodePrefHeight(child, -1, VBox.getMargin(child), getWidth());
    }

    // borrowed from Region computeChildPrefAreaHeight
    private double nodePrefHeight(Node child, double prefBaselineComplement, Insets margin, double width) {
        double top = margin != null ? snapSpace(margin.getTop()) : 0;
        double bottom = margin != null ? snapSpace(margin.getBottom()) : 0;

        double alt = -1;
        if (child.isResizable() && child.getContentBias() == Orientation.HORIZONTAL) { // height
                                                                                       // depends
                                                                                       // on
                                                                                       // width
            double left = margin != null ? snapSpace(margin.getLeft()) : 0;
            double right = margin != null ? snapSpace(margin.getRight()) : 0;
            alt = snapSize(boundedSize(child.minWidth(-1), width != -1 ? width - left - right : child.prefWidth(-1),
                    child.maxWidth(-1)));
        }

        if (prefBaselineComplement != -1) {
            double baseline = child.getBaselineOffset();
            if (child.isResizable() && baseline == BASELINE_OFFSET_SAME_AS_HEIGHT) {
                // When baseline is same as height, the preferred height of the
                // node will be above the baseline, so we need to add
                // the preferred complement to it
                return top + snapSize(boundedSize(child.minHeight(alt), child.prefHeight(alt), child.maxHeight(alt)))
                        + bottom + prefBaselineComplement;
            } else {
                // For all other Nodes, it's just their baseline and the
                // complement.
                // Note that the complement already contain the Node's preferred
                // (or fixed) height
                return top + baseline + prefBaselineComplement + bottom;
            }
        } else {
            return top + snapSize(boundedSize(child.minHeight(alt), child.prefHeight(alt), child.maxHeight(alt)))
                    + bottom;
        }
    }

    double nodePrefWidth(Node child, double baselineComplement, Insets margin, double height, boolean fillHeight) {
        double left = margin != null ? snapSpace(margin.getLeft()) : 0;
        double right = margin != null ? snapSpace(margin.getRight()) : 0;
        double alt = -1;
        if (height != -1 && child.isResizable() && child.getContentBias() == Orientation.VERTICAL) { // width
                                                                                                     // depends
                                                                                                     // on
                                                                                                     // height
            double top = margin != null ? snapSpace(margin.getTop()) : 0;
            double bottom = margin != null ? snapSpace(margin.getBottom()) : 0;
            double bo = child.getBaselineOffset();
            final double contentHeight = bo == BASELINE_OFFSET_SAME_AS_HEIGHT && baselineComplement != -1 ? height
                    - top - bottom - baselineComplement : height - top - bottom;
            if (fillHeight) {
                alt = snapSize(boundedSize(child.minHeight(-1), contentHeight, child.maxHeight(-1)));
            } else {
                alt = snapSize(boundedSize(child.minHeight(-1), child.prefHeight(-1),
                        Math.min(child.maxHeight(-1), contentHeight)));
            }
        }
        return left + snapSize(boundedSize(child.minWidth(alt), child.prefWidth(alt), child.maxWidth(alt))) + right;
    }

    // Borrowed from Region
    private static double boundedSize(double min, double pref, double max) {
        double a = pref >= min ? pref : min;
        double b = min >= max ? min : max;
        return a <= b ? a : b;
    }

}
