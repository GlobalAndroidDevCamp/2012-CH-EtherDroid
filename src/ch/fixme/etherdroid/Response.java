package ch.fixme.etherdroid;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Response {

    public int code = 0;
    public String message = "";
    public HashMap<String, String> data = new HashMap<String, String>(0);

    private static final String KEY_CODE = "code";
    private static final String KEY_MSG = "message";
    private static final String KEY_DATA = "data";

    public Response() {
    }

    public Response(String json) {
        try {
            JSONObject res = new JSONObject(json);
            code = res.getInt(KEY_CODE);
            message = res.getString(KEY_MSG);
            if (!res.isNull(KEY_DATA)) {
                JSONObject dataObj = res.getJSONObject(KEY_DATA);
                JSONArray names = dataObj.names();
                for (int i = 0; i < names.length(); i++) {
                    data.put(names.getString(i), dataObj.getString(names.getString(i)));
                }
            } else {
                data = null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
