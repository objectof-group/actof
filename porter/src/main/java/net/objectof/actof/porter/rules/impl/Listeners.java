package net.objectof.actof.porter.rules.impl;


public class Listeners {

    public static Listener drop() {
        return (s, d) -> d.setDropped(true);
    }

    public static Listener echo(String text) {
        return (s, d) -> System.out.println(text);
    }

    public static Listener printKey() {
        return (s, d) -> System.out.println(s.getKey());
    }

    public static Listener printKind() {
        return (s, d) -> System.out.println(s.getKind());
    }

    public static Listener printValue() {
        return (s, d) -> System.out.println(s.getValue());
    }

    public static Listener print(Transformer toValue) {
        return (s, d) -> System.out.println(toValue.apply(s, d));
    }

}
