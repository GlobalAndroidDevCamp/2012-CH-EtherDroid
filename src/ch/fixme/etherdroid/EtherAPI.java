package ch.fixme.etherdroid;

public class EtherAPI {

    public String mUrl;
    public String mApiKey;
    public String mPadID;
    private static final int API_VERSION = 1;

    public EtherAPI(String host, String apikey) {
        this.mUrl = "http://" + host + "/api/" + API_VERSION + "/";
        this.mApiKey = apikey;
    }

    public String getText(String padId) {
        this.mPadID = padId;
        return Net.get(mUrl + "getText?apikey=" + mApiKey + "&padID=" + padId);
    }
}
