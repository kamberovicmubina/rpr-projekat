package sample;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.Scanner;

public class DatabaseDAO {
    private static DatabaseDAO instance;
    private Connection connection;
    private PreparedStatement getCompanyQuery;

    public static DatabaseDAO getInstance() {
        if (instance == null) instance = new DatabaseDAO();
        return instance;
    }

    private DatabaseDAO () {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            getCompanyQuery = connection.prepareStatement("SELECT * FROM company");
        } catch (SQLException e) {
            // database is not created
            regenerateDatabase();
            try {
                getCompanyQuery = connection.prepareStatement("SELECT * FROM company");
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }

    }

    private void regenerateDatabase ()  {
        Scanner input = null;
        try {
            input = new Scanner(new FileInputStream("database.db.sql"));
            String query = "";
            while (input.hasNext()) {
                query += input.nextLine();
                if (query.charAt(query.length() - 1) == ';') {
                    try {
                        Statement statement = connection.createStatement();
                        statement.execute(query);
                        query = "";
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            }
            input.close();
        } catch (FileNotFoundException e2) {
            e2.printStackTrace();
        }

    }
}
