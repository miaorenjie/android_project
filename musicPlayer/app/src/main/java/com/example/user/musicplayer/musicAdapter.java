package com.example.user.musicplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

/**
 * Created by user on 2016/9/5.
 */
public class musicAdapter extends BaseAdapter{
    private ArrayList<music> myData;
    private LayoutInflater minflater;
    private Context mcontext;

    public musicAdapter(Context context,ArrayList<music>data)
    {
        minflater=LayoutInflater.from(context);
        mcontext=context;
        myData=data;
    }
    @Override
    public int getCount() {
        return myData.size();
    }

    @Override
    public Object getItem(int position) {
        return myData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if(convertView==null)
        {
            viewHolder=new ViewHolder();
            convertView=minflater.inflate(R.layout.item,parent,false);
            viewHolder.mtextview= (TextView) convertView.findViewById(R.id.playname);
            viewHolder.mimageView= (ImageView) convertView.findViewById(R.id.image);
            viewHolder.artist= (TextView) convertView.findViewById(R.id.artist);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.mtextview.setText((String)myData.get(position).getPlayName());
        viewHolder.artist.setText((String)myData.get(position).getArtistName());
        viewHolder.mimageView.setImageBitmap((Bitmap)myData.get(position).getListcover());
        return convertView;
    }

    public final class ViewHolder
    {
        TextView mtextview;
        ImageView mimageView;
        TextView artist;
    }
}
