package com.example.nilot.minesweeperneo;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by nilot on 27-06-2017.
 */

public class MinesweeperArrAdapter<T> extends ArrayAdapter<T> {
    List<String> list;
    FullscreenActivity fullscreenActivity;
    public  MinesweeperArrAdapter(Context context, int resource, List<T> objects, FullscreenActivity fullscreenActivity){
        super(context,resource,objects);
        list = (List<String>)objects;
        this.fullscreenActivity = fullscreenActivity;
    }
    public int getPixelsFromDPs(int dps){
        Resources r = fullscreenActivity.getResources();
        int  px = (int) (TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dps, r.getDisplayMetrics()));
        return px;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        TextView text = new TextView(this.getContext());
        text.setText(list.get(position));
        text.setGravity(Gravity.CENTER);
        if(list.get(position).equals("1")){
            text.setBackgroundColor(Color.RED);
        }
        else {
            text.setBackgroundColor(Color.GREEN);
        }
        text.setBackgroundResource(R.drawable.grid_items_border);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(120,100);
        params.width = getPixelsFromDPs(100);
        params.height = getPixelsFromDPs(50);

        // Set the TextView layout parameters
        text.setLayoutParams(params);
        return text;
    }

}
