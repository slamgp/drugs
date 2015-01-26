package com.example.drugs3.view;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.drugs3.R;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainListFragment extends Fragment implements android.view.View.OnTouchListener, TextWatcher {
	
	private float startPosition = 0;
	private float endPosition = 0;	
	private static final String NAME = "name";
	private static final String IMG = "image";
	private ArrayList<HashMap<String, Object>> mainList;
	private SimpleAdapter mainAdapter;
	private ListView lv; 
	private String[] arrFrom;
	private int[] arrTo;
	private EditText etFind;
	  
	final int MENU_OPEN = 1;
	final int MENU_ADD = 2;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		 
		View v = inflater.inflate(R.layout.main_list_fragment, container, false);
		etFind = (EditText) v.findViewById(R.id.etFind);

		etFind.addTextChangedListener(this);
		
		lv = (ListView) v.findViewById(R.id.mainList);
		lv.setOnTouchListener(this);
		//lv.setOnItemClickListener(this);
		//lv.setOnItemLongClickListener(this);
		
		registerForContextMenu(lv);
		
		mainList = new ArrayList<HashMap<String,Object>>();
		
		HashMap<String, Object> myMapping;
		myMapping = new HashMap<String, Object>();
		myMapping.put(NAME, "Амизон");
		myMapping.put(IMG, R.drawable.btn_star_big_off);
		mainList.add(myMapping);
	
		myMapping = new HashMap<String, Object>();
		myMapping.put(NAME, "Карвалол");
		myMapping.put(IMG, R.drawable.btn_star_big_off);
		mainList.add(myMapping);
		
		myMapping = new HashMap<String, Object>();
		myMapping.put(NAME, "Цитрамон");
		myMapping.put(IMG, R.drawable.btn_star_big_on);
		mainList.add(myMapping);
		
		arrFrom = new String[2];
		arrTo = new int[2];
		arrFrom[0] = NAME;
		arrFrom[1] = IMG;
		arrTo[0] = R.id.name;
		arrTo[1] = R.id.image
				;
		mainAdapter =new SimpleAdapter(getActivity(), mainList, R.layout.main_list_ithem, 
				arrFrom,arrTo);
		
		lv.setAdapter(mainAdapter);
		return v;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		//Toast.makeText(generalActivity,"yach", Toast.LENGTH_LONG).show();
		if(v.getId() == R.id.mainList)
		{
			switch(event.getAction())
			{
				case MotionEvent.ACTION_DOWN: startPosition = event.getX();
				break;
				case MotionEvent.ACTION_UP: endPosition = event.getX();
				if(Math.abs(endPosition - startPosition)>30)
				{
					MainActivity act = (MainActivity)getActivity();
					act.setStratPosition(startPosition);
					act.setEndPosition(endPosition);
					act.slaidingFragments();
				}
				break;
			}
		}
		return false;
	}


	@Override
	public void afterTextChanged(Editable s) {
		Toast.makeText(getActivity(), "слово поиска: " + etFind.getText() , Toast.LENGTH_SHORT).show();
		
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		Toast.makeText(getActivity(), "слово поиска: " + etFind.getText() , Toast.LENGTH_SHORT).show();
		
	}

	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {

		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(Menu.NONE, 1, Menu.NONE, getResources().getString(R.string.open_description_str));
		menu.add(Menu.NONE, 2, Menu.NONE, getResources().getString(R.string.add_favorites_str));
	}

	
@Override
	public boolean onContextItemSelected(MenuItem item) {
  		AdapterContextMenuInfo acmi;
		switch (item.getItemId()) {
		case MENU_OPEN:
			openIthem();
			break;
		case MENU_ADD:
			acmi = (AdapterContextMenuInfo) item.getMenuInfo();
			addToFavorite((int)acmi.id);
			break;	
		default:
			break;
		}
		return super.onContextItemSelected(item);
	}

//service method start************************************
private void openIthem()
{
	((MainActivity) getActivity()).showDescription();
}

private void addToFavorite(int position)
{
	HashMap<String, Object> hm = mainList.get(position);
	if((Integer) hm.get(IMG) == R.drawable.btn_star_big_off)
	{
		hm.put(IMG, R.drawable.btn_star_big_on);
	mainList.set(position, hm);
	
	

		}else hm.put(IMG, R.drawable.btn_star_big_off);
	mainAdapter.notifyDataSetChanged();
}
//service method finish***********************************	

}
