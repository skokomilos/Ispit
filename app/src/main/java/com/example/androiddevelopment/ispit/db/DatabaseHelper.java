package com.example.androiddevelopment.ispit.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.androiddevelopment.ispit.db.model.Kontakt;
import com.example.androiddevelopment.ispit.db.model.Telefon;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by androiddevelopment on 20.3.17..
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME    = "priprema.db";
    private static final int    DATABASE_VERSION = 1;

    private Dao<Kontakt, Integer> mTelefonDao = null;
    private Dao<Telefon, Integer> mKontaktDao = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Telefon.class);
            TableUtils.createTable(connectionSource, Kontakt.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Telefon.class, true);
            TableUtils.dropTable(connectionSource, Kontakt.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Dao<Telefon,Integer> getTelefonDao() throws SQLException {
        if (mKontaktDao == null) {
            mKontaktDao = getDao(Kontakt.class);
        }

        return mKontaktDao;
    }

    public Dao<Kontakt,Integer> getKontaktDao() throws SQLException {
        if (mTelefonDao == null) {
            mTelefonDao = getDao(Telefon.class);
        }

        return mTelefonDao;
    }



    @Override
    public void close() {
        mTelefonDao = null;
        mKontaktDao = null;

        super.close();
    }
}
