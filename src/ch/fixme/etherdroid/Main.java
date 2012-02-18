package ch.fixme.etherdroid;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.os.AsyncTask;
import android.app.ProgressDialog;
import android.app.Dialog;
import android.content.Context;

public class Main extends Activity
{
        
    private EtherAPI mApi;
    private static final int DIALOG_GETTEXT = 1;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mApi = new EtherAPI("http://62.220.136.218:9001", "BFrMshLVWcrG4B6BsFeDRk1Iritq2Dfz");
        new GetTextTask().execute("GAGDC2012");
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        ProgressDialog dialog = null;
        switch(id) {
        case DIALOG_GETTEXT:
            dialog = new ProgressDialog(this);
            dialog.setMessage("Loading...");
            dialog.setCancelable(false);  
            dialog.setIndeterminate(true);
            break;
        }
        return dialog;
    }

    private class GetTextTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            showDialog(DIALOG_GETTEXT);
        }

        @Override
        protected String doInBackground(String... ids) {
            return mApi.getText(ids[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            TextView txt = (TextView)findViewById(R.id.txt);
            txt.setText(result);
            dismissDialog(DIALOG_GETTEXT);
        }

    }
}
