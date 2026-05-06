import data.DbConnection;
import ui.GUI;

public class Main {

    public static void main(String[] args) {

        DbConnection.createTables();

        GUI.launch();
    }
}