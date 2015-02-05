package com.example.drugs3.model.dao;

import java.sql.Date;



public class Chest {
	private Preparat preparat;
	private Date startData;
	private Date endData;

	public Chest(Preparat preparat,Date startData, Date endData)
	{
		this.preparat = preparat;
		this.startData = startData;
		this.endData = endData;
	}

	public Preparat getPreparat() {
		return preparat;
	}

	public Date getStartData() {
		return startData;
	}

	public void setStartData(Date startData) {
		this.startData = startData;
	}

	public Date getEndData() {
		return endData;
	}

	public void setEndData(Date endData) {
		this.endData = endData;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return preparat.getName();
	}
	
	
	
}
