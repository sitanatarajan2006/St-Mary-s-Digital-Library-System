package ui;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

public class GUI extends JFrame {

    public GUI() {

        setTitle("St Mary's Digital Library System");

        setSize(1000, 650);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("Books", new BooksPanel());
        tabs.addTab("Members", new MembersPanel());
        tabs.addTab("Borrow Records", new BorrowPanel());

        add(tabs);
    }

    public static void launch() {

        SwingUtilities.invokeLater(new Runnable() {

            public void run() {

                new GUI().setVisible(true);
            }
        });
    }
}