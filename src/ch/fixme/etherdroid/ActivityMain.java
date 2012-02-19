package ch.fixme.etherdroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ActivityMain extends Activity {

    private static final int DIALOG_LOADING = 1;
    private static final int DIALOG_ADD = 2;
    private static final String QUERY_READ = "SELECT _id,host,port from hosts";
    private SimpleCursorAdapter mAdapter;
    private String[] mFrom;
    private int[] mTo;
    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mContext = getApplicationContext();
        mFrom = new String[] { "host" };
        mTo = new int[] { R.id.list_title };
        new ListTask().execute();
        // Open reader by default (FIXME)
        // Intent i = new Intent(Intent.ACTION_VIEW);
        // i.setData(Uri.parse("pad://62.220.136.218:9001/BFrMshLVWcrG4B6BsFeDRk1Iritq2Dfz/GADC2012"));
        // startActivity(i);
        // finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        AlertDialog dialog = null;
        switch (id) {
            case DIALOG_LOADING:
                dialog = new ProgressDialog(this);
                dialog.setCancelable(false);
                dialog.setMessage("Loading...");
                ((ProgressDialog) dialog).setIndeterminate(true);
                break;
            case DIALOG_ADD:
                break;
        }
        return dialog;
    }

    private class ListTask extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected void onPreExecute() {
            showDialog(DIALOG_LOADING);
        }

        @Override
        protected Cursor doInBackground(Void... unused) {
            SQLiteDatabase db = new Database(mContext).getReadableDatabase();
            Cursor c = db.rawQuery(QUERY_READ, null);
            db.close();
            return c;
        }

        @Override
        protected void onPostExecute(Cursor c) {
            mAdapter = new SimpleCursorAdapter(mContext, R.layout.list_item, c, mFrom, mTo);
            ListView list = (ListView) findViewById(R.id.list_hosts);
            list.setAdapter(mAdapter);
            dismissDialog(DIALOG_LOADING);
        }

    }

}
