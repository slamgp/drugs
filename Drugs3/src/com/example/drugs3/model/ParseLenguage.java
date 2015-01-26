package com.example.drugs3.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.example.drugs3.R;

import android.content.Context;
import android.util.Log;

public class ParseLenguage {
	
	private Context context;
	
	public ParseLenguage(Context context)
	{
		this.context = context;
	}
	
	public List<Lenguage> getLenguages()
	{
		ArrayList<Lenguage> lengs = new ArrayList<Lenguage>();
		XmlPullParser xmlP = context.getResources().getXml(R.xml.lenguages);
		Lenguage thisLenguage = null;
		
		try {
			String thisTeg = "";
			while (xmlP.getEventType() != XmlPullParser.END_DOCUMENT)
			{		
				switch(xmlP.getEventType()){
					case XmlPullParser.START_TAG:
						if(xmlP.getName().equals("lenguage"))
						{
							thisLenguage = new Lenguage();
						}
						thisTeg = xmlP.getName();
						break;
					case XmlPullParser.TEXT:
						if(thisTeg.equals("name")) thisLenguage.setName(xmlP.getText());
						if(thisTeg.equals("shrot-name")) thisLenguage.setShortName(xmlP.getText());
						break;
					case XmlPullParser.END_TAG:
						if(xmlP.getName().equals("lenguage"))
						{
							lengs.add(thisLenguage);
						}
						break;
				}
				xmlP.next();
			}
		} catch (XmlPullParserException e) {
			Log.d("panchenko", "xmlNullParserException");
			lengs = null;
		} catch (IOException e) {
			Log.d("panchenko", "IOException");
			lengs = null;
		}
		
		return lengs;	
	}
	

}
