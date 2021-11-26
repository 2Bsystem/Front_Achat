package a2bsystem.com.achat.Models;

import java.io.Serializable;

public class Achat implements Serializable {
    private String ftgnr;
    private String fourni;
    private String dateRecep;
    private String site;
    private String depotCode;
    private String code;
    private String name;
    private Integer poidsVar;
    private Integer pcesVar;
    private String modeSaisie;
    private Double colis;
    private Double pieces;
    private Double pdsBrut;
    private Double tare;
    private Double pdsNet;
    private Double piecesU;
    private Double pdsBrutU;
    private Double tareU;
    private Double pdsNetU;
    private String uniteFact;
    private Double pu;
    private Double montant;
    private String bestnr;
    private String bestradnr;
    private int statut;

    public Achat() {
    }

    public Achat(String ftgnr, String fourni, String dateRecep, String site,String depotCode, String code, String name, Integer poidsVar, Integer pcesVar, String modeSaisie, Double colis, Double pieces, Double pdsBrut, Double tare, Double pdsNet, Double piecesU, Double pdsBrutU, Double tareU, Double pdsNetU, String uniteFact, Double pu, Double montant, String bestnr, String bestradnr, int statut) {
        this.ftgnr = ftgnr;
        this.fourni = fourni;
        this.dateRecep = dateRecep;
        this.site = site;
        this.depotCode = depotCode;
        this.code = code;
        this.name = name;
        this.poidsVar = poidsVar;
        this.pcesVar = pcesVar;
        this.modeSaisie = modeSaisie;
        this.colis = colis;
        this.pieces = pieces;
        this.pdsBrut = pdsBrut;
        this.tare = tare;
        this.pdsNet = pdsNet;
        this.piecesU = piecesU;
        this.pdsBrutU = pdsBrutU;
        this.tareU = tareU;
        this.pdsNetU = pdsNetU;
        this.uniteFact = uniteFact;
        this.pu = pu;
        this.montant = montant;
        this.bestnr = bestnr;
        this.bestradnr = bestradnr;
        this.statut = statut;
    }


    public String getFourni() {
        return fourni.trim();
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

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPoidsVar() {
        return poidsVar;
    }

    public void setPoidsVar(Integer poidsVar) {
        this.poidsVar = poidsVar;
    }

    public Integer getPcesVar() {
        return pcesVar;
    }

    public void setPcesVar(Integer pcesVar) {
        this.pcesVar = pcesVar;
    }

    public String getModeSaisie() {
        return modeSaisie;
    }

    public void setModeSaisie(String modeSaisie) {
        this.modeSaisie = modeSaisie;
    }

    public Double getColis() {
        return colis;
    }

    public void setColis(Double colis) {
        this.colis = colis;
    }

    public Double getPieces() {
        return pieces;
    }

    public void setPieces(Double pieces) {
        this.pieces = pieces;
    }

    public Double getPdsNet() {
        return pdsNet;
    }

    public void setPdsNet(Double pdsNet) {
        this.pdsNet = pdsNet;
    }

    public Double getTare() {
        return tare;
    }

    public void setTare(Double tare) {
        this.tare = tare;
    }


    public Double getPiecesU() {
        return piecesU;
    }

    public void setPiecesU(Double piecesU) {
        this.piecesU = piecesU;
    }

    public Double getPdsNetU() {
        return pdsNetU;
    }

    public void setPdsNetU(Double pdsNetU) {
        this.pdsNetU = pdsNetU;
    }

    public Double getTareU() {
        return tareU;
    }

    public void setTareU(Double tareU) {
        this.tareU = tareU;
    }

    public String getUniteFact() {
        return uniteFact;
    }

    public void setUniteFact(String uniteFact) {
        this.uniteFact = uniteFact;
    }

    public Double getPu() {
        return pu;
    }

    public void setPu(Double pu) {
        this.pu = pu;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public int getStatut() {
        return statut;
    }

    public Double getPdsBrut() {
        return pdsBrut;
    }

    public void setPdsBrut(Double pdsBrut) {
        this.pdsBrut = pdsBrut;
    }

    public Double getPdsBrutU() {
        return pdsBrutU;
    }

    public void setPdsBrutU(Double pdsBrutU) {
        this.pdsBrutU = pdsBrutU;
    }

    public String getBestnr() {
        return bestnr;
    }

    public void setBestnr(String bestnr) {
        this.bestnr = bestnr;
    }

    public String getBestradnr() {
        return bestradnr;
    }

    public void setBestradnr(String bestradnr) {
        this.bestradnr = bestradnr;
    }

    public void setStatut(int statut) {
        this.statut = statut;
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
}
