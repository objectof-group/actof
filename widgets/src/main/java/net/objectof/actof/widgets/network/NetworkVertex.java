package net.objectof.actof.widgets.network;


import java.util.Set;

import javafx.scene.Node;


public interface NetworkVertex {

    Coordinate getPosition();

    Set<NetworkEdge> getEdges();

    Node getFXNode();

}
