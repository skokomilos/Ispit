package com.example.androiddevelopment.ispit;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.Toast;

import com.example.androiddevelopment.ispit.db.DatabaseHelper;
import com.example.androiddevelopment.ispit.db.model.Kontakt;
import com.example.androiddevelopment.ispit.db.model.Telefon;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.List;

public class FirstActivity extends AppCompatActivity {

    private SharedPreferences preferences;
    private DatabaseHelper databaseHelper;
    public static String ACTOR_KEY = "ACTOR_KEY";
    public static String TOAST = "notif_toast";
    public static String NOTIFICATION = "notif_status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.show();
        }

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        final ListView listView = (ListView)findViewById(R.id.lv_listaKontakta);

        try {
            List<Kontakt> list = getDatabaseHelper().getKontaktDao().queryForAll();
            ListAdapter adapter = new ArrayAdapter<>(FirstActivity.this,R.layout.list_item,list);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Kontakt kontakt = (Kontakt) listView.getItemAtPosition(position);
                    Intent intent = new Intent(FirstActivity.this,SecondActivity.class);
                    intent.putExtra(ACTOR_KEY,kontakt.getIdKontakta());
                    startActivity(intent);
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_first, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_create:

                final Dialog dialog = new Dialog(FirstActivity.this);
                dialog.setTitle("Osnovni podaci o glumcu");
                dialog.setContentView(R.layout.dialog_kontakt);

                Button ok = (Button)dialog.findViewById(R.id.ok);

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        EditText ime = (EditText) dialog.findViewById(R.id.et_ime);
                        EditText prezime = (EditText) dialog.findViewById(R.id.et_prezime);
                        EditText adresa = (EditText) dialog.findViewById(R.id.et_adresa);

                        Kontakt k = new Kontakt();
                        k.setImeKontakta(ime.getText().toString());
                        k.setPrezimeKontakta(prezime.getText().toString());
                        k.setAdresaKontakta(adresa.getText().toString());

                        try{
                            getDatabaseHelper().getKontaktDao().create(k);
                            showMessage("Glumac dodat na listu");
                            refresh();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
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
            case R.id.action_about:

                AlertDialog.Builder aboutDialog = new AlertDialog.Builder(FirstActivity.this);
                aboutDialog.setIcon(R.drawable.ic_action_about);
                aboutDialog.setTitle("autor aplikacije");
                aboutDialog.setMessage("milos skoko");
                aboutDialog.setCancelable(false);

                aboutDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                aboutDialog.show();
                break;
            case R.id.action_settings:
                startActivity(new Intent(FirstActivity.this,SettingsActivity.class));
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    public void  showMessage(String message) {
        boolean toast = preferences.getBoolean(TOAST, false);
        boolean notification = preferences.getBoolean(NOTIFICATION, false);

        if (toast) {
            Toast.makeText(FirstActivity.this, message, Toast.LENGTH_SHORT).show();
        }
        if (notification) {
            statusMessage(message);
        }
    }

    private void statusMessage(String message){
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.ic_notif_icon);
        builder.setContentTitle("pripremni test");
        builder.setContentText(message);
        notificationManager.notify(1, builder.build());
    }

    void refresh() {
        ListView listview = (ListView) findViewById(R.id.lv_listaKontakta);

        if (listview != null){

            ArrayAdapter<Kontakt> adapter = (ArrayAdapter<Kontakt>) listview.getAdapter();

            if(adapter!= null)
            {
                try {
                    adapter.clear();
                    List<Kontakt> list = getDatabaseHelper().getKontaktDao().queryForAll();
                    adapter.addAll(list);
                    adapter.notifyDataSetChanged();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

    public DatabaseHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }
}
