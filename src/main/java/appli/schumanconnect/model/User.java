package appli.schumanconnect.model;

import java.util.Objects;

public class User {
    private int id_user;
    private String nom;
    private String prenom;
    private String email;
    private String password;
    private Role role;
    private String last_log;

    public User(int id_user, String nom, String prenom, String email, String password, Role role, String last_log) {
        this.id_user = id_user;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.password = password;
        this.role = role;
        this.last_log = last_log;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getLast_log() {
        return last_log;
    }

    public void setLast_log(String last_log) {
        this.last_log = last_log;
    }

    @Override
    public String toString() {
        return "User{" +
                "id_user=" + id_user +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", last_log='" + last_log + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id_user == user.id_user &&
                Objects.equals(nom, user.nom) &&
                Objects.equals(prenom, user.prenom) &&
                Objects.equals(email, user.email) &&
                Objects.equals(password, user.password) &&
                role == user.role &&
                Objects.equals(last_log, user.last_log);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_user, nom, prenom, email, password, role, last_log);
    }
}
