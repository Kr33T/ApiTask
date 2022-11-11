package com.example.apitask;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class AdapterApps extends BaseAdapter {
    private Context mContext;
    List<apps> appsList;

    public AdapterApps(Context mContext, List<apps> appsList)
    {
        this.mContext = mContext;
        this.appsList = appsList;
    }

    @Override
    public int getCount() { return appsList.size(); }

    @Override
    public Object getItem(int i) { return appsList.get(i); }

    @Override
    public long getItemId(int i) { return appsList.get(i).getApp_id(); }

    private Bitmap getUserImage(String encodedImg)
    {

        if(encodedImg!=null&& !encodedImg.equals("null")) {
            byte[] bytes = Base64.decode(encodedImg, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }
        else
        {
            return null;
        }
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        View v = View.inflate(mContext, R.layout.item_layuot, null);
        TextView appNameTV = v.findViewById(R.id.appNameTV);
        TextView appAgeLimit = v.findViewById(R.id.appAgeLimitTV);
        TextView appPrice = v.findViewById(R.id.appPriceTV);
        ImageView image = v.findViewById(R.id.imageIV);
        apps apps = appsList.get(i);
        appNameTV.setText(apps.getAppName());
        appAgeLimit.setText(Integer.valueOf(apps.getAppAgeLimit()).toString() + "+");
        if(Double.valueOf(apps.getAppPrice()) == 0)
        {
            appPrice.setText("free");
        }
        else
        {
            appPrice.setText(Double.valueOf(apps.getAppPrice()).toString() + " руб.");
        }
        if(apps.getAppImage().equals("null"))
        {
            image.setImageResource(R.drawable.empty);
        }
        else
        {
            image.setImageBitmap(getUserImage(apps.getAppImage()));
        }
        return v;
    }
}
