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
		tv.setText(Html.fromHtml("<!DOCTYPE HTML PUBLIC &quot;-//W3C//DTD HTML 4.01 Transitional//EN&quot;> <HTML> <HEAD> <TITLE>П.01.03/05719 - ТЕРЖИНАН</TITLE> <META HTTP-EQUIV=&quot;Content-Type&quot; CONTENT=&quot;text/html; charset=WINDOWS-1251&quot;> </HEAD> <BODY> <p><span class=&quot;tit&quot;>ТЕРЖИНАН</span></p> </p> <p><b>Виробник, країна:</b> Лабораторії БУШАРА-РЕКОРДАТІ, Франція</p> <p><b>Форма випуску:</b> Таблетки вагінальні № 6, № 10</p> <p><b>Діючі речовини:</b> 1 таблетка містить: тернідазолу - 200.0 мг, неоміцину сульфату - 100.0 мг, ністатину - 100000 ОД, преднізолону - 3.0 мг</p> <b>Спосіб застосування та дози</b>.<br> Вводять глибоко у піхву по 1 таблетці на добу. Середня тривалість курсу лікування – 10 діб, у випадках підтвердженого мікозу – 20 діб. Лікування не припиняють під час менструації. Перед  введенням  у  піхву таблетку слід змочити водою протягом 20 - 30 с. Потрібно полежати 10 - 15 хв після введення препарату. <br> <br> </BODY> </HTML>"));
		
		return v;
		
	}

}
