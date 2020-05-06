package hk.hkucs.ncov2019;

import android.os.Build;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class DownloadData {
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static JSONArray getDataFromApi(String urlStr){
        JSONArray res = new JSONArray();
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder jsonStr = new StringBuilder();
            String str;
            while ((str = reader.readLine()) != null) {
                str = new String(str.getBytes(), StandardCharsets.UTF_8);
                jsonStr.append(str);
            }
            JSONObject jObj = new JSONObject(jsonStr.toString());
            res = jObj.getJSONArray("results");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }
}
