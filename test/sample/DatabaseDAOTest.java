package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;
import sample.classes.*;

import java.io.File;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class DatabaseDAOTest {

    @Test
    void addClient () {
        DatabaseDAO.removeInstance();
        File dbfile = new File("database.db");
        dbfile.delete();
        DatabaseDAO dao = DatabaseDAO.getInstance();
        LocalDate date = LocalDate.of(1999, 9, 9);
        Client client = new Client("Klijent provjera", date, "Adresa", "092833332", "mailll@gmail.com", null, 92.3);
        client.setId(dao.getNextAvailableClientId());
        dao.executeInsertClient(client);
        ObservableList<Client> clientsFromDatabase = FXCollections.observableArrayList();
        clientsFromDatabase.addAll(dao.executeGetClientsQuery());
        boolean test = false;
        for (Client c : clientsFromDatabase) {
            if (c.getId() == client.getId()) {
                test = true;
                break;
            }
        }
        assertTrue(test);
    }

    @Test
    void addContract () {
        DatabaseDAO.removeInstance();
        File dbfile = new File("database.db");
        dbfile.delete();
        DatabaseDAO dao = DatabaseDAO.getInstance();
        LocalDate date = LocalDate.of(1999, 10, 9);
        LocalDate date2 = LocalDate.of(2019, 12, 12);
        Contract contract = new Contract("Testni ugovor", null, date, date2);
        contract.setId(dao.getNextAvailableContractId());
        ObservableList<Contract> contracts = FXCollections.observableArrayList();
        Client client = new Client("Klijent provjera", date, "Adresa", "092833332", "mailll@gmail.com", contracts, 92.3);
        client.setId(dao.getNextAvailableClientId());
        contract.setPerson(client);
        contracts.add(contract);
        dao.executeInsertClient(client);
        dao.executeInsertContract(contract);
        ObservableList<Contract> contractsFromDatabase = FXCollections.observableArrayList();
        contractsFromDatabase.addAll(dao.executeGetClientContractsQuery(client.getId()));
        assertEquals(contract.getId(), contractsFromDatabase.get(contractsFromDatabase.size()-1).getId());
    }

    @Test
    void deleteContract () {
        DatabaseDAO.removeInstance();
        File dbfile = new File("database.db");
        dbfile.delete();
        DatabaseDAO dao = DatabaseDAO.getInstance();
        LocalDate date = LocalDate.of(1999, 9, 19);
        LocalDate date2 = LocalDate.of(2019, 12, 12);
        Contract contract = new Contract("Testni ugovor", null, date, date2);
        contract.setId(dao.getNextAvailableContractId());
        ObservableList<Contract> contracts = FXCollections.observableArrayList();
        contracts.add(contract);
        Client client = new Client("Klijent provjera", date, "Adresa", "092833332", "mailll@gmail.com", contracts, 92.3);
        client.setId(dao.getNextAvailableClientId());
        contract.setPerson(client);
        dao.executeInsertClient(client);
        dao.executeInsertContract(contract);
        dao.executeDeleteContract(contract.getId());
        ObservableList<Contract> contractsFromDatabase = dao.executeGetClientContractsQuery(client.getId());
        assertTrue(contractsFromDatabase.size() == 0); // we added one contract associated with this client and then deleted it
    }

    @Test
    void localDateFromString () {
        DatabaseDAO dao = DatabaseDAO.getInstance();
        String s = "1998 1 23";
        String s1 = "2009 2 2";
        String s2 = "20 03";
        String s3 = "";
        LocalDate localDate = dao.getLocalDateFromString(s);
        LocalDate ld = LocalDate.of(1998, 1, 23);
        LocalDate localDate1 = dao.getLocalDateFromString(s1);
        LocalDate ld1 = LocalDate.of(2009, 2, 2);
        LocalDate localDate2 = dao.getLocalDateFromString(s2);
        LocalDate localDate3 = dao.getLocalDateFromString(s3);
        assertAll( () -> assertEquals(localDate, ld),
                () -> assertEquals(localDate1, ld1),
                () -> assertEquals(localDate2, null),
                () -> assertEquals(localDate3, null));
    }

    @Test
    void testDeleteClient () {
        DatabaseDAO.removeInstance();
        File dbfile = new File("database.db");
        dbfile.delete();
        DatabaseDAO dao = DatabaseDAO.getInstance();
        LocalDate date = LocalDate.of(2009, 9, 9);
        Client client = new Client();
        client.setId(dao.getNextAvailableClientId());
        client.setName("Test klijent");
        client.setAddress("Adresa");
        client.setDateOfBirth(date);
        client.setPhoneNumber("09333333");
        client.setEMail("test@hotmail.com");
        client.setContractList(null);
        client.setProfit(289);
        dao.executeInsertClient(client);
        dao.executeDeleteClient(client.getId());
        ObservableList<Client> clientsFromDatabase = FXCollections.observableArrayList();
        clientsFromDatabase.addAll(dao.executeGetClientsQuery());
        boolean test = true;
        for (Client c : clientsFromDatabase) {
            if (c.getId() == client.getId()) {
                test = false;
                break;
            }
        }
        assertTrue(test);
    }

    @Test
    void testInsertCompany () {
        DatabaseDAO.removeInstance();
        File dbfile = new File("database.db");
        dbfile.delete();
        DatabaseDAO dao = DatabaseDAO.getInstance();
        Company company = new Company();
        company.setId(2);
        company.setName("Test company");
        company.setAddress("Test address");
        ObservableList<Employee> employees = FXCollections.observableArrayList();
        LocalDate date = LocalDate.of(1987, 2, 2);
        Employee e = new Employee("Ime", date, "Titova", "099999999", "ime@gmail.com", null);
        employees.add(e);
        Client client = new Client();
        ObservableList<Client> clients = FXCollections.observableArrayList();
        clients.add(client);
        company.setClients(clients);
        company.setEmployees(employees);
        Department department = new Department("New test department");
        department.setEmployees(employees);
        ObservableList<Department> departments = FXCollections.observableArrayList();
        departments.add(department);
        company.setDepartments(departments);
        ObservableList<String> services = FXCollections.observableArrayList();
        services.addAll("Service 1", "Service 2", "Service 3");
        company.setServices(services);
        Person owner = new Person("Vlasnik", date, "Adressa", "099949999", "vlasnik@gmail.com");
        dao.executeInsertPerson(owner);
        company.setOwner(owner);
        dao.executeInsertCompany(company);
        Company company1 = dao.executeGetCompanyQuery(company.getId());
        assertAll(() -> assertEquals(company1.getId(), company.getId()),
                () -> assertEquals(company1.getName(), company.getName()));
    }
}