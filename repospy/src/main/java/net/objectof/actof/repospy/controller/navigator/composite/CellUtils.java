package net.objectof.actof.repospy.controller.navigator.composite;

import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Cell;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;
import net.objectof.actof.repospy.controller.navigator.composite.editors.Editor;
import net.objectof.actof.repospy.controller.navigator.composite.editors.TextEditor;

/**
 * Copied so I could make modifications to it.
 * @author Nathaniel Sherry
 *
 */

//Package protected - not intended for external use
class CellUtils {
 
 static int TREE_VIEW_HBOX_GRAPHIC_PADDING = 3; 
 
 /***************************************************************************
  *                                                                         *
  * Private fields                                                          *
  *                                                                         *
  **************************************************************************/    
 
 @SuppressWarnings("rawtypes")
private final static StringConverter defaultStringConverter = new StringConverter<Object>() {
     @Override public String toString(Object t) {
         return t == null ? null : t.toString();
     }

     @Override public Object fromString(String string) {
         return string;
     }
 };
 
 @SuppressWarnings("rawtypes")
private final static StringConverter defaultTreeItemStringConverter =
     new StringConverter<TreeItem>() {
         @Override public String toString(TreeItem treeItem) {
             return (treeItem == null || treeItem.getValue() == null) ? 
                     "" : treeItem.getValue().toString();
         }

         @SuppressWarnings("unchecked")
		@Override public TreeItem fromString(String string) {
             return new TreeItem(string);
         }
     };
 
 /***************************************************************************
  *                                                                         *
  * General convenience                                                     *
  *                                                                         *
  **************************************************************************/    
 
 /*
  * Simple method to provide a StringConverter implementation in various cell
  * implementations.
  */
 @SuppressWarnings("unchecked")
static <T> StringConverter<T> defaultStringConverter() {
     return defaultStringConverter;
 }
 
 /*
  * Simple method to provide a TreeItem-specific StringConverter 
  * implementation in various cell implementations.
  */
 @SuppressWarnings("unchecked")
static <T> StringConverter<TreeItem<T>> defaultTreeItemStringConverter() {
     return defaultTreeItemStringConverter;
 }
 
 private static <T> String getItemText(Cell<T> cell, StringConverter<T> converter) {
     return converter == null ?
         cell.getItem() == null ? "" : cell.getItem().toString() :
         converter.toString(cell.getItem());
 }
 
 
 static Node getGraphic(TreeItem<?> treeItem) {
     return treeItem == null ? null : treeItem.getGraphic();
 }
 

 
 /***************************************************************************
  *                                                                         *
  * ChoiceBox convenience                                                   *
  *                                                                         *
  **************************************************************************/   
 
 static <T> void updateItem(final Cell<T> cell, 
                            final StringConverter<T> converter,
                            final ChoiceBox<T> choiceBox) {
     updateItem(cell, converter, null, null, choiceBox);
 }
 
 static <T> void updateItem(final Cell<T> cell,
                            final StringConverter<T> converter,
                            final HBox hbox,
                            final Node graphic,
                            final ChoiceBox<T> choiceBox) {
     if (cell.isEmpty()) {
         cell.setText(null);
         cell.setGraphic(null);
     } else {
         if (cell.isEditing()) {
             if (choiceBox != null) {
                 choiceBox.getSelectionModel().select(cell.getItem());
             }
             cell.setText(null);
             
             if (graphic != null) {
                 hbox.getChildren().setAll(graphic, choiceBox);
                 cell.setGraphic(hbox);
             } else {
                 cell.setGraphic(choiceBox);
             }
         } else {
             cell.setText(getItemText(cell, converter));
             cell.setGraphic(graphic);
         }
     }
 };
 
