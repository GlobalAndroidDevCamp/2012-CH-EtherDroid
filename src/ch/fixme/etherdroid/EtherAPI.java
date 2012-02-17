package ch.fixme.etherdroid;

import android.app.Activity;
import android.os.Bundle;
import java.lang.StringBuffer;

public class EtherAPI
{

    private String mUrl;
    private static final int API_VERSION = 1;

    public EtherAPI(String url) {
        this.mUrl = url + "/api/" + API_VERSION + "/";
    }

    public String getText(String padId, String revision) {
        if(!new String().equals(revision)){
            revision = "$" + revision;
        } else {
            revision = "";
        }
        return Net.get(mUrl + "getText?padID=" + padId + revision);
    }
}
