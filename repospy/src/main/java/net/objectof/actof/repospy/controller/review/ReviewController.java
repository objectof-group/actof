package net.objectof.actof.repospy.controller.review;


import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import net.objectof.actof.common.util.RepoUtils;
import net.objectof.actof.repospy.changes.EditingChange;

public class ReviewController {
	
	@FXML TableView<EditingChange> table;
	
	@FXML TableColumn<EditingChange, String> entity;
	@FXML TableColumn<EditingChange, String> kind;
	@FXML TableColumn<EditingChange, String> stereotype;
	@FXML TableColumn<EditingChange, String> oldvalue;
	@FXML TableColumn<EditingChange, String> newvalue;
	
	@FXML
    private void initialize() {
		
		entity    .setCellValueFactory(features -> new SimpleStringProperty( features.getValue().getKey() ));
		kind      .setCellValueFactory(features -> new SimpleStringProperty( features.getValue().getKind().getComponentName() ));
		stereotype.setCellValueFactory(features -> new SimpleStringProperty( features.getValue().getStereotype().toString() ));
		oldvalue  .setCellValueFactory(features -> new SimpleStringProperty( RepoUtils.resToString(features.getValue().oldValue()) ));
		newvalue  .setCellValueFactory(features -> new SimpleStringProperty( RepoUtils.resToString(features.getValue().newValue()) ));
		
		
		entity    .setCellFactory(column -> new StyledTableCell<>("-fx-font-weight: bold;"));
		kind      .setCellFactory(column -> new StyledTableCell<>("-fx-text-fill: #999999;"));
		stereotype.setCellFactory(column -> new StyledTableCell<>("-fx-text-fill: #999999;"));
		
	}
	
	public void setChanges(ObservableList<EditingChange> changes) {
		table.getItems().setAll(changes);
	}
	
	class StyledTableCell<S, T> extends TableCell<S, T> {
		
		private String style;
		
		public StyledTableCell(String style) {
			this.style = style;
		}
		
		@Override
        protected void updateItem(T item, boolean empty) {
            super.updateItem(item, empty);

            setStyle(style);
            
            if (item == null || empty) {
                setText(null);
            } else {
            	setText(item.toString());
            }
        }
	}
	
}
