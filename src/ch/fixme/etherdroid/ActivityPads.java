package ch.fixme.etherdroid;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class ActivityPads extends ListActivity {

	private static final int DIALOG_ADD = 1;
	private static final String QUERY_READ = "SELECT _id, name, padid FROM pads WHERE hostid=?";
	private static final String QUERY_ADD = "INSERT INTO pads (name, padid, hostid) VALUES (?,?,?)";
	private static final String QUERY_OPEN = "SELECT pads._id, pads.padid, hosts.host, hosts.port, hosts.apikey "
			+ "FROM pads JOIN hosts ON hosts._id=pads.hostid WHERE pads._id=?";
	private SQLiteDatabase mDb;
	private Cursor mCursor;
	private SimpleCursorAdapter mAdapter;
	private Context mContext;
	protected long mHostID;

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
		mHostID = getIntent().getExtras().getLong("hostid");
		new ListTask().execute(mHostID + "");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mDb != null && mDb.isOpen()) {
			mDb.close();
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		new OpenPad().execute(id);
		super.onListItemClick(l, v, position, id);
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
							new AddPad()
									.execute(txt_host.getText().toString(),
											txt_port.getText().toString(),
											mHostID + "");
						}
					});
			builder.setNegativeButton("Cancel", null);
			// Create
			dialog = builder.create();
			break;
		}
		return dialog;
	}

	private class ListTask extends AsyncTask<String, Void, Cursor> {

		@Override
		protected Cursor doInBackground(String... data) {
			return mDb.rawQuery(QUERY_READ, data);
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

	private class OpenPad extends AsyncTask<Long, Void, Uri> {

		@Override
		protected Uri doInBackground(Long... data) {
			Cursor c = mDb.rawQuery(QUERY_OPEN, new String[] { data[0] + "" });
			c.moveToFirst();
			return Uri.parse("pad://" + c.getString(2) + ":" + c.getString(3)
					+ "/" + c.getString(4) + "/" + c.getString(1)); // Ugly
		}

		@Override
		protected void onPostExecute(Uri uri) {
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(uri);
			startActivity(i);
		}
	}

}
