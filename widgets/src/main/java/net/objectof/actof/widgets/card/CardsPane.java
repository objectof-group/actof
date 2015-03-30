package net.objectof.actof.widgets.card;


import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;


public class CardsPane extends Pane {

    public enum Layout {
        ROUND_ROBIN {

            public int pickColumn(double heights[], int lastColumn) {
                int selectedColumn = lastColumn + 1;
                if (selectedColumn >= heights.length) {
                    selectedColumn = 0;
                }
                return selectedColumn;
            }
        },

        SHORTEST_COLUMN {

            public int pickColumn(double heights[], int lastColumn) {
                int selectedColumn = 0;
                for (int i = 1; i < heights.length; i++) {
                    if (heights[i] < heights[selectedColumn]) {
                        selectedColumn = i;
                    }
                }
                return selectedColumn;
            }
        };

        public int pickColumn(double heights[], int lastColumn) {
            return 0;
        }
    }

    private double columnWidth = 400;
    private boolean performingLayout = false;

    private SimpleObjectProperty<Layout> layout = new SimpleObjectProperty<CardsPane.Layout>(Layout.SHORTEST_COLUMN);
    private SimpleDoubleProperty nodeSpacing = new SimpleDoubleProperty(0);

    public CardsPane(double columnWidth) {
        this.columnWidth = columnWidth;
        nodeSpacing.addListener((Observable change) -> requestLayout());
    }

    public void setSpacing(double spacing) {
        nodeSpacing.set(spacing);
        requestLayout();
    }

    public double getSpacing() {
        return nodeSpacing.get();
    }

    public DoubleProperty spacingProperty() {
        return nodeSpacing;
    }

    public void setLayout(Layout layout) {
        this.layout.set(layout);
    }

    public Layout getLayout() {
        return layout.get();
    }

    public ObjectProperty<Layout> layoutProperty() {
        return layout;
    }

    protected void layoutChildren() {

        performingLayout = true;

        double heights[] = new double[columnCount()];

        int lastColumn = -1; // round robin will increments
        for (Node node : getManagedChildren()) {
            int selectedColumn = layout.get().pickColumn(heights, lastColumn);
            lastColumn = selectedColumn;

            double height = nodePrefHeight(node, -1, VBox.getMargin(node), realColumnWidth());

            node.resize(snapSpace(realColumnWidth()), snapSpace(height));
            node.relocate(xForColumn(selectedColumn), yForNode(selectedColumn, heights));

            if (heights[selectedColumn] > 0) {
                heights[selectedColumn] = (int) (heights[selectedColumn] + snapSpace(nodeSpacing.get() + height));
            } else {
                heights[selectedColumn] = (int) (heights[selectedColumn] + snapSpace(+height));
            }
        }

        performingLayout = false;

    }

    private int columnCount() {
        int columnCount = (int) Math.floor(getWidth() / columnWidth);

        // always at least 1 column
        columnCount = Math.max(columnCount, 1);

        // only as many columns as nodes
        // columnCount = Math.min(columnCount, getManagedChildren().size());

        return columnCount;
    }

    private double realColumnWidth() {
        int count = columnCount();
        double spacing = nodeSpacing.get() * (count - 1);
        double insets = getInsets().getLeft() + getInsets().getRight();
        double available = getWidth() - insets - spacing;
        return available / (double) count;
    }

    private double xForColumn(int column) {
        double inset = getInsets().getLeft();
        double columnOffset = realColumnWidth() + nodeSpacing.get();
        return snapSize(inset + column * columnOffset);
    }

    private double yForNode(int column, double height[]) {
        double spacingHeight = height[column] == 0 ? 0 : nodeSpacing.get();
        return snapSpace(getInsets().getTop() + spacingHeight + height[column]);
    }

    @Override
    public Orientation getContentBias() {
        return Orientation.HORIZONTAL;
    }

    @Override
    protected double computeMinWidth(double height) {
        return computePrefWidth(height);
    }

    @Override
    protected double computeMinHeight(double width) {
        return computePrefHeight(width);
    }

    @Override
    protected double computePrefWidth(double height) {
        Insets insets = getInsets();
        return snapSpace(insets.getLeft()) + columnWidth + snapSpace(insets.getRight());
    }

    @Override
    protected double computePrefHeight(double width) {
        Insets insets = getInsets();

        int columnCount = (int) Math.floor(width / columnWidth);
        columnCount = Math.max(columnCount, 1);
        double heights[] = new double[columnCount];
        double realColumnWidth = width / columnCount;

        int lastColumn = -1; // round robin will increments
        for (Node node : getManagedChildren()) {
            int selectedColumn = layout.get().pickColumn(heights, lastColumn);
            lastColumn = selectedColumn;

            double height = nodePrefHeight(node, -1, VBox.getMargin(node), realColumnWidth);

            if (heights[selectedColumn] > 0) {
                heights[selectedColumn] = (int) (heights[selectedColumn] + snapSpace(nodeSpacing.get() + height));
            } else {
                heights[selectedColumn] = (int) (heights[selectedColumn] + snapSpace(+height));
            }
        }

        double height = 0;
        for (int i = 0; i < heights.length; i++) {
            height = Math.max(height, heights[i]);
        }

        return snapSpace(insets.getTop() + snapSpace(height) + insets.getBottom());
    }

    @Override
    public void requestLayout() {
        if (performingLayout) { return; }
        super.requestLayout();
    }

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

    private static double boundedSize(double min, double pref, double max) {
        double a = pref >= min ? pref : min;
        double b = min >= max ? min : max;
        return a <= b ? a : b;
    }

}
