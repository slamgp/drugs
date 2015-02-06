package com.example.drugs3.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.drugs3.R;
import com.example.drugs3.controller.DescriptionController;
import com.example.drugs3.controller.MyChestController;
import com.example.drugs3.model.dao.Chest;
import com.example.drugs3.model.dao.DBHelper;
import com.example.drugs3.model.dao.Preparat;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class MyChestFragment extends Fragment implements OnItemClickListener{

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
		
		lv.setOnItemClickListener(this);
		
		cretaeList();
		
		return viewInflate;
		
	}
	
	private void cretaeList()
	{
		
		mainAdapter = myChestController.createAdapter();
		
		listChest =  myChestController.getListChest();
		
		mainList = myChestController.getMainList();
		
		lv.setAdapter(mainAdapter);
		
	}
	
	private void openIthem(int id)
	{
		Log.d("panchenko", "selected record = " + id); 
		Preparat prep = ((Chest) listChest.get(id)).getPreparat();
		DescriptionController descriptionController = new DescriptionController(getActivity());
		descriptionController.openIthem(prep);

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view,
	        int position, long id) {
		openIthem(position);	
	}

}
