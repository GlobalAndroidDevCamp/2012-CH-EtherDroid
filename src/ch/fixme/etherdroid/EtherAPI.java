package ch.fixme.etherdroid;

import android.app.Activity;
import android.os.Bundle;
import java.lang.StringBuffer;

public class EtherAPI
{

    private String mUrl;
    private String mApiKey;
    private static final int API_VERSION = 1;

    public EtherAPI(String url, String apikey) {
        this.mUrl = url + "/api/" + API_VERSION + "/";
        this.mApiKey = apikey;
    }

    public String getText(String padId) {
        return Net.get(mUrl + "getText?apikey=" + mApiKey + "&padID=" + padId);
    }
}
