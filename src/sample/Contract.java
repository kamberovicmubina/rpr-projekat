package sample;

import java.util.Date;

public class Contract {
    private Person person;
    private Date signDate;
    private Date endDate;

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Date getSignDate() {
        return signDate;
    }

    public void setSignDate(Date signDate) {
        this.signDate = signDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Contract(Person person, Date signDate, Date endDate) {
        this.person = person;
        this.signDate = signDate;
        this.endDate = endDate;
    }
}
