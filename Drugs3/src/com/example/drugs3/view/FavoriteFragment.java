package com.example.drugs3.view;

import java.util.List;

import com.example.drugs3.R;
import com.example.drugs3.controller.DescriptionController;
import com.example.drugs3.controller.FavoriteController;
import com.example.drugs3.controller.MyChestController;
import com.example.drugs3.model.dao.Chest;
import com.example.drugs3.model.dao.Favorite;
import com.example.drugs3.model.dao.Preparat;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class FavoriteFragment extends Fragment implements OnItemClickListener {

	private FavoriteController favoriteController;
	LayoutInflater layoutInflater;
	private ListView lv; 
	private List<Favorite> listFavorite;
	private List  mainList;
	private SimpleAdapter mainAdapter;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		favoriteController= new FavoriteController(getActivity());
		
		layoutInflater = inflater;
		View viewInflate = inflater.inflate(R.layout.favorite_fragment, null, false);
		
		lv = (ListView) viewInflate.findViewById(R.id.listFavorite);
		
		lv.setOnItemClickListener(this);
		
		cretaeList();
		
		return viewInflate;
		
	}
	
	
	private void cretaeList()
	{	
		mainAdapter = favoriteController.createAdapter();
		
		listFavorite =  favoriteController.getListFavorite();
		
		mainList = favoriteController.getMainList();
		
		lv.setAdapter(mainAdapter);
		
	}

	private void openIthem(int id)
	{
		Log.d("panchenko", "selected record = " + id); 
		Preparat prep = listFavorite.get(id).getPreparat();
		DescriptionController descriptionController = new DescriptionController(getActivity());
		descriptionController.openIthem(prep);

	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view,
	        int position, long id) {
		openIthem(position);
	}

}
