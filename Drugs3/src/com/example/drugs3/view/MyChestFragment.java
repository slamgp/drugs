package com.example.drugs3.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
	private List listChest;
	private List  mainList;
	private SimpleAdapter mainAdapter;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d("panchenko", "3333");
		
		myChestController= new MyChestController(getActivity());
		
		layoutInflater = inflater;
		View viewInflate = inflater.inflate(R.layout.my_chest_fragment, null, false);
		
		lv = (ListView) viewInflate.findViewById(R.id.listChest);
		
		cretaeList();
		
		return viewInflate;
		
	}
	
	private void cretaeList()
	{
		listChest =  myChestController.selectChest();
		
		mainAdapter = myChestController.createAdapter();
		
		mainList = myChestController.getMainList();
		
		lv.setAdapter(mainAdapter);
		
	}

}
