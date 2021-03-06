package com.matthewbulat.mdxmap;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;

/**
 * Created by Matthew Bulat on 08/04/2015.
 */
public class GPSTracker extends Service implements LocationListener {

    private final Context context;
    private boolean isGPSEnabled=false;
    private boolean isNetworkEnabled = false;
    protected boolean canGetLocation=false;
    private double latitude;
    private double longitude;
    private static final long MinDistanceChangeForUpdates=10;
    private static final long MINTimeBWUpdates=1000*60*1;
    private Location location;

    protected LocationManager locationManager;
    public GPSTracker(Context context){
        this.context=context;
        getLocation();
    }
    public Location getLocation(){
        try{
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if(!isGPSEnabled && !isNetworkEnabled){

            }else{
                this.canGetLocation=true;
                if(isNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MINTimeBWUpdates, MinDistanceChangeForUpdates, this);
                }

                if(locationManager!=null){
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                    if(location!= null){
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                    }
                }
            }
            if(isGPSEnabled){
                if(location==null){
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,MINTimeBWUpdates,MinDistanceChangeForUpdates,this);
                    if(locationManager!=null){
                        location=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if(location!=null){
                            latitude=location.getLatitude();
                            longitude=location.getLongitude();
                        }
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return location;
    }
    public void stopUsingGPS(){
       if(locationManager!=null){
           locationManager.removeUpdates(GPSTracker.this);
       }
    }
    public double getLatitude(){
      if(location!=null){
          latitude=location.getLatitude();
      }
        return latitude;
    }
    public double getLongitude(){
        if(location!=null){
            longitude=location.getLongitude();
        }
        return longitude;
    }

    public boolean isCanGetLocation(){
        return this.canGetLocation;
    }

    public void showSettingsAlerts(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("GPS is settings");
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }
    @Override
    public void onLocationChanged(Location arg0){

    }
    @Override
    public void onProviderDisabled(String arg0){

    }
    @Override
    public void onProviderEnabled(String arg0){

    }
    @Override
    public void onStatusChanged(String arg0,int args1,Bundle args2){

    }
    @Override
    public IBinder onBind(Intent intent){
        return null;
    }
}
