package ch.fixme.etherdroid;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "etherdroid.db";
    private static final String CREATE_TABLE_HOSTS = "CREATE TABLE hosts (_id INTEGER PRIMARY KEY, host TEXT, port INTEGER, apikey TEXT)";
    private static final String DROP_TABLE_HOSTS = "DROP TABLE IF EXISTS hosts";

    public Database(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_HOSTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // FIXME: Don't delete all the data ;)
        db.execSQL(DROP_TABLE_HOSTS);
        onCreate(db);
    }

}
