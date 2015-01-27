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
import android.util.Log;
import android.widget.Toast;

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
		//Toast.makeText(myContext, dbPath.toString(), Toast.LENGTH_LONG).show();
		try {
			createDataBase();
		//	openDataBase(); 
	//		Toast.makeText(myContext, "good", Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			myDataBase = null;
	//		Toast.makeText(myContext, "bad", Toast.LENGTH_LONG).show();
		}
	}
	
	private void createDataBase() throws IOException{
		dbPath.mkdirs();
	    dbFile = new File(dbPath, dbName);
	    if(!dbFile.exists()) {
	    	Log.d("panchenko", "no find");
	    	dbFile.createNewFile();
	    	try {
	            copyFromZipFile();
	        } catch (IOException e) {
	        	dbFile = null;
	        	Log.d("panchenko", "error");
	        }
	     }else
	     {
	    	 Log.d("panchenko", "find");
	    	  copyFromZipFile();
	     }
	}
	
	private void copyFromZipFile() throws IOException{
	    InputStream is = myContext.getResources().openRawResource(R.raw.apteka);
	  //  Toast.makeText(myContext, "is", Toast.LENGTH_LONG).show();
	    OutputStream myOutput = new FileOutputStream(dbFile.getAbsolutePath());
	  //  Toast.makeText(myContext, "outpt", Toast.LENGTH_LONG).show();
	    ZipInputStream zis = new ZipInputStream(new BufferedInputStream(is));
	   // Toast.makeText(myContext, "zip", Toast.LENGTH_LONG).show();
	    Log.d("panchenko", " copy1" );
	    try {
	        ZipEntry ze;
	        while ((ze = zis.getNextEntry()) != null) {
	            ByteArrayOutputStream baos = new ByteArrayOutputStream();
	            //byte[] buffer = new byte[1024];
	            byte[] buffer = new byte[is.available()];
	            int count;
	            int i = 1;
	            
	            baos.write(buffer,0,zis.read(buffer));
	            baos.writeTo(myOutput);
	            
	       /*    while ((count = zis.read(buffer)) != -1) {
	            	Log.d("panchenko", " " + i);
	            	i++;
	                baos.write(buffer, 0, count);
	               // Log.d("panchenko", ""+baos.toString());
	             //   Log.d("panchenko", " write");
		            baos.writeTo(myOutput);
		          //  Log.d("panchenko", " close");
		            baos.close();
	             //   break;
	            }*/
	        }
	    	} catch (Exception e) {
	        	Log.d("panchenko", " error copy1" );
			}finally {
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
