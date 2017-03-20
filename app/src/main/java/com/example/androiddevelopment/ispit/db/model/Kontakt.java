package com.example.androiddevelopment.ispit.db.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by androiddevelopment on 20.3.17..
 */

@DatabaseTable(tableName = Kontakt.TABLE_NAME_IMENIK)
public class Kontakt {

    public static final String TABLE_NAME_IMENIK = "kontakti";
    public static final String FIELD_KONTAKT_ID = "id";
    public static final String FIELD_KONTAKT_IME = "ime";
    public static final String FIELD_KONTAKT_PREZIME = "prezime";
    public static final String FIELD_KONTAKT_ADRESA = "adresa";
    public static final String FIELD_KONTAKT_TELEFONI = "telefoni";


    @DatabaseField(columnName = FIELD_KONTAKT_ID, generatedId = true)
    private int idKontakta;

    @DatabaseField(columnName = FIELD_KONTAKT_IME)
    private String imeKontakta;

    @DatabaseField(columnName = FIELD_KONTAKT_PREZIME)
    private String prezimeKontakta;

    @DatabaseField(columnName = FIELD_KONTAKT_ADRESA)
    private String adresaKontakta;

    @ForeignCollectionField(columnName = FIELD_KONTAKT_TELEFONI, eager = true)
    private ForeignCollection<Telefon> telefoni;

    public Kontakt() {
    }


    public int getIdKontakta() {
        return idKontakta;
    }

    public void setIdKontakta(int idKontakta) {
        this.idKontakta = idKontakta;
    }

    public String getImeKontakta() {
        return imeKontakta;
    }

    public void setImeKontakta(String imeKontakta) {
        this.imeKontakta = imeKontakta;
    }

    public String getPrezimeKontakta() {
        return prezimeKontakta;
    }

    public void setPrezimeKontakta(String prezimeKontakta) {
        this.prezimeKontakta = prezimeKontakta;
    }

    public String getAdresaKontakta() {
        return adresaKontakta;
    }

    public void setAdresaKontakta(String adresaKontakta) {
        this.adresaKontakta = adresaKontakta;
    }

    public ForeignCollection<Telefon> getTelefoni() {
        return telefoni;
    }

    public void setTelefoni(ForeignCollection<Telefon> telefoni) {
        this.telefoni = telefoni;
    }

    @Override
    public String toString() {
        return  imeKontakta;
    }
}
