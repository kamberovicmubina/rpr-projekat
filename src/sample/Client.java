package sample;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class Client extends Person {
    List<Contract> contractList;

    public Client(String name, LocalDate dateOfBirth, String address, String phoneNumber, String eMail, List<Contract> contractList) {
        super(name, dateOfBirth, address, phoneNumber, eMail);
        this.contractList = contractList;
    }

    public List<Contract> getContractList() {
        return contractList;
    }

    public void setContractList(List<Contract> contractList) {
        this.contractList = contractList;
    }

    @Override
    public String toString () {
        return getName();
    }
}
