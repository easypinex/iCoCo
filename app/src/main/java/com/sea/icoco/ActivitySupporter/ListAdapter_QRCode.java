package com.sea.icoco.ActivitySupporter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.sea.icoco.Control.DataControler;
import com.sea.icoco.MainActivity;
import com.sea.icoco.R;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by AndyChuo on 2016/4/5.
 * 建立自訂義清單:
 *      單一項目利用 adapter_list_item 介面元件來建立
 */
public class ListAdapter_QRCode extends BaseAdapter
{
    private AppCompatActivity activity;
    DataControler dataControler= MainActivity.dataControler;;
    private static LayoutInflater inflater = null;
    int point = dataControler.exchangePoint;
    public ListAdapter_QRCode(AppCompatActivity a)
    {
        activity = a;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount()
    {
        return 1;
    }

    public Object getItem(int position)
    {
        return position;
    }

    public long getItemId(int position)
    {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        if(convertView==null)
        {
            convertView = inflater.inflate(R.layout.adapter_qrcode_item, null);
        }
        try {
            final ImageView qr_img = (ImageView) convertView.findViewById(R.id.qr_item_img);
            JSONObject qrJson = new JSONObject();
            qrJson.put("uid",dataControler.userData.getId());
            qrJson.put("fbid",dataControler.userData.getFbid());
            qrJson.put("ac",dataControler.ctbcData.getAc());
            qrJson.put("bankNo","822");
            qrJson.put("name",dataControler.userData.getName());
            qrJson.put("point",1);
            BuilderQRcode BuilderQRcode = new BuilderQRcode(qr_img,qrJson.toString());
            BuilderQRcode.setWidth(250);
            BuilderQRcode.setHeight(250);
            Bitmap result = BuilderQRcode.getBitmap();
            qr_img.setImageBitmap(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return convertView;
    }
}
