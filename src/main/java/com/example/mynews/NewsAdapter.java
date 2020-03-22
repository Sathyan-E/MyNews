package com.example.mynews;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.zip.Inflater;

public class NewsAdapter extends ArrayAdapter<News> {
    //constructor
    public NewsAdapter(Activity context, List<News> list)
    {
        super(context,0,list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView== null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }
        //getting the current News Object using position integer
        News currentNews =getItem(position);
        //binding textView for Title
        TextView title = (TextView)convertView.findViewById(R.id.title);
        //setting text on title TextView
        title.setText(currentNews.getTitle());
        //binding name TexView
        TextView name=(TextView)convertView.findViewById(R.id.name);
        //setting name of current News object on name TextView
        name.setText(currentNews.getSectionName());
        return convertView;
    }
}
