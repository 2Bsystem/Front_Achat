package a2bsystem.com.achat.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    public static final String LOG = "DatabaseHelper";

    // Database Version
    public static final int DATABASE_VERSION = 2;

    // Database Name
    public static final String DATABASE_NAME = "casseManager";

    // Table Names
    public static final String TABLE_ARTICLE        = "articles";
    // Common column names
    public static final String KEY_ID = "id";

    // ARTICLE  Table - column names
    public static final String KEY_ARTNR       = "artnr";
    public static final String KEY_Q_GCAR_LIB1 = "q_gcar_lib1";
    public static final String KEY_MOMSKOD     = "momskod";




    // Article table create statement
    public static final String CREATE_TABLE_ARTICLE = "CREATE TABLE "
            + TABLE_ARTICLE
            + "("
            + KEY_ID          + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_ARTNR       + " TEXT NOT NULL,"
            + KEY_MOMSKOD     + " TEXT NOT NULL,"
            + KEY_Q_GCAR_LIB1 + " TEXT NOT NULL"
            + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_ARTICLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARTICLE);
        // create new tables
        onCreate(db);
    }

}
