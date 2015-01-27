package com.example.drugs3.model.dao;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.example.drugs3.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MyDBHelper {
	
	private Context myContext;
	private String dbName;
	private File  dbPath;
	private SQLiteDatabase myDataBase;
	private File dbFile;
	private ProgressDialog pd;
	private int bdSize;
	
	final int STATUS_DOWNLOAD_START = 1; // загрузка началась
	final int STATUS_DOWNLOAD_FILE = 2; // файл загружен
	final int STATUS_DOWNLOAD_END = 3; // загрузка закончена
	
	public MyDBHelper(Context cont, String dbName)
	{
		this.myContext = cont;
		this.dbName = dbName;
		dbPath =  myContext.getExternalCacheDir();
		try {
			createDataBase();
		//	openDataBase(); 
		} catch (IOException e) {
			myDataBase = null;
		}
		pd = new ProgressDialog(myContext);
		pd.setTitle(myContext.getResources().getString(R.string.pd_title));
		Log.d("panchenko", " copy3" );
	//	pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	    // включаем анимацию ожидания
	 //   pd.setIndeterminate(true);
	}
	
	private void createDataBase() throws IOException{
		dbPath.mkdirs();
	    dbFile = new File(dbPath, dbName);
	    if(!dbFile.exists()) {
	    	Log.d("panchenko", "no find");
	    	dbFile.createNewFile(); 
			Log.d("panchenko", "pered main");
	    	mainProcessing();
	     }else
	     {
	    	 Log.d("panchenko", "pered main");
	    	 mainProcessing();
	     }
	}
	
	private void copyFromZipFile() {
	      
	    InputStream is = myContext.getResources().openRawResource(R.raw.apteka);
	  //  Toast.makeText(myContext, "is", Toast.LENGTH_LONG).show();
	    try {
			OutputStream myOutput = new FileOutputStream(dbFile.getAbsolutePath());
	  //  Toast.makeText(myContext, "outpt", Toast.LENGTH_LONG).show();
	    ZipInputStream zis = new ZipInputStream(new BufferedInputStream(is));
	   // Toast.makeText(myContext, "zip", Toast.LENGTH_LONG).show();
	    Log.d("panchenko", " copy1" );
		Log.d("panchenko", " copy2" );
		Log.d("panchenko", " copy4" );
		bdSize = is.available(); 
		Log.d("panchenko", " copy5" );
	//	pd.setMax(bdSize);
		Log.d("panchenko", " copy6" );
	    
	  /*  Handler h = new Handler() {
	        public void handleMessage(android.os.Message msg) {
	          switch (msg.what) {
	          case STATUS_DOWNLOAD_START:
	        	pd.setProgress(0);
	        	pd.show();
	            break;
	          case STATUS_DOWNLOAD_FILE:
	        	  pd.setProgress(msg.arg1);
	            break;
	          case STATUS_DOWNLOAD_END:
	            Toast.makeText(myContext, "finish!", Toast.LENGTH_LONG).show();
	        	pd.cancel();
	            break;
	          }
	        };
	      };
	      h.sendEmptyMessage(STATUS_DOWNLOAD_START);*/
	      Log.d("panchenko", " copy7" );
	   
	      try {
	        ZipEntry ze;
	        while ((ze = zis.getNextEntry()) != null) {
	            ByteArrayOutputStream baos = new ByteArrayOutputStream();
	            byte[] buffer = new byte[1024];
	          //  byte[] buffer = new byte[is.available()];
	            int count;
	            int i = 1;
	        
	            
	           while ((count = zis.read(buffer)) != -1) {
	            	Log.d("panchenko", " " + i);
	            	i++;
	                baos.write(buffer, 0, count);
	               // Log.d("panchenko", ""+baos.toString());
	             //   Log.d("panchenko", " write");
		            baos.writeTo(myOutput);
		            bdSize = bdSize - 1024;
		       //     h.obtainMessage(STATUS_DOWNLOAD_FILE,1024,bdSize);
	             //   break;
	            }
	           baos.close();
	        }
	    	} catch (Exception e) {
	        	Log.d("panchenko", " error copy1" );
			}finally {
	    		zis.close();
	    		myOutput.flush();
	    		myOutput.close();
	    		is.close();
	    	}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	
	public SQLiteDatabase openDataBase() throws SQLException{
	    myDataBase = SQLiteDatabase.openDatabase(dbFile.getAbsolutePath(), null, SQLiteDatabase.OPEN_READWRITE);
	    return myDataBase;
	}
	

	
	private void mainProcessing() {
	    // Здесь трудоемкие задачи переносятся в дочерний поток.
		Log.d("panchenko", "run1");
	    Thread thread = new Thread(null, doBackgroundThreadProcessing,
	            "Background");
	    thread.start();
	}
	
	private Runnable doBackgroundThreadProcessing = new Runnable() {
	    public void run() {
	        	Log.d("panchenko", "run2");
				copyFromZipFile();
	
	    }
	};
	
}
