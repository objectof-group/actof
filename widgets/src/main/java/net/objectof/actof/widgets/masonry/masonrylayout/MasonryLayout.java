package net.objectof.actof.widgets.masonry.masonrylayout;


public interface MasonryLayout {

    int pickColumn(double heights[], int lastColumn);

    double clearance(double heights[], double lastClearance, int column);

}