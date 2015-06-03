package com.matthewbulat.mdxmap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by Matthew Bulat on 27/05/2015.
 */
public class Database extends SQLiteOpenHelper{
    private static String DATABASE_PATH;
    private static final String DATABASE_NAME = "roomlocation";
    private static String DBFILE_PATH;
    private static final int SCHEMA_VERSION=1;
    public SQLiteDatabase dsSqlite;
    private final Context myContext;

    public Database(Context context){
        super(context,DATABASE_NAME,null,SCHEMA_VERSION);
        if(android.os.Build.VERSION.SDK_INT >= 17){
            DATABASE_PATH = context.getApplicationInfo().dataDir + "/databases/";
        }
        else
        {
            DATABASE_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        }
        DBFILE_PATH = DATABASE_PATH+DATABASE_NAME;
        this.myContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void createDatabase(){
        createDB();
    }

    public boolean openDataBase() throws SQLException
    {
        dsSqlite = SQLiteDatabase.openDatabase(DBFILE_PATH, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        return dsSqlite != null;
    }

    private void createDB() {
        if(!checkDataBase()){
            this.getReadableDatabase();
            this.close();
            copyDBFromResource();
        }
    }
    private boolean checkDataBase()
    {
        File dbFile = new File(DATABASE_PATH + DATABASE_NAME);
        return dbFile.exists();
    }
    private void copyDBFromResource() {
        InputStream inputStream = null;
        OutputStream outputStream= null;
        try{
            inputStream = myContext.getAssets().open(DATABASE_NAME);
            outputStream = new FileOutputStream(DBFILE_PATH);
            byte[] buffer = new byte[1024];
            int length;
            while((length=inputStream.read(buffer))>0){
                outputStream.write(buffer,0,length);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
        }catch (IOException e){

        }
    }
    @Override
    public synchronized void close()
    {
        if(dsSqlite != null)
            dsSqlite.close();
        super.close();
    }


}
