package a2bsystem.com.achat.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import a2bsystem.com.achat.Models.Article;


public class ArticleController {

    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private String[] allColumns = {
            DatabaseHelper.KEY_ID,
            DatabaseHelper.KEY_ARTNR,
            DatabaseHelper.KEY_MOMSKOD,
            DatabaseHelper.KEY_Q_GCAR_LIB1};

    public ArticleController(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }



    public Article createArticle(String artnr, String momskod, String q_gcar_lib1) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.KEY_ARTNR, artnr);
        values.put(DatabaseHelper.KEY_MOMSKOD, momskod);
        values.put(DatabaseHelper.KEY_Q_GCAR_LIB1, q_gcar_lib1);

        long insertId = database.insert(DatabaseHelper.TABLE_ARTICLE, null,
                values);

        Cursor cursor = database.query(DatabaseHelper.TABLE_ARTICLE,
                allColumns, DatabaseHelper.KEY_ID + " = " + insertId, null,
                null, null, null);

        cursor.moveToFirst();
        Article newArticle = cursorToArticle(cursor);
        cursor.close();

        return newArticle;
    }



    public void deleteArticle(Article article) {
        long id = article.getId();

        database.delete(DatabaseHelper.TABLE_ARTICLE, DatabaseHelper.KEY_ID
                + " = " + id, null);
    }



    public List<Article> getAllArticles() {
        List<Article> articles = new ArrayList<Article>();

        Cursor cursor = database.query(DatabaseHelper.TABLE_ARTICLE,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Article article = cursorToArticle(cursor);
            articles.add(article);
            cursor.moveToNext();
        }
        cursor.close();
        return articles;
    }

    public Article getArticleById (long id) {

        String selectQuery =
                "SELECT  * FROM " + DatabaseHelper.TABLE_ARTICLE +
                        " WHERE "
                        + DatabaseHelper.KEY_ID + " = " + id;

        Cursor cursor = database.rawQuery(selectQuery, null);

        if(cursor != null){
            cursor.moveToFirst();
        }

        Article article = new Article();
        article.setId(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.KEY_ID)));
        article.setArtnr(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_ARTNR)));
        article.setMomskod(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_MOMSKOD)));
        article.setQ_gcar_lib1(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_Q_GCAR_LIB1)));

        cursor.close();

        return article;
    }

    public Article getArticleByArtnr (String artnr) {

        String selectQuery =
                "SELECT * FROM " + DatabaseHelper.TABLE_ARTICLE +
                        " WHERE "
                        + DatabaseHelper.KEY_ARTNR + " = '" + artnr +"'";

        Cursor cursor = database.rawQuery(selectQuery, null);

        if(cursor != null){
            cursor.moveToFirst();
        }

        Article article = new Article();

        if(cursor.getCount()>0){
            article.setId(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.KEY_ID)));
            article.setArtnr(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_ARTNR)));
            article.setMomskod(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_MOMSKOD)));
            article.setQ_gcar_lib1(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_Q_GCAR_LIB1)));
        }
        else {
            article.setId(0);
            article.setArtnr("artnr");
            article.setMomskod("momskod");
            article.setQ_gcar_lib1("qg_car_lib1");
        }

        cursor.close();

        return article;
    }

    public int existArticleByArtnr (String artnr) {
        String selectQuery =
                "SELECT * FROM " + DatabaseHelper.TABLE_ARTICLE +
                        " WHERE "
                        + DatabaseHelper.KEY_ARTNR + " = '" + artnr +"'";

        Cursor cursor = database.rawQuery(selectQuery, null);

        if(cursor != null){
            cursor.moveToFirst();
        }

        cursor.close();

        return cursor.getCount();
    }

    public int existArticleByLib (String lib) {
        String selectQuery =
                "SELECT * FROM " + DatabaseHelper.TABLE_ARTICLE +
                        " WHERE "
                        + DatabaseHelper.KEY_Q_GCAR_LIB1 + " = '" + lib +"'";

        Cursor cursor = database.rawQuery(selectQuery, null);

        if(cursor != null){
            cursor.moveToFirst();
        }

        cursor.close();

        return cursor.getCount();
    }

    public void deleteAll() {
        database.delete(DatabaseHelper.TABLE_ARTICLE, null, null);
    }



    private Article cursorToArticle(Cursor cursor) {
        Article article = new Article();
        article.setId(cursor.getLong(0));
        article.setArtnr(cursor.getString(1));
        article.setMomskod(cursor.getString(2));
        article.setQ_gcar_lib1(cursor.getString(3));
        return article;
    }


}
