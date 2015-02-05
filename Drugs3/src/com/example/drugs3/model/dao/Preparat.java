package com.example.drugs3.model.dao;

public class Preparat {

	private int id;
	private String name;
	private boolean isFavorite;
	private boolean isChest;
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return name;
	}
	
	public Preparat()
	{		
	}
	
	public Preparat(int id, String name, Boolean isFavorite, Boolean isChest) {
		this.id = id;
		this.name = name;
		this.isFavorite = isFavorite;
		this.isChest = isChest;
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

	public boolean isFavorite() {
		return isFavorite;
	}

	public void setFavorite(boolean isFavorite) {
		this.isFavorite = isFavorite;
	}
	
	public boolean isChest() {
		return isChest;
	}

	public void setChest(boolean isChest) {
		this.isChest = isChest;
	}	


}
