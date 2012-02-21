package ch.fixme.etherdroid;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class ActivityPads extends ListActivity {

	private static final int DIALOG_ADD = 1;
	private static final String QUERY_READ = "SELECT _id,name,padid from pads";
	private static final String QUERY_ADD = "INSERT INTO pads (name, padid, hostid) values (?,?,?)";
	private SQLiteDatabase mDb;
	private Cursor mCursor;
	private SimpleCursorAdapter mAdapter;
	private Context mContext;
	protected String mHostID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pads);
		mContext = getApplicationContext();
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		mAdapter = new SimpleCursorAdapter(mContext, R.layout.list_pad, null,
				new String[] { "name", "padid" }, new int[] {
						R.id.list_pad_name, R.id.list_pad_id });
		setListAdapter(mAdapter);
		mDb = new Database(this).getWritableDatabase();
		new ListTask().execute();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mDb != null && mDb.isOpen()) {
			mDb.close();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		case R.id.menu_add:
			showDialog(DIALOG_ADD);
			return true;
		}
		return (super.onOptionsItemSelected(item));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		AlertDialog dialog = null;
		switch (id) {
		case DIALOG_ADD:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Add pad");
			// Custom view
			View layout = getLayoutInflater().inflate(R.layout.dialog_add_pad,
					null);
			builder.setView(layout);
			// Buttons actions
			final EditText txt_host = (EditText) layout
					.findViewById(R.id.add_pad_name);
			final EditText txt_port = (EditText) layout
					.findViewById(R.id.add_pad_id);
			builder.setPositiveButton("Add",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							new AddPad().execute(txt_host.getText().toString(),
									txt_port.getText().toString(), mHostID);
						}
					});
			builder.setNegativeButton("Cancel", null);
			// Create
			dialog = builder.create();
			break;
		}
		return dialog;
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

	private class AddPad extends AsyncTask<String, Void, Void> {

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
