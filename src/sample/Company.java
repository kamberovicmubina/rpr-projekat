package sample;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class Company {
    private String name;
    private String address;
    private ObservableList<Department> departments;
    private ObservableList<Employee> employees;
    private static ObservableList<Client> clients = FXCollections.observableArrayList();
    private Person owner;
    private ObservableList<String> services;
    private ObjectProperty<Client> clickedClient = new SimpleObjectProperty<>();

    public ObjectProperty<Client> clickedClientProperty()  {
        return clickedClient;
    }

    public Client getClickedClient() {
        return clickedClient.get();
    }

    public void setClickedClient(Client c) {
        clickedClient.set(c);
    }

    public ObservableList<String> getServices() {
        return services;
    }

    public void setServices(ObservableList<String> services) {
        this.services = services;
    }

    public ObservableList<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(ObservableList<Department> departments) {
        this.departments = departments;
    }

    public ObservableList<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(ObservableList<Employee> employees) {
        this.employees = employees;
    }

    public ObservableList<Client> getClients() {
        return clients;
    }

    public void setClients(ObservableList<Client> clients) {
        this.clients = clients;
    }

    public Company(String name, String address, ObservableList<Department> departments, ObservableList<Employee> employees, ObservableList<Client> clients, Person owner, ObservableList<String> services) {
        this.name = name;
        this.address = address;
        this.departments = departments;
        this.employees = employees;
        this.clients = clients;
        this.owner = owner;
        this.services = services;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public static void addClient(Client newClient) {
        clients.add(newClient);
    }

    public void removeClient() {
        int i = 0;
        for (Client c : getClients()) {
            if (c.equals(clickedClient.getValue())) {
                clients.remove(i, i+1); // we delete ith client from the list
                clickedClient.set(null);
                break;
            }
            i++;
        }
    }

    public void changeClient(Client oldClient, Client newClient) {
        oldClient.setName(newClient.getName());
        oldClient.setDateOfBirth(newClient.getDateOfBirth());
        oldClient.setAddress(newClient.getAddress());
        oldClient.setPhoneNumber(newClient.getPhoneNumber());
        oldClient.setEMail(newClient.getEMail());
    }

}