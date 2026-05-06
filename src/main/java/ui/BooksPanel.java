package ui;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class BooksPanel {

    private VBox view;

    public BooksPanel() {

        view = new VBox();

        Label label = new Label("Books Panel");

        view.getChildren().add(label);
    }

    public VBox getView() {

        return view;
    }
}