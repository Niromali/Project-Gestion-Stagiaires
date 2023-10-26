package com.epg.java.models;

public class Technologie {
	
	private int id;
   private String nom;

public Technologie() {
	super();
}

public Technologie(int id, String nom) {
	super();
	this.id = id;
	this.nom = nom;
}

public int getId() {
	return id;
}

public void setId(int id) {
	this.id = id;
}

public String getNom() {
	return nom;
}

public void setNom(String nom) {
	this.nom = nom;
}

@Override
public String toString() {
	return nom+" ";
}




}
