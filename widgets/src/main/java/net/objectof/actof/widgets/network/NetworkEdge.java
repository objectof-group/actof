package net.objectof.actof.widgets.network;


import javafx.scene.Node;


public interface NetworkEdge<T extends Node> {

    T from();

    T to();

}
