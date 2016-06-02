package me.blog.eyeballss.mylogger;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by kesl on 2016-06-02.
 */
public class ListViewAdapter extends BaseAdapter {

    ArrayList<String> datas;
    Activity context;

    public ListViewAdapter(Activity context, ArrayList<String> datas) {
        super();
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView txtView;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.item_listview, null); //여기서 인플레이터, 즉 객체화.
            holder = new ViewHolder();
            holder.txtView = (TextView) convertView.findViewById(R.id.item_txtView);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtView.setText(datas.get(position));

        return convertView;
    }
}
