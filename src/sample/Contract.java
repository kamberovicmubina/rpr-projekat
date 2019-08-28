package sample;

import java.time.LocalDate;
import java.util.Date;

public class Contract {
    private String titleOfContract;
    private Person person;
    private LocalDate signDate;
    private LocalDate endDate;

    public Contract(String title, Person person, LocalDate signDate, LocalDate endDate) {
        titleOfContract = title;
        this.person = person;
        this.signDate = signDate;
        this.endDate = endDate;
    }

    public Contract() {
    }

    public String getTitleOfContract() {
        return titleOfContract;
    }

    public void setTitleOfContract(String titleOfContract) {
        this.titleOfContract = titleOfContract;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public LocalDate getSignDate() {
        return signDate;
    }

    public void setSignDate(LocalDate signDate) {
        this.signDate = signDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }


}
