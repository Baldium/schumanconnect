package appli.schumanconnect.model;

public class Dossier {

    private int idDossier;
    private String dateCreation;
    private String filiereInteret;
    private String motivEtudiant;
    private int statut;
    private int refEtudiant;

    public Dossier(int idDossier, String dateCreation, String filiereInteret, String motivEtudiant, int statut, int refEtudiant) {
        this.idDossier = idDossier;
        this.dateCreation = dateCreation;
        this.filiereInteret = filiereInteret;
        this.motivEtudiant = motivEtudiant;
        this.statut = statut;
        this.refEtudiant = refEtudiant;
    }

    public int getIdDossier() {
        return idDossier;
    }

    public void setIdDossier(int idDossier) {
        this.idDossier = idDossier;
    }

    public String getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getFiliereInteret() {
        return filiereInteret;
    }

    public void setFiliereInteret(String filiereInteret) {
        this.filiereInteret = filiereInteret;
    }

    public String getMotivEtudiant() {
        return motivEtudiant;
    }

    public void setMotivEtudiant(String motivEtudiant) {
        this.motivEtudiant = motivEtudiant;
    }

    public int getStatut() {
        return statut;
    }

    public void setStatut(int statut) {
        this.statut = statut;
    }

    public int getRefEtudiant() {
        return refEtudiant;
    }

    public void setRefEtudiant(int refEtudiant) {
        this.refEtudiant = refEtudiant;
    }

    @Override
    public String toString() {
        return "Dossier{" +
                "idDossier=" + idDossier +
                ", dateCreation='" + dateCreation + '\'' +
                ", filiereInteret='" + filiereInteret + '\'' +
                ", motivEtudiant='" + motivEtudiant + '\'' +
                ", statut=" + statut +
                ", refEtudiant=" + refEtudiant +
                '}';
    }
}
