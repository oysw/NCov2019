package hk.hkucs.ncov2019;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CountAdapter extends BaseAdapter {
    private Context context;
    private JSONArray areaList;
    private int layout;

    public CountAdapter(Context context, JSONArray areaList, int layout){
        this.context = context;
        this.areaList = areaList;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return areaList.length();
    }

    @Override
    public Object getItem(int position) {
        Object obj = null;
        try {
            obj = areaList.getJSONObject(position);
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
        TextView provinceName = convertView.findViewById(R.id.province_name);
        TextView confirmedCount = convertView.findViewById(R.id.confirmed_count);
        TextView suspectedCount = convertView.findViewById(R.id.suspected_count);
        TextView curedCount = convertView.findViewById(R.id.cured_count);
        TextView deadCount = convertView.findViewById(R.id.dead_count);
        try {
            JSONObject area = areaList.getJSONObject(position);
            provinceName.setText(area.getString("provinceEnglishName"));
            confirmedCount.setText(area.getString("confirmedCount"));
            suspectedCount.setText(area.getString("suspectedCount"));
            curedCount.setText(area.getString("curedCount"));
            deadCount.setText(area.getString("deadCount"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return convertView;
    }
}
