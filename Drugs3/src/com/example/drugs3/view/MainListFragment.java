package com.example.drugs3.view;


import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.drugs3.R;
import com.example.drugs3.controller.DescriptionController;
import com.example.drugs3.controller.MainListController;
import com.example.drugs3.controller.MyChestController;
import com.example.drugs3.model.dao.DBHelper;
import com.example.drugs3.model.dao.Preparat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;


public class MainListFragment extends Fragment implements android.view.View.OnTouchListener, TextWatcher, OnItemClickListener {
	
	private float startPosition = 0;
	private float endPosition = 0;	
	private List mainList;
	private SimpleAdapter mainAdapter;
	private ListView lv; 
	private EditText etFind;
	private int position;
	private MyChestController myChestController;
	private View viewInflate;
	private LayoutInflater layoutInflater;
	
	private MainListController mainListController;
	
	 DatePicker startDate;;
	 DatePicker endDate;

	List<Preparat> allPreparat;
	 
	
	final int MENU_OPEN = 1;
	final int MENU_ADD = 2;
	final int MENU_ADD_CH = 3;
	
	private android.content.DialogInterface.OnClickListener myDialogListener = new  android.content.DialogInterface.OnClickListener(){
		@Override
		public void onClick(DialogInterface dialog, int which) {
			Log.d("panchenko", "vubral " +which); 
			if(which == -1)
			{
				int yearS = startDate.getYear();
				int monthS = startDate.getMonth();
				int dayS = startDate.getDayOfMonth();
				
				int yearF = endDate.getYear();
				int monthF = endDate.getMonth();
				int dayF = endDate.getDayOfMonth();
				
				Date start = new Date(yearS - 1900, monthS, dayS);
				Date end = new Date(yearF - 1900, monthF, dayF);
			
				if(end.before(start)){
					Toast.makeText(getActivity(), "Incorrect date", Toast.LENGTH_LONG).show();
				}else{
					myChestController.setStart(start);
					myChestController.setEnd(end);
					myChestController.addPreparatToDbChest();
					dialog.cancel();
				}
			
			}else dialog.cancel();
		}



		
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		mainListController = new MainListController(getActivity());
		
		layoutInflater = inflater;
		 
		viewInflate = inflater.inflate(R.layout.main_list_fragment, container, false);
		etFind = (EditText) viewInflate.findViewById(R.id.etFind);

		etFind.addTextChangedListener(this);
		
		lv = (ListView) viewInflate.findViewById(R.id.mainList);
	//	lv.setOnTouchListener(this);
		lv.setOnItemClickListener(this);
		//lv.setOnItemLongClickListener(this);
		
		registerForContextMenu(lv);
		
		initAdapter();
		
		return viewInflate;
	}

	private void initAdapter()
	{
		mainAdapter = mainListController.createAdapter();
		allPreparat = mainListController.getAllPreparat();
		mainList = mainListController.getMainList();
		
		
		
		lv.setAdapter(mainAdapter);
	
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
		String str = etFind.getText().toString();
		mainListController.createListFromFilter(str);
		initAdapter();
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	//	Toast.makeText(getActivity(), "слово поиска: " + etFind.getText() , Toast.LENGTH_SHORT).show();
		
	}

	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {

		super.onCreateContextMenu(menu, v, menuInfo);
		
	//	menu.add(Menu.NONE, 1, Menu.NONE, getResources().getString(R.string.open_description_str));
		menu.add(Menu.NONE, 2, Menu.NONE, getResources().getString(R.string.add_favorites_str));
		menu.add(Menu.NONE, 3, Menu.NONE, getResources().getString(R.string.add_chest_str));
	}

	
@Override
	public boolean onContextItemSelected(MenuItem item) {
  		AdapterContextMenuInfo acmi = (AdapterContextMenuInfo)item.getMenuInfo();
		switch (item.getItemId()) {
		case MENU_OPEN:
			openIthem(acmi.position);
			break;
		case MENU_ADD:
			acmi = (AdapterContextMenuInfo) item.getMenuInfo();
			addToFavorite((int)acmi.id);
			break;	
		case MENU_ADD_CH:
			acmi = (AdapterContextMenuInfo) item.getMenuInfo();
			addToChest((int)acmi.id);
			break;	
		default:
			break;
		}
		return super.onContextItemSelected(item);
	}

//service method start************************************


private void openIthem(int id)
{
	Log.d("panchenko", "selected record = " + id); 
	Preparat prep = allPreparat.get(id);
	DescriptionController descriptionController = new DescriptionController(getActivity());
	descriptionController.openIthem(prep);

}

private void addToFavorite(int position)
{
	Log.d("panchenko", "add input"); 
	Map hm = (Map) mainList.get(position);
	Preparat prep = allPreparat.get(position);
	if((Integer) hm.get(mainListController.IMG) == R.drawable.btn_star_big_off)
	{
		hm.put(MainListController.IMG, R.drawable.btn_star_big_on);
	mainList.set(position, hm);
	
	Log.d("panchenko", "id to favorite =  " + prep.getId()); 
		mainListController.insertIntoFavorite(prep.getId());
		prep.setFavorite(true);
		

	}else
	{
		hm.put(MainListController.IMG, R.drawable.btn_star_big_off);
		mainListController.deleteFromFavorite(prep.getId());
		prep.setFavorite(false);
	};
	mainAdapter.notifyDataSetChanged();
}

private void addToChest(int position)
{	
	Map hm = (Map) mainList.get(position);
	Preparat prep = allPreparat.get(position);
	myChestController = new MyChestController(getActivity());
	
	if((Integer) hm.get(MainListController.IMG_CHEST) == R.drawable.btn_star_big_off)
	{
		hm.put(MainListController.IMG_CHEST, R.drawable.btn_chest_star_on);
		mainList.set(position, hm);
	
		Log.d("panchenko", "id to favorite =  " + prep.getId()); 
		prep.setChest(true);
		
		myChestController.setPreparatToAddChest(prep);
		
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		int nextDay = day + 1;

		
		AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
		adb.setTitle(R.string.name_title_dialog_add_chest);
		View view = layoutInflater.inflate(R.layout.input_chest_layout, null);
		startDate = (DatePicker) view.findViewById(R.id.start_date);
		endDate = (DatePicker) view.findViewById(R.id.end_date);
		startDate.init(year, month, day,null);

		endDate.init(year, month, nextDay,null);
		
		adb.setPositiveButton(getActivity().getResources().getString(R.string.btn_ok), myDialogListener);
		adb.setNegativeButton(getActivity().getResources().getString(R.string.btn_cancel), myDialogListener);
		adb.setView(view);
		adb.show();

	}else
	{
		hm.put(MainListController.IMG_CHEST, R.drawable.btn_star_big_off);
		myChestController.deleteFromChest(prep.getId());
		prep.setChest(false);
	};
	mainAdapter.notifyDataSetChanged();

}

@Override
public void onItemClick(AdapterView<?> parent, View view,
        int position, long id) {
	openIthem(position);
}

//service method finish***********************************	

}
