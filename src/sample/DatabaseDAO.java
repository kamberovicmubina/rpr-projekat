package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.classes.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Scanner;

public class DatabaseDAO {
    private static DatabaseDAO instance;
    private Connection connection;
    private PreparedStatement getCompanyQuery, getServicesQuery, getClientsQuery, getClientContractsQuery, getOneClientQuery, getOnePersonQuery, insertCompanyQuery,
                                insertClientQuery, insertContractQuery, insertPersonQuery, deleteClientQuery, deleteContractQuery, deleteServiceQuery, changeClientQuery,
                                changeCompanyQuery, insertServiceQuery;

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

    public Connection getConnection() {
        return connection;
    }

    public static void removeInstance() {
        instance = null;
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
            String address = rs.getString(3);
            String departments = rs.getString(4);
            ObservableList<Department> departmentList = getDepartmentsFromString (departments);
            String employees = rs.getString(5);
            ObservableList<Employee> employeeList = getEmployeesFromString (employees);
            String clients = rs.getString(6);
            ObservableList<Client> clientList = getClientsFromString (clients);
            String services = rs.getString(7);
            ObservableList<String> serviceList = getServicesFromString (services);
            int ownerId = rs.getInt(8);
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
        serviceList.addAll(Arrays.asList(names));
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
                client.setId(idClient);
                clients.add(client);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }

