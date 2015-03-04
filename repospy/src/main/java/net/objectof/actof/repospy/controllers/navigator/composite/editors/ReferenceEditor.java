package net.objectof.actof.repospy.controllers.navigator.composite.editors;


import java.util.HashSet;
import java.util.Set;

import net.objectof.actof.common.util.RepoUtils;
import net.objectof.actof.repospy.controllers.navigator.composite.CompositeEntry;
import net.objectof.model.Resource;
import net.objectof.model.impl.IKind;


public class ReferenceEditor extends AbstractComboboxEditor {

    public ReferenceEditor(CompositeEntry entry) {
        super(entry, true);
    }

    @Override
    protected Set<Object> getElements() {
        IKind<?> ikind = (IKind<?>) getEntry().kind;
        String title = ikind.getTitle();
        Iterable<Resource<?>> ress = getEntry().getController().repository.getStagingTx().enumerate(title);

        Set<Object> names = new HashSet<>();
        for (Object o : ress) {
            names.add(o);
        }
        if (getEntry().getFieldValue() == null) {
            names.add(null);
        }
        return names;
    }

    @Override
    protected void addListeners() {

        field.valueProperty().addListener((obs, oldval, newval) -> {
            boolean valid = validate(newval);
            validationUI(valid);

            if (valid) {
                if (valid) {
                    onComplete.accept(field.getValue());
                } else {
                    onCancel.run();
                }
            }

        });

    }

    @Override
    protected boolean validate(String text) {
        try {
            String oldKind = getEntry().resourceKind().getComponentName();
            String newKind = RepoUtils.stringIdToKind(text);

            if (!newKind.equals(oldKind)) { return false; }
            String label = RepoUtils.stringIdToLabel(text);

            Integer.parseInt(label);

            return true;
        }
        catch (NullPointerException e) {
            e.printStackTrace();
            return true;
        }
        catch (Exception e) {
            return false;
        }

    }

}
