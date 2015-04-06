package net.objectof.actof.widgets.network.edgestyles;


import javafx.beans.binding.DoubleBinding;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import net.objectof.actof.widgets.network.NetworkEdge;
import net.objectof.actof.widgets.network.NetworkPane.EdgeStyle;
import net.objectof.actof.widgets.network.NetworkVertex;


public class LineEdgeStyle implements EdgeStyle {

    @Override
    public Path createReferenceLine(NetworkEdge edge) {

        NetworkVertex vSource, vDest;
        vSource = edge.sourceVertex();
        vDest = edge.destVertex();

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

        LineTo curve = new LineTo();
        curve.xProperty().bind(destX);
        curve.yProperty().bind(destY);
        path.getElements().add(curve);

        path.strokeWidthProperty().bind(edge.widthProperty());
        path.strokeProperty().bind(edge.colorProperty());
        return path;
    }

}
