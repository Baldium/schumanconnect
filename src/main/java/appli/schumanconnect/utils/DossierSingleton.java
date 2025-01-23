package appli.schumanconnect.utils;

import appli.schumanconnect.model.Dossier;

public class DossierSingleton {
    private static DossierSingleton instance;
    private Dossier dossierId;

    private DossierSingleton() {}

    public static DossierSingleton getInstance() {
        if (instance == null) {
            instance = new DossierSingleton();
        }
        return instance;
    }

    public void setDossierId(Dossier dossierId) {
        this.dossierId = dossierId;
    }

    public Dossier getDossierId() {
        return dossierId;
    }
}
