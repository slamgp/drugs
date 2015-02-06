package com.example.drugs3.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.widget.SimpleAdapter;

import com.example.drugs3.R;
import com.example.drugs3.model.dao.Chest;
import com.example.drugs3.model.dao.DBHelper;
import com.example.drugs3.model.dao.Favorite;

public class FavoriteController {
	private Context context;
	DBHelper myDB;
	private List<Favorite> listFavorite;
	private ArrayList<HashMap<String, Object>> mainList;
	private SimpleAdapter mainAdapter;
	public final static String NAME = "name";
	
	public FavoriteController(Context context)
	{
		this.context = context;
		this.myDB = new DBHelper(context);
		mainAdapter = null;
		mainList = null;
		listFavorite = null;
	}
	
	public void selectFavorite()
	{
		listFavorite = null;
		listFavorite = myDB.selectFavorite();
		Log.d("panchenko", "favorit controller select list"); 
	}
	
	public SimpleAdapter createAdapter()
	{
		selectFavorite();
		mainList = new ArrayList<HashMap<String,Object>>();
		Log.d("panchenko", "favorit controller start create ain list");
		for(Favorite favorite: listFavorite)
		{
			Log.d("panchenko", "fav1");
			HashMap< String, Object> hm = new HashMap<String, Object>();
			Log.d("panchenko", "fav2");
			hm.put(NAME, favorite.getPreparat());
			Log.d("panchenko", "fav3" + favorite);
			mainList.add(hm);
		}
		Log.d("panchenko", "favorit controller finish create ain list");
	
		String [] arrFrom = new String[1];
		int [] arrTo = new int[1];
	
		arrFrom[0] = NAME;
		arrTo[0] = R.id.namePreparaToFavorite;

		mainAdapter = new SimpleAdapter(context, mainList, R.layout.favorite_ithem, arrFrom, arrTo);
		
		return mainAdapter;
}

	public List<Favorite> getListFavorite() {
		return listFavorite;
	}

	public void setListFavorite(List<Favorite> listFavorite) {
		this.listFavorite = listFavorite;
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
