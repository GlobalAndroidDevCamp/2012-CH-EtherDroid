package ch.fixme.etherdroid;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class ActivityMain extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        // Open reader by default (FIXME)
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse("pad://62.220.136.218:9001/BFrMshLVWcrG4B6BsFeDRk1Iritq2Dfz/GADC2012"));
        startActivity(i);
        finish();
    }

    // @Override
    // public boolean onCreateOptionsMenu(Menu menu) {
    // MenuInflater inflater = getMenuInflater();
    // inflater.inflate(R.menu.main, menu);
    // return true;
    // }
    //
    // @Override
    // public boolean onOptionsItemSelected(MenuItem item) {
    // switch (item.getItemId()) {
    // default:
    // return super.onOptionsItemSelected(item);
    // }
    // }
    //
    // @Override
    // protected Dialog onCreateDialog(int id) {
    // AlertDialog dialog = null;
    // return dialog;
    // }

}
