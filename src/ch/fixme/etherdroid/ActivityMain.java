package ch.fixme.etherdroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class ActivityMain extends Activity {

    private static final int DIALOG_LOADING = 1;
    private static final int DIALOG_ADD = 2;
    private static final int DIALOG_ERROR = 3;
    private static final String QUERY_READ = "SELECT _id,host,port from hosts";
    private static final String QUERY_ADD = "INSERT INTO hosts (host, port, apikey) values (?,?,?)";
    private SimpleCursorAdapter mAdapter;
    private Context mContext;
    private SQLiteDatabase mDb;
    private Cursor mCursor;
	private String mErrorMessage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		mContext = getApplicationContext();
		mAdapter = new SimpleCursorAdapter(mContext, R.layout.list_host, null,
				new String[] { "host" }, new int[] { R.id.list_title });
		ListView list = (ListView) findViewById(R.id.list_hosts); 
		list.setAdapter(mAdapter);
		list.setEmptyView(findViewById(R.id.empty));
		list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// List saved pads from this host
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.putExtra("hostid", id);
				i.setClass(ActivityMain.this, ActivityPads.class);
				startActivity(i);
			}
		});
		
        mDb = new Database(mContext).getWritableDatabase();
        new ListTask().execute();
        // FIXME: Just for dev -> open reader
//        Intent i = new Intent(Intent.ACTION_VIEW);
//        i.setData(Uri.parse("pad://62.220.136.218:9001/BFrMshLVWcrG4B6BsFeDRk1Iritq2Dfz/GADC2012"));
//        startActivity(i);
//        finish();
    }

    @Override
    public void onDestroy() {
		if (mCursor != null && !mCursor.isClosed()) {
			mCursor.close();
		}
        if (mDb != null && mDb.isOpen()) {
            mDb.close();
        }
        super.onDestroy();
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
                showDialog(DIALOG_ADD);
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
                dialog.setCancelable(true);
                dialog.setMessage("Loading...");
                ((ProgressDialog) dialog).setIndeterminate(true);
                break;
            case DIALOG_ADD:
                AlertDialog.Builder b1 = new AlertDialog.Builder(this);
                b1.setTitle("Add host");
                // Custom view
				View layout = getLayoutInflater().inflate(R.layout.dialog_add_host, null);
				b1.setView(layout);
                // Buttons actions
                final EditText txt_host = (EditText) layout.findViewById(R.id.add_host);
                final EditText txt_port = (EditText) layout.findViewById(R.id.add_port);
                final EditText txt_key = (EditText) layout.findViewById(R.id.add_apikey);
                b1.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    	addHost(txt_host, txt_port, txt_key);
                    }
                });
                b1.setNegativeButton("Cancel", null);
                // Create
                dialog = b1.create();
                break;
            case DIALOG_ERROR:
                AlertDialog.Builder b2 = new AlertDialog.Builder(this);
                b2.setTitle("Error");
                b2.setMessage(mErrorMessage);
                b2.setNeutralButton("Ok", null);
                dialog = b2.create();
            	break;
        }
        return dialog;
    }
    
	private void addHost(EditText host, EditText port, EditText key) {
		if (new String("").equals(host.getText().toString())) {
			mErrorMessage = "Please fill in the Host field";
			showDialog(DIALOG_ERROR);
			return;
		}
		if (new String("").equals(port.getText().toString())) {
			mErrorMessage = "Please fill in the Port field";
			showDialog(DIALOG_ERROR);
			return;
		}
		if (new String("").equals(key.getText().toString())) {
			mErrorMessage = "Please fill in the APIKey field";
			showDialog(DIALOG_ERROR);
			return;
		}
		new AddHost().execute(host.getText().toString(), port.getText()
				.toString(), key.getText().toString());
	}

    private class ListTask extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... unused) {
            return mDb.rawQuery(QUERY_READ, null);
        }

        @Override
        protected void onPostExecute(Cursor c) {
        	mCursor = c;
        	mAdapter.changeCursor(mCursor);
        }
    }

    private class AddHost extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... data) {
            mDb.execSQL(QUERY_ADD, data);
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            mCursor.requery();
            mAdapter.notifyDataSetChanged();
			Toast.makeText(mContext, "Host successfully added!",
					Toast.LENGTH_SHORT).show();
        }
    }
}
