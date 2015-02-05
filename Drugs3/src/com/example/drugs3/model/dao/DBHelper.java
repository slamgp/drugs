package com.example.drugs3.model.dao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.example.drugs3.R;
import com.example.drugs3.controller.MyChestController;
import com.example.drugs3.view.MainActivity;

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
    public static final int  INFO = 3;
    public static final int  ID_PREPARAT = 1;
   
    private AsyncTask<String, Integer, File> asc; 
    
 
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
           copyDataBase();
          // openDataBase();
       }

   }
   
   public boolean checkDataBase(){
       return DB_FILE.exists();
   }
   
   protected void copyDataBase() throws IOException{

	   
	 asc = new AsyncTask<String, Integer, File>() {

		   private Exception m_error = null;
		   ProgressDialog pd = new ProgressDialog(myContext);
		   @Override
		   protected void onPreExecute() {
			   super.onPreExecute();
			   Log.d("panchenko", "onPreExecute start"); 
			pd.setTitle(myContext.getResources().getString(R.string.app_name)); 
		    pd.setMessage(myContext.getResources().getString(R.string.pd_title));
		    Log.d("panchenko", "onPreExecute start 1");   
		    pd.setCancelable(false);
		    pd.setMax(100);
		    pd
		      .setProgressStyle(ProgressDialog.STYLE_SPINNER);
		    pd.setIndeterminate(true);
		    pd.show();
		    Log.d("panchenko", "onPreExecute end");
		   }
	   
		   @Override
		   protected File doInBackground(String... params) {
			   Log.d("panchenko", "EXECUTE");
			   long totalSize;
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
			      totalSize = ze.getSize();
			      byte[] buffer = new byte[1024];
			    int length;
			     	while ((length = zis.read(buffer))>0){
			    		myOutput.write(buffer, 0, length);
			    		downloadedSize += zis.available();
			   			//int k = (int)(downloadedSize/totalSize *100);
			   			//Log.d("panchenko", "koeff = " + k + " ( " + downloadedSize + "/" + totalSize + ")");
			    	//	publishProgress(k);
			    		
			    	}
			    	Log.d("panchenko", "external 2");
			    }
			    Log.d("panchenko", "coping finish");	
			       //��������� ������
			    myOutput.flush();
			    myOutput.close();
			    myInput.close();
			  // isExsist = true;
			    Log.d("panchenko", "selected main list start");	
			    Log.d("panchenko", "selected main list finish");	
			    Log.d("panchenko", "close");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    return null;   
		   }
		   
		   @Override
		    protected void onProgressUpdate(Integer... progress) {
		        super.onProgressUpdate(progress);
		    }
		   
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
	        ((MainActivity) myContext).selectMainListPreparation();
		    Log.d("panchenko", "onPostExecute start"); 
		   }
	   };
	   
	asc.execute();

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
		String query = "Select preparation._id, preparation.name,favorite.id_preparat,chest.id_preparat  from preparation LEFT JOIN favorite on  preparation._id=favorite.id_preparat LEFT JOIN chest on  preparation._id=chest.id_preparat ; ";
		Log.d("panchenko", query); 
		Cursor c = myDataBase.rawQuery(query, null);
		int  i = 0;
		while (c.moveToNext())
		{
			int favorite_id = c.getInt(2);
			int chest_id = c.getInt(3);
			
			boolean isFavorite = false;
			boolean isChest = false;
			if(favorite_id > 0) isFavorite = true;
			if(chest_id > 0) isChest = true;
			Preparat prep = new Preparat(c.getInt(0),c.getString(1),isFavorite,isChest);
		//	Log.d("panchenko", ); 
			resList.add(prep);
		}
		Log.d("panchenko", "end select"); 
		myDataBase.close();
		return resList;
	}


	public String selectInfoById(int id)
	{
		Log.d("panchenko", "start select"); 
		String res = "";
		openDataBase();
		String guery = "Select info from preparation where _id = " + id;
		Log.d("panchenko", guery); 
		Cursor c = myDataBase.rawQuery(guery, null);
		Log.d("panchenko", "query good"); 
		int  i = 0;
		while (c.moveToNext())
		{
			Log.d("panchenko", "1"); 
			res = c.getString(0);
		}
		Log.d("panchenko", "end select"); 
		myDataBase.close();
		return res;
	}
	
	public void insertIntoFavorite(int id)
	{
		Log.d("panchenko", "start insert "+ id); 
		String res = "";
		openDataBase();
		String guery = "Insert into favorite (id_preparat) values(" + id + ")";
		Log.d("panchenko", guery); 
		myDataBase.execSQL(guery);
		myDataBase.close();
		Log.d("panchenko", "finish insert"); 
	}
	
	public void deleteFromFavorite(int id)
	{
		Log.d("panchenko", "start select " + id); 
		String res = "";
		openDataBase();
		String guery = "delete from favorite where id_preparat = " + id;
		Log.d("panchenko", guery); 
		myDataBase.execSQL(guery);
		myDataBase.close();
	}
	
	public void insertIntoChest(int id, Date start, Date end)
	{
		Log.d("panchenko", "start insert "+ id); 
		
		Log.d("panchenko", "start insert "+ start); 
		Log.d("panchenko", "start insert "+ end); 
		String res = "";
		openDataBase();
		String guery = "Insert into chest (id_preparat,start_data,end_data) values(" + id +", '" +  MyChestController.DateToSqlite(start)+ "', '"+  MyChestController.DateToSqlite(end)+ "')";
		Log.d("panchenko", guery); 
		myDataBase.execSQL(guery);
		myDataBase.close();
		Log.d("panchenko", "finish insert"); 
	}
	
	public void deleteFromChest(int id)
	{
		Log.d("panchenko", "start select " + id); 
		String res = "";
		openDataBase();
		String guery = "delete from chest where id_preparat = " + id;
		Log.d("panchenko", guery); 
		myDataBase.execSQL(guery);
		myDataBase.close();
	}



	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

	public List<Chest> selectChest()
	{
		//deleteOldPreparatsFromChest();
		
		Log.d("panchenko", "start select"); 
		List <Chest> resList = new ArrayList<Chest>();
		openDataBase();
		
		final Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR) - 1900;
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		
		Date currentDate = new Date(year,month,day);
	//	String query = "Select chest.start_data,chest.end_data, preparation._id, preparation.name, preparation.code from chest  LEFT JOIN preparation on chest.id_preparat = preparation._id  where start_data <= " + currentDate + ";";
		String query = "Select chest.start_data,chest.end_data, preparation._id, preparation.name, preparation.code from chest  LEFT JOIN preparation on chest.id_preparat = preparation._id;";
		Log.d("panchenko", query); 
		Cursor c = myDataBase.rawQuery(query, null);
		int  i = 0;
		Log.d("panchenko", "vuborka"); 
		while (c.moveToNext())
		{
			Log.d("panchenko", "vuborka 1"); 
			boolean isFavorite = false;
			Log.d("panchenko", "create preparat"); 
			Preparat prep = new Preparat(c.getInt(2),c.getString(3),isFavorite,false);
			
			Date start = MyChestController.DateTFromSqlite(c.getString(0));
			Date end =  MyChestController.DateTFromSqlite(c.getString(1));
			Log.d("panchenko", c.getString(0)); 
			Log.d("panchenko", c.getString(1)); 
			Chest chest = new Chest(prep, start, end);
			Log.d("panchenko", chest.toString() + "-" + chest.getStartData().toString() + "-"+ chest.getEndData()); 
			resList.add(chest);
		}
		Log.d("panchenko", "end select"); 
		myDataBase.close();
		return resList;
	}
	
	public List<Favorite> selectFavorite()
	{
		//deleteOldPreparatsFromChest();
		
		Log.d("panchenko", "start select"); 
		List <Favorite> resList = new ArrayList<Favorite>();
		openDataBase();

		String query = "select preparation._id, preparation.name, preparation.code from favorite  LEFT JOIN preparation on favorite.id_preparat = preparation._id;";
		
		Log.d("panchenko", query); 
		Cursor c = myDataBase.rawQuery(query, null);
		int  i = 0;
		Log.d("panchenko", "vuborka"); 
		while (c.moveToNext())
		{
			Log.d("panchenko", "vuborka 1"); 
			Log.d("panchenko", "create preparat"); 
			Preparat prep = new Preparat(c.getInt(0),c.getString(1),false,true);
			
			Favorite favorite = new Favorite(prep);
			Log.d("panchenko", favorite.toString()); 
			resList.add(favorite);
		}
		Log.d("panchenko", "end select"); 
		myDataBase.close();
		return resList;
	}
	
	private void deleteOldPreparatsFromChest()
	{

		openDataBase();
		
		final Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR) - 1900;
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		
		Date currentDate = new Date(year,month,day);
		//String query = "delete from chest where end_data < " + currentDate +" ;";
		String query = "delete from chest ;";
		myDataBase.execSQL(query);
		myDataBase.close();

	}
	
    
}
