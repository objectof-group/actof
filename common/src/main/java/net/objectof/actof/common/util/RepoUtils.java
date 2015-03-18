package net.objectof.actof.common.util;


import net.objectof.aggr.Aggregate;
import net.objectof.model.Id;
import net.objectof.model.Resource;
import net.objectof.model.Stereotype;


public class RepoUtils {

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
        if (o == null) { return "None"; }

        if (isResource(o)) {
            Resource<?> res = (Resource<?>) o;
            return prettyPrintRes(res);
        }

        if (o instanceof String) { return "\"" + o.toString() + "\""; }
        if (o instanceof Stereotype) { return prettyPrintStereotype((Stereotype) o); }

        return o.toString();
    }

    public static String prettyPrintRes(Resource<?> res) {
        if (res == null) { return "None"; }
        String name = res.id().kind().getComponentName();
        String[] parts = name.split("\\.");
        name = parts[parts.length - 1];

        Stereotype st = res.id().kind().getStereotype();

        if (st == Stereotype.COMPOSED) {
            return name + " #" + res.id().label().toString();
        } else {
            return name;
        }
    }

    public static String prettyPrintStereotype(Stereotype st) {
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
