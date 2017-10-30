package com.sea.icoco;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.journeyapps.barcodescanner.camera.CameraInstance;
import com.sea.icoco.ActivitySupporter.ListAdapter_BuyItem;
import com.sea.icoco.ActivitySupporter.ListAdapter_QRCode;
import com.sea.icoco.Control.DataControler;

import org.json.JSONException;
import org.json.JSONObject;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback ,GoogleMap.OnInfoWindowClickListener {

    public static GoogleMap mMap;
    DataControler dataControler = MainActivity.dataControler;
    AlertDialog.Builder dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        MainActivity.initMapActivity = true;
        dialogCreate();
    }

    private void dialogCreate() {
        ListAdapter_QRCode adapterItem = new ListAdapter_QRCode(MapsActivity.this);
        dialog = new AlertDialog.Builder(MapsActivity.this)
                .setTitle("請出示供商家掃描")
                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setAdapter(adapterItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_map, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_diaryMap:
                startActivity(new Intent(MapsActivity.this, DiaryMapsActivity.class));
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        dataControler.gpsData.moveCamera();
        mMap.setOnInfoWindowClickListener(this);
        if (dataControler.shopData.onLoadSuccess()) {
            for (int i = 0; i < dataControler.shopData.getShopData().length(); i++) {
                try {
                    JSONObject shop = dataControler.shopData.getShopData().getJSONObject(i);
                    String service = shop.getString("service");
                    if (service.equals("0")){
                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(Double.parseDouble(shop.getString("latitude")), Double.parseDouble(shop.getString("longitude"))))
                                .title(shop.getString("shop_name"))
                                .snippet("至該店出示QRCode消費即可集點!")
                                .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("shop", 100, 100))));
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public Bitmap resizeMapIcons(String iconName, int width, int height) {
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(iconName, "drawable", getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        dialog.show();
    }
}