 static <T> ChoiceBox<T> createChoiceBox(
         final Cell<T> cell,
         final ObservableList<T> items,
         final ObjectProperty<StringConverter<T>> converter) {
     ChoiceBox<T> choiceBox = new ChoiceBox<T>(items);
     choiceBox.setMaxWidth(Double.MAX_VALUE);
     choiceBox.converterProperty().bind(converter);
     choiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<T>() {
         @Override
         public void changed(ObservableValue<? extends T> ov, T oldValue, T newValue) {
             if (cell.isEditing()) {
                 cell.commitEdit(newValue);
             }
         }
     });
     return choiceBox;
 }
 
 
 
 /***************************************************************************
  *                                                                         *
  * TextField convenience                                                   *
  *                                                                         *
  **************************************************************************/  
 
 static <T> void updateItem(final Cell<T> cell, 
                            final StringConverter<T> converter,
                            final TextEditor textField) {
     updateItem(cell, converter, null, null, textField);
 }
 
 static <T> void updateItem(final Cell<T> cell, 
                            final StringConverter<T> converter,
                            final HBox hbox,
                            final Node graphic,
                            final Editor textField) {
     if (cell.isEmpty()) {
         cell.setText(null);
         cell.setGraphic(null);
     } else {
         if (cell.isEditing()) {
             /*if (textField != null) {
                 textField.setText(getItemText(cell, converter));
             }*/
             cell.setText(null);
             
             if (graphic != null) {
                 hbox.getChildren().setAll(graphic, textField.getNode());
                 cell.setGraphic(hbox);
             } else {
                 cell.setGraphic(textField.getNode());
             }
         } else {
             cell.setText(getItemText(cell, converter));
             cell.setGraphic(graphic);
         }
     }
 }
 
 static <T> void startEdit(final Cell<T> cell, 
                           final StringConverter<T> converter,
                           final HBox hbox,
                           final Node graphic,
                           final Editor editor) {
     /*
	 if (textField != null) {
         textField.setText(getItemText(cell, converter));
     }*/
     cell.setText(null);
     
     if (graphic != null) {
         hbox.getChildren().setAll(graphic, editor.getNode());
         cell.setGraphic(hbox);
     } else {
         cell.setGraphic(editor.getNode());
     }
     
     editor.focus();

 }
 
 static <T> void cancelEdit(Cell<T> cell, final StringConverter<T> converter, Node graphic) {
     cell.setText(getItemText(cell, converter));
     cell.setGraphic(graphic);
 }
 
 
 /***************************************************************************
  *                                                                         *
  * ComboBox convenience                                                    *
  *                                                                         *
  **************************************************************************/ 
 
 static <T> void updateItem(Cell<T> cell, StringConverter<T> converter, ComboBox<T> comboBox) {
     updateItem(cell, converter, null, null, comboBox);
 }
 
 static <T> void updateItem(final Cell<T> cell, 
                            final StringConverter<T> converter,
                            final HBox hbox,
                            final Node graphic,
                            final ComboBox<T> comboBox) {
     if (cell.isEmpty()) {
         cell.setText(null);
         cell.setGraphic(null);
     } else {
         if (cell.isEditing()) {
             if (comboBox != null) {
                 comboBox.getSelectionModel().select(cell.getItem());
             }
             cell.setText(null);
             
             if (graphic != null) {
                 hbox.getChildren().setAll(graphic, comboBox);
                 cell.setGraphic(hbox);
             } else {
                 cell.setGraphic(comboBox);
             }
         } else {
             cell.setText(getItemText(cell, converter));
             cell.setGraphic(graphic);
         }
     }
 };
 
 static <T> ComboBox<T> createComboBox(final Cell<T> cell,
                                       final ObservableList<T> items,
                                       final ObjectProperty<StringConverter<T>> converter) {
     ComboBox<T> comboBox = new ComboBox<T>(items);
     comboBox.converterProperty().bind(converter);
     comboBox.setMaxWidth(Double.MAX_VALUE);
     comboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<T>() {
         @Override public void changed(ObservableValue<? extends T> ov, T oldValue, T newValue) {
             if (cell.isEditing()) {
                 cell.commitEdit(newValue);
             }
         }
     });
     return comboBox;
 }
}

