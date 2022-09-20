package edu.polytechnique.inf553;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Person {

    private String name;
    private int id;
    private String role;
    private List<Program> programs;
    private boolean valid;
    private String email;
    private boolean hasInternship;
    private Timestamp dateFiche;
    private Timestamp dateReport;
    private Timestamp dateSlides;

    public Person(String name, int id, String role, boolean valid, String email) {
        this.name = name;
        this.id = id;
        this.role = role;
        this.valid = valid;
        this.programs = new ArrayList<>();
        this.email = email;
        this.hasInternship = false;
    }

    public Person(int id, String name, String email) {
        this.name = name;
        this.id = id;
        this.email = email;
    }

    public Person(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean getValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public void addProgram(Program program) {
        programs.add(program);
    }

    public List<Program> getPrograms() {
        return programs;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean hasInternship() {
        return this.hasInternship;
    }


    public Timestamp getDateFiche() {
        return this.dateFiche;
    }

    public void setDateFiche(Timestamp dateFiche) {
        this.dateFiche = dateFiche;
    }

    public Timestamp getDateReport() {
        return this.dateReport;
    }

    public void setDateReport(Timestamp dateReport) {
        this.dateReport = dateReport;
    }

    public Timestamp getDateSlides() {
        return this.dateSlides;
    }

    public void setDateSlides(Timestamp dateSlides) {
        this.dateSlides = dateSlides;
    }


    @Override
    public String toString() {
        return "Person{" + "name='" + name + '\'' + ", id=" + id + ", role='" + role + '\'' + ", programs=" + programs + ", valid=" + valid + ", email='" + email + '\''  + ", dateFiche=" + dateFiche + ", dateReport=" + dateReport + ", dateSlides=" + dateSlides + '}';
    }
}
