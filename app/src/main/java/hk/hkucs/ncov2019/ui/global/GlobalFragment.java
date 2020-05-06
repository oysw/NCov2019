package hk.hkucs.ncov2019.ui.global;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ListView;

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

public class GlobalFragment extends Fragment {

    private WebView globalWebView;
    private GlobalViewModel globalViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        globalViewModel =
                ViewModelProviders.of(this).get(GlobalViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_global, container, false);
        final ListView listView = root.findViewById(R.id.list_global);
        globalWebView = root.findViewById(R.id.global_web);
        globalViewModel.getData().observe(getViewLifecycleOwner(), new Observer<JSONArray>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onChanged(JSONArray jsonArray) {
                CountAdapter adapter = new CountAdapter(root.getContext(), jsonArray, R.layout.simple_row);
                listView.setAdapter(adapter);
                WebSettings settings = globalWebView.getSettings();
                WebView.setWebContentsDebuggingEnabled(true);
                settings.setDomStorageEnabled(true);
                settings.setSupportZoom(true);
                settings.setBuiltInZoomControls(true);
                settings.setLoadWithOverviewMode(true);
                settings.setUseWideViewPort(true);
                settings.setDefaultTextEncodingName("UTF-8");
                settings.setAllowFileAccess(true);
                settings.setJavaScriptEnabled(true);
                settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
                globalWebView.clearCache(true);
                JSONArray confirmedData = new JSONArray();
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject temp = new JSONObject();
                    try {
                        temp.put("name", jsonArray.getJSONObject(i).getString("countryEnglishName"));
                        temp.put("value", jsonArray.getJSONObject(i).getString("confirmedCount"));
                        confirmedData.put(i, temp);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                JavaToJs obj = new JavaToJs(root.getContext(), confirmedData);
                globalWebView.addJavascriptInterface(obj, "confirmedData");
                globalWebView.loadUrl("file:///android_asset/global.html");
            }
        });
        new GetDataFromApi().execute();
        return root;
    }

    private class GetDataFromApi extends AsyncTask<String, Void, Void>{
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected Void doInBackground(String... strings) {
            try {
                JSONArray areaList = DownloadData.getDataFromApi("https://lab.isaaclin.cn/nCoV/api/area");
                int index = 0;
                JSONArray res = new JSONArray();
                for (int i = 0; i < areaList.length(); i++){
                    JSONObject jo = areaList.getJSONObject(i);
                    if (!jo.getString("countryEnglishName").equals("China") || jo.getString("provinceEnglishName").equals("China")){
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
                globalViewModel.setData(res);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
