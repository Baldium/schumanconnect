package appli.schumanconnect.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Fourniture {
    private final SimpleStringProperty fournisseur;
    private final SimpleStringProperty outil;
    private final SimpleIntegerProperty quantite;

    public Fourniture(String fournisseur, String outil, int quantite) {
        this.fournisseur = new SimpleStringProperty(fournisseur);
        this.outil = new SimpleStringProperty(outil);
        this.quantite = new SimpleIntegerProperty(quantite);
    }

    public String getFournisseur() {
        return fournisseur.get();
    }

    public SimpleStringProperty fournisseurProperty() {
        return fournisseur;
    }

    public String getOutil() {
        return outil.get();
    }

    public SimpleStringProperty outilProperty() {
        return outil;
    }

    public int getQuantite() {
        return quantite.get();
    }

    public SimpleIntegerProperty quantiteProperty() {
        return quantite;
    }
}