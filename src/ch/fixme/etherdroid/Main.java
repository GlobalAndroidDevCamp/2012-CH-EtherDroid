package ch.fixme.etherdroid;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Main extends Activity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        EtherAPI api = new EtherAPI("http://62.220.136.218:9001");
        String res = api.getText("GADC2012", null);

        TextView txt = (TextView)findViewById(R.id.txt);
        txt.setText(res);
    }
}
