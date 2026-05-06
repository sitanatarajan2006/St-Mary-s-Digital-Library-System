package ui;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class MembersPanel {

    private VBox view;

    public MembersPanel() {

        view = new VBox();

        Label label = new Label("Members Panel");

        view.getChildren().add(label);
    }

    public VBox getView() {

        return view;
    }
}