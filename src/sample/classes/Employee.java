package sample.classes;

import sample.classes.Contract;
import sample.classes.Person;

import java.time.LocalDate;
import java.util.List;

public class Employee extends Person {
    List<Contract> contractList;

    public Employee(String name, LocalDate dateOfBirth, String address, String phoneNumber, String eMail, List<Contract> contractList) {
        super(name, dateOfBirth, address, phoneNumber, eMail);
        this.contractList = contractList;
    }

    public List<Contract> getContractList() {
        return contractList;
    }

    public void setContractList(List<Contract> contractList) {
        this.contractList = contractList;
    }
}
