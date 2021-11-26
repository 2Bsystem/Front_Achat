package a2bsystem.com.achat.Models;

import java.io.Serializable;

public class Article implements Serializable {

    private long id;
    private String artnr;
    private String q_gcar_lib1;
    private String momskod;

    public Article() {
    }

    public Article(String artnr, String momskod, String q_gcar_lib1) {
        this.artnr = artnr;
        this.q_gcar_lib1 = q_gcar_lib1;
        this.momskod = momskod;
    }

    public String getArtnr() {
        return artnr;
    }

    public void setArtnr(String artnr) {
        this.artnr = artnr;
    }

    public String getQ_gcar_lib1() {
        return q_gcar_lib1;
    }

    public void setQ_gcar_lib1(String q_gcar_lib1) {
        this.q_gcar_lib1 = q_gcar_lib1;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMomskod() {
        return momskod;
    }

    public void setMomskod(String momskod) {
        this.momskod = momskod;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", artnr='" + artnr + '\'' +
                ", q_gcar_lib1='" + q_gcar_lib1 + '\'' +
                ", momskod='" + momskod + '\'' +
                '}';
    }
}
