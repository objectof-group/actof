package net.objectof.actof.widgets.network.edgestyles;


import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.NumberBinding;
import javafx.beans.binding.When;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import net.objectof.actof.widgets.network.NetworkEdge;
import net.objectof.actof.widgets.network.NetworkPane.EdgeStyle;
import net.objectof.actof.widgets.network.NetworkVertex;


public class CubicEdgeStyle implements EdgeStyle {

    public Path createReferenceLine(NetworkEdge edge) {

        NetworkVertex vSource, vDest;
        vSource = edge.getSourceVertex();
        vDest = edge.getDestinationVertex();
        if (vSource == null || vDest == null) { return null; }

        // actual x/y positions for source and destination of line based on node
        // position and line offset
        DoubleBinding destX = vDest.xProperty().add(edge.destOffsetXProperty());
        DoubleBinding destY = vDest.yProperty().add(edge.destOffsetYProperty());
        DoubleBinding sourceX = vSource.xProperty().add(edge.sourceOffsetXProperty());
        DoubleBinding sourceY = vSource.yProperty().add(edge.sourceOffsetYProperty());

        Path path = new Path();
        MoveTo move = new MoveTo();
        move.xProperty().bind(sourceX);
        move.yProperty().bind(sourceY);
        path.getElements().add(move);

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
        curve.xProperty().bind(destX);
        curve.yProperty().bind(destY);
        path.getElements().add(curve);

        path.strokeWidthProperty().bind(edge.widthProperty());
        path.strokeProperty().bind(edge.colorProperty());
        return path;
    }

}
