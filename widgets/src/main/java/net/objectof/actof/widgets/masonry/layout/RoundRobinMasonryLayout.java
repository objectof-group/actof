package net.objectof.actof.widgets.masonry.layout;


public class RoundRobinMasonryLayout implements MasonryLayout {

    @Override
    public int pickColumn(double[] heights, int lastColumn) {
        int selectedColumn = lastColumn + 1;
        if (selectedColumn >= heights.length) {
            selectedColumn = 0;
        }
        return selectedColumn;
    }

    @Override
    public double clearance(double[] heights, double lastClearance, int column) {
        return 0;
    }

}
