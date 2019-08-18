package sample;

import java.util.Date;
import java.util.List;

public class Employee extends Person {
    List<Contract> contractList;

    public Employee(String name, Date dateOfBirth, String address, String phoneNumber, String eMail, List<Contract> contractList) {
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
