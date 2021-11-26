package a2bsystem.com.achat.Activities.consultation;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import a2bsystem.com.achat.Activities.saisie.achat.SaisieAchat;
import a2bsystem.com.achat.Adapters.ConsultationAdapter;
import a2bsystem.com.achat.Helper;
import a2bsystem.com.achat.Models.Consultation;
import a2bsystem.com.achat.R;

public class Consultations extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    private ArrayList<Consultation> consultations;

    private String[] sites;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultation);

        initFields();
        initListeners();
        setGetSites();
        setGetAchats();

    }

    @Override
    protected void onResume() {
        super.onResume();
        setGetSites();
        setGetAchats();
    }

    private void initFields(){
        bottomNavigationView = findViewById(R.id.bottom_navigation_cons_add);
    }

    private void initListeners(){

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.add_onglet:

                        selectSite();

                        break;
                }
                return false;
            }
        });
    }


    private void loadConsultations() {


        ListView listView = findViewById(R.id.consultation_listview);


        ConsultationAdapter adapter = new ConsultationAdapter(Consultations.this, R.layout.consultation_lines, consultations);
        listView.setAdapter(adapter);

        // Ecoute des clicks sur les lignes
        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(consultations.get(position).getStatut()){
                    Intent SaisieActivity = new Intent(Consultations.this, SaisieAchat.class);
                    SaisieActivity.putExtra("consultation",consultations.get(position));
                    startActivity(SaisieActivity);
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                deleteConsultation(position);
                return true;
            }
        });

    }

    private void deleteConsultation(final int pos){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Suppression");
        builder.setMessage("Confirmer la suppression?");
        builder.setPositiveButton("Non", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                consultations.get(pos).setStatut(false);
                loadConsultations();
            }
        });
        builder.show();
    }

    private void selectSite() {

        if (sites == null || sites.length == 0) {

            showError("Pas de site", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
        }

        else if (sites.length == 1) {

            Consultation consultation = new Consultation(sites[0],"","","","", date,"","",true);
            Intent NewSaisieActivity = new Intent(Consultations.this, SaisieAchat.class);
            NewSaisieActivity.putExtra("consultation", consultation);
            startActivity(NewSaisieActivity);
        }
        else {
            AlertDialog.Builder b = new AlertDialog.Builder(this);
            b.setTitle("Choix du site");
            b.setItems(sites, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                    Consultation consultation = new Consultation(sites[which],"","","","", date,"","",true);
                    Intent NewSaisieActivity = new Intent(Consultations.this, SaisieAchat.class);
                    NewSaisieActivity.putExtra("consultation", consultation);
                    startActivity(NewSaisieActivity);
                }

            });

            b.show();
        }
    }


    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }


    private void lockUI()
    {
        bottomNavigationView.setEnabled(false);
    }

    private void unlockUI()
    {
        bottomNavigationView.setEnabled(true);
    }

    public void showError(String message, DialogInterface.OnClickListener listener)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Erreur");
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", listener);
        builder.show();
    }

    private void setGetSites() {
        RequestParams params = Helper.GenerateParams(Consultations.this);
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_YEAR, 1);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        params.put("date_recep",sdf.format(c.getTime()));

        String URL = Helper.GenereateURI(Consultations.this, params, "getsocietes");

        //Verouillage de l'interface
        lockUI();

        // Call API JEE

        GetSites task = new GetSites();
        task.execute(new String[] { URL });
    }



    private void setGetAchats() {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_YEAR, 1);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        RequestParams params = Helper.GenerateParams(Consultations.this);
        params.put("date_recep",sdf.format(c.getTime()));
        params.put("acheteur", Helper.acheteur);
        String URL = Helper.GenereateURI(Consultations.this, params, "getachats");

        System.out.println("aaaa " + URL);

        //Verouillage de l'interface
        lockUI();

        // Call API JEE

        GetAchats task = new GetAchats();
        task.execute(new String[] { URL });
    }

    private class GetSites extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String output = null;
            for (String url : urls) {
                output = getOutputFromUrl(url);
            }
            return output;
        }

        private String getOutputFromUrl(String url) {
            StringBuffer output = new StringBuffer("");
            try {
                InputStream stream = getHttpConnection(url);
                BufferedReader buffer = new BufferedReader(
                        new InputStreamReader(stream));
                String s = "";
                while ((s = buffer.readLine()) != null)
                    output.append(s);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return output.toString();
        }

        // Makes HttpURLConnection and returns InputStream
        private InputStream getHttpConnection(String urlString)
                throws IOException {
            InputStream stream = null;
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();

            try {
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                httpConnection.setRequestMethod("GET");
                httpConnection.connect();

                if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    stream = httpConnection.getInputStream();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return stream;
        }

        @Override
        protected void onPostExecute(String output) {
            unlockUI();
            System.out.println(output);
            if(output.equalsIgnoreCase("-1"))
            {
                showError("Impossible de récupérer les sites", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
            }
            else {

                try {

                    JSONArray jsonArray = new JSONArray(output);
                    consultations = new ArrayList<>();

                    sites = new String[jsonArray.length()];

                    date = jsonArray.getJSONObject(0).getString("Date");

                    for(int i=0; i<jsonArray.length();i++)
                    {
                        try {
                            sites[i] = jsonArray.getJSONObject(i).getString("FtgNamn");
                        }catch(Exception ex) { }
                    }


                } catch (Exception ex) {}
            }
        }
    }


    private class GetAchats extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String output = null;
            for (String url : urls) {
                output = getOutputFromUrl(url);
            }
            return output;
        }

        private String getOutputFromUrl(String url) {
            StringBuffer output = new StringBuffer("");
            try {
                InputStream stream = getHttpConnection(url);
                BufferedReader buffer = new BufferedReader(
                        new InputStreamReader(stream));
                String s = "";
                while ((s = buffer.readLine()) != null)
                    output.append(s);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return output.toString();
        }

        // Makes HttpURLConnection and returns InputStream
        private InputStream getHttpConnection(String urlString)
                throws IOException {
            InputStream stream = null;
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();

            try {
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                httpConnection.setRequestMethod("GET");
                httpConnection.connect();

                if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    stream = httpConnection.getInputStream();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return stream;
        }

        @Override
        protected void onPostExecute(String output) {
            unlockUI();
            if(output.equalsIgnoreCase("-1"))
            {
                showError("Impossible de récupérer les achats", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
            }
            else {

                try {

                    JSONArray jsonArray = new JSONArray(output);
                    consultations = new ArrayList<>();

                    for(int i = 0 ; i<jsonArray.length() ; i++){

                        String montant = String.format("%.2f",Double.parseDouble(jsonArray.getJSONObject(i).getString("Montant")));

                        consultations.add(new Consultation(
                                jsonArray.getJSONObject(i).getString("Societe"),
                                jsonArray.getJSONObject(i).getString("CodeFournisseur"),
                                jsonArray.getJSONObject(i).getString("Fournisseur"),
                                jsonArray.getJSONObject(i).getString("Depot"),
                                jsonArray.getJSONObject(i).getString("DepotCode"),
                                jsonArray.getJSONObject(i).getString("Date"),
                                Double.toString(round(Double.parseDouble(jsonArray.getJSONObject(i).getString("Colis")),1)),
                                montant,
                                true)
                        );
                    }
                    loadConsultations();


                } catch (Exception ex) {System.out.println(ex);}
            }
        }
    }
}
