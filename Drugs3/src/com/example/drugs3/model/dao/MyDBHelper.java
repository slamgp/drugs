package com.example.drugs3.model.dao;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.example.drugs3.R;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class MyDBHelper {
	
	private Context myContext;
	private String dbName;
	private File  dbPath;
	private SQLiteDatabase myDataBase;
	private File dbFile;
	
	public MyDBHelper(Context cont, String dbName)
	{
		this.myContext = cont;
		this.dbName = dbName;
		dbPath =  myContext.getExternalCacheDir();
		if(dbPath == null)
		{
			dbPath = myContext.getFilesDir();
		}
		try {
			createDataBase();
			openDataBase(); 
		} catch (IOException e) {
			myDataBase = null;
		}
	}
	
	private void createDataBase() throws IOException{
		dbPath.mkdirs();
	    dbFile = new File(dbPath, dbName);
	    if(!dbFile.exists()) {
	    	dbFile.createNewFile();
	        try {
	            copyFromZipFile();
	        } catch (IOException e) {
	        	dbFile = null;
	        }
	     }
	}
	
	private void copyFromZipFile() throws IOException{
	    InputStream is = myContext.getResources().openRawResource(R.raw.apteka);
	    OutputStream myOutput = new FileOutputStream(dbFile.getAbsolutePath());
	    ZipInputStream zis = new ZipInputStream(new BufferedInputStream(is));
	    try {
	        ZipEntry ze;
	        while ((ze = zis.getNextEntry()) != null) {
	            ByteArrayOutputStream baos = new ByteArrayOutputStream();
	            byte[] buffer = new byte[1024];
	            int count;
	            
	            while ((count = zis.read(buffer)) != -1) {
	                baos.write(buffer, 0, count);
	            }
	            baos.writeTo(myOutput);
	        }
	    } finally {
	        zis.close();
	        myOutput.flush();
	        myOutput.close();
	        is.close();
	    }
	}
	
	public SQLiteDatabase openDataBase() throws SQLException{
	    myDataBase = SQLiteDatabase.openDatabase(dbFile.getAbsolutePath(), null, SQLiteDatabase.OPEN_READWRITE);
	    return myDataBase;
	}
	
	
}
