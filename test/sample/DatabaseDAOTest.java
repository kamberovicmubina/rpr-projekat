package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;
import sample.classes.Client;
import sample.classes.Contract;

import java.io.File;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class DatabaseDAOTest {

    @Test
    void addClient () {
        DatabaseDAO.removeInstance();
        File dbfile = new File("/resources/database.db");
        dbfile.delete();
        DatabaseDAO dao = DatabaseDAO.getInstance();
        LocalDate date = LocalDate.of(1999, 9, 9);
        Client client = new Client("Klijent provjera", date, "Adresa", "092833332", "mailll@gmail.com", null, 92.3);
        client.setId(dao.getNextAvailableClientId());
        dao.executeInsertClient(client);
        ObservableList<Client> clientsFromDatabase = dao.executeGetClientsQuery();
        assertEquals(client.getName(), clientsFromDatabase.get(clientsFromDatabase.size()-1).getName());
    }

    @Test
    void addContract () {
        DatabaseDAO.removeInstance();
        File dbfile = new File("/resources/database.db");
        dbfile.delete();
        DatabaseDAO dao = DatabaseDAO.getInstance();
        LocalDate date = LocalDate.of(1999, 9, 9);
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
        ObservableList<Contract> contractsFromDatabase = dao.executeGetClientContractsQuery(client.getId());
        assertEquals(contract.getId(), contractsFromDatabase.get(contractsFromDatabase.size()-1).getId());
    }

    @Test
    void deleteContract () {
        DatabaseDAO.removeInstance();
        File dbfile = new File("/resources/database.db");
        dbfile.delete();
        DatabaseDAO dao = DatabaseDAO.getInstance();
        LocalDate date = LocalDate.of(1999, 9, 9);
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

}