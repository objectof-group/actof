package net.objectof.actof.minion.common.minionhandler;


import javafx.scene.paint.Color;


public enum MinionColor {

    RED {

        public Color toFXColor() {
            return Color.valueOf("#B71C1C");
        }
    },
    PINK {

        public Color toFXColor() {
            return Color.valueOf("#880E4F");
        }
    },
    PURPLE {

        public Color toFXColor() {
            return Color.valueOf("#4A148C");
        }
    },
    DEEP_PURPLE {

        public Color toFXColor() {
            return Color.valueOf("#311B92");
        }
    },
    INDIGO {

        public Color toFXColor() {
            return Color.valueOf("#1A237E");
        }
    },
    BLUE {

        public Color toFXColor() {
            return Color.valueOf("#0D47A1");
        }
    },
    LIGHT_BLUE {

        public Color toFXColor() {
            return Color.valueOf("#01579B");
        }
    },
    CYAN {

        public Color toFXColor() {
            return Color.valueOf("#006064");
        }
    },
    TEAL {

        public Color toFXColor() {
            return Color.valueOf("#004D40");
        }
    },
    GREEN {

        public Color toFXColor() {
            return Color.valueOf("#1B5E20");
        }
    },
    LIGHT_GREEEN {

        public Color toFXColor() {
            return Color.valueOf("#33691E");
        }
    },
    LIME {

        public Color toFXColor() {
            return Color.valueOf("#827717");
        }
    },
    YELLOW {

        public Color toFXColor() {
            return Color.valueOf("#F57F17");
        }
    },
    AMBER {

        public Color toFXColor() {
            return Color.valueOf("#FF6F00");
        }
    },
    ORANGE {

        public Color toFXColor() {
            return Color.valueOf("#E65100");
        }
    },
    DEEP_ORANGE {

        public Color toFXColor() {
            return Color.valueOf("#BF360C");
        }
    },
    BROWN {

        public Color toFXColor() {
            return Color.valueOf("#3E2723");
        }
    }

    ;

    public Color toFXColor() {
        return Color.BLACK;
    }

    public String prettyName() {
        return this.name().substring(0, 1) + this.name().substring(1).toLowerCase();
    }
}
