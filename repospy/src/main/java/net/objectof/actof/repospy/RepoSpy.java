package net.objectof.actof.repospy;

import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;

public class RepoSpy extends Application {

	@Override
	public void start(Stage primaryStage) throws IOException {
		RepoSpyController spy = new RepoSpyController(primaryStage);
		spy.initUI();
	}
	
	public static void main(String[] args) {
		System.setProperty("prism.lcdtext", "false");
		launch(args);
	}

}
