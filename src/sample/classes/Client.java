package sample.classes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.classes.Contract;
import sample.classes.Person;

import java.io.Serializable;
import java.time.LocalDate;

public class Client extends Person implements Serializable {
    ObservableList<Contract> contractList = FXCollections.observableArrayList();
    double profit;

    public Client(String name, LocalDate dateOfBirth, String address, String phoneNumber, String eMail, ObservableList<Contract> contractList) {
        super(name, dateOfBirth, address, phoneNumber, eMail);
        this.contractList = contractList;
    }

    public Client(String name, LocalDate dateOfBirth, String address, String phoneNumber, String eMail, ObservableList<Contract> contractList, double profit) {
        super(name, dateOfBirth, address, phoneNumber, eMail);
        this.contractList = contractList;
        this.profit = profit;
    }

    public Client() {
    }

    public ObservableList<Contract> getContractList() {
        return contractList;
    }

    public void setContractList(ObservableList<Contract> contractList) {
        this.contractList = contractList;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public void addToProfit(double newProfit) {
        profit = profit + newProfit;
    }

    @Override
    public String toString () {
        return getName();
    }
}
