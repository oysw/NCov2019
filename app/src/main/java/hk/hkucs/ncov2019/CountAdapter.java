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

    class ViewHolder{
        TextView provinceName;
        TextView confirmedCount;
        TextView suspectedCount;
        TextView curedCount;
        TextView deadCount;
    }

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
        ViewHolder holder = new ViewHolder();
        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(layout, parent, false);
            holder.provinceName = convertView.findViewById(R.id.province_name);
            holder.confirmedCount = convertView.findViewById(R.id.confirmed_count);
            holder.suspectedCount = convertView.findViewById(R.id.suspected_count);
            holder.curedCount = convertView.findViewById(R.id.cured_count);
            holder.deadCount = convertView.findViewById(R.id.dead_count);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        try {
            JSONObject area = areaList.getJSONObject(position);
            holder.provinceName.setText(area.getString("provinceEnglishName"));
            holder.confirmedCount.setText(area.getString("confirmedCount"));
            holder.suspectedCount.setText(area.getString("suspectedCount"));
            holder.curedCount.setText(area.getString("curedCount"));
            holder.deadCount.setText(area.getString("deadCount"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return convertView;
    }
}
