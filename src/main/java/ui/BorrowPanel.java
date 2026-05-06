package ui;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class BorrowPanel {

    private VBox view;

    public BorrowPanel() {

        view = new VBox();

        Label label = new Label("Borrow Records Panel");

        view.getChildren().add(label);
    }

    public VBox getView() {

        return view;
    }
}