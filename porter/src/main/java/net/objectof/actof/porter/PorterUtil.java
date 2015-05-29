package net.objectof.actof.porter;


import net.objectof.aggr.Aggregate;
import net.objectof.ext.Archetype;
import net.objectof.model.Kind;
import net.objectof.model.Resource;
import net.objectof.model.Stereotype;
import net.objectof.model.Transaction;


public class PorterUtil {

    /**
     * Checks if the given object is a resource from the old repo
     * 
     * @param object
     * @return
     */
    public static boolean isResourceStale(Transaction tx, Object object) {
        if (!(object instanceof Resource)) { return false; }
        Resource<Object> res = (Resource<Object>) object;
        if (res.tx().getPackage().equals(tx.getPackage())) { return false; }
        return true;
    }

    public static String kindName(Kind<?> kind) {
        if (kind.getStereotype() == Stereotype.REF) {
            return kindName(kind.getParts().get(0));
        } else {
            return kind.getComponentName();
        }
    }

    public static boolean isContainer(Kind<?> kind) {
        return kind.getStereotype().getModel() == Archetype.CONTAINER;
    }

    public static boolean isQualified(Resource<?> parent) {
        if (parent == null) {
            return true;
        } else {
            return parent.id().kind().getStereotype() == Stereotype.COMPOSED;
        }
    }

    public static Object unqualify(Object key, Resource<Aggregate<Object, Object>> parent) {
        if (!isQualified(parent)) { return key; }
        if (!(key instanceof String)) { return key; }
        String keyString = key.toString();
        int lastIndex = keyString.lastIndexOf('.');
        if (lastIndex == -1) { return key; }
        return keyString.substring(lastIndex + 1);
    }

    public static Kind<?> kindFromKey(Transaction toTx, String key) {
        return kindFromKey(toTx.getPackage().getParts(), key);
    }

    public static Kind<?> kindFromKey(Iterable<? extends Kind<?>> kinds, String key) {
        for (Kind<?> kind : kinds) {
            String componentName = kind.getComponentName();
            if (componentName.equals(key)) { return kind; }
            if (key.startsWith(componentName)) {
                Kind<?> recursed = kindFromKey(kind.getParts(), key);
                if (recursed != null) { return recursed; }
            }
        }
        return null;
    }

}
