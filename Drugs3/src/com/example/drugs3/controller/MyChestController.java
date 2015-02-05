package com.example.drugs3.controller;







import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.net.http.SslCertificate.DName;
import android.util.Log;

import com.example.drugs3.model.dao.Chest;
import com.example.drugs3.model.dao.DBHelper;
import com.example.drugs3.model.dao.Preparat;


public class MyChestController {
	private Preparat preparatToAddChest;
	private Date start;
	private Date end;
	private Context context;
	DBHelper myDB;
	
	public MyChestController(Context context)
	{
		this.context = context;
		this.myDB = new DBHelper(context);
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
	
	public List<Chest> selectChest()
	{
		List<Chest> listChest = null;
		listChest = myDB.selectChest();
		
		return listChest;
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


}
