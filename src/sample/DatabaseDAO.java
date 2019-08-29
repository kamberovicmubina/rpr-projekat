package sample;

import com.sun.javafx.image.IntPixelGetter;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.time.LocalDate;
import java.util.Scanner;

public class DatabaseDAO {
    private static DatabaseDAO instance;
    private Connection connection;
    private PreparedStatement getCompanyQuery, getClientsQuery, getClientContractsQuery, getOneClientQuery;
    private ObservableList<Client> clients = FXCollections.observableArrayList();

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

    public ObservableList<Client> executeGetClientsQuery () {
        ObservableList<Client> clients = FXCollections.observableArrayList();
        try {
            getClientsQuery = connection.prepareStatement("SELECT * FROM client");
            ResultSet rs = getClientsQuery.executeQuery();
            while (rs.next()) {
                String name = rs.getString(1);
                String dateOfBirth = rs.getString(2);
                LocalDate dateLocal = getLocalDateFromString (dateOfBirth);
                String address = rs.getString(3);
                String phone = rs.getString(4);
                String eMail = rs.getString(5);
                int idClient = rs.getInt(8);
                ObservableList<Contract> contractsList = executeGetClientContractsQuery(idClient);
                double profit = rs.getDouble(7);
                Client client = new Client(name, dateLocal, address, phone, eMail, contractsList, profit);
                clients.add(client);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }

    private LocalDate getLocalDateFromString(String dateOfBirth) {
        //string has to be in following format in order for this function to work: "YYYY MM DD"
        if (dateOfBirth.matches("^\\d\\d\\d\\d \\d\\d \\d\\d$") ) {
            // check if month is between 01 and 12
            if (dateOfBirth.charAt(5) == '0' || (dateOfBirth.charAt(5) == '1' && (dateOfBirth.charAt(6) == '0' || dateOfBirth.charAt(6) == '1' || dateOfBirth.charAt(6) == '2'))){
                // check if day is between 01 and 31
                if (dateOfBirth.charAt(8) >= '0' && dateOfBirth.charAt(8) < '4') {
                    if (dateOfBirth.charAt(8) == '3' && (dateOfBirth.charAt(9) == '0' || dateOfBirth.charAt(9) == '1')) {
                        int year = Integer.parseInt(dateOfBirth.substring(0, 4));
                        int month = Integer.parseInt(dateOfBirth.substring(5, 7));
                        int day = Integer.parseInt(dateOfBirth.substring(8, 10));
                        LocalDate localDate = LocalDate.of(year, month, day);
                        return localDate;
                    }
                }
            }
        }
        return null;
    }

    public ObservableList<Contract> executeGetClientContractsQuery (int idClient) {
        ObservableList<Contract> contracts = FXCollections.observableArrayList();
        try {
            getClientContractsQuery = connection.prepareStatement("SELECT contract.* FROM contract, client WHERE client.id = contract.person AND client.id=?");
            getClientContractsQuery.setInt(1, idClient);
            ResultSet rs = getClientContractsQuery.executeQuery();
            while (rs.next()) {
                String title = rs.getString(1);
                String signDate = rs.getString(4);
                LocalDate signLocal = getLocalDateFromString(signDate);
                String endDate = rs.getString(5);
                LocalDate endLocal = getLocalDateFromString(endDate);
                Person person = executeGetOneClient(idClient);
                Contract contract = new Contract(title, person, signLocal, endLocal);
                contracts.add(contract);
                ((Client) person).setContractList(contracts);
                contract.setPerson(person);
                contracts.remove(contract);
                contracts.add(contract);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contracts;
    }


    private Client executeGetOneClient (int idClient) {
        Client client = null;
        try {
            getOneClientQuery = connection.prepareStatement("SELECT * FROM client WHERE id=?");
            getOneClientQuery.setInt(1,idClient);
            ResultSet rs = getOneClientQuery.executeQuery();
            // one row will be returned
            String name = rs.getString(1);
            String dateOfBirth = rs.getString(2);
            LocalDate dateLocal = getLocalDateFromString (dateOfBirth);
            String address = rs.getString(3);
            String phone = rs.getString(4);
            String eMail = rs.getString(5);
            double profit = rs.getDouble(7);
            client = new Client(name, dateLocal, address, phone, eMail, null, profit);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return client;
    }

   /* public void setAddClientQuery () {
        try {
            addClientQuery = connection.prepareStatement("INSERT INTO client VALUES ()");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/
}
