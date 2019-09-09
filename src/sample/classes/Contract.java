package sample.classes;

import java.io.Serializable;
import java.time.LocalDate;

public class Contract implements Serializable {
    private String titleOfContract;
    private Person person;
    private LocalDate signDate;
    private LocalDate endDate;
    private int id;
    private Double value;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Contract(String title, Person person, LocalDate signDate, LocalDate endDate) {
        titleOfContract = title;
        this.person = person;
        this.signDate = signDate;
        this.endDate = endDate;
    }

    public Contract(String title, Person person, LocalDate signDate, LocalDate endDate, Double value) {
        titleOfContract = title;
        this.person = person;
        this.signDate = signDate;
        this.endDate = endDate;
        this.value = value;
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

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}