package com.matthewbulat.mdxmap;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Matthew Bulat on 27/05/2015.
 */
public class DatabaseAdapter {
    protected static final String TAG = "DataAdapter";

    private final Context mContext;
    private SQLiteDatabase mDb;
    private Database mDbHelper;

    public DatabaseAdapter(Context context){
        this.mContext = context;
        mDbHelper = new Database(mContext);
    }

    public DatabaseAdapter createDatabase() throws SQLException{
        mDbHelper.createDatabase();
        return this;
    }

    public ArrayList<LocationObject> getAllLocation() throws SQLException{
        ArrayList<LocationObject> locationArrayList= new ArrayList<>();

        try{
            mDbHelper.openDataBase();
            mDbHelper.close();
            mDb = mDbHelper.getReadableDatabase();

            Cursor cursor = mDb.rawQuery("select * from roomlocation", null);

            while(cursor.moveToNext()){
              LocationObject room = new LocationObject();
                room.setRoomNumber(cursor.getString(0));
                room.setLatitude(Double.parseDouble(cursor.getString(1)));
                room.setLongitude(Double.parseDouble(cursor.getString(2)));
                room.setBuilding(cursor.getString(3));
                room.setFloor(cursor.getInt(4));
                locationArrayList.add(room);
            }
            mDb.close();
            cursor.close();

        }catch(SQLException mSQLException){
            Log.e(TAG, "open >>"+ mSQLException.toString());
            throw mSQLException;
        }
        Collections.sort(locationArrayList,new CustomComparator());
        return locationArrayList;
    }
    public class CustomComparator implements Comparator<LocationObject>{

        @Override
        public int compare(LocationObject lhs, LocationObject rhs) {
            return (lhs).getRoomNumber().compareTo(rhs.getRoomNumber());
        }
    }
    public void close()
    {
        mDbHelper.close();
    }

}
