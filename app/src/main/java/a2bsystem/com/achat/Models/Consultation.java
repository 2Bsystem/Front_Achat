package a2bsystem.com.achat.Models;

import java.io.Serializable;

public class Consultation implements Serializable {
    private String site;
    private String ftgnr;
    private String fourni;
    private String depot;
    private String depotCode;
    private String dateRecep;
    private String tonnage;
    private String montant;
    private boolean statut;

    public Consultation(String site, String ftgnr, String fourni, String depot, String depotCode, String dateRecep, String tonnage, String montant, boolean statut) {
        this.site = site;
        this.ftgnr = ftgnr;
        this.fourni = fourni;
        this.depot = depot;
        this.depotCode = depotCode;
        this.dateRecep = dateRecep;
        this.tonnage = tonnage;
        this.montant = montant;
        this.statut = statut;
    }

    public Consultation() {
    }


    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getFourni() {
        return fourni;
    }

    public void setFourni(String fourni) {
        this.fourni = fourni;
    }

    public String getDateRecep() {
        return dateRecep;
    }

    public void setDateRecep(String dateRecep) {
        this.dateRecep = dateRecep;
    }

    public String getTonnage() {
        return tonnage;
    }

    public void setTonnage(String tonnage) {
        this.tonnage = tonnage;
    }

    public String getMontant() {
        return montant;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public boolean getStatut() {
        return statut;
    }

    public void setStatut(boolean statut) {
        this.statut = statut;
    }

    public String getDepot() {
        return depot;
    }

    public void setDepot(String depot) {
        this.depot = depot;
    }

    public String getFtgnr() {
        return ftgnr;
    }

    public void setFtgnr(String ftgnr) {
        this.ftgnr = ftgnr;
    }

    public String getDepotCode() {
        return depotCode;
    }

    public void setDepotCode(String depotCode) {
        this.depotCode = depotCode;
    }

    public boolean isStatut() {
        return statut;
    }
}
