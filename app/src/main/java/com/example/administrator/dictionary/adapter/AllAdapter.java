package com.example.administrator.dictionary.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.dictionary.R;
import com.example.administrator.dictionary.config.ConfigFinal;

import java.util.Set;

/**
 * Created by Administrator on 2016/5/11.
 */
public class AllAdapter extends BaseAdapter {


    private Context mcontext;
    private Cursor mcursor;
    private LayoutInflater layoutInflater;

    private Set<Integer> selection;
    private int mode = -1;

    public AllAdapter(Context context, Cursor cursor) {
        mcontext = context;
        mcursor = cursor;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setSelection(Set<Integer> selection, int mode) {
        this.selection = selection;
        this.mode = mode;
    }

    @Override
    public int getCount() {
        return mcursor.getCount();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        Holder holder;
        if (view == null) {
            holder = new Holder();
            view = layoutInflater.inflate(R.layout.list_view_item, null);
            holder.tv_text = (TextView) view.findViewById(R.id.text2);
            holder.tv_title = (TextView) view.findViewById(R.id.text1);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        mcursor.moveToPosition(position);
        holder.tv_title.setText(mcursor.getString(mcursor.getColumnIndex("word")));

        if(mode!=ConfigFinal.HIDE){
            holder.tv_text.setText(mcursor.getString(mcursor.getColumnIndex("interpret")));
        }else {
            holder.tv_text.setVisibility(View.INVISIBLE);
        }

        if (mode == ConfigFinal.HISTORY_MODE) {
            if (selection.contains(position)) {
                view.setBackgroundResource(R.color.item);
            } else {
                view.setBackgroundResource(R.color.white);
            }
        } else if (mode == ConfigFinal.LISTBOOK_MODE) {
            if (selection.contains(position)) {
                view.setBackgroundResource(R.color.item);
            } else {
                view.setBackgroundResource(R.color.white);
            }
        } else {
            view.setBackgroundResource(R.color.white);
        }




        return view;
    }

    final class Holder {
        TextView tv_title;
        TextView tv_text;
    }


    public int getMode(){
        return mode;
    }
}
