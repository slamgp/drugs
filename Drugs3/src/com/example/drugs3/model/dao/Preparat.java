package com.example.drugs3.model.dao;

public class Preparat {

	private int id;
	private String name;
	private String info;
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return name;
	}
	
	public Preparat()
	{		
	}
	
	public Preparat(int id, String name, String info) {
		this.id = id;
		this.name = name;
		this.info = info;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	
	

}
