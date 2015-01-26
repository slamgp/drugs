package com.example.drugs3.view;

import com.example.drugs3.R;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MyChestFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d("panchenko", "3333");
		return inflater.inflate(R.layout.my_chest_fragment, null);
		
	}

}
