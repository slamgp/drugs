package com.example.drugs3.view;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.drugs3.R;
import com.example.drugs3.controller.MyChestController;
import com.example.drugs3.model.dao.Chest;
import com.example.drugs3.model.dao.DBHelper;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MyChestFragment extends Fragment {

	private MyChestController myChestController;
	LayoutInflater layoutInflater;
	private ListView lv; 
	private ArrayList<Chest> listChest;
	private ArrayList<HashMap<String, Object>> mainList;
	private SimpleAdapter mainAdapter;
	
	public final static String NAME = "name";
	public final static String START = "start";
	public final static String END = "end";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d("panchenko", "3333");
		
		myChestController= new MyChestController(getActivity());
		
		layoutInflater = inflater;
		View viewInflate = inflater.inflate(R.layout.my_chest_fragment, null, false);
		
		lv = (ListView) viewInflate.findViewById(R.id.listChest);
		
		listChest = (ArrayList<Chest>) myChestController.selectChest();
		
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
		
		mainAdapter = new SimpleAdapter(getActivity(), mainList, R.layout.my_chest_ithem, arrFrom, arrTo);
		
		lv.setAdapter(mainAdapter);
		
		
		return viewInflate;
		
	}

}
