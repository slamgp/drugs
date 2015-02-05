package com.example.drugs3.model.dao;

public class Favorite {
	private Preparat preparat;

	public Favorite(Preparat preparat)
	{
		this.preparat = preparat;
	}

	public Preparat getPreparat() {
		return preparat;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return preparat.getName();
	}
	
	
	
}
