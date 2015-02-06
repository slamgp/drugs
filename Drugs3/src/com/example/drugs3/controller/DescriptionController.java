package com.example.drugs3.controller;

import com.example.drugs3.model.dao.Preparat;
import com.example.drugs3.view.MainActivity;

import android.content.Context;
import android.util.Log;

public class DescriptionController {
	
	Context context;
	
	public DescriptionController(Context context)
	{
		this.context = context;
	}
	
	public void openIthem(Preparat prep)
	{
		MainListController mainListController = new MainListController(context); 
		Log.d("panchenko", "prep id record = " + prep.getId()); 
		String discription = mainListController.selectInfoById(prep.getId());
		((MainActivity) context).showDescription(discription);
	}

}
