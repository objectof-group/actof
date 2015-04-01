package net.objectof.actof.widgets.masonry;


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
import net.objectof.actof.widgets.masonry.layout.GridMasonryLayout;
import net.objectof.actof.widgets.masonry.layout.MasonryLayout;
import net.objectof.actof.widgets.masonry.layout.RoundRobinMasonryLayout;
import net.objectof.actof.widgets.masonry.layout.ShortestColumnMasonryLayout;


public class MasonryPane extends Pane {

    /******************************************************
     * Static Layout Fields
     ******************************************************/

    public static final MasonryLayout LAYOUT_SHORTEST_COLUMN = new ShortestColumnMasonryLayout();
    public static final MasonryLayout LAYOUT_ROUND_ROBIN = new RoundRobinMasonryLayout();
    public static final MasonryLayout LAYOUT_GRID = new GridMasonryLayout();

    /******************************************************
     * Private Fields
     ******************************************************/
    private static final double DEFAULT_COLUMN_WIDTH = 400;
    private double columnWidth = DEFAULT_COLUMN_WIDTH;
    private boolean performingLayout = false;

    /******************************************************
     * Public Properties
     ******************************************************/

    private SimpleObjectProperty<MasonryLayout> layout = new SimpleObjectProperty<MasonryLayout>(LAYOUT_SHORTEST_COLUMN);

    public void setLayout(MasonryLayout layout) {
        this.layout.set(layout);
    }

    public MasonryLayout getLayout() {
        return layout.get();
    }

    public ObjectProperty<MasonryLayout> layoutProperty() {
        return layout;
    }

    private SimpleDoubleProperty nodeSpacing = new SimpleDoubleProperty(0);

    public void setSpacing(double spacing) {
        nodeSpacing.set(spacing);
    }

    public double getSpacing() {
        return nodeSpacing.get();
    }

    public DoubleProperty spacingProperty() {
        return nodeSpacing;
    }

    /******************************************************
     * Constructor
     ******************************************************/

    public MasonryPane() {
        this(DEFAULT_COLUMN_WIDTH);
    }

    public MasonryPane(double columnWidth) {
        this.columnWidth = columnWidth;
        nodeSpacing.addListener((Observable change) -> requestLayout());
        layout.addListener((Observable change) -> requestLayout());
    }

    /******************************************************
     * Override/Inherited methods
     ******************************************************/

    @Override
    protected void layoutChildren() {

        performingLayout = true;

        double heights[] = new double[columnCount(getWidth())];

        int lastColumn = -1; // round robin will increments
        double clearance = 0; // determines min y position, useful for grid
        int selectedColumn = 0;
        for (Node node : getManagedChildren()) {
            // figure out which column this node should go into, and what the
            // clearance for this row is
            selectedColumn = layout.get().pickColumn(heights, lastColumn);
            clearance = layout.get().clearance(heights, clearance, selectedColumn);
            lastColumn = selectedColumn;

            // calculate position/size of node
            double width = realColumnWidth(getWidth());
            double height = nodePrefHeight(node, -1, VBox.getMargin(node), realColumnWidth(getWidth()));
            double y = yForNode(selectedColumn, heights, clearance);
            double x = xForColumn(selectedColumn);

            // position the node
            node.resize(snapSize(width), snapSize(height));
            node.relocate(snapSpace(x), snapSpace(getInsets().getTop() + y));

            // update the height value for this column
            heights[selectedColumn] = y + height;
        }

        performingLayout = false;

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

        double heights[] = new double[columnCount(width)];

        int lastColumn = -1; // round robin will increments
        double clearance = 0; // determines min y position, useful for grid
        int selectedColumn = 0;
        for (Node node : getManagedChildren()) {
            // figure out which column this node should go into, and what the
            // clearance for this row is
            selectedColumn = layout.get().pickColumn(heights, lastColumn);
            clearance = layout.get().clearance(heights, clearance, selectedColumn);
            lastColumn = selectedColumn;

            // calculate position/size of node (w/o x/width)
            double height = nodePrefHeight(node, -1, VBox.getMargin(node), realColumnWidth(width));
            double y = yForNode(selectedColumn, heights, clearance);

            // update the height value for this column
            heights[selectedColumn] = y + height;
        }

        double height = 0;
        for (double h : heights) {
            height = Math.max(height, h);
        }

        return snapSpace(insets.getTop() + snapSpace(height) + insets.getBottom());
    }

    @Override
    public void requestLayout() {
        if (performingLayout) { return; }
        super.requestLayout();
    }

    private int columnCount(double width) {

        int columnCount = 1;
        if (columnWidth > 0) {
            columnCount = (int) Math.floor(width / columnWidth);
        }

        // always at least 1 column
        columnCount = Math.max(columnCount, 1);

        // only as many columns as nodes
        columnCount = Math.min(columnCount, getManagedChildren().size());

        return columnCount;
    }

    /******************************************************
     * Private Methods
     ******************************************************/

    private double realColumnWidth(double width) {
        int count = columnCount(width);
        double spacing = nodeSpacing.get() * (count - 1);
        double insets = getInsets().getLeft() + getInsets().getRight();
        double available = width - insets - spacing;
        return available / (double) count;
    }

    private double xForColumn(int column) {
        double inset = getInsets().getLeft();
        double columnOffset = realColumnWidth(getWidth()) + nodeSpacing.get();
        return snapSize(inset + column * columnOffset);
    }

    private double yForNode(int column, double heights[], double clearance) {
        double y = Math.max(clearance, heights[column]);
        double spacingHeight = y == 0 ? 0 : nodeSpacing.get();
        return spacingHeight + y;
    }

    // borrowed from Region computeChildPrefAreaHeight
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

    // Borrowed from Region
    private static double boundedSize(double min, double pref, double max) {
        double a = pref >= min ? pref : min;
        double b = min >= max ? min : max;
        return a <= b ? a : b;
    }

}
