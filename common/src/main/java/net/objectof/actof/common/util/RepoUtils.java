package net.objectof.actof.common.util;


import net.objectof.aggr.Aggregate;
import net.objectof.model.Id;
import net.objectof.model.Resource;
import net.objectof.model.Stereotype;


public class RepoUtils {

    public static final String SEPARATOR = " \u2023 ";

    public enum PrintStyle {
        SHORT, LONG
    }

    public static Id<?> getIdOrNull(Object res) {
        if (isAggregate(res)) { return ((Resource<?>) res).id(); }
        return null;
    }

    public static boolean isAggregateStereotype(Stereotype st) {
        if (st == Stereotype.COMPOSED) { return true; }
        if (st == Stereotype.MAPPED) { return true; }
        if (st == Stereotype.INDEXED) { return true; }
        if (st == Stereotype.SET) { return true; }
        return false;
    }

    public static boolean isAggregate(Object agg) {
        return agg instanceof Aggregate;
    }

    public static boolean isResource(Object res) {
        return res instanceof Resource;
    }

    public static boolean isResourceTransient(Resource<?> res) {
        return res.id().label().toString().startsWith("-");
    }

    public static boolean isEmptyAggregate(Object o) {
        if (!isAggregate(o)) { return false; }
        Aggregate<Object, Resource<?>> agg = (Aggregate<Object, Resource<?>>) o;
        for (Object key : agg.keySet()) {
            if (agg.get(key) != null) { return false; }
        }
        return true;
    }

    public static String idToString(Id<?> id) {
        return id.kind().getComponentName() + "-" + id.label().toString();
    }

    public static String stringIdToLabel(String string) {
        String[] parts = string.split("-", 2);
        return parts[1];
    }

    public static String stringIdToKind(String string) {
        String[] parts = string.split("-", 2);
        return parts[0];
    }

    public static String resToString(Object o) {

        if (o == null) { return "null"; }

        if (isResource(o)) {
            Resource<?> res = (Resource<?>) o;
            return res.id().kind().getComponentName() + "-" + res.id().label().toString();
        }

        return o.toString();
    }

    public static String prettyPrint(Object o) {
        return prettyPrint(o, PrintStyle.SHORT);
    }

    public static String prettyPrint(Object o, PrintStyle style) {
        if (o == null) { return "None"; }

        if (isResource(o)) { return prettyPrintRes((Resource<?>) o, style); }
        if (o instanceof String) { return "\"" + o.toString() + "\""; }
        if (o instanceof Stereotype) { return prettyPrintStereotype((Stereotype) o); }

        return o.toString();
    }

    private static String prettyPrintRes(Resource<?> res, PrintStyle style) {
        if (res == null) { return "None"; }

        String name = res.id().kind().getComponentName();
        if (style == PrintStyle.SHORT) {
            String[] parts = name.split("\\.");
            name = parts[parts.length - 1];
        } else {
            name = name.replace(".", SEPARATOR);
        }

        Stereotype st = res.id().kind().getStereotype();

        if (st == Stereotype.COMPOSED) {
            return name + " #" + res.id().label().toString();
        } else {
            return name;
        }
    }

    private static String prettyPrintStereotype(Stereotype st) {
        switch (st) {
            case BOOL:
                return "Boolean";
            case NUM:
                return "Real Number";
            case REF:
                return "Reference";
            case INT:
                return "Integer";
            case FN:
                return "Function";

            case INDEXED:
            case MAPPED:
            case TEXT:
            case COMPOSED:
            case MEDIA:
            case MOMENT:
            case NIL:
            case SET:
            default:
                String pretty = st.toString().toLowerCase();
                return pretty.substring(0, 1).toUpperCase() + pretty.substring(1);
        }
    }

}
