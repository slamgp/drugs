package com.example.drugs3.view;

import java.util.List;

import com.example.drugs3.R;
import com.example.drugs3.controller.FavoriteController;
import com.example.drugs3.controller.MyChestController;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class FavoriteFragment extends Fragment {

	private FavoriteController favoriteController;
	LayoutInflater layoutInflater;
	private ListView lv; 
	private List listFavorite;
	private List  mainList;
	private SimpleAdapter mainAdapter;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		favoriteController= new FavoriteController(getActivity());
		
		layoutInflater = inflater;
		View viewInflate = inflater.inflate(R.layout.favorite_fragment, null, false);
		
		lv = (ListView) viewInflate.findViewById(R.id.listFavorite);
		
		cretaeList();
		
		return viewInflate;
		
	}
	
	
	private void cretaeList()
	{
		listFavorite =  favoriteController.selectFavorite();
		
		mainAdapter = favoriteController.createAdapter();
		
		mainList = favoriteController.getMainList();
		
		lv.setAdapter(mainAdapter);
		
	}

}
