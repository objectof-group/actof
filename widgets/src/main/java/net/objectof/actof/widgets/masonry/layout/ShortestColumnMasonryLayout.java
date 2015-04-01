package net.objectof.actof.widgets.masonry.layout;


public class ShortestColumnMasonryLayout implements MasonryLayout {

    @Override
    public int pickColumn(double[] heights, int lastColumn) {
        int selectedColumn = 0;
        for (int i = 1; i < heights.length; i++) {
            if (heights[i] < heights[selectedColumn]) {
                selectedColumn = i;
            }
        }
        return selectedColumn;
    }

    @Override
    public double clearance(double[] heights, double lastClearance, int column) {
        return 0;
    }
}
