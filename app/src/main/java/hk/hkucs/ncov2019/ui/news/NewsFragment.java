package hk.hkucs.ncov2019.ui.news;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import hk.hkucs.ncov2019.DownloadData;
import hk.hkucs.ncov2019.R;

public class NewsFragment extends Fragment {

    private NewsViewModel newsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        newsViewModel =
                ViewModelProviders.of(this).get(NewsViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_news, container, false);
        final ListView listView = root.findViewById(R.id.list_news);
        newsViewModel.getData().observe(getViewLifecycleOwner(), new Observer<JSONArray>() {
            @Override
            public void onChanged(JSONArray jsonArray) {
                NewsAdapter adapter = new NewsAdapter(root.getContext(), jsonArray, R.layout.news_row);
                listView.setAdapter(adapter);
            }
        });
        new GetDataFromApi().execute();
        return root;
    }

    private class GetDataFromApi extends AsyncTask<String, Void, Void>{
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected Void doInBackground(String... strings) {
            JSONArray newsList = DownloadData.getDataFromApi("https://lab.isaaclin.cn/nCoV/api/news");
            newsViewModel.setData(newsList);
            return null;
        }
    }

    private class NewsAdapter extends BaseAdapter{
        Context context;
        JSONArray newsList;
        int layout;

        public NewsAdapter(Context context, JSONArray newsList, int layout){
            this.context = context;
            this.newsList = newsList;
            this.layout = layout;
        }

        @Override
        public int getCount() {
            return newsList.length();
        }

        @Override
        public Object getItem(int position) {
            Object obj = null;
            try {
                obj = newsList.getJSONObject(position);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return obj;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(layout, parent, false);
            TextView newsDateView = convertView.findViewById(R.id.news_date);
            TextView titleView = convertView.findViewById(R.id.news_title);
            TextView link = convertView.findViewById(R.id.news_link);
            try {
                JSONObject news = newsList.getJSONObject(position);
                Date newsDate = new Date(news.getLong("pubDate"));
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                newsDateView.setText(format.format(newsDate));
                titleView.setText(news.getString("title"));
                final String newsURL = news.getString("sourceUrl");
                String spanStr = "Browse";
                SpannableString str = new SpannableString(spanStr);
                str.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(newsURL));
                        startActivity(intent);
                    }
                }, 0, spanStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                link.setText(str);
                link.setMovementMethod(LinkMovementMethod.getInstance());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return convertView;
        }
    }
}
