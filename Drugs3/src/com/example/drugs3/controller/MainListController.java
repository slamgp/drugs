package com.example.drugs3.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.drugs3.R;
import com.example.drugs3.model.dao.DBHelper;
import com.example.drugs3.model.dao.Favorite;
import com.example.drugs3.model.dao.Preparat;
import com.example.drugs3.view.MainActivity;

import android.content.Context;
import android.util.Log;
import android.widget.SimpleAdapter;

public class MainListController {
	
	private Context context;
	DBHelper myDB;
	List<Preparat> allPreparat;
	private ArrayList<HashMap<String, Object>> mainList;
	private SimpleAdapter mainAdapter;
	public static final String NAME = "name";
	public static final String IMG = "image";
	public static final String IMG_CHEST = "imageCH";

	
	public MainListController(Context context)
	{
		this.context = context;
		this.myDB = new DBHelper(context);
		mainAdapter = null;
		mainList = null;
		allPreparat = null;
	}
	
	public void selectAllPreparat()
	{
		allPreparat = null;
		allPreparat = ((MainActivity) context).getAllPreparat();
	}
	
	public SimpleAdapter createAdapter()
	{
		selectAllPreparat();
		mainList = new ArrayList<HashMap<String,Object>>();
		
		for(Preparat prep: allPreparat)
		{
			HashMap<String, Object>  myMapping = new HashMap<String, Object>();
			myMapping.put(NAME, prep);
			if (prep.isFavorite())
			{
				myMapping.put(IMG, R.drawable.btn_star_big_on);
			}else myMapping.put(IMG, R.drawable.btn_star_big_off);
			
			if (prep.isChest())
			{
				myMapping.put(IMG_CHEST, R.drawable.btn_chest_star_on);
			}else myMapping.put(IMG_CHEST, R.drawable.btn_star_big_off);
			
			mainList.add(myMapping);	
		}
		
		
		String arrFrom[] = new String[3];
		int arrTo[] = new int[3];
		arrFrom[0] = NAME;
		arrFrom[1] = IMG;
		arrFrom[2] = IMG_CHEST;
		
		arrTo[0] = R.id.name;
		arrTo[1] = R.id.image;
		arrTo[2] = R.id.imageCH;
		
		mainAdapter =new SimpleAdapter(context, mainList, R.layout.main_list_ithem,arrFrom,arrTo);
		
		return mainAdapter;
}

	public List<Preparat> getAllPreparat() {
		return allPreparat;
	}

	public void setAllPreparat(List<Preparat> allPreparat) {
		this.allPreparat = allPreparat;
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
	
	public String selectInfoById(int id)
	{
		return myDB.selectInfoById(id);
	}
	
	public void insertIntoFavorite(int id)
	{
		myDB.insertIntoFavorite(id);
	}
	
	public void deleteFromFavorite (int id)
	{
		myDB.deleteFromFavorite(id);
	}
	
	
}
