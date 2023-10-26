package com.epg.java.models;

public class DomaineActivite {
	
	private int id;
   private String nom;

public DomaineActivite() {
	super();
}

public DomaineActivite(int id, String nom) {
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
	return "DomaineActivite [id=" + id + ", nom=" + nom + "]";
}




}
