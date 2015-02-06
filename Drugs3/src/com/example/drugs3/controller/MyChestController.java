package com.example.drugs3.controller;


import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.net.http.SslCertificate.DName;
import android.util.Log;
import android.widget.SimpleAdapter;

import com.example.drugs3.R;
import com.example.drugs3.model.dao.Chest;
import com.example.drugs3.model.dao.DBHelper;
import com.example.drugs3.model.dao.Preparat;
import com.example.drugs3.view.MainActivity;


public class MyChestController {
	private Preparat preparatToAddChest;
	private Date start;
	private Date end;
	private Context context;
	DBHelper myDB;
	List<Chest> listChest;
	private ArrayList<HashMap<String, Object>> mainList;
	private SimpleAdapter mainAdapter;
	public final static String NAME = "name";
	public final static String START = "start";
	public final static String END = "end";
	
	public MyChestController(Context context)
	{
		this.context = context;
		this.myDB = new DBHelper(context);
		mainAdapter = null;
		mainList = null;
		listChest = null;
	}
	
	
	public Date getStart() {
		return start;
	}



	public void setStart(Date start) {
		this.start = start;
	}



	public Date getEnd() {
		return end;
	}



	public void setEnd(Date end) {
		this.end = end;
	}



	public Preparat getPreparatToAddChest() {
		return preparatToAddChest;
	}



	public void setPreparatToAddChest(Preparat preparatToAddChest) {
		this.preparatToAddChest = preparatToAddChest;
	}

	

	public void addPreparatToDbChest()
	{
		myDB.insertIntoChest(preparatToAddChest.getId(),start,end);
	}
	
	public void selectChest()
	{
		listChest = null;
		listChest = myDB.selectChest();
	}

	public SimpleAdapter createAdapter()
	{
		selectChest();
		
		mainList = new ArrayList<HashMap<String,Object>>();
		for(Chest chest: listChest)
		{
			HashMap< String, Object> hm = new HashMap<String, Object>();
			hm.put(NAME, chest.getPreparat());
			hm.put(START,  MyChestController.DateToSqlite(chest.getStartData()));
			hm.put(END,  MyChestController.DateToSqlite(chest.getEndData()));
			mainList.add(hm);
		}
	
		String [] arrFrom = new String[3];
		int [] arrTo = new int[3];
	
		arrFrom[0] = NAME;
		arrFrom[1] = START;
		arrFrom[2] = END;
		arrTo[0] = R.id.namePreparaTochest;
		arrTo[1] = R.id.startDataChest;
		arrTo[2] = R.id.endtDataChest;
	
		mainAdapter = new SimpleAdapter(context, mainList, R.layout.my_chest_ithem, arrFrom, arrTo);
		
		return mainAdapter;
}
	public List<Chest> getListChest() {
		return listChest;
	}


	public void setListChest(List<Chest> listChest) {
		this.listChest = listChest;
	}


	public static String DateToSqlite(Date date)
	{
		String result = null;
		String str = date.toString();
		String[] mass = str.split("-");
		if(mass.length > 0)
		{
			result = mass[2] + "." + mass[1]+"." + mass[0];
		}
		return result;
	}
	
	public static Date DateTFromSqlite(String date)
	{
		Date result = null;
		Log.d("panchenko", date); 
		SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
		Log.d("panchenko", "SimpleDateFormat"); 
		try {
			java.util.Date dat =  format.parse(date);
			Log.d("panchenko", " 1 " + dat.toString());
			result = new Date(dat.getYear(),dat.getMonth(),dat.getDate());
			Log.d("panchenko", " 2 " +result.toString()); 
		} catch (ParseException e) {
			Log.d("panchenko", e.toString()); 
			result = null;
		}
		return result;
	}
	
	public void deleteFromChest(int id)
	{
		myDB.deleteFromChest(id);
	}


	public DBHelper getMyDB() {
		return myDB;
	}


	public void setMyDB(DBHelper myDB) {
		this.myDB = myDB;
	}


	public ArrayList<HashMap<String, Object>> getMainList() {
		return mainList;
	}


	public void setMainList(ArrayList<HashMap<String, Object>> mainList) {
		this.mainList = mainList;
	}


	public SimpleAdapter getMainAdapter() {
		return mainAdapter;
	}


	public void setMainAdapter(SimpleAdapter mainAdapter) {
		this.mainAdapter = mainAdapter;
	}


}
