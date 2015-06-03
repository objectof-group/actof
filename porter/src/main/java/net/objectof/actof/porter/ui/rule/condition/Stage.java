package net.objectof.actof.porter.ui.rule.condition;


public enum Stage {

    MATCH {

        public String toString() {
            return "Match";
        }
    },
    BEFORE {

        public String toString() {
            return "Before";
        }
    },
    KEY {

        public String toString() {
            return "Key Transform";
        }
    },
    VALUE {

        public String toString() {
            return "Value Transform";
        }
    },
    AFTER {

        public String toString() {
            return "After";
        }
    };

}
