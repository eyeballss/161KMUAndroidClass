package me.blog.eyeballss.mylogger;

import android.app.Activity;
import android.util.Log;
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

    public void remove(int position){
        datas.remove(position);
    }

    public String getText(int position){
        return datas.get(position).toString();
    }

    public void show(){
        for(int i=0; i<datas.size(); i++){
            Log.d("ListViewAdapter", i+"번째 데이터는 "+datas.get(i));
        }
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
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
