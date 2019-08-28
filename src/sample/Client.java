package sample;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class Client extends Person {
    ObservableList<Contract> contractList = FXCollections.observableArrayList();
    double profit;

    public Client(String name, LocalDate dateOfBirth, String address, String phoneNumber, String eMail, ObservableList<Contract> contractList) {
        super(name, dateOfBirth, address, phoneNumber, eMail);
        this.contractList = contractList;
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
