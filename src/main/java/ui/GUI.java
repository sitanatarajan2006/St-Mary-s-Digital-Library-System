package ui;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;

public class GUI {

    private BorderPane mainView;

    public GUI() {

        mainView = new BorderPane();

        TabPane tabPane = new TabPane();

        Tab booksTab = new Tab("Books");
        booksTab.setContent(new BooksPanel().getView());
        booksTab.setClosable(false);

        Tab membersTab = new Tab("Members");
        membersTab.setContent(new MembersPanel().getView());
        membersTab.setClosable(false);

        Tab borrowTab = new Tab("Borrow Records");
        borrowTab.setContent(new BorrowPanel().getView());
        borrowTab.setClosable(false);

        tabPane.getTabs().add(booksTab);
        tabPane.getTabs().add(membersTab);
        tabPane.getTabs().add(borrowTab);

        mainView.setCenter(tabPane);
    }

    public BorderPane getMainView() {

        return mainView;
    }
}