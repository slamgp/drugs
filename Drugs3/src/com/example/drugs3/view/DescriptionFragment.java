package com.example.drugs3.view;

import com.example.drugs3.R;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DescriptionFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.description_layaut, container, false);
		TextView tv = (TextView) v.findViewById(R.id.textView3);
		String discription = ((MainActivity) getActivity()).getDiscription();
		tv.setText(Html.fromHtml(discription));
		
		return v;
		
	}

}
