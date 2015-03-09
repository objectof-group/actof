package net.objectof.actof.common.util;


import net.objectof.actof.common.controller.repository.RepositoryController;
import net.objectof.aggr.Aggregate;
import net.objectof.model.Id;
import net.objectof.model.Kind;
import net.objectof.model.Resource;
import net.objectof.model.Stereotype;
import net.objectof.model.impl.IMoment;


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

    public static Object valueFromString(Kind<?> kind, String text, RepositoryController repository) {

        switch (kind.getStereotype()) {
            case BOOL:
                return (Boolean.parseBoolean(text));
            case COMPOSED:
                throw new UnsupportedOperationException();
            case FN:
                throw new UnsupportedOperationException();
            case INDEXED:
                throw new UnsupportedOperationException();
            case INT:
                return (Long.parseLong(text));
            case MAPPED:
                Resource<?> newMap = resFromString(text, repository);
                return (newMap);
            case MEDIA:
                throw new UnsupportedOperationException();
            case MOMENT:
                return new IMoment(text);
            case NUM:
                return (Double.parseDouble(text));
            case REF:
                Resource<?> newValue = resFromString(text, repository);
                return (newValue);
            case SET:
                throw new UnsupportedOperationException();
            case TEXT:
                return (text);
            default:
                throw new UnsupportedOperationException();

        }

    }

    private static Resource<?> resFromString(String id, RepositoryController repository) {
        String[] parts = id.split("-", 2);
        String kind = parts[0];
        String label = parts[1];
        return repository.getStagingTx().retrieve(kind, label);
    }

}
