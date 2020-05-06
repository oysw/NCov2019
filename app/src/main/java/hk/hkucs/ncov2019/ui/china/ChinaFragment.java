package hk.hkucs.ncov2019.ui.china;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import hk.hkucs.ncov2019.CountAdapter;
import hk.hkucs.ncov2019.DownloadData;
import hk.hkucs.ncov2019.JavaToJs;
import hk.hkucs.ncov2019.R;

public class ChinaFragment extends Fragment {
    private ChinaViewModel chinaViewModel;
    private WebView chinaWebView;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        chinaViewModel = ViewModelProviders.of(this).get(ChinaViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_china, container, false);
        final ListView chinaListView = root.findViewById(R.id.list_china);
        chinaWebView = root.findViewById(R.id.china_web);
        chinaViewModel.getData().observe(getViewLifecycleOwner(), new Observer<JSONArray>() {
            @SuppressLint("AddJavascriptInterface")
            @Override
            public void onChanged(JSONArray jsonArray) {
                CountAdapter adapter = new CountAdapter(root.getContext(), jsonArray, R.layout.simple_row);
                chinaListView.setAdapter(adapter);
                WebSettings settings = chinaWebView.getSettings();
                WebView.setWebContentsDebuggingEnabled(true);
                settings.setDomStorageEnabled(true);
                settings.setSupportZoom(true);
                settings.setUseWideViewPort(true);
                settings.setDefaultTextEncodingName("UTF-8");
                settings.setAllowFileAccess(true);
                settings.setJavaScriptEnabled(true);
                settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
                chinaWebView.clearCache(true);
                JSONArray confirmedData = new JSONArray();
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject temp = new JSONObject();
                    try {
                        temp.put("name", jsonArray.getJSONObject(i).getString("provinceEnglishName"));
                        temp.put("value", jsonArray.getJSONObject(i).getString("confirmedCount"));
                        confirmedData.put(i, temp);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                JavaToJs obj = new JavaToJs(root.getContext(), confirmedData);
                chinaWebView.addJavascriptInterface(obj, "confirmedData");
                chinaWebView.loadUrl("file:///android_asset/china.html");
            }
        });
        new GetDataFromApi().execute();
        return root;
    }

    private class GetDataFromApi extends AsyncTask<String, Void, Void> {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected Void doInBackground(String... strings) {
            try {
                JSONArray areaList = DownloadData.getDataFromApi("https://lab.isaaclin.cn/nCoV/api/area");
                int index = 0;
                JSONArray res = new JSONArray();
                for (int i = 0; i < areaList.length(); i++){
                    JSONObject jo = areaList.getJSONObject(i);
                    if (jo.getString("countryEnglishName").equals("China") && !jo.getString("provinceEnglishName").equals("China")){
                        res.put(index, jo);
                        index++;
                    }
                }
                ArrayList<JSONObject> resList = new ArrayList<>();
                for(int i = 0; i < res.length(); i++){
                    resList.add(res.getJSONObject(i));
                }
                Collections.sort(resList, new Comparator<JSONObject>() {
                    @Override
                    public int compare(JSONObject o1, JSONObject o2) {
                        try {
                            return -Integer.compare(o1.getInt("confirmedCount"), o2.getInt("confirmedCount"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return 0;
                    }
                });
                for(int i = 0; i < res.length(); i++){
                    res.put(i, resList.get(i));
                }
                chinaViewModel.setData(res);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
