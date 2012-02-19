package ch.fixme.etherdroid;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class ActivityReader extends Activity {

    private EtherAPI mApi;
    private static final int DIALOG_LOADING = 1;
    private static final int DIALOG_ERROR = 2;
    private static final String TAG = "ActivityReader";

    private Response mLastResponse;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reader);
        // Vertical scroll
        ((TextView) findViewById(R.id.padText)).setMovementMethod(ScrollingMovementMethod
                .getInstance());

        // Get information and create api access
        // URI: pad://host:port/apikey/padID
        try {
            final Uri uri = getIntent().getData();
            final List<String> path = uri.getPathSegments();
            mApi = new EtherAPI(uri.getHost() + ":" + uri.getPort(), path.get(0));
            new GetTextTask().execute(path.get(1));
        } catch (Exception e) {
            mLastResponse = new Response();
            mLastResponse.code = Response.CODE_WRONG_PARAM;
            mLastResponse.message = "URI is missing data";
            showDialog(DIALOG_ERROR);
            Log.e(TAG, e.getMessage());
        }
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
            case R.id.menu_refresh:
                new GetTextTask().execute(mApi.mPadID);
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
            case DIALOG_ERROR:
                if (mLastResponse != null) {
                    dialog = new AlertDialog.Builder(this).setMessage(mLastResponse.message)
                            .setNeutralButton("Ok", null).create();
                }
                break;
        }
        return dialog;
    }

    private void handleResponse(Response res) {
        mLastResponse = res;
        TextView txt = (TextView) findViewById(R.id.padText);
        switch (res.code) {
            case Response.CODE_OK:
                if (res.data.containsKey("text")) {
                    txt.setText(res.data.get("text"));
                }
                break;
            default:
                showDialog(DIALOG_ERROR);
                break;
        }
    }

    private class GetTextTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            showDialog(DIALOG_LOADING);
        }

        @Override
        protected String doInBackground(String... ids) {
            return mApi.getText(ids[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            handleResponse(new Response(result));
            dismissDialog(DIALOG_LOADING);
        }

    }
}
