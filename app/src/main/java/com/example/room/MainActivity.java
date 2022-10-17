package com.example.room;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText editTextFornavn, editTextEtternavn;
    private TextView visAlle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextFornavn = findViewById(R.id.fornavn);
        editTextEtternavn = findViewById(R.id.etternavn);
        visAlle = findViewById(R.id.visalle);

        findViewById(R.id.lagre).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUser();
            }
        });

        findViewById(R.id.vis).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showUser();
            }
        });
    }

    private void saveUser() {
        final String fornavn = editTextFornavn.getText().toString().trim();
        final String etternavn = editTextEtternavn.getText().toString().trim();

        class SaveUser extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids){

                User user = new User();
                user.setFirstName(fornavn);
                user.setLastName(etternavn);

                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .userDao()
                        .insert(user);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
            }
        }
        SaveUser st = new SaveUser();
        st.execute();
    }
    private void showUser() {
        class ShowUser extends AsyncTask<Void, Void, List<User> >{
            @Override
            protected List<User> doInBackground(Void... voids) {
                List<User> alle=DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .userDao()
                        .getAll();
                return alle;
            }
            @Override
            protected void onPostExecute(List<User> alle) {
                super.onPostExecute(alle);
                Toast.makeText(getApplicationContext(), "Vist", Toast.LENGTH_LONG).show();
                String tekst="";
                for (User bruker : alle) {
                    tekst = tekst + "Fornavn: " +
                            bruker.getFirstName() + " ,Etternavn: " +
                            bruker.getLastName();
                }
                visAlle.setText(tekst);
            }
        }
        ShowUser su = new ShowUser();
        su.execute();
    }
}