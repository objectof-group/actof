package net.objectof.actof.widgets.network;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.NumberBinding;
import javafx.beans.binding.When;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;


public class NetworkPane extends Pane {

    private boolean performingLayout = false;

    private ObservableList<NetworkVertex> vertices = FXCollections.observableArrayList();

    public NetworkPane() {
        setStyle("-fx-background-color: #ffffff;");
        vertices.addListener(new InvalidationListener() {

            @Override
            public void invalidated(Observable observable) {
                regenerateChildren();
                requestLayout();
            }
        });
        regenerateChildren();
    }

    public ObservableList<NetworkVertex> getVertices() {
        return vertices;
    }

    @Override
    public void requestLayout() {
        if (performingLayout) { return; }
        super.requestLayout();
    }

    public void add(NetworkVertex vertex) {
        vertices.add(vertex);
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

        // LineTo curve = new LineTo();

        // double dx = centerTo.getX() - centerFrom.getX();
        // double dy = centerTo.getY() - centerFrom.getY();
        // double slope = dx / dy;
        DoubleBinding offsetDx = edge.destOffsetXProperty().subtract(edge.sourceOffsetXProperty());
        DoubleBinding offsetDy = edge.destOffsetYProperty().subtract(edge.sourceOffsetYProperty());
        DoubleBinding dx = vDest.xProperty().add(offsetDx).subtract(vSource.xProperty());
        DoubleBinding dy = vDest.yProperty().add(offsetDy).subtract(vSource.yProperty());
        DoubleBinding slope = dx.divide(dy);
        BooleanBinding vertical = slope.greaterThan(1).or(slope.lessThan(-1));

        NumberBinding cx1 = new When(vertical).then(vSource.xProperty()).otherwise(
                vSource.xProperty().add(dx.divide(3)));
        NumberBinding cx2 = new When(vertical).then(vDest.xProperty()).otherwise(
                vDest.xProperty().subtract(dx.divide(3)));

        NumberBinding cy1 = new When(vertical).then(vSource.yProperty().add(dy.divide(3))).otherwise(
                vSource.yProperty());
        NumberBinding cy2 = new When(vertical).then(vDest.yProperty().subtract(dy.divide(3))).otherwise(
                vDest.yProperty());

        CubicCurveTo curve = new CubicCurveTo();
        //
        curve.controlX1Property().bind(cx1);
        curve.controlY1Property().bind(cy1);
        curve.controlX2Property().bind(cx2);
        curve.controlY2Property().bind(cy2);

        //
        // if (slope.get() > 1) {
        // // more x than y
        // curve.setControlY1(from.getY() + dy.get() / 3);
        // curve.setControlY2(to.getY() - dy.get() / 3);
        // } else {
        // // more y than x
        // curve.setControlX1(from.getX() + dx.get() / 3);
        // curve.setControlX2(to.getX() - dx.get() / 3);
        // }

        curve.xProperty().bind(vDest.xProperty().add(edge.destOffsetXProperty()));
        curve.yProperty().bind(vDest.yProperty().add(edge.destOffsetYProperty()));
        path.getElements().add(curve);

        path.setStrokeWidth(2);
        path.setStroke(Color.web("#424242"));
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
