package net.objectof.actof.widgets.masonry.layout;


public interface MasonryLayout {

    int pickColumn(double heights[], int lastColumn);

    double clearance(double heights[], double lastClearance, int column);

}
