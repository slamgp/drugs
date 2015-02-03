package com.example.drugs3.view;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.example.drugs3.R;
import com.example.drugs3.model.Lenguage;
import com.example.drugs3.model.ParseLenguage;
import com.example.drugs3.model.dao.DBHelper;
import com.example.drugs3.model.dao.Preparat;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.text.AlteredCharSequence;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

public class MainActivity extends Activity implements android.view.View.OnClickListener{
	
	
	private DBHelper myDB;
	private List<Preparat> allPreparat;
	
	final private  String currentLenguageKey = "currentLenguage";
	private String currentLenguage = null;
	private String newInterfLenguage = null;
	private Locale locale;
	
	private List<Lenguage> listLenguages;
	private List<String> listLenguagesName ;
	
	private String discription;

	private FragmentTransaction fTrans;
	private Fragment currentFragment = null;
	private Fragment mainFragment;
	private Fragment myChestFragment;
	private Fragment favoriteFragment;
	private Fragment descriptionFragment;
	private List<Fragment> listFragment;
	
	private float startPosition = 0;
	private float endPosition = 0;
	
	private UiLifecycleHelper uiHelper; 
	private Session.StatusCallback callback = new Session.StatusCallback() {
		
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			OnSessionStateChenges(session, state, exception);
			
		}
	};
	Session currentSession;
	
	public final String[] permissions = {"publish_stream"};

	
	private Button btnMain;
	private Button btnChest;
	private Button btnFavorite;
	private Button btnSearch;
	private Button btnBarCode;
	private Button btnThanks;
	private Button btnFeedBack;
	private LoginButton fbLofinButton;
	
	private EditText etFind;
	
	private GraphUser user;
	private HashMap<Fragment,View> mappingFragmentToButton;
	private Menu mainMenu;
	//Overriid method start***************************************************
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.activity_main);
		
		btnMain = (Button) findViewById(R.id.btn_main_list);
		btnChest = (Button) findViewById(R.id.btn_chest);
		btnFavorite = (Button) findViewById(R.id.btn_favorite);
		btnSearch = (Button) findViewById(R.id.btn_search);
		btnBarCode = (Button) findViewById(R.id.btn_bar_code);
		btnThanks = (Button) findViewById(R.id.btn_thanks);
		btnFeedBack = (Button) findViewById(R.id.btn_feedback);
		fbLofinButton = (LoginButton) findViewById(R.id.LoginButton);
		
		etFind = (EditText) findViewById(R.id.etFind);
	
		myChestFragment = new MyChestFragment();
		favoriteFragment = new FavoriteFragment();
		mainFragment = new MainListFragment();
		descriptionFragment = new DescriptionFragment();
		
		listFragment = new ArrayList<Fragment>();
		listFragment.add(mainFragment);
		listFragment.add(myChestFragment);
		listFragment.add(favoriteFragment);
	
		mappingFragmentToButton = new HashMap<Fragment, View>();
		
		mappingFragmentToButton.put(myChestFragment, btnChest);
		mappingFragmentToButton.put(favoriteFragment, btnFavorite);
		mappingFragmentToButton.put(mainFragment, btnMain);
	
		btnMain.setOnClickListener(this);	
		btnChest.setOnClickListener(this);
		btnFavorite.setOnClickListener(this);
		btnSearch.setOnClickListener(this);
		btnBarCode.setOnClickListener(this);
		btnThanks.setOnClickListener(this);
		btnFeedBack.setOnClickListener(this);
		fbLofinButton.setOnClickListener(this);
	
		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);
		
		fbLofinButton.setReadPermissions(Arrays.asList("public_profile","email"));

		
		listLenguages = new ParseLenguage(this).getLenguages();
		listLenguagesName = new ArrayList<String>();
		
		for(Lenguage leng:listLenguages)
		{
			listLenguagesName.add(leng.getName());
		}
		
		readPreferences();
		
		if(currentLenguage == null)
		{
			if ((listLenguages != null))
			{
				showDialog(1);
			}
		}else 
		{	
			updateLocale();	
		}

		    
			myDB = new DBHelper(this);
			try {
				myDB.createDataBase();
				selectMainListPreparation();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
	//	changeFragment(mainFragment);
	//	paintButton(btnMain);
	}	
	
	@Override
	protected Dialog onCreateDialog(int id) {
		AlertDialog.Builder adb = new AlertDialog.Builder(this);
		adb.setTitle(R.string.name_title_dialog);
		ArrayAdapter<String> adp = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item,listLenguagesName);
		adb.setAdapter(adp, myClickListener);
		return adb.create();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		mainMenu = menu;
		return true;
	}
	
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId())
		{
			case R.id.action_settings:
				openLenguageDialog();
			break;
		}
		return super.onOptionsItemSelected(item);
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Session session = Session.getActiveSession();
		if(session != null && (session.isClosed() || session.isOpened()))
		{
			OnSessionStateChenges(session, session.getState(), null);
		}
		uiHelper.onResume();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	public void onClick(View v) {
		fTrans = getFragmentManager().beginTransaction();
		switch(v.getId())
		{
		case R.id.btn_main_list: 
			changeFragment(mainFragment);
		break;
		case R.id.btn_chest: 		
			changeFragment(myChestFragment);
		break;
		case R.id.btn_favorite: 
			changeFragment(favoriteFragment);
		break;
		case R.id.btn_search: 
		break;
		default:
			paintButton(v);
			break;
		};		
		paintButton(v);
	}
		
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {	
		
		switch(event.getAction())
		{
			case MotionEvent.ACTION_DOWN: startPosition = event.getX();
			break;
			case MotionEvent.ACTION_UP: endPosition = event.getX();
			slaidingFragments();
			break;
			default: break;
		}
		return true;
	}
	
	
	public List<Preparat> getAllPreparat() {
		return allPreparat;
	}
	
	public String getDiscription() {
		return discription;
	}
	
	public void selectMainListPreparation()
	{
		allPreparat = myDB.selectAllDrugs();
	}	
	//Overriid method end***************************************************

	//My servise method start*****************************	
	public void showDescription(String discription)
	{
		this.discription = discription;
		changeFragment(descriptionFragment);		
	}
	
	private void openLenguageDialog() {
		
		AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
		myDialog.setTitle(getResources().getString(R.string.action_settings));
		View layout = getLayoutInflater().inflate(R.layout.settings_layout, null);
		
		Spinner spinnerLenguage = (Spinner) layout.findViewById(R.id.spinner_lenguage);
		
		int idCurrent = -1;
		
		
		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item,listLenguagesName);
		spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerLenguage.setAdapter(spinnerAdapter);
		if(currentLenguage != null)
		{
			for(int i = 0; i < listLenguages.size(); i++)
			{
				if(listLenguages.get(i).getShortName().equals(currentLenguage))
				{
					idCurrent = i;
					break;
				}
			}
			//Toast.makeText(MainActivity.this, "  " + idCurrent, Toast.LENGTH_SHORT).show();
			if(idCurrent != -1)
			{
				spinnerLenguage.setSelection(idCurrent);
			}
		};
		
		spinnerLenguage.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				newInterfLenguage = listLenguages.get(position).getShortName();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				newInterfLenguage = null;
				
			}
		});
		
		myDialog.setView(layout);
		myDialog.setPositiveButton("ОК", new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				 savedPreferences();
				 if(newInterfLenguage != null)
				 {
					 currentLenguage = newInterfLenguage;
					 newInterfLenguage = null;
					 savedPreferences();	
					 updateLocale(); 
				 }
			}
			
		});
		myDialog.create();
		myDialog.show();
	}
	
	private void paintButton(View v)
	{
		
		btnMain.setBackgroundColor(getResources().getColor(R.color.color_not_put_button)); 
		btnChest.setBackgroundColor(getResources().getColor(R.color.color_not_put_button)); 
		btnFavorite.setBackgroundColor(getResources().getColor(R.color.color_not_put_button)); 
		if(v == btnMain)
		{
			btnMain.setBackgroundColor(getResources().getColor(R.color.color_put_button)); 
		}
		if(v == btnChest)
		{
			btnChest.setBackgroundColor(getResources().getColor(R.color.color_put_button)); 
		}
		if(v == btnFavorite)
		{
			btnFavorite.setBackgroundColor(getResources().getColor(R.color.color_put_button)); 
		}
	}
	
	public EditText getEtFind()	
	{
		return etFind;
	}
	
	public void setStratPosition(float startPosition)
	{
		this.startPosition = startPosition;
	}
	public void setEndPosition(float endPosition)
	{
		this.endPosition = endPosition;
	}
	public void slaidingFragments()
	{
		if(Math.abs(endPosition - startPosition)<30)
		{
			return;
		}
		int indexCurrentFragment = listFragment.indexOf(currentFragment);
		int indexNewFragment = 0;
		//Toast.makeText(this, "stert= " + startPosition + "   end= " + endPosition, Toast.LENGTH_LONG).show();
		if(startPosition > endPosition)
		{
			if (indexCurrentFragment <= 0) 
			{
				indexNewFragment = (listFragment.size() - 1);
			}else indexNewFragment  = indexCurrentFragment - 1;
		}
		if(startPosition < endPosition)
		{
			if (indexCurrentFragment >= (listFragment.size() - 1)) 
			{
				indexNewFragment = 0;
			}else indexNewFragment  = indexCurrentFragment + 1;		
		}
		//Toast.makeText(this, "index fragment   " + indexCurrentFragment + "       " + indexNewFragment , Toast.LENGTH_LONG).show();
		if(indexNewFragment >= 0 && indexNewFragment < listFragment.size())
		{
			Fragment thisFrag = listFragment.get(indexNewFragment);
			//Toast.makeText(this, "-" + thisFrag.toString() , Toast.LENGTH_LONG).show();
			if(thisFrag != null)
			{
				changeFragment(thisFrag);
			}
		}
	
	}
	
	private void changeFragment(Fragment frag)
	{
		fTrans = getFragmentManager().beginTransaction();
		if(currentFragment == null)
		{
			fTrans.add(R.id.frgmCont, frag);
		}else{
			fTrans.replace(R.id.frgmCont, frag);				
		}
		currentFragment = frag;
		fTrans.commit();	
		View v = mappingFragmentToButton.get(currentFragment);
	//	Toast.makeText(this, v.toString(), Toast.LENGTH_SHORT).show();
		paintButton(v);
	}
	
	private void readPreferences()
	{
		 SharedPreferences sPref = getPreferences(MODE_PRIVATE);
		 currentLenguage = sPref.getString(currentLenguageKey, null);
	}
	
	private void savedPreferences()
	{
		 SharedPreferences sPref = getPreferences(MODE_PRIVATE);
		 Editor ed = sPref.edit();
		 ed.putString(currentLenguageKey, currentLenguage);
		 ed.commit();
	}
	

	
	private void updateLocale()
	{
		locale = new Locale(currentLenguage);
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		getBaseContext().getResources().updateConfiguration(config, null);
		updateView();
	}
	
	private void updateView()
	{
		Resources res = this.getResources();
		String title = "";
		if(user != null)
		{
			title = res.getString(R.string.app_name) + " (" + user.getName() + ")";
		}else
		{
			title = res.getString(R.string.app_name);
		}
		setTitle(title);
		btnMain.setText(res.getString(R.string.btn_main_list));
		btnChest.setText(res.getString(R.string.btn_chest));
		btnFavorite.setText(res.getString(R.string.btn_favorite));
		btnSearch.setText(res.getString(R.string.btn_search));
		btnBarCode.setText(res.getString(R.string.btn_bar_code));
		btnThanks.setText(res.getString(R.string.btn_thanks));
		btnFeedBack.setText(res.getString(R.string.btn_feedback));
		if(mainMenu != null)
		{
			mainMenu.clear();
			getMenuInflater().inflate(R.menu.main, mainMenu);
		}
	}

	  
	OnClickListener myClickListener = new OnClickListener() {
			@Override
		    public void onClick(DialogInterface dialog, int which) {
				currentLenguage = listLenguages.get(which).getShortName();
				savedPreferences();		
				updateLocale();
		    }

	};
	
	
	public DBHelper getMyDB() {
		return myDB;
	}

	
	//My servise method end*****************************

	//FaceBook Method start*******************************
	public void OnSessionStateChenges(Session session, SessionState state, Exception exception){
		if(session != null && session.isOpened())
		{
			//Toast.makeText(MainActivity.this, "session opened", Toast.LENGTH_LONG).show();
			Request.newMeRequest(session, new Request.GraphUserCallback() {
				
				@Override
				public void onCompleted(GraphUser user, Response response) {
					if(user != null)
					{
						MainActivity.this.user = user;
					//	Toast.makeText(MainActivity.this, user.getName(), Toast.LENGTH_LONG).show();
						//MainActivity.this.setTitle( user.getName());
						updateView();
					}
					
				}
			}).executeAsync();
		}else{
			//Toast.makeText(MainActivity.this, "session no opened", Toast.LENGTH_LONG).show();
		}
	}
	//end FaceBook Method*********************************
}
