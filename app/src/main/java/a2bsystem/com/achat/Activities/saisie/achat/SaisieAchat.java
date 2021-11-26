package a2bsystem.com.achat.Activities.saisie.achat;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import a2bsystem.com.achat.Activities.saisie.ligne.SaisieLigneAchat;
import a2bsystem.com.achat.Adapters.AchatAdapter;
import a2bsystem.com.achat.Helper;
import a2bsystem.com.achat.Models.Achat;
import a2bsystem.com.achat.Models.Consultation;
import a2bsystem.com.achat.R;

public class SaisieAchat extends AppCompatActivity {

    private EditText eFourni;
    private Spinner sDepot;
    private Spinner sAcheteur;
    private EditText eDate;
    private EditText eSite;
    BottomNavigationView bottomNavigationView;
    private Consultation consultation;

    private int year, month, day;

    // Variables
    private String[] Fournisseurs;
    private ArrayList<String> Depots;
    private ArrayList<String> Acheteurs;


    private ArrayList<Achat> achats = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saisie_achat);

        initFields();
        initListeners();
        loadIntent();
        setGetDepots();
        setGetAcheteurs();
        setGetBp();
     }

    @Override
    protected void onResume() {
        super.onResume();
        setGetBp();
    }

    private void initFields(){
        eFourni = findViewById(R.id.achat_fourni);
        sDepot = findViewById(R.id.achat_depot);
        sAcheteur = findViewById(R.id.achat_acheteur);
        eDate = findViewById(R.id.achat_date);
        eSite = findViewById(R.id.achat_site);
        bottomNavigationView = findViewById(R.id.bottom_navigation_saisie_add);
    }

    private void initListeners(){

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.add_onglet:

                        if(eFourni.getText().toString().equalsIgnoreCase("")){
                            showError("Veuillez saisir un fournisseur", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                        }
                        else {
                            Intent NewSaisieActivity = new Intent(SaisieAchat.this, SaisieLigneAchat.class);
                            NewSaisieActivity.putExtra("achat",
                                    new Achat(
                                            consultation.getFtgnr(),
                                            consultation.getFourni(),
                                            consultation.getDateRecep(),
                                            consultation.getSite(),
                                            consultation.getDepotCode(),
                                            "",
                                            "",
                                            0,
                                            0,
                                            "",
                                            0.0,
                                            0.0,
                                            0.0,
                                            0.0,
                                            0.0,
                                            0.0,
                                            0.0,
                                            0.0,
                                            0.0,
                                            "",
                                            0.0,
                                            0.0,
                                            "",
                                            "",
                                            0)
                            );
                            startActivity(NewSaisieActivity);
                        }

                        break;
                }
                return false;
            }
        });



        eFourni.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    setGetFournisseurs();
                    return true; // Focus will do whatever you put in the logic.
                }
                return false;  // Focus will change according to the actionId
            }
        });

        eDate.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                consultation.setDateRecep(eDate.getText().toString());
                setGetBp();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }
        });

    }

    private void loadIntent() {
        Intent intent = getIntent();
        consultation = (Consultation) intent.getSerializableExtra("consultation");
        String date = consultation.getDateRecep();
        day = Integer.parseInt(date.substring(0,date.indexOf('/')));
        date = date.substring(date.indexOf('/') + 1);
        month = Integer.parseInt(date.substring(0,date.indexOf('/')));
        date = date.substring(date.indexOf('/') + 1);
        year = Integer.parseInt(date);

        showDate(year, month, day);
        eSite.setText(consultation.getSite());

        if(!consultation.getFourni().equalsIgnoreCase("")){
            eFourni.setText(consultation.getFourni());
            eFourni.setFocusable(false);
            eFourni.setBackgroundResource(R.drawable.border);
            sDepot.setFocusable(false);
            sDepot.setBackgroundResource(R.drawable.border);
            sAcheteur.setFocusable(false);
            sAcheteur.setBackgroundResource(R.drawable.border);
            eDate.setFocusable(false);
            eDate.setBackgroundResource(R.drawable.border);
        }
    }


    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }


    private void setTotal(){
        Achat total = new Achat(consultation.getFtgnr(), consultation.getFourni(), consultation.getDateRecep(), consultation.getSite(),consultation.getDepotCode(), "TOTAL", "",0,0,"",0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0, "", 0.0, 0.0,"","",3);
        for ( int i = 0 ; i < achats.size() ; i++ ){
            total.setColis(total.getColis() + achats.get(i).getColis());
            total.setPieces(total.getPieces() + achats.get(i).getPieces());
            total.setPdsNet(total.getPdsNet() + achats.get(i).getPdsNet());
            total.setMontant(round(total.getMontant(),2) + round(achats.get(i).getMontant(),2));
        }
        achats.add(total);
    }

    private void loadAchats() {

        ListView listView = findViewById(R.id.achat_listview);

        AchatAdapter adapter = new AchatAdapter(SaisieAchat.this, R.layout.achat_lines, achats);
        listView.setAdapter(adapter);

        // Ecoute des clicks sur les lignes
        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position < achats.size() - 1 && achats.get(position).getStatut() != 2){
                    Intent SaisieLignesActivity = new Intent(SaisieAchat.this, SaisieLigneAchat.class);
                    SaisieLignesActivity.putExtra("achat",achats.get(position));
                    startActivity(SaisieLignesActivity);
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(position < achats.size() - 1 && achats.get(position).getStatut() != 2){
                    deleteAchat(position);
                }
                return true;
            }
        });
    }

    private void deleteAchat(final int pos){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Suppression");
        builder.setMessage("Confirmer la suppression?");
        builder.setPositiveButton("Non", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });
        builder.setNegativeButton("Oui", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {

                achats.get(pos).setStatut(2);
                setDeleteBp(pos);
                loadAchats();
            }
        });
        builder.show();
    }

    public void setDate(View view) {
        showDialog(999);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month - 1, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    showDate(arg1, arg2 + 1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        String m = Integer.toString(month);
        String d = Integer.toString(day);
        if(month < 10){
            m = "0"+ m;
        }
        if(day < 10){
            d = "0"+ d;
        }
        eDate.setText(new StringBuilder().append(d).append("/")
                .append(m).append("/").append(year));
    }


    private void selectFournisseur() {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Choix du Fournisseur");
        b.setItems(Fournisseurs, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                eFourni.setText(Fournisseurs[which].substring(Fournisseurs[which].indexOf(']')+2));
                consultation.setFtgnr(Fournisseurs[which].substring(1,Fournisseurs[which].indexOf(']')));
                consultation.setFourni(eFourni.getText().toString());
                eFourni.setEnabled(false);
                setGetBp();
            }

        });

        b.show();
    }


    private void lockUI()
    {
        bottomNavigationView.setEnabled(false);
        eSite.setEnabled(false);
        eDate.setEnabled(false);
        eFourni.setEnabled(false);
        sDepot.setEnabled(false);
        sAcheteur.setEnabled(false);
    }

    private void unlockUI()
    {
        bottomNavigationView.setEnabled(true);
        eSite.setEnabled(true);
        eDate.setEnabled(true);
        eFourni.setEnabled(true);
        sDepot.setEnabled(true);
        sAcheteur.setEnabled(true);
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


    private void setGetBp() {
        // Construction de l'URL
        RequestParams params = Helper.GenerateParams(SaisieAchat.this);
        params.put("date_recep",consultation.getDateRecep());
        params.put("acheteur", Helper.acheteur);
        params.put("fournisseur", consultation.getFtgnr());
        String URL = Helper.GenereateURI(SaisieAchat.this, params, "getbp");

        //Verouillage de l'interface
        lockUI();

        // Call API JEE
        GetBp task = new GetBp();
        task.execute(new String[] { URL });
    }

    private void setDeleteBp(final int pos) {
        // Construction de l'URL
        RequestParams params = Helper.GenerateParams(SaisieAchat.this);
        params.put("BestNr",achats.get(pos).getBestnr());
        params.put("BestRadNr", achats.get(pos).getBestradnr());
        String URL = Helper.GenereateURI(SaisieAchat.this, params, "deletebp");

        //Verouillage de l'interface
        lockUI();

        // Call API JEE
        DeleteBp task = new DeleteBp();
        task.execute(new String[] { URL });
    }

    private void setGetFournisseurs() {
        // Construction de l'URL
        RequestParams params = Helper.GenerateParams(SaisieAchat.this);
        params.put("field", eFourni.getText().toString().replace("'","''"));
        String URL = Helper.GenereateURI(SaisieAchat.this, params, "getfournisseurs");

        //Verouillage de l'interface
        lockUI();

        // Call API JEE
        GetFournisseurs task = new GetFournisseurs();
        task.execute(new String[] { URL });
    }

    private void setGetDepots() {
        // Construction de l'URL
        RequestParams params = Helper.GenerateParams(SaisieAchat.this);
        String URL = Helper.GenereateURI(SaisieAchat.this, params, "getdepots");

        //Verouillage de l'interface
        lockUI();

        // Call API JEE
        GetDepots task = new GetDepots();
        task.execute(new String[] { URL });
    }

    private void setGetAcheteurs() {
        // Construction de l'URL
        RequestParams params = Helper.GenerateParams(SaisieAchat.this);
        String URL = Helper.GenereateURI(SaisieAchat.this, params, "getacheteurs");

        //Verouillage de l'interface
        lockUI();

        // Call API JEE
        GetAcheteurs task = new GetAcheteurs();
        task.execute(new String[] { URL });
    }

    private class GetBp extends AsyncTask<String, Void, String> {
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
                showError("Impossible de récupérer les ventes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
            }
            else {

                try {

                    JSONArray jsonArray = new JSONArray(output);
                    achats = new ArrayList<>();

                    try {

                        for(int i = 0 ; i<jsonArray.length() ; i++){
                            achats.add(new Achat(
                                    consultation.getFtgnr(),
                                    consultation.getFourni(),
                                    consultation.getDateRecep(),
                                    consultation.getSite(),
                                    consultation.getDepotCode(),
                                    jsonArray.getJSONObject(i).getString("Code"),
                                    jsonArray.getJSONObject(i).getString("Produit"),
                                    0,
                                    0,
                                    "",
                                    Double.parseDouble(jsonArray.getJSONObject(i).getString("Colis")),
                                    Double.parseDouble(jsonArray.getJSONObject(i).getString("Pieces")),
                                    0.0,
                                    0.0,
                                    Double.parseDouble(jsonArray.getJSONObject(i).getString("Poids")),
                                    0.0,
                                    0.0,
                                    0.0,
                                    0.0,
                                    "",
                                    Double.parseDouble(jsonArray.getJSONObject(i).getString("PU")),
                                    round(Double.parseDouble(jsonArray.getJSONObject(i).getString("Montant")),2),
                                    jsonArray.getJSONObject(i).getString("BestNr"),
                                    jsonArray.getJSONObject(i).getString("BestRadNr"),
                                    1)
                            );
                        }

                    } catch (Exception ex) {System.out.println("aaaaaaaaa" + ex);}

                    loadAchats();
                    setTotal();


                } catch (Exception ex) {}
            }
        }
    }


    private class DeleteBp extends AsyncTask<String, Void, String> {
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
            loadAchats();
        }
    }


    private class GetFournisseurs extends AsyncTask<String, Void, String> {
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
                showError("Fournisseur Inconnu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        eFourni.setText("");
                    }
                });
            }
            else {

                try {
                    JSONArray jsonArray = new JSONArray(output);
                    // Si c'est un code fournisseur complet
                    if (jsonArray.getJSONObject(0).getString("exist").equalsIgnoreCase("1")){

                        eFourni.setText(jsonArray.getJSONObject(0).getString("lib"));
                        consultation.setFtgnr(jsonArray.getJSONObject(0).getString("code"));
                        consultation.setFourni(jsonArray.getJSONObject(0).getString("lib"));
                        eFourni.setEnabled(false);
                        setGetBp();
                    }
                    // Si c'est un libelle fournisseur complet
                    else if (jsonArray.getJSONObject(0).getString("exist").equalsIgnoreCase("2")){

                        eFourni.setText(jsonArray.getJSONObject(0).getString("lib"));
                        consultation.setFtgnr(jsonArray.getJSONObject(0).getString("code"));
                        consultation.setFourni(jsonArray.getJSONObject(0).getString("lib"));
                        eFourni.setEnabled(false);
                        setGetBp();
                    }
                    // Si il n'y a qu'un seul fournisseur correspondant
                    else if (jsonArray.getJSONObject(0).getString("exist").equalsIgnoreCase("3") && jsonArray.length() == 1){

                        eFourni.setText(jsonArray.getJSONObject(0).getString("lib"));
                        consultation.setFtgnr(jsonArray.getJSONObject(0).getString("code"));
                        consultation.setFourni(jsonArray.getJSONObject(0).getString("lib"));
                        eFourni.setEnabled(false);
                        setGetBp();
                    }
                    // Si c'est un morceau de code ou lib fournisseur
                    else if (jsonArray.getJSONObject(0).getString("exist").equalsIgnoreCase("3")){

                        Fournisseurs = new String[jsonArray.length()];

                        for(int i=0; i<jsonArray.length();i++)
                        {
                            Fournisseurs[i] = "[" + jsonArray.getJSONObject(i).getString("code").trim() + "] " + jsonArray.getJSONObject(i).getString("lib").trim();
                        }
                        selectFournisseur();
                    }
                    else {
                        showError("Fournisseur Inconnu", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                eFourni.setText("");
                                eFourni.requestFocus();
                            }
                        });
                    }

                } catch (Exception ex) {}
            }
        }
    }


    private class GetDepots extends AsyncTask<String, Void, String> {
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
                showError("Impossible de récupérer les dépôts", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
            }
            else {

                try {

                    JSONArray jsonArray = new JSONArray(output);
                    Depots = new ArrayList<>();

                    for(int i=0; i<jsonArray.length();i++)
                    {
                        try {
                            Depots.add(jsonArray.getJSONObject(i).getString("LagPlatsNamn"));
                        }catch(Exception ex) { }
                    }
                    sDepot.setAdapter(new ArrayAdapter<>(SaisieAchat.this, R.layout.spinner, Depots));

                    sDepot.setSelection(Depots.indexOf(Helper.depot),true);

                } catch (Exception ex) {}
            }
        }
    }

    private class GetAcheteurs extends AsyncTask<String, Void, String> {
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
                showError("Impossible de récupérer les acheteurs", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
            }
            else {

                try {

                    JSONArray jsonArray = new JSONArray(output);
                    Acheteurs = new ArrayList<>();

                    for(int i=0; i<jsonArray.length();i++)
                    {
                        try {
                            Acheteurs.add(jsonArray.getJSONObject(i).getString("inkhandlbeskr"));
                        }catch(Exception ex) { }
                    }
                    sAcheteur.setAdapter(new ArrayAdapter<>(SaisieAchat.this, R.layout.spinner, Acheteurs));
                    sAcheteur.setSelection(Acheteurs.indexOf(Helper.acheteur),true);

                } catch (Exception ex) {}
            }
        }
    }
}
