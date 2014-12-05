package net.objectof.actof.repospy.controller.navigator.composite;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;
import net.objectof.actof.repospy.controller.navigator.composite.editors.Editor;
import net.objectof.actof.repospy.controller.navigator.composite.editors.EditorUtils;
import net.objectof.model.Stereotype;

/**
 * Lifted from TextFieldTreeTableCell so that it can handle different stereotypes
 *
 */
public class CompositeTreeTableCell extends TreeTableCell<CompositeEntry, CompositeEntry> {

    
    /***************************************************************************
     *                                                                         *
     * Static cell factories                                                   *
     *                                                                         *
     **************************************************************************/
    
    /**
     * Provides a {@link TextField} that allows editing of the cell content when
     * the cell is double-clicked, or when 
     * {@link javafx.scene.control.TreeTableView#edit(int, javafx.scene.control.TreeTableColumn)} is called.
     * This method will only  work on {@link TreeTableColumn} instances which are of
     * type String.
     * 
     * @return A {@link Callback} that can be inserted into the 
     *      {@link TreeTableColumn#cellFactoryProperty() cell factory property} of a 
     *      TreeTableColumn, that enables textual editing of the content.
     */
    public static <S> Callback<TreeTableColumn<S,String>, TreeTableCell<S,String>> forTreeTableColumn() {
        return forTreeTableColumn(new DefaultStringConverter());
    }
    
    /**
     * Provides a {@link TextField} that allows editing of the cell content when
     * the cell is double-clicked, or when 
     * {@link javafx.scene.control.TreeTableView#edit(int, javafx.scene.control.TreeTableColumn) } is called.
     * This method will work  on any {@link TreeTableColumn} instance, regardless of 
     * its generic type. However, to enable this, a {@link StringConverter} must 
     * be provided that will convert the given String (from what the user typed 
     * in) into an instance of type T. This item will then be passed along to the 
     * {@link TreeTableColumn#onEditCommitProperty()} callback.
     * 
     * @param converter A {@link StringConverter} that can convert the given String 
     *      (from what the user typed in) into an instance of type T.
     * @return A {@link Callback} that can be inserted into the 
     *      {@link TreeTableColumn#cellFactoryProperty() cell factory property} of a 
     *      TreeTableColumn, that enables textual editing of the content.
     */
    public static <S,T> Callback<TreeTableColumn<S,T>, TreeTableCell<S,T>> forTreeTableColumn(
            final StringConverter<T> converter) {
        return new Callback<TreeTableColumn<S,T>, TreeTableCell<S,T>>() {
            @Override public TreeTableCell<S,T> call(TreeTableColumn<S,T> list) {
                return new TextFieldTreeTableCell<S,T>(converter);
            }
        };
    }
    
    
    /***************************************************************************
     *                                                                         *
     * Fields                                                                  *
     *                                                                         *
     **************************************************************************/    
    
    private Editor editor;
    
    
    
    /***************************************************************************
     *                                                                         *
     * Constructors                                                            *
     *                                                                         *
     **************************************************************************/
	public CompositeTreeTableCell() {
		super();
		
		converter.set(new StringConverter<CompositeEntry>(){

			@Override
			public String toString(CompositeEntry entry) {
				return entry.title();
			}

			@Override
			public CompositeEntry fromString(String string) {
				// Never called
				return null;
			}});
	}

    
        
    
    
    /***************************************************************************
     *                                                                         *
     * Properties                                                              *
     *                                                                         *
     **************************************************************************/
    
    // --- converter
    private ObjectProperty<StringConverter<CompositeEntry>> converter = 
            new SimpleObjectProperty<StringConverter<CompositeEntry>>(this, "converter");

    /**
     * The {@link StringConverter} property.
     */
    public final ObjectProperty<StringConverter<CompositeEntry>> converterProperty() { 
        return converter; 
    }
    
    /** 
     * Sets the {@link StringConverter} to be used in this cell.
     */
    public final void setConverter(StringConverter<CompositeEntry> value) { 
        converterProperty().set(value); 
    }
    
    /**
     * Returns the {@link StringConverter} used in this cell.
     */
    public final StringConverter<CompositeEntry> getConverter() { 
        return converterProperty().get(); 
    }  
    
    
    
    /***************************************************************************
     *                                                                         *
     * Public API                                                              *
     *                                                                         *
     **************************************************************************/
	
	
    /** {@inheritDoc} */
    @Override public void startEdit() {
        if (! isEditable() 
                || ! getTreeTableView().isEditable() 
                || ! getTableColumn().isEditable()) {
            return;
        }
        
        Stereotype st = getItem().getStereotype();
        if (!EditorUtils.isStereotypeSupported(st)) { return; }
        
        super.startEdit();

        if (isEditing()) {
            editor = EditorUtils.createConfiguredEditor(this, getConverter(), st);
            CellUtils.startEdit(this, getConverter(), null, null, editor);
        }
    }
    
    /** {@inheritDoc} */
    @Override public void cancelEdit() {
        super.cancelEdit();
        CellUtils.cancelEdit(this, getConverter(), null);
    }
    
    /** {@inheritDoc} */
    @Override public void updateItem(CompositeEntry item, boolean empty) {
    	super.updateItem(item, empty);
        CellUtils.updateItem(this, getConverter(), null, null, editor);
    }
	
}
