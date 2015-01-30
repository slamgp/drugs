package com.example.drugs3.model.dao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.example.drugs3.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper{
	 //����������� ��������� ���� � ���� ������ ����������
    private  File DB_PATH;
    private  File DB_FILE;
    
    public static final int  ID = 0;
    public static final int  NAME = 1;
    public static final int  INFO = 2;
    public static final int  ID_PREPARAT = 1;
 
    private static String DB_NAME = "apteka";
 
    private SQLiteDatabase myDataBase;
 
    private final Context myContext;
    
    public DBHelper(Context cont)
    {
    	super(cont, DB_NAME, null, 1);
    	this.myContext = cont;
    	if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
    	{
    		Log.d("panchenko", "flash card");
    		DB_PATH =  myContext.getExternalCacheDir();
    	}else{
    		DB_PATH =  new File("/data/data/"
    				+ myContext.getPackageName() + "/databases/");
    		Log.d("panchenko", "memory divice");
    	}
    	Log.d("panchenko", "1 " + DB_PATH.getAbsolutePath() + DB_NAME);
    	DB_FILE = new File(DB_PATH,DB_NAME);
    	Log.d("panchenko", "2 " + DB_FILE.getAbsolutePath());
    	
    	
    }
    

   public void createDataBase() throws IOException{

       boolean dbExist = checkDataBase();

       if(dbExist){
    	   Log.d("panchenko", "base finded");
    	//   openDataBase();
       }else{

    	   Log.d("panchenko", "base not finded");	
           this.getReadableDatabase();

           Log.d("panchenko", "copy data base");	
           copyDataBase();
          // openDataBase();
       }

   }
   
   private boolean checkDataBase(){
       return DB_FILE.exists();
   }
   
   protected void copyDataBase() throws IOException{
	   final ProgressDialog pd = new ProgressDialog(myContext);
	   
	   new AsyncTask<String, Integer, File>() {
		   private Exception m_error = null;
		   
		   @Override
		   protected void onPreExecute() {
			   Log.d("panchenko", "onPreExecute start"); 
			pd.setTitle(myContext.getResources().getString(R.string.app_name)); 
		    pd.setMessage(myContext.getResources().getString(R.string.pd_title));
		    Log.d("panchenko", "onPreExecute start 1");   
		    pd.setCancelable(false);
		    pd.setMax(100);
		    pd
		      .setProgressStyle(ProgressDialog.STYLE_SPINNER);
		 
		    pd.show();
		    Log.d("panchenko", "onPreExecute end");
		   }
	   
		   @Override
		   protected File doInBackground(String... params) {
			   Log.d("panchenko", "doInBackground start");
			   int totalSize;
			   int downloadedSize = 0;
			   
		       //��������� ��������� �� ��� �������� �����
			   Log.d("panchenko", "copy start");
		       InputStream myInput;
			try {
				myInput = myContext.getAssets().open("apteka.zip");
				ZipInputStream zis = new ZipInputStream(myInput);
			       
				Log.d("panchenko", "is= "+ myInput.toString());	
			       //���� �� ����� ��������� ��
			    String outFileName = DB_FILE.getAbsolutePath();
			    Log.d("panchenko","new bd = " + outFileName);	
			       //��������� ������ ���� ������ ��� ��������� �����
			    OutputStream myOutput = new FileOutputStream(outFileName);
			    Log.d("panchenko", "os= "+ myOutput.toString());	
			       //���������� ����� �� ��������� ����� � ���������
			       			   
			       
			    ZipEntry ze;
			    while ((ze = zis.getNextEntry()) != null) {
			    totalSize = zis.available();
			    byte[] buffer = new byte[1024];
			    int length;
			    	while ((length = zis.read(buffer))>0){
			    		myOutput.write(buffer, 0, length);
			    		downloadedSize += length;
			    		Log.d("panchenko", "" + length + "  " + totalSize);
			    //		publishProgress(downloadedSize, totalSize);
			    		
			    	}
			    	Log.d("panchenko", "external 2");
			    }
			    Log.d("panchenko", "coping finish");	
			       //��������� ������
			    myOutput.flush();
			    myOutput.close();
			    myInput.close();
			    Log.d("panchenko", "close");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			  Log.d("panchenko", "doInBackground end");  
		       try {
		            Thread.sleep(5000);
		        } catch (InterruptedException e) {
		            e.printStackTrace();
		        }
		    return null;   
		   }
		/*   @Override
		   protected void onProgressUpdate(Integer... values) {
			   Log.d("panchenko", "onProgressUpdate start");   
		    pd
		      .setProgress((int) ((values[0] / (float) values[1]) * 100));
		    Log.d("panchenko", "onProgressUpdate end");
		   };*/
		   @Override
		   protected void onPostExecute(File file) {
			   Log.d("panchenko", "onPostExecute start"); 
		    // ���������� ���������, ���� �������� ������
		    if (m_error != null) {
		     m_error.printStackTrace();
		     return;
		    }
		    // ��������� �������� � ������� ��������� ����
		    pd.hide();
		    Log.d("panchenko", "onPostExecute start"); 
		   }
	   }.execute();
   }
   
   private void openDataBase() throws SQLException{
	   
       //��������� ��
	   Log.d("panchenko", "open db");	
       String myPath = DB_FILE.getAbsolutePath();
       myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
      
   }
   
   @Override
   public synchronized void close() {
	   Log.d("panchenko", "close db");
           if(myDataBase != null)
        	   Log.d("panchenko", "close db not null");
               myDataBase.close();

           super.close();

   }


	public List<Preparat> selectAllDrugs()
	{
		Log.d("panchenko", "start select"); 
		List <Preparat> resList = new ArrayList<Preparat>();
		openDataBase();
		Cursor c = myDataBase.rawQuery("Select * from preparation", null);
		int  i = 0;
		while (c.moveToNext())
		{
			Preparat prep = new Preparat(c.getInt(DBHelper.ID),c.getString(DBHelper.NAME),c.getString(DBHelper.INFO));
			Log.d("panchenko", c.getString(2)); 
			resList.add(prep);
		}
		Log.d("panchenko", "end select"); 
		myDataBase.close();
		return resList;
	}



	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}


    
}
