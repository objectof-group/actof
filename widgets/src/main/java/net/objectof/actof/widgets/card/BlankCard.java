package net.objectof.actof.widgets.card;


import javafx.geometry.Insets;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;


public class BlankCard extends AnchorPane {

    protected BorderPane card;

    private double radius = 5;
    private String colour = "transparent";
    private String shadowColour = "#777";
    private double shadowRadius = 8;
    private double shadowOffsetX = 1, shadowOffsetY = 0;
    private boolean hasShadow = false;

    public BlankCard() {
        card = new BorderPane();
        AnchorPane.setTopAnchor(card, 0d);
        AnchorPane.setBottomAnchor(card, 0d);
        AnchorPane.setLeftAnchor(card, 0d);
        AnchorPane.setRightAnchor(card, 0d);
        getChildren().add(card);
        setPadding(new Insets(6));
        card.setPadding(new Insets(10));
        buildStyle();
    }

    public void setRadius(double radius) {
        this.radius = radius;
        buildStyle();
    }

    public double getRadius() {
        return radius;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
        buildStyle();
    }

    public void setShadowColour(String colour) {
        this.shadowColour = colour;
        buildStyle();
    }

    public void setShadowRadius(double rad) {
        this.shadowRadius = rad;
        buildStyle();
    }

    public double getShadowOffsetX() {
        return shadowOffsetX;
    }

    public void setShadowOffsetX(double shadowOffsetX) {
        this.shadowOffsetX = shadowOffsetX;
        buildStyle();
    }

    public double getShadowOffsetY() {
        return shadowOffsetY;
    }

    public void setShadowOffsetY(double shadowOffsetY) {
        this.shadowOffsetY = shadowOffsetY;
        buildStyle();
    }

    public String getShadowColour() {
        return shadowColour;
    }

    public double getShadowRadius() {
        return shadowRadius;
    }

    public boolean isHasShadow() {
        return hasShadow;
    }

    public void setHasShadow(boolean hasShadow) {
        this.hasShadow = hasShadow;
        buildStyle();
    }

    public void setInnerPadding(Insets padding) {
        card.setPadding(padding);
    }

    public Insets getInnerPadding() {
        return card.getPadding();
    }

    private void buildStyle() {
        if (!hasShadow) {

        }
        String rad = "-fx-background-radius: " + radius + "px; ";
        String col = "-fx-background-color: " + colour + "; ";
        String shadow = "";
        if (hasShadow) {
            shadow = "-fx-effect: dropshadow(gaussian, " + shadowColour + " , " + shadowRadius + ", -2, "
                    + shadowOffsetY + "," + shadowOffsetX + ")";
        }
        card.setStyle(rad + col + shadow);
    }
}
