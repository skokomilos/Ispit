package com.example.androiddevelopment.ispit.db.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by androiddevelopment on 20.3.17..
 */

@DatabaseTable(tableName = Telefon.TABLE_NAME_IMENIK)
public class Telefon {

    public static final String TABLE_NAME_IMENIK = "telefon";
    public static final String FIELD_TELEFON_ID = "id";
    public static final String FIELD_TELEFON_MOBILNI = "mobilni";
    public static final String FIELD_TELEFON_KUCNI = "kucni";
    public static final String FIELD_TELEFON_POSLOVNI = "poslovni";
    public static final String FIELD_TELEFOn_KONTAKT = "kontakt";


    @DatabaseField(columnName = FIELD_TELEFON_ID, generatedId = true)
    private int id;

    @DatabaseField(columnName = FIELD_TELEFON_MOBILNI)
    private String mobilni;

    @DatabaseField(columnName = FIELD_TELEFON_KUCNI)
    private String kucni;

    @DatabaseField(columnName = FIELD_TELEFON_POSLOVNI)
    private String poslovni;

    @DatabaseField(columnName = FIELD_TELEFOn_KONTAKT, foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private Kontakt kontakt;


    public Telefon() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMobilni() {
        return mobilni;
    }

    public void setMobilni(String mobilni) {
        this.mobilni = mobilni;
    }

    public String getKucni() {
        return kucni;
    }

    public void setKucni(String kucni) {
        this.kucni = kucni;
    }

    public String getPoslovni() {
        return poslovni;
    }

    public void setPoslovni(String poslovni) {
        this.poslovni = poslovni;
    }

    public Kontakt getKontakt() {
        return kontakt;
    }

    public void setKontakt(Kontakt kontakt) {
        this.kontakt = kontakt;
    }

    @Override
    public String toString() {
        return "Telefon{" +
                "kucni=" + kucni +
                ", mobilni=" + mobilni +
                ", poslovni=" + poslovni +
                '}';
    }
}
