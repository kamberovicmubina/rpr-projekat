package sample;

import java.util.List;

public class Company {
    private String name;
    private String address;
    private List<Department> departments;
    private List<Employee> employees;
    private List<Client> clients;
    private Person owner;

    public Company(String name, String address, List<Department> departments, List<Employee> employees, Person owner) {
        this.name = name;
        this.address = address;
        this.departments = departments;
        this.employees = employees;
        this.owner = owner;
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

    public List<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    /*public void addClient(Client newClient) {
        clients.add(newClient);
    }

    public void removeClient(Client client) {
        clients.remove(client);
    }

    public void changeClient(Client oldClient, Client newClient) {

    }*/

}
