package net.objectof.actof.widgets.network;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.NumberBinding;
import javafx.beans.binding.When;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.collections.SetChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;


public class NetworkPane extends Pane {

    private boolean performingLayout = false;
    private Map<NetworkVertex, SetChangeListener<NetworkEdge>> edgeListeners = new HashMap<>();

    private ObservableList<NetworkVertex> vertices = FXCollections.observableArrayList();

    public NetworkPane() {
        setStyle("-fx-background-color: #ffffff;");
        vertices.addListener(this::verticesChanged);
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

            if (fxNodes.contains(vertex.getFXNode())) {

                Node fxNode = vertex.getFXNode();
                // calculate position/size of node
                double height = nodePrefHeight(fxNode);
                double width = nodePrefWidth(fxNode);

                // position the node
                fxNode.resize(snapSize(width), snapSize(height));

                // bind the node layout position to the Coordinate returned by
                // the position function
                fxNode.layoutXProperty().bind(
                        vertex.xProperty().add(getInsets().getLeft()).subtract(fxNode.getLayoutBounds().getMinX()));
                fxNode.layoutYProperty().bind(
                        vertex.yProperty().add(getInsets().getLeft()).subtract(fxNode.getLayoutBounds().getMinY()));

                // remove this node from the list of nodes remaining to lay out.
                fxNodes.remove(fxNode);
            }

        }

        for (Node fxNode : fxNodes) {
            fxNode.setTranslateX(getInsets().getLeft());
            fxNode.setTranslateY(getInsets().getTop());
        }

        performingLayout = false;

    }

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
            Path line = createReferenceLine(e);
            getChildren().add(line);
        }

        // add nodes
        for (NetworkVertex v : getVertices()) {
            getChildren().add(v.getFXNode());
        }

    }

    public Path createReferenceLine(NetworkEdge edge) {

        NetworkVertex vSource, vDest;
        vSource = edge.sourceVertex();
        vDest = edge.destVertex();

        Path path = new Path();
        MoveTo move = new MoveTo();
        move.xProperty().bind(vSource.xProperty().add(edge.sourceOffsetXProperty()));
        move.yProperty().bind(vSource.yProperty().add(edge.sourceOffsetYProperty()));
        path.getElements().add(move);

        // actual x/y positions for source and destination of line based on node
        // position and line offset
        DoubleBinding destX = vDest.xProperty().add(edge.destOffsetXProperty());
        DoubleBinding destY = vDest.yProperty().add(edge.destOffsetYProperty());
        DoubleBinding sourceX = vSource.xProperty().add(edge.sourceOffsetXProperty());
        DoubleBinding sourceY = vSource.yProperty().add(edge.sourceOffsetYProperty());

        // calculate total slope to determine how to build curve
        DoubleBinding dx = destX.subtract(sourceX);
        DoubleBinding dy = destY.subtract(sourceY);
        DoubleBinding slope = dx.divide(dy);
        DoubleBinding invslope = dy.divide(dx);

        DoubleProperty zero = new SimpleDoubleProperty(0);
        DoubleProperty one = new SimpleDoubleProperty(1);

        // is the curve/line more horizontal or vertical
        BooleanBinding vertical = slope.greaterThan(1).or(slope.lessThan(-1));

        // scaling function for control point coords which is maximized at 1.5
        // at 0/90 degrees and minimized at 0 at 45
        NumberBinding scale = new When(vertical).then(invslope).otherwise(slope);
        scale = new When(scale.lessThan(0)).then(zero.subtract(scale)).otherwise(scale);
        scale = one.subtract(scale);
        scale = scale.multiply(1.5);

        // control point positions using boolean binding to change value based
        // on value of variable 'vertical'. We apply a non-zero control point
        // coordinate to the dominant axis
        NumberBinding cx1 = new When(vertical).then(sourceX).otherwise(sourceX.add(dx.multiply(scale)));
        NumberBinding cx2 = new When(vertical).then(destX).otherwise(destX.subtract(dx.multiply(scale)));
        NumberBinding cy1 = new When(vertical).then(sourceY.add(dy.multiply(scale))).otherwise(sourceY);
        NumberBinding cy2 = new When(vertical).then(destY.subtract(dy.multiply(scale))).otherwise(destY);

        CubicCurveTo curve = new CubicCurveTo();
        curve.controlX1Property().bind(cx1);
        curve.controlY1Property().bind(cy1);
        curve.controlX2Property().bind(cx2);
        curve.controlY2Property().bind(cy2);

        // set end point of curve
        curve.xProperty().bind(vDest.xProperty().add(edge.destOffsetXProperty()));
        curve.yProperty().bind(vDest.yProperty().add(edge.destOffsetYProperty()));
        path.getElements().add(curve);

        path.strokeWidthProperty().bind(edge.widthProperty());
        path.strokeProperty().bind(edge.colorProperty());
        return path;
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