    private LocalDate getLocalDateFromString(String dateOfBirth) {
        if (dateOfBirth.length() == 0) {
            return null;
        }
        //string has to be in one of the following formats in order for this function to work
        int year = Integer.parseInt(dateOfBirth.substring(0, 4));
        if (dateOfBirth.matches("^\\d\\d\\d\\d \\d\\d \\d\\d$") ) {
            int month = Integer.parseInt(dateOfBirth.substring(5, 7));
            int day = Integer.parseInt(dateOfBirth.substring(8, 10));
            LocalDate localDate = LocalDate.of(year, month, day);
            return localDate;
        } else if (dateOfBirth.matches("^\\d\\d\\d\\d \\d \\d\\d$")) {
            int month = Integer.parseInt(dateOfBirth.substring(5, 6));
            int day = Integer.parseInt(dateOfBirth.substring(7, 9));
            LocalDate localDate = LocalDate.of(year, month, day);
            return localDate;
        } else if (dateOfBirth.matches("^\\d\\d\\d\\d \\d \\d$")) {
            int month = Integer.parseInt(dateOfBirth.substring(5, 6));
            int day = Integer.parseInt(dateOfBirth.substring(7, 8));
            LocalDate localDate = LocalDate.of(year, month, day);
            return localDate;
        } else if (dateOfBirth.matches("^\\d\\d\\d\\d \\d\\d \\d$")) {
            int month = Integer.parseInt(dateOfBirth.substring(5, 7));
            int day = Integer.parseInt(dateOfBirth.substring(8, 9));
            LocalDate localDate = LocalDate.of(year, month, day);
            return localDate;
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
                int idContract = rs.getInt(2);
                contract.setId(idContract);
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
            while (rs.next()) {
                String name = rs.getString(1);
                String dateOfBirth = rs.getString(2);
                LocalDate dateLocal = getLocalDateFromString(dateOfBirth);
                String address = rs.getString(3);
                String phone = rs.getString(4);
                String eMail = rs.getString(5);
                double profit = rs.getDouble(7);
                client = new Client(name, dateLocal, address, phone, eMail, null, profit);
                return client;
            }
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
            while (rs.next()) {
                String name = rs.getString(2);
                String dateOfBirth = rs.getString(3);
                LocalDate dateLocal = getLocalDateFromString (dateOfBirth);
                String address = rs.getString(4);
                String phone = rs.getString(5);
                String eMail = rs.getString(6);
                person = new Person(name, dateLocal, address, phone, eMail);
                return person;
            }
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
            int idCompany = getNextIdFromResultSet(result);
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

    public int getNextAvailableClientId () {
        PreparedStatement helpStatement = null;
        try {
            helpStatement = connection.prepareStatement("SELECT id FROM client ORDER BY id DESC");
            ResultSet result = helpStatement.executeQuery();
            int idClient = getNextIdFromResultSet(result);
            return idClient;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private int getNextIdFromResultSet (ResultSet result) {
        int id = 0;
        try {
            while (result.next()) {
                result.getInt(1);
                id++;
            }
            id++;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    public void executeInsertClient (Client client) {
        try {
            insertClientQuery = connection.prepareStatement("INSERT INTO client VALUES (?,?,?,?,?,?,?,?)");
            insertClientQuery.setString(1, client.getName());
            insertClientQuery.setString(2, getStringFromLocalDate(client.getDateOfBirth()));
            insertClientQuery.setString(3, client.getAddress());
            insertClientQuery.setString(4, client.getPhoneNumber());
            insertClientQuery.setString(5, client.getEMail());
            insertClientQuery.setString(6, getStringFromContracts(client.getContractList()));
            insertClientQuery.setDouble(7, client.getProfit());
            insertClientQuery.setInt(8, client.getId());
            insertClientQuery.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        updateCompanyClients(client.getId());
    }

    private void updateCompanyClients(int id) {
        try {
            changeCompanyQuery = connection.prepareStatement("UPDATE company SET clients=? WHERE id=?");
            PreparedStatement helpStatement = connection.prepareStatement("SELECT clients FROM company WHERE id=?");
            helpStatement.setInt(1, 1);
            ResultSet rs = helpStatement.executeQuery();
            String newClients = "";
            while (rs.next()) {
                newClients = addIdToString(rs.getString(1), id);
            }
            changeCompanyQuery.setString(1, newClients);
            changeCompanyQuery.setInt(2, 1);
            changeCompanyQuery.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String addIdToString(String oldString, int id) {
        oldString += " " + String.valueOf(id);
        return oldString;
    }

    private String getStringFromContracts(ObservableList<Contract> contractList) {
        if (contractList == null || contractList.size() == 0) return "";
        String contracts = "";
        for (int i = 0; i < contractList.size(); i++) {
            contracts += contractList.get(i).getId();
            if (i != contractList.size()-1) contracts += " ";
        }
        return contracts;
    }

    public static String getStringFromLocalDate(LocalDate dateOfBirth) {
        String date = "";
        date += String.valueOf(dateOfBirth.getYear()) + " " + dateOfBirth.getMonthValue() + " " + String.valueOf(dateOfBirth.getDayOfMonth());
        return date;
    }

    public void executeInsertContract (Contract contract) {
        try {
            insertContractQuery = connection.prepareStatement("INSERT INTO contract VALUES (?,?,?,?,?)");
            insertContractQuery.setString(1, contract.getTitleOfContract());
            insertContractQuery.setInt(2, contract.getId());
            insertContractQuery.setInt(3, contract.getPerson().getId());
            insertContractQuery.setString(4, getStringFromLocalDate(contract.getSignDate()));
            insertContractQuery.setString(5, getStringFromLocalDate(contract.getEndDate()));
            insertContractQuery.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // update client
        try {
            changeCompanyQuery = connection.prepareStatement("UPDATE client SET contracts=? WHERE id=?");
            PreparedStatement helpStatement = connection.prepareStatement("SELECT contracts FROM client WHERE id=?");
            helpStatement.setInt(1, contract.getPerson().getId()) ;
            ResultSet rs = helpStatement.executeQuery();
            String newContracts = "";
            while (rs.next()) {
                newContracts = addIdToString(rs.getString(1), contract.getId());
            }
            changeCompanyQuery.setString(1, newContracts);
            changeCompanyQuery.setInt(2, contract.getPerson().getId());
            changeCompanyQuery.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        updateCompanyClients(contract.getPerson().getId());
    }

    public int getNextAvailableContractId () {
        PreparedStatement helpStatement = null;
        try {
            helpStatement = connection.prepareStatement("SELECT id FROM contract ORDER BY id DESC");
            ResultSet result = helpStatement.executeQuery();
            int idContract = getNextIdFromResultSet(result);
            return idContract;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void executeInsertPerson (Person person) {
        try {
            insertPersonQuery = connection.prepareStatement("INSERT INTO person VALUES(?,?,?,?,?,?)");
            PreparedStatement helpStatement = connection.prepareStatement("SELECT id FROM person ORDER BY id DESC");
            ResultSet result = helpStatement.executeQuery();
            int idPerson = getNextIdFromResultSet(result);
            person.setId(idPerson);
            insertPersonQuery.setInt(1, idPerson);
            insertPersonQuery.setString(2, person.getName());
            insertPersonQuery.setString(3, getStringFromLocalDate(person.getDateOfBirth()));
            insertPersonQuery.setString(4, person.getAddress());
            insertPersonQuery.setString(5, person.getPhoneNumber());
            insertPersonQuery.setString(6, person.getEMail());
            insertPersonQuery.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void executeDeleteClient (int id) {
        try {
            deleteClientQuery = connection.prepareStatement("DELETE FROM client WHERE id=?");
            deleteClientQuery.setInt(1, id);
            deleteClientQuery.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        updateCompanyClients(id);
    }

    private String deleteIdFromString(String string, int id) {
        String idString = "";
        StringBuilder builder = null;
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) != ' ') {
                idString += string.charAt(i);
            } else {
                int idNumber = Integer.parseInt(idString);
                if (idNumber == id) { // we need to delete this
                    int startIndex, endIndex;
                    if (i != string.length()-1) { // and a blank space after the number
                        startIndex = string.indexOf(idString);
                        endIndex = startIndex + idString.length() + 1;
                    } else {
                        if (i == 0) startIndex = 0;
                        else startIndex = string.indexOf(idString) - 1; // blank space before the number
                        endIndex = startIndex + idString.length();
                    }
                    builder = new StringBuilder(string);
                    builder.delete(startIndex, endIndex);
                    string = builder.toString();
                    return string;
                }
                idString = "";
            }
        }
        return string;
    }

    public void executeDeleteContract (int id) {
        System.out.println("IZ DATABASEDAO BRISANJE UGOVORA ID " + id);
        int personId = 0;
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT person FROM contract WHERE id=?");
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next())
                personId = rs.getInt(1);
            deleteContractQuery = connection.prepareStatement("DELETE FROM contract WHERE id=?");
            deleteContractQuery.setInt(1, id);
            deleteContractQuery.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // update client
        try {
            changeCompanyQuery = connection.prepareStatement("UPDATE client SET contracts=? WHERE id=?");
            PreparedStatement helpStatement = connection.prepareStatement("SELECT contracts FROM client WHERE id=?");
            helpStatement.setInt(1, personId);
            ResultSet rs = helpStatement.executeQuery();
            String newContracts = "";
            while (rs.next()) {
                newContracts = deleteIdFromString(rs.getString(1), id);
            }
            changeCompanyQuery.setString(1, newContracts);
            changeCompanyQuery.setInt(2, personId);
            changeCompanyQuery.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        updateCompanyClients(personId);
    }

    public void executeChangeClient (Client client) {
        try {
            changeClientQuery = connection.prepareStatement("UPDATE client SET name=?, date_of_birth=?, address=?, phone_number=?, e_mail=?, contracts=?, profit=? WHERE id=?");
            changeClientQuery.setString(1, client.getName());
            changeClientQuery.setString(2, getStringFromLocalDate(client.getDateOfBirth()));
            changeClientQuery.setString(3, client.getAddress());
            changeClientQuery.setString(4, client.getPhoneNumber());
            changeClientQuery.setString(5, client.getEMail());
            changeClientQuery.setString(6, getStringFromContracts(client.getContractList()));
            changeClientQuery.setDouble(7, client.getProfit());
            changeClientQuery.setInt(8, client.getId());
            changeClientQuery.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        updateCompanyClients(client.getId());
    }

    public void executeInsertService (String newService) {
        try {
            insertServiceQuery = connection.prepareStatement("UPDATE company SET services=? WHERE id=?");
            getServicesQuery = connection.prepareStatement("SELECT services FROM company WHERE id=?");
            getServicesQuery.setInt(1, 1);
            ResultSet rs = getServicesQuery.executeQuery();
            String services = "";
            while (rs.next()) {
                services = rs.getString(1);
            }
            services += ", " + newService;
            insertServiceQuery.setString(1, services);
            insertServiceQuery.setInt(2, 1);
            insertServiceQuery.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<String> executeGetServices () {
        String services = "";
        try {
            getServicesQuery = connection.prepareStatement("SELECT services FROM company WHERE id=?");
            getServicesQuery.setInt(1, 1);
            ResultSet rs = getServicesQuery.executeQuery();
            while (rs.next()) {
                services = rs.getString(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return getServicesFromString(services);
    }

    public void executeDeleteService (String service) {
        // update company
        try {
            deleteServiceQuery = connection.prepareStatement("UPDATE company SET services=? WHERE id=?");
            getServicesQuery = connection.prepareStatement("SELECT services FROM company WHERE id=?");
            getServicesQuery.setInt(1, 1);
            ResultSet rs = getServicesQuery.executeQuery();
            String services = "";
            while (rs.next()) {
                services = rs.getString(1);
            }
            deleteServiceQuery.setString(1, deleteStringFromString(services, service));
            deleteServiceQuery.setInt(2, 1);
            deleteServiceQuery.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String deleteStringFromString(String services, String service) {
        String serviceInString = "";
        StringBuilder builder = null;
        int startIndex, endIndex;
        for (int i = 0; i < services.length(); i++) {
            if ((services.charAt(i) != ',' && services.charAt(i) != ' ') || i == services.length()-1) {
                serviceInString += services.charAt(i);
            } else {
                if (serviceInString.equals(service)) { // we need to delete this
                    startIndex = services.indexOf(serviceInString); // and comma and blank space after the service
                    endIndex = startIndex + serviceInString.length() + 2;
                    builder = new StringBuilder(services);
                    builder.delete(startIndex, endIndex);
                    services = builder.toString();
                    return services;
                }
                serviceInString = "";
            }
        }
        // last word
        if (services.indexOf(serviceInString) == 0) startIndex = 0;
        else startIndex = services.indexOf(serviceInString) - 2; // comma and blank space before the service
        endIndex = startIndex + serviceInString.length();
        builder = new StringBuilder(services);
        builder.delete(startIndex, endIndex);
        services = builder.toString();
        return services;
    }
}