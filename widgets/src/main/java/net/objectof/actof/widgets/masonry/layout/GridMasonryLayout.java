package net.objectof.actof.widgets.masonry.layout;


public class GridMasonryLayout implements MasonryLayout {

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
        // only compute new clearance if we're starting a new row
        if (column == 0) {
            double height = 0;
            for (double h : heights) {
                height = Math.max(height, h);
            }
            return height;
        } else {
            return lastClearance;
        }
    }

}
