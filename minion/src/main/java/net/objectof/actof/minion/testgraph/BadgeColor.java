package net.objectof.actof.minion.testgraph;


import javafx.scene.paint.Color;


public enum BadgeColor {

    RED {

        public Color toColor() {
            return Color.valueOf("#B71C1C");
        }
    },
    PINK {

        public Color toColor() {
            return Color.valueOf("#880E4F");
        }
    },
    PURPLE {

        public Color toColor() {
            return Color.valueOf("#4A148C");
        }
    },
    DEEP_PURPLE {

        public Color toColor() {
            return Color.valueOf("#311B92");
        }
    },
    INDIGO {

        public Color toColor() {
            return Color.valueOf("#1A237E");
        }
    },
    BLUE {

        public Color toColor() {
            return Color.valueOf("#0D47A1");
        }
    },
    LIGHT_BLUE {

        public Color toColor() {
            return Color.valueOf("#01579B");
        }
    },
    CYAN {

        public Color toColor() {
            return Color.valueOf("#006064");
        }
    },
    TEAL {

        public Color toColor() {
            return Color.valueOf("#004D40");
        }
    },
    GREEN {

        public Color toColor() {
            return Color.valueOf("#1B5E20");
        }
    },
    LIGHT_GREEEN {

        public Color toColor() {
            return Color.valueOf("#33691E");
        }
    },
    LIME {

        public Color toColor() {
            return Color.valueOf("#827717");
        }
    },
    YELLOW {

        public Color toColor() {
            return Color.valueOf("#F57F17");
        }
    },
    AMBER {

        public Color toColor() {
            return Color.valueOf("#FF6F00");
        }
    },
    ORANGE {

        public Color toColor() {
            return Color.valueOf("#E65100");
        }
    },
    DEEP_ORANGE {

        public Color toColor() {
            return Color.valueOf("#BF360C");
        }
    },
    BROWN {

        public Color toColor() {
            return Color.valueOf("#3E2723");
        }
    }

    ;

    public Color toColor() {
        return Color.BLACK;
    }

}
