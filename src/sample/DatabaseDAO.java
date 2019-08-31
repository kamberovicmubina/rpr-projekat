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
    private PreparedStatement getCompanyQuery, getClientsQuery, getClientContractsQuery, getOneClientQuery, getOnePersonQuery, insertCompanyQuery,
                                insertClientQuery;
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

    public Company executeGetCompanyQuery (int idCompany) {
        Company company = null;
        try {
            getCompanyQuery = connection.prepareStatement("SELECT * FROM company WHERE id = ?");
            getCompanyQuery.setInt(1, idCompany);
            ResultSet rs = getCompanyQuery.executeQuery();
            // only one row will be returned
            String name = rs.getString(1);
            String address = rs.getString(2);
            String departments = rs.getString(3);
            ObservableList<Department> departmentList = getDepartmentsFromString (departments);
            String employees = rs.getString(4);
            ObservableList<Employee> employeeList = getEmployeesFromString (employees);
            String clients = rs.getString(5);
            ObservableList<Client> clientList = getClientsFromString (clients);
            String services = rs.getString(6);
            ObservableList<String> serviceList = getServicesFromString (services);
            int ownerId = rs.getInt(7);
            Person owner = executeGetOnePerson(ownerId);
            company = new Company(name, address, departmentList, employeeList, clientList, owner, serviceList);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return company;
    }

    private ObservableList<String> getServicesFromString(String services) {
        if (services.length() == 0) return null;
        ObservableList<String> serviceList = FXCollections.observableArrayList();
        String[] names = services.split(", ");
        for (String s : names) {
            serviceList.add(s);
        }
        return serviceList;
    }

    private ObservableList<Client> getClientsFromString(String clients) {
        if (clients.length() == 0) return null;
        ObservableList<Client> clientObservableList = FXCollections.observableArrayList();
        String[] ids = clients.split(" ");
        for (String s : ids) {
            int id = Integer.parseInt(s);
            clientObservableList.add(executeGetOneClient(id));
        }
        return  clientObservableList;
    }

    private ObservableList<Employee> getEmployeesFromString(String employees) {
        if (employees.length() == 0) return null;
        ObservableList<Employee> emp = FXCollections.observableArrayList();
        // in string employees we have ids of employees
        String[] ids = employees.split(" ");
        for (String s : ids) {
            int id = Integer.parseInt(s);
            emp.add((Employee) executeGetOnePerson(id));
        }
        return emp;
    }

    private ObservableList<Department> getDepartmentsFromString(String departments) {
        if (departments.length() == 0) return null;
        ObservableList<Department> deps = FXCollections.observableArrayList();
        String[] names = departments.split(", ");
        for (String s : names) {
            deps.add(new Department(s));
        }
        return deps;
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
        if (dateOfBirth.length() == 0) return null;
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

    public Person executeGetOnePerson (int idPerson) {
        Person person = null;
        try {
            getOnePersonQuery = connection.prepareStatement("SELECT * FROM person WHERE id=?");
            getOnePersonQuery.setInt(1, idPerson);
            ResultSet rs = getOnePersonQuery.executeQuery();
            // one row will be returned
            String name = rs.getString(2);
            String dateOfBirth = rs.getString(3);
            LocalDate dateLocal = getLocalDateFromString (dateOfBirth);
            String address = rs.getString(4);
            String phone = rs.getString(5);
            String eMail = rs.getString(6);
            person = new Person(name, dateLocal, address, phone, eMail);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return person;
    }

    public void executeInsertCompany (Company company) {
        try {
            insertCompanyQuery = connection.prepareStatement("INSERT INTO company VALUES(?,?,?,?,?,?,?,?)");
            PreparedStatement helpStatement = connection.prepareStatement("SELECT id FROM company ORDER BY id DESC");
            ResultSet result = helpStatement.executeQuery();
            int idCompany = 0;
            while (result.next()) {
                result.getInt(2);
                idCompany++;
            }
            idCompany++;
            insertCompanyQuery.setString(1, company.getName());
            insertCompanyQuery.setInt(2, idCompany);
            insertCompanyQuery.setString(3, company.getAddress());
            insertCompanyQuery.setString(4, getStringFromDepartments(company.getDepartments()));
            insertCompanyQuery.setString(5, getStringFromEmployees(company.getEmployees()));
            insertCompanyQuery.setString(6, getStringFromClients(company.getClients()));
            insertCompanyQuery.setString(7, getStringFromServices(company.getServices()));
            insertCompanyQuery.setInt(8, company.getOwner().getId());
            insertCompanyQuery.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private String getStringFromServices(ObservableList<String> services) {
        String serviceList = "";
        for (int i = 0; i < services.size(); i++) {
            serviceList += services.get(i);
            if (i != services.size()-1) serviceList += ", ";
        }
        return serviceList;
    }

    private String getStringFromClients(ObservableList<Client> clients) {
        String clientList = "";
        for (int i = 0; i < clients.size(); i++) {
            clientList += clients.get(i).getId();
            if (i != clients.size()-1) clientList += " ";
        }
        return clientList;
    }

    private String getStringFromEmployees(ObservableList<Employee> employees) {
        String emps = "";
        for (int i = 0; i < employees.size(); i++) {
            emps += employees.get(i).getId();
            if (i != employees.size()-1) emps += " ";
        }
        return emps;
    }

    private String getStringFromDepartments(ObservableList<Department> departments) {
        String deps = "";
        for (int i = 0; i < departments.size(); i++) {
            deps += departments.get(i).getNameOfDepartment();
            if (i != departments.size()-1) deps += ", ";
        }
        return deps;
    }

    public void executeInsertClient (Client client) {
        try {
            insertClientQuery = connection.prepareStatement("INSERT INTO client VALUES (?,?,?,?,?,?,?,?)");
            PreparedStatement helpStatement = connection.prepareStatement("SELECT id FROM client ORDER BY id DESC");
            ResultSet result = helpStatement.executeQuery();
            int idClient = 0;
            while (result.next()) {
                result.getInt(8);
                idClient++;
            }
            idClient++;
            insertClientQuery.setString(1, client.getName());
            insertClientQuery.setString(2, getStringFromLocalDate(client.getDateOfBirth()));
            insertClientQuery.setString(3, client.getAddress());
            insertClientQuery.setString(4, client.getPhoneNumber());
            insertClientQuery.setString(5, client.getEMail());
            insertClientQuery.setString(6, getStringFromContracts(client.getContractList()));
            insertClientQuery.setDouble(7, client.getProfit());
            insertClientQuery.setInt(8, idClient);
            insertClientQuery.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String getStringFromContracts(ObservableList<Contract> contractList) {
        String contracts = "";
        for (int i = 0; i < contractList.size(); i++) {
            contracts += contractList.get(i).getId();
            if (i != contractList.size()-1) contracts += " ";
        }
        return contracts;
    }

    private String getStringFromLocalDate(LocalDate dateOfBirth) {
        String date = "";
        date += String.valueOf(dateOfBirth.getYear()) + " " + String.valueOf(dateOfBirth.getMonth()) + " " + String.valueOf(dateOfBirth.getDayOfMonth());
        return date;
    }


}
