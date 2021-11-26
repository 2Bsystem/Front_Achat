package a2bsystem.com.achat.Activities.saisie.ligne;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

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

import a2bsystem.com.achat.Helper;
import a2bsystem.com.achat.Models.Achat;
import a2bsystem.com.achat.R;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.conn.ssl.SSLSocketFactory;

import static a2bsystem.com.achat.Helper.GetList;

public class SaisieLigneAchat extends AppCompatActivity {

    EditText eFourni;
    EditText eDate;
    EditText eSite;
    EditText eLib;
    EditText eArtnr;
    EditText eColis;
    EditText ePieces;
    EditText ePiecesU;
    EditText ePdsNet;
    EditText ePdsNetU;
    EditText ePdsBrut;
    EditText ePdsBrutU;
    EditText eTare;
    EditText eTareU;
    EditText eUnite;
    EditText ePu;
    EditText eMontant;
    EditText eDernPrix;
    EditText eDernQte;
    EditText eDernDate;
    EditText eNouvPrixBase;
    EditText eAncPrixBase;
    EditText eAncUnite;
    BottomNavigationView bottomNavigationView;
    Achat achat = new Achat();
    AsyncHttpClient client = new AsyncHttpClient();

    private String [] Articles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saisie_ligne_achat);

        initFields();
        initListeners();
        loadAchat();
        manageFocus();

    }

    private void initFields() {
        eFourni     = findViewById(R.id.saisie_ligne_four);
        eDate       = findViewById(R.id.saisie_ligne_date);
        eSite       = findViewById(R.id.saisie_ligne_site);
        eArtnr      = findViewById(R.id.saisie_ligne_produit_artnr);
        eLib        = findViewById(R.id.saisie_ligne_produit_lib);
        eColis      = findViewById(R.id.saisie_ligne_colis);
        ePieces     = findViewById(R.id.saisie_ligne_pieces);
        ePiecesU    = findViewById(R.id.saisie_ligne_piecesU);
        ePdsNet     = findViewById(R.id.saisie_ligne_pdsNet);
        ePdsNetU    = findViewById(R.id.saisie_ligne_pdsNetU);
        ePdsBrut    = findViewById(R.id.saisie_ligne_pdsBrut);
        ePdsBrutU   = findViewById(R.id.saisie_ligne_pdsBrutU);
        eTare       = findViewById(R.id.saisie_ligne_tare);
        eTareU      = findViewById(R.id.saisie_ligne_tareU);
        eUnite      = findViewById(R.id.saisie_ligne_unite);
        ePu         = findViewById(R.id.saisie_ligne_pu);
        eMontant    = findViewById(R.id.saisie_ligne_montant);
        bottomNavigationView = findViewById(R.id.bottom_navigation_valid_ligne);
        eDernPrix   = findViewById(R.id.saisie_ligne_dern_prix);
        eDernQte    = findViewById(R.id.saisie_ligne_dern_qte);
        eDernDate   = findViewById(R.id.saisie_ligne_dern_date);
        eNouvPrixBase   = findViewById(R.id.saisie_ligne_nouveau_prix);
        eAncPrixBase    = findViewById(R.id.saisie_ligne_ancien_prix);
        eAncUnite   = findViewById(R.id.saisie_ligne_ancien_unite);
    }

    private void initListeners() {

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.add_onglet:

                        if(achat.getBestnr().equalsIgnoreCase("")){
                            setCreateBp();
                        }
                        else {
                            setUpdateBp();
                        }

                        break;
                }
                return false;
            }
        });

    }


    private void fillFields(){
        eFourni.setText(achat.getFourni());
        eDate.setText(achat.getDateRecep());
        eSite.setText(achat.getSite());
        eArtnr.setText(achat.getCode());
        eLib.setText(achat.getName());
        if(achat.getColis() != 0) {
            eColis.setText(achat.getColis() + "");
        }
        else {
            eColis.setText("");
        }

        ePieces.setText(achat.getPieces()+"");
        ePiecesU.setText(achat.getPiecesU()+"");
        ePdsNet.setText(achat.getPdsNet()+"");
        ePdsNetU.setText(achat.getPdsNetU()+"");
        eTare.setText(achat.getTare()+"");
        eTareU.setText(achat.getTareU()+"");
        ePdsBrut.setText(achat.getPdsBrut()+"");
        ePdsBrutU.setText(achat.getPdsBrutU()+"");
        eUnite.setText(achat.getUniteFact());
        ePu.setText(achat.getPu()+"");
        eMontant.setText(achat.getMontant()+"");
    }

    private void saveFields(){
        if(eColis.getText().toString().equalsIgnoreCase("")){
            achat.setColis(0.0);
        }
        else {
            achat.setColis(Double.parseDouble(eColis.getText().toString()));
        }
        achat.setPieces(Double.parseDouble(ePieces.getText().toString()));
        achat.setPiecesU(Double.parseDouble(ePiecesU.getText().toString()));
        achat.setPdsNet(Double.parseDouble(ePdsNet.getText().toString()));
        achat.setPdsNetU(Double.parseDouble(ePdsNetU.getText().toString()));
        achat.setTare(Double.parseDouble(eTare.getText().toString()));
        achat.setTareU(Double.parseDouble(eTareU.getText().toString()));
        achat.setPdsBrut(Double.parseDouble(ePdsBrut.getText().toString()));
        achat.setPdsBrutU(Double.parseDouble(ePdsBrutU.getText().toString()));
        achat.setPu(Double.parseDouble(ePu.getText().toString()));
        achat.setMontant(Double.parseDouble(eMontant.getText().toString()));
    }

    private void loadAchat(){
        Intent intent = getIntent();
        achat = (Achat) intent.getSerializableExtra("achat");
        if(!achat.getCode().equalsIgnoreCase("")){
            eArtnr.setText(achat.getCode());
            eColis.requestFocus();
            setGetArticles();
        }
        fillFields();
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

    public void calculate(int type) {
        saveFields();

        if( achat.getPdsNetU() != 0 && type == 0) {

            achat.setPdsNet(achat.getColis() * achat.getPdsNetU());
        }
        else if (achat.getColis() != 0){
            achat.setPdsNetU(achat.getPdsNet() / achat.getColis());
        }


        if( achat.getPdsBrutU() != 0 && type == 0) {

            achat.setPdsBrut(achat.getColis() * achat.getPdsBrutU());
        }
        else if (achat.getColis() != 0){
            achat.setPdsBrutU(achat.getPdsBrut() / achat.getColis());
        }

        if( achat.getPiecesU() != 0 && type == 0) {

            achat.setPieces(achat.getColis() * achat.getPiecesU());
        }
        else if (achat.getColis() != 0){

            achat.setPiecesU(achat.getPdsBrut() / achat.getColis());
        }

        if( achat.getTareU() != 0 ) {
            achat.setTare(achat.getColis() * achat.getTareU());
        }
        if (achat.getModeSaisie().equalsIgnoreCase("Poids brut total")) {
            if(achat.getPdsBrut() != 0) {
                achat.setTareU(achat.getTare() * achat.getPdsBrutU() / achat.getPdsBrut());
            }
            if(achat.getPdsBrutU() - achat.getTareU() > 0 && achat.getPdsBrut() - achat.getTare() > 0) {
                achat.setPdsNetU(achat.getPdsBrutU() - achat.getTareU());
                achat.setPdsNet(achat.getPdsBrut() - achat.getTare());
            }
            else {
                achat.setPdsNetU(0.0);
                achat.setPdsNet(0.0);
            }
        }


        fillFields();
    }


    public void manageFocus(){

        eArtnr.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                setGetArticles();
                eColis.requestFocus();
                return false;
            }
        });

        eColis.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                calculate(0);
                if (actionId == EditorInfo.IME_ACTION_NEXT) {

                    switch (achat.getModeSaisie()) {

                        case "Poids net total":
                            ePdsNet.post(new Runnable() {
                                @Override
                                public void run() {
                                    ePdsNet.requestFocus();
                                }
                            });
                            break;

                        case "Poids brut total":
                            ePdsBrut.post(new Runnable() {
                                @Override
                                public void run() {
                                    ePdsBrut.requestFocus();
                                }
                            });
                            break;

                        case "NULL":
                            ePiecesU.post(new Runnable() {
                                @Override
                                public void run() {
                                    ePiecesU.requestFocus();
                                }
                            });
                            break;

                        case "Colis":
                            eNouvPrixBase.post(new Runnable() {
                                @Override
                                public void run() {
                                    eNouvPrixBase.requestFocus();
                                }
                            });
                            break;

                        case "pieces totales":
                            ePieces.post(new Runnable() {
                                @Override
                                public void run() {
                                    ePieces.requestFocus();
                                }
                            });
                            break;

                        default:
                            ePiecesU.post(new Runnable() {
                                @Override
                                public void run() {
                                    ePiecesU.requestFocus();
                                }
                            });
                            break;

                    }
                }
                return false;
            }

        });

        ePiecesU.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                calculate(1);
                if (actionId == EditorInfo.IME_ACTION_NEXT) {

                    switch (achat.getModeSaisie()) {

                        case "Poids net total":
                            eColis.post(new Runnable() {
                                @Override
                                public void run() {
                                    eColis.requestFocus();
                                }
                            });
                            break;

                        case "Poids brut total":
                            eColis.post(new Runnable() {
                                @Override
                                public void run() {
                                    eColis.requestFocus();
                                }
                            });
                            break;

                        case "NULL":
                            ePieces.post(new Runnable() {
                                @Override
                                public void run() {
                                    ePieces.requestFocus();
                                }
                            });
                            break;

                        case "Colis":
                            eColis.post(new Runnable() {
                                @Override
                                public void run() {
                                    eColis.requestFocus();
                                }
                            });
                            break;

                        case "pieces totales":
                            eColis.post(new Runnable() {
                                @Override
                                public void run() {
                                    eColis.requestFocus();
                                }
                            });
                            break;

                        default:
                            ePieces.post(new Runnable() {
                                @Override
                                public void run() {
                                    ePieces.requestFocus();
                                }
                            });
                            break;
                    }
                }
                return false;
            }

        });

        ePieces.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                calculate(1);
                if (actionId == EditorInfo.IME_ACTION_NEXT) {

                    switch (achat.getModeSaisie()) {

                        case "Poids net total":
                            eColis.post(new Runnable() {
                                @Override
                                public void run() {
                                    eColis.requestFocus();
                                }
                            });
                            break;

                        case "Poids brut total":
                            eColis.post(new Runnable() {
                                @Override
                                public void run() {
                                    eColis.requestFocus();
                                }
                            });
                            break;

                        case "NULL":
                            ePdsBrutU.post(new Runnable() {
                                @Override
                                public void run() {
                                    ePdsBrutU.requestFocus();
                                }
                            });
                            break;

                        case "Colis":
                            eColis.post(new Runnable() {
                                @Override
                                public void run() {
                                    eColis.requestFocus();
                                }
                            });
                            break;

                        case "pieces totales":
                            eNouvPrixBase.post(new Runnable() {
                                @Override
                                public void run() {
                                    eNouvPrixBase.requestFocus();
                                }
                            });
                            break;

                        default:
                            ePdsBrutU.post(new Runnable() {
                                @Override
                                public void run() {
                                    ePdsBrutU.requestFocus();
                                }
                            });
                            break;
                    }

                }
                return false;
            }

        });

        ePdsBrutU.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                calculate(1);
                if (actionId == EditorInfo.IME_ACTION_NEXT) {

                    switch (achat.getModeSaisie()) {

                        case "Poids net total":
                            eColis.post(new Runnable() {
                                @Override
                                public void run() {
                                    eColis.requestFocus();
                                }
                            });
                            break;

                        case "Poids brut total":
                            eColis.post(new Runnable() {
                                @Override
                                public void run() {
                                    eColis.requestFocus();
                                }
                            });
                            break;

                        case "NULL":
                            ePdsBrut.post(new Runnable() {
                                @Override
                                public void run() {
                                    ePdsBrut.requestFocus();
                                }
                            });
                            break;

                        case "Colis":
                            eColis.post(new Runnable() {
                                @Override
                                public void run() {
                                    eColis.requestFocus();
                                }
                            });
                            break;

                        case "pieces totales":
                            eColis.post(new Runnable() {
                                @Override
                                public void run() {
                                    eColis.requestFocus();
                                }
                            });
                            break;

                        default:
                            ePdsBrut.post(new Runnable() {
                                @Override
                                public void run() {
                                    ePdsBrut.requestFocus();
                                }
                            });
                            break;
                    }

                }
                return false;
            }

        });

        ePdsBrut.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                calculate(1);
                if (actionId == EditorInfo.IME_ACTION_NEXT) {

                    switch (achat.getModeSaisie()) {

                        case "Poids net total":
                            eColis.post(new Runnable() {
                                @Override
                                public void run() {
                                    eColis.requestFocus();
                                }
                            });
                            break;

                        case "Poids brut total":
                            eTareU.post(new Runnable() {
                                @Override
                                public void run() {
                                    eTareU.requestFocus();
                                }
                            });
                            break;

                        case "NULL":
                            eTareU.post(new Runnable() {
                                @Override
                                public void run() {
                                    eTareU.requestFocus();
                                }
                            });
                            break;

                        case "Colis":
                            eColis.post(new Runnable() {
                                @Override
                                public void run() {
                                    eColis.requestFocus();
                                }
                            });
                            break;

                        case "pieces totales":
                            eColis.post(new Runnable() {
                                @Override
                                public void run() {
                                    eColis.requestFocus();
                                }
                            });
                            break;

                        default:
                            eTareU.post(new Runnable() {
                                @Override
                                public void run() {
                                    eTareU.requestFocus();
                                }
                            });
                            break;
                    }

                }
                return false;
            }

        });

        eTareU.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                calculate(1);
                if (actionId == EditorInfo.IME_ACTION_NEXT) {

                    switch (achat.getModeSaisie()) {

                        case "Poids net total":
                            eColis.post(new Runnable() {
                                @Override
                                public void run() {
                                    eColis.requestFocus();
                                }
                            });
                            break;

                        case "Poids brut total":
                            eNouvPrixBase.post(new Runnable() {
                                @Override
                                public void run() {
                                    eNouvPrixBase.requestFocus();
                                }
                            });
                            break;

                        case "NULL":
                            eTare.post(new Runnable() {
                                @Override
                                public void run() {
                                    eTare.requestFocus();
                                }
                            });
                            break;

                        case "Colis":
                            eColis.post(new Runnable() {
                                @Override
                                public void run() {
                                    eColis.requestFocus();
                                }
                            });
                            break;

                        case "pieces totales":
                            eColis.post(new Runnable() {
                                @Override
                                public void run() {
                                    eColis.requestFocus();
                                }
                            });
                            break;

                        default:
                            eTare.post(new Runnable() {
                                @Override
                                public void run() {
                                    eTare.requestFocus();
                                }
                            });
                            break;
                    }

                }
                return false;
            }

        });

        eTare.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                calculate(1);
                if (actionId == EditorInfo.IME_ACTION_NEXT) {

                    switch (achat.getModeSaisie()) {

                        case "Poids net total":
                            eColis.post(new Runnable() {
                                @Override
                                public void run() {
                                    eColis.requestFocus();
                                }
                            });
                            break;

                        case "Poids brut total":
                            eNouvPrixBase.post(new Runnable() {
                                @Override
                                public void run() {
                                    eNouvPrixBase.requestFocus();
                                }
                            });
                            break;

                        case "NULL":
                            ePdsNetU.post(new Runnable() {
                                @Override
                                public void run() {
                                    ePdsNetU.requestFocus();
                                }
                            });
                            break;

                        case "Colis":
                            break;

                        case "pieces totales":
                            eColis.post(new Runnable() {
                                @Override
                                public void run() {
                                    eColis.requestFocus();
                                }
                            });
                            break;

                        default:
                            ePdsNetU.post(new Runnable() {
                                @Override
                                public void run() {
                                    ePdsNetU.requestFocus();
                                }
                            });
                            break;
                    }

                }
                return false;
            }

        });

        ePdsNetU.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                calculate(1);
                if (actionId == EditorInfo.IME_ACTION_NEXT) {

                    switch (achat.getModeSaisie()) {

                        case "Poids net total":
                            eColis.post(new Runnable() {
                                @Override
                                public void run() {
                                    eColis.requestFocus();
                                }
                            });
                            break;

                        case "Poids brut total":
                            eColis.post(new Runnable() {
                                @Override
                                public void run() {
                                    eColis.requestFocus();
                                }
                            });
                            break;

                        case "NULL":
                            ePdsNet.post(new Runnable() {
                                @Override
                                public void run() {
                                    ePdsNet.requestFocus();
                                }
                            });
                            break;

                        case "Colis":
                            eColis.post(new Runnable() {
                                @Override
                                public void run() {
                                    eColis.requestFocus();
                                }
                            });
                            break;

                        case "pieces totales":
                            eColis.post(new Runnable() {
                                @Override
                                public void run() {
                                    eColis.requestFocus();
                                }
                            });
                            break;

                        default:
                            ePdsNet.post(new Runnable() {
                                @Override
                                public void run() {
                                    ePdsNet.requestFocus();
                                }
                            });
                            break;
                    }

                }
                return false;
            }

        });

        ePdsNet.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                calculate(1);
                if (actionId == EditorInfo.IME_ACTION_NEXT) {

                    switch (achat.getModeSaisie()) {

                        case "Poids net total":
                            eNouvPrixBase.post(new Runnable() {
                                @Override
                                public void run() {
                                    eNouvPrixBase.requestFocus();
                                }
                            });
                            break;

                        case "Poids brut total":
                            eColis.post(new Runnable() {
                                @Override
                                public void run() {
                                    eColis.requestFocus();
                                }
                            });
                            break;

                        case "NULL":
                            eNouvPrixBase.post(new Runnable() {
                                @Override
                                public void run() {
                                    eNouvPrixBase.requestFocus();
                                }
                            });
                            break;

                        case "Colis":
                            eColis.post(new Runnable() {
                                @Override
                                public void run() {
                                    eColis.requestFocus();
                                }
                            });
                            break;

                        case "pieces totales":
                            eColis.post(new Runnable() {
                                @Override
                                public void run() {
                                    eColis.requestFocus();
                                }
                            });
                            break;

                        default:
                            eNouvPrixBase.post(new Runnable() {
                                @Override
                                public void run() {
                                    eNouvPrixBase.requestFocus();
                                }
                            });
                            break;
                    }

                }
                return false;
            }

        });


        ePu.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                calculate(1);
                    if (actionId == EditorInfo.IME_ACTION_NEXT) {
                        if(achat.getBestnr().equalsIgnoreCase("")){
                            setCreateBp();
                        }
                        else {
                            setUpdateBp();
                        }
                    }
                return false;
            }

        });

        eNouvPrixBase.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                calculate(1);
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    ePu.post(new Runnable() {
                        @Override
                        public void run() {
                            ePu.requestFocus();
                        }
                    });
                }
                return false;
            }

        });
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    private void newAchat(){
        String ftgnr = achat.getFtgnr();
        String fourni = achat.getFourni();
        String date = achat.getDateRecep();
        String site = achat.getSite();
        String depot = achat.getDepotCode();
        achat =  new Achat(
                ftgnr,
                fourni,
                date,
                site,
                depot,
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
                0
        );
        eDernDate.setText("");
        eDernPrix.setText("");
        eDernQte.setText("");
        eNouvPrixBase.setText("");
        eAncPrixBase.setText("");
        eAncUnite.setText("");
        fillFields();
        eArtnr.requestFocus();
        InputMethodManager imm = (InputMethodManager)   getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

    }



    private void selectArticle() {
        ContextThemeWrapper themedContext = new ContextThemeWrapper(this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
        AlertDialog.Builder b = new AlertDialog.Builder(themedContext);
        b.setTitle("Choix Article");
        b.setItems(Articles, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                eArtnr.setText(Articles[which].substring(1, Articles[which].indexOf("]")));
                eLib.setText(Articles[which].substring(Articles[which].indexOf("]")+1));
                setGetFields(eArtnr.getText().toString());
                setGetHisto(eArtnr.getText().toString());
                setGetPrixBase(eArtnr.getText().toString());

                InputMethodManager imm = (InputMethodManager)   getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }

        });

        b.show();
    }


    private void lockUI()
    {
        eArtnr.setEnabled(false);
        eColis.setEnabled(false);
        ePieces.setEnabled(false);
        ePdsNet.setEnabled(false);
        ePdsBrut.setEnabled(false);
        eTare.setEnabled(false);
        ePu.setEnabled(false);
        eMontant.setEnabled(false);
        bottomNavigationView.setEnabled(false);
    }

    private void unlockUI()
    {
        eArtnr.setEnabled(true);
        eColis.setEnabled(true);
        ePieces.setEnabled(true);
        ePdsNet.setEnabled(true);
        ePdsBrut.setEnabled(true);
        eTare.setEnabled(true);
        ePu.setEnabled(true);
        eMontant.setEnabled(true);
        bottomNavigationView.setEnabled(true);
    }


    private void setGetFields(String artnr) {
        RequestParams params = Helper.GenerateParams(SaisieLigneAchat.this);

        params.put("artnr", artnr);

        String URL = Helper.GenereateURI(SaisieLigneAchat.this, params, "getfields");
        System.out.println("aaaa " + URL);


        //Verouillage de l'interface
        lockUI();

        // Call API JEE

        GetFields task = new GetFields();
        task.execute(new String[] { URL });
    }


    private void setGetHisto(String artnr) {
        RequestParams params = Helper.GenerateParams(SaisieLigneAchat.this);

        params.put("artnr", artnr);

        String URL = Helper.GenereateURI(SaisieLigneAchat.this, params, "gethisto");

        //Verouillage de l'interface
        lockUI();

        // Call API JEE

        GetHisto task = new GetHisto();
        task.execute(new String[] { URL });
    }

    private void setGetPrixBase(String artnr) {
        RequestParams params = Helper.GenerateParams(SaisieLigneAchat.this);

        params.put("artnr", artnr);
        params.put("date",achat.getDateRecep());

        String URL = Helper.GenereateURI(SaisieLigneAchat.this, params, "getprixbase");


        //Verouillage de l'interface
        lockUI();

        // Call API JEE

        GetPrixBase task = new GetPrixBase();
        task.execute(new String[] { URL });
    }


    private void setGetArticles() {
        RequestParams params = Helper.GenerateParams(SaisieLigneAchat.this);
        params.put("field",eArtnr.getText().toString().replace("'","''"));
        params.put("lagstalle",achat.getDepotCode());
        String URL = Helper.GenereateURI(SaisieLigneAchat.this, params, "getarticles");

        //Verouillage de l'interface
        lockUI();

        // Call API JEE

        GetArticle task = new GetArticle();
        task.execute(new String[] { URL });
    }

    private void setUpdateBp() {
        RequestParams params = Helper.GenerateParams(SaisieLigneAchat.this);
        params.put("bestnr",achat.getBestnr());
        params.put("bestradnr",achat.getBestradnr());
        params.put("colis", eColis.getText());
        params.put("pieces", ePieces.getText());
        params.put("poids", ePdsNet.getText());

        params.put("pu",achat.getPu());

        String URL = Helper.GenereateURI(SaisieLigneAchat.this, params, "updatebp");


        //Verouillage de l'interface
        lockUI();

        // Call API JEE

        UpdateBp task = new UpdateBp();
        task.execute(new String[] { URL });
    }

    private void setCreateBp() {

        RequestParams params = Helper.GenerateParams(SaisieLigneAchat.this);
        params.put("ftgnr",achat.getFtgnr());
        params.put("artnr",eArtnr.getText().toString());
        params.put("date",achat.getDateRecep());
        params.put("ua1",eColis.getText().toString());
        params.put("ua3",ePieces.getText().toString());
        params.put("ua5",ePdsBrut.getText().toString());
        params.put("ua6",eTare.getText().toString());
        params.put("ua9",ePdsNet.getText().toString());
        params.put("pu",ePu.getText().toString());

        // Si un nouveau prix de base est rentré
        if(eNouvPrixBase.getText().toString().equalsIgnoreCase("")){
            params.put("nouvPrixBase","-1");
        }
        else {
            params.put("nouvPrixBase",eNouvPrixBase.getText().toString());
        }

        params.put("acheteur", Helper.acheteur);

        String URL = Helper.GenereateURI(SaisieLigneAchat.this, params, "createbp");

        System.out.println("llll " + URL);


        //Verouillage de l'interface
        lockUI();

        // Call API JEE

        CreateBp task = new CreateBp();
        task.execute(new String[] { URL });
    }

    private class GetFields extends AsyncTask<String, Void, String> {
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
                showError("Impossible de récupérer les champs", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
            }
            else {

                try {

                    JSONArray jsonArray = new JSONArray(output);

                    achat.setCode(jsonArray.getJSONObject(0).getString("ArtNr"));
                    achat.setName(jsonArray.getJSONObject(0).getString("q_gcar_lib1"));
                    achat.setPcesVar(jsonArray.getJSONObject(0).getInt("ArtFtgSpec2"));
                    achat.setPoidsVar(jsonArray.getJSONObject(0).getInt("ArtFtgSpec3"));
                    achat.setModeSaisie(jsonArray.getJSONObject(0).getString("q_modesaisie_achat"));
                    achat.setUniteFact(jsonArray.getJSONObject(0).getString("altenhetkod"));
                    achat.setPiecesU(jsonArray.getJSONObject(0).getDouble("ua2"));
                    achat.setPdsBrutU(jsonArray.getJSONObject(0).getDouble("ua4"));
                    achat.setTareU(jsonArray.getJSONObject(0).getDouble("ua6"));
                    achat.setPdsNetU(jsonArray.getJSONObject(0).getDouble("ua8"));

                } catch (Exception ex) {}
                fillFields();
            }
        }
    }


    private class GetHisto extends AsyncTask<String, Void, String> {
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
                showError("Impossible de récupérer l'historique d'achat...", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
            }
            else {

                try {

                    JSONArray jsonArray = new JSONArray(output);

                    eDernDate.setText(jsonArray.getJSONObject(0).getString("BestLevDat"));
                    eDernPrix.setText(Double.toString(round(Double.parseDouble(jsonArray.getJSONObject(0).getString("Vb_inpris")),2)));
                    eDernQte.setText(Double.toString(round(Double.parseDouble(jsonArray.getJSONObject(0).getString("q_gcbp_ua9")),3)));

                    // On met le dernier prix dans pu
                    ePu.setText(eDernPrix.getText());

                } catch (Exception ex) {}
            }
        }
    }


    private class GetPrixBase extends AsyncTask<String, Void, String> {
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
                showError("Impossible de récupérer le prix de base...", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
            }
            else {

                try {

                    JSONArray jsonArray = new JSONArray(output);

                    eAncPrixBase.setText(Double.toString(round(Double.parseDouble(jsonArray.getJSONObject(0).getString("prix")),3)));
                    eAncUnite.setText(jsonArray.getJSONObject(0).getString("unite"));

                } catch (Exception ex) {}
            }
        }
    }


    private class GetArticle extends AsyncTask<String, Void, String> {
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

            System.out.println("aaaaaaaaa " + output);
            if(output.equalsIgnoreCase("-1"))
            {
                showError("Article Inconnu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        newAchat();
                    }
                });
            }
            else {

                try {

                    JSONArray jsonArray = new JSONArray(output);

                    // Si c'est un code article complet
                    if (jsonArray.getJSONObject(0).getString("exist").equalsIgnoreCase("1")){

                        eArtnr.setText(jsonArray.getJSONObject(0).getString("code"));
                        eLib.setText(jsonArray.getJSONObject(0).getString("lib"));
                        setGetFields(eArtnr.getText().toString());
                        setGetHisto(eArtnr.getText().toString());
                        setGetPrixBase(eArtnr.getText().toString());
                    }
                    // Si c'est un libelle article complet
                    else if (jsonArray.getJSONObject(0).getString("exist").equalsIgnoreCase("2")){

                        eArtnr.setText(jsonArray.getJSONObject(0).getString("code"));
                        eLib.setText(jsonArray.getJSONObject(0).getString("lib"));
                        setGetFields(eArtnr.getText().toString());
                        setGetHisto(eArtnr.getText().toString());
                        setGetPrixBase(eArtnr.getText().toString());
                    }
                    // Si il n'y a qu'un seul article correspondant
                    else if (jsonArray.getJSONObject(0).getString("exist").equalsIgnoreCase("3") && jsonArray.length() == 1){

                        eArtnr.setText(jsonArray.getJSONObject(0).getString("code"));
                        eLib.setText(jsonArray.getJSONObject(0).getString("lib"));
                        setGetFields(eArtnr.getText().toString());
                        setGetHisto(eArtnr.getText().toString());
                        setGetPrixBase(eArtnr.getText().toString());
                    }
                    // Si c'est un morceau de code ou lib article
                    else if (jsonArray.getJSONObject(0).getString("exist").equalsIgnoreCase("3")){

                        Articles = new String[jsonArray.length()];

                        for(int i=0; i<jsonArray.length();i++)
                        {
                            Articles[i] = "[" + jsonArray.getJSONObject(i).getString("code").trim() + "] "
                                    + jsonArray.getJSONObject(i).getString("lib").trim() + '\n' + "STOCK : "
                                    + round(Double.parseDouble(jsonArray.getJSONObject(i).getString("stock")), 0);

                        }
                        selectArticle();
                    }
                    else {
                        showError("Article Inconnu", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                eArtnr.setText("");
                                eArtnr.requestFocus();
                            }
                        });
                    }

                } catch (Exception ex) {System.out.println(ex);}

                InputMethodManager imm = (InputMethodManager)   getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        }
    }


    private class CreateBp extends AsyncTask<String, Void, String> {
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
                showError("Impossible de créer la ligne...", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
            }
            else {

                newAchat();
            }
        }
    }


    private class UpdateBp extends AsyncTask<String, Void, String> {
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
                showError("Impossible de mettre à jour la ligne...", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
            }
            else {
                newAchat();
            }
        }
    }
}
