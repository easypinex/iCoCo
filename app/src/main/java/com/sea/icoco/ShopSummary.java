package com.sea.icoco;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sea.icoco.Control.DataControler;

import org.json.JSONException;

public class ShopSummary extends AppCompatActivity {
    DataControler dataControler = MainActivity.dataControler;
    ImageView writeDiary_img;
    TextView shopName_txv,shopAddres_txv;
    String shopUid_str;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_summary);
        shopUid_str = this.getIntent().getStringExtra("shopUid");
        findView();
        setView();
        setClickEvent();
        onViewReady();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_summary, menu);
        return true;
    }

    private void setView() {
        try {
            shopName_txv.setText(dataControler.shopData.getShopData_uid().getJSONObject(shopUid_str).getString("shop_name"));
            shopAddres_txv.setText(dataControler.shopData.getShopData_uid().getJSONObject(shopUid_str).getString("address"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setClickEvent() {
        writeDiary_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ShopSummary.this,TakePictureActivity.class));
            }
        });
    }

    private void onViewReady() {
//        shopUid.setText(shopUid_str);
    }

    private void findView() {
        writeDiary_img = (ImageView) findViewById(R.id.writeDiary_img);
        shopName_txv = (TextView) findViewById(R.id.shopName_txv);
        shopAddres_txv = (TextView) findViewById(R.id.shopAddres_txv);
    }
}
