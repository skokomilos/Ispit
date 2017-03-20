package com.example.androiddevelopment.ispit;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androiddevelopment.ispit.db.DatabaseHelper;
import com.example.androiddevelopment.ispit.db.model.Kontakt;
import com.example.androiddevelopment.ispit.db.model.Telefon;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by androiddevelopment on 20.3.17..
 */

public class SecondActivity extends AppCompatActivity {

    private SharedPreferences preferences;
    private DatabaseHelper databaseHelper;
    private Kontakt kontakt;
    private FirstActivity firstActivity;
    public static String TOAST = "notif_toast";
    public static String NOTIFICATION = "notif_status";

    private EditText ime;
    private EditText prezime;
    private EditText adresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.show();
        }
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        int kljuc = getIntent().getExtras().getInt(FirstActivity.ACTOR_KEY);

        try {
            kontakt = getDatabaseHelper().getKontaktDao().queryForId(kljuc);

            TextView ime = (EditText) findViewById(R.id.tv_ime);
            TextView prezime = (EditText) findViewById(R.id.tv_prezime);
            TextView adresa = (EditText) findViewById(R.id.tv_adresa);

            ime.setText(kontakt.getImeKontakta());
            prezime.setText(kontakt.getPrezimeKontakta());
            adresa.setText(kontakt.getAdresaKontakta());


        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            final ListView listView = (ListView) findViewById(R.id.lv_spisakTelefona);
            final List<Telefon> list = getDatabaseHelper().getTelefonDao().queryBuilder()
                    .where()
                    .eq(Telefon.FIELD_TELEFON_KONTAKT, kontakt.getIdKontakta())
                    .query();
            final ListAdapter adapter = new ArrayAdapter<>(this, R.layout.list_item, list);
            listView.setAdapter(adapter);

            //duzim klikom na bilo koji item, tj spisak filmova, se brise cela lista nakon potvrde u dialogu

            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(SecondActivity.this);
                    dialog.setTitle("Da li zelite da obrisete ovaj film ?");
                    dialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                getDatabaseHelper().getTelefonDao().delete(list);
                                refresh();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            dialog.dismiss();
                        }
                    });
                    dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                    return false;
                }
            });
            //implementacija klika na item, ispisuje toast poruku u kojoj su detalji unetog filma
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Telefon t = (Telefon) listView.getItemAtPosition(position);
                    Toast.makeText(SecondActivity.this, "Kucni telefon: "+t.getKucni()+"\nMobilni telefon: "+t.getMobilni()+"\nPoslovni telfon"+t.getPoslovni(),Toast.LENGTH_LONG).show();
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_second, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_create:
                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.dialog_telefon);

                Button ok  = (Button)dialog.findViewById(R.id.ok);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText mobilni = (EditText)dialog.findViewById(R.id.et_mobilni);
                        EditText kucni = (EditText)dialog.findViewById(R.id.et_kucni);
                        EditText poslovni = (EditText)dialog.findViewById(R.id.et_poslovni);

                        Telefon f = new Telefon();
                        f.setMobilni(mobilni.getText().toString());
                        f.setKucni(kucni.getText().toString());
                        f.setPoslovni(poslovni.getText().toString());
                        f.setKontakt(kontakt);

                        try {
                            getDatabaseHelper().getTelefonDao().create(f);
                            showMessage("Dodat film u listu");
                            refresh();
                        } catch (SQLException e){
                            e.printStackTrace();
                        }
                        refresh();
                        showMessage("New added");
                        dialog.dismiss();
                    }
                });
                final Button cancel = (Button) dialog.findViewById(R.id.cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;
            case R.id.action_delete:
                try {
                    getDatabaseHelper().getKontaktDao().delete(kontakt);
                    showMessage("Glumac obrisan");
                } catch (SQLException e){
                    e.printStackTrace();
                }
                firstActivity.refresh();
                finish();
                break;
            case R.id.action_edit:


                kontakt.setImeKontakta(ime.getText().toString());
                kontakt.setPrezimeKontakta(prezime.getText().toString());
                kontakt.setAdresaKontakta(adresa.getEditableText().toString());

                ime.setText(kontakt.getImeKontakta());
                prezime.setText(kontakt.getPrezimeKontakta());
                adresa.setText(kontakt.getAdresaKontakta());

                try{
                    getDatabaseHelper().getKontaktDao().update(kontakt);
                    showMessage("Kontakt detail updated");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            // ovo sam radio jer nikako drugacije nisam mogao da implmentiram back dugme u SecondActivity
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void refresh() {
        ListView listview = (ListView) findViewById(R.id.lv_spisakTelefona);

        if (listview != null){
            ArrayAdapter<Telefon> adapter = (ArrayAdapter<Telefon>) listview.getAdapter();

            if(adapter!= null)
            {
                try {
                    adapter.clear();
                    List<Telefon> list = getDatabaseHelper().getTelefonDao().queryBuilder()
                            .where()
                            .eq(Telefon.FIELD_TELEFON_KONTAKT, kontakt.getIdKontakta())
                            .query();
                    adapter.addAll(list);
                    adapter.notifyDataSetChanged();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void statusMessage(String message){
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.ic_stat_buy);
        builder.setContentTitle("pripremni test");
        builder.setContentText(message);
        notificationManager.notify(1, builder.build());

    }

    public void  showMessage(String message){
        boolean toast = preferences.getBoolean(TOAST, false);
        boolean notification = preferences.getBoolean(NOTIFICATION, false);

        if (toast){
            Toast.makeText(SecondActivity.this, message, Toast.LENGTH_SHORT).show();
        }
        if (notification){
            statusMessage(message);
        }
    }


    //Metoda koja komunicira sa bazom podataka
    public DatabaseHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        // nakon rada sa bazo podataka potrebno je obavezno
        //osloboditi resurse!
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

}
