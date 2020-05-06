package hk.hkucs.ncov2019;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Build;
import android.webkit.JavascriptInterface;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class JavaToJs extends Object {
    private JSONArray array;
    private Context context;

    public JavaToJs(JSONArray array){
        this.array = array;
    }

    public JavaToJs(Context context, JSONArray array){
        this.array = array;
        this.context = context;
    }

    @JavascriptInterface
    public int getCount(){
        return array.length();
    }

    @JavascriptInterface
    public String getItemName(int index) throws JSONException {
        return array.getJSONObject(index).getString("name");
    }

    @JavascriptInterface
    public int getConfirmedCount(int index) throws JSONException {
        return array.getJSONObject(index).getInt("value");
    }

    @JavascriptInterface
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String getGeoJSON(String location) throws IOException, JSONException {
        AssetManager manager = context.getAssets();
        InputStream is = manager.open("json/" + location);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder str = new StringBuilder();
        String temp;
        while ((temp = reader.readLine()) != null){
            temp = new String(temp.getBytes(), StandardCharsets.UTF_8);
            str.append(temp);
        }
        JSONObject object = new JSONObject(str.toString());
        return object.toString();
    }
}
