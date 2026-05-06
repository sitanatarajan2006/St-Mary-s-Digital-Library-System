# St. Mary's Digital Library System

A simple desktop app to manage books, members, and borrow records for a library. Built with JavaFX and SQLite.

## What you need

- Java 22 or higher
- An internet connection the first time you run it (Maven downloads the dependencies)

## How to run it

1. Download or clone the project
2. Open a terminal inside the project folder
3. Run this command:

Windows:
```
mvnw.cmd javafx:run
```

Mac or Linux:
```
./mvnw javafx:run
```

## Notes

- The database (library.db) gets created automatically when you first run the app, so you do not need to set anything up.
- The first run might be slow while it downloads everything.
- You might see some warnings in the terminal. These are from Maven on newer Java versions, and the app still works fine.
