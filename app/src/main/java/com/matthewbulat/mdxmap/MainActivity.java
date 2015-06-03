package com.matthewbulat.mdxmap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;


public class MainActivity extends Activity {
    GPSTracker gps;
    DatabaseAdapter db;
    CustomAdapter dataAdapter=null;
    private ArrayList<LocationObject> roomLocation;
    double latitudeCurrent;
    double longitudeCurrent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DatabaseAdapter(getApplicationContext());
        db.createDatabase();
        roomLocation=db.getAllLocation();
        dataAdapter= new CustomAdapter(this,R.layout.row_for_room,roomLocation);
        ListView listView = (ListView) findViewById(R.id.roomList);
        listView.setAdapter(dataAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void displayLocation(View view) {
        gps = new GPSTracker(MainActivity.this);
        if (gps.canGetLocation) {
            latitudeCurrent = gps.getLatitude();
            longitudeCurrent = gps.getLongitude();
            MapActivity(longitudeCurrent,latitudeCurrent,"test");
        } else {
            gps.showSettingsAlerts();
        }
    }
    private class CustomAdapter extends ArrayAdapter<LocationObject>{
        private ArrayList<LocationObject> roomList;
        public CustomAdapter(Context context, int textViewResourceId, ArrayList<LocationObject> roomList){
            super(context, textViewResourceId, roomList);
            this.roomList= new ArrayList<>();
            this.roomList.addAll(roomList);
        }
        private class ViewHolder{
            TextView code;
        }
        @Override
            public View getView(final int position, View convertView, ViewGroup parent){
            final ViewHolder holder;
            roomList.get(position);

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.row_for_room, null);

                holder = new ViewHolder();
                holder.code = (TextView) convertView.findViewById(R.id.heading);
                convertView.setTag(holder);
                holder.code.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openLocation(holder.code.getText().toString());
                    }
                });
            }else{
                holder=(ViewHolder)convertView.getTag();
            }

            holder.code.setText(roomList.get(position).getRoomNumber());
            holder.code.setTag(roomList);
            return convertView;
        }
        private void openLocation(String position){
            for(int i=0;i<=roomList.size();i++){
                if(roomList.get(i).getRoomNumber().equals(position)){
                    MapActivity(roomList.get(i).getLongitude(),roomList.get(i).getLatitude(),roomList.get(i).getRoomNumber());
                    i=roomList.size();
                }
            }
        }
    }
    public void MapActivity(double longitude, double latitude,String roNumber){
        Intent act = new Intent(this,MapsActivity.class);
        act.putExtra("LONGITUDE",longitude);
        act.putExtra("LATITUDE",latitude);
        act.putExtra("ROOMNUMBER",roNumber);
        startActivity(act);
    }
}
