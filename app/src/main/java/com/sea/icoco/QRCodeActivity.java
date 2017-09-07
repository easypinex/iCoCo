package com.sea.icoco;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.sea.icoco.ActivitySupporter.BuilderQRcode;
import com.sea.icoco.Control.DataControler;

import org.json.JSONException;
import org.json.JSONObject;

public class QRCodeActivity extends AppCompatActivity
{
    ImageView qr_img;
    DataControler dataControler = MainActivity.dataControler;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        findView();

        final JSONObject qrJson = new JSONObject();
        String couponid = "-1";
        try
        {
            qrJson.put("uid",dataControler.userData.getId());
            qrJson.put("fbid",dataControler.userData.getFbid());
            qrJson.put("ac",dataControler.ctbcData.getAc());
            qrJson.put("bankNo","822");
            qrJson.put("name",dataControler.userData.getName());
            qrJson.put("coupon",couponid);
        } catch (Exception e) { Log.e("Debug QRCode",e.toString()); }

        String coupon_id = getIntent().getStringExtra("couponid");
        if (coupon_id != null)
        {
            try
            {
                qrJson.put("coupon", coupon_id);
                new BuilderQRcode(qr_img, qrJson.toString()).build();
            } catch (Exception e){}
        }
        else
        {
            if(dataControler.couponData.getCouponData().length()>0)
            {
                new AlertDialog.Builder(this).setTitle("提醒").setMessage("要使用優惠劵嗎").setPositiveButton("是", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        QRCodeActivity.this.finish();
                        startActivity(new Intent(QRCodeActivity.this,SearchCouponActivity.class));
                    }
                }).setNegativeButton("否", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        try
                        {
                            new BuilderQRcode(qr_img,qrJson.toString()).build();
                        } catch (JSONException e) { e.printStackTrace(); }
                    }
                }).setCancelable(false).show();
            }
            else if (dataControler.couponData.getCouponData().length()<1)
            {
                try
                {
                    new BuilderQRcode(qr_img, qrJson.toString()).build();
                }catch (Exception e1) {};
            }
        }





    }

    private void findView()
    {
        qr_img = (ImageView) findViewById(R.id.qr_img);
    }
}
