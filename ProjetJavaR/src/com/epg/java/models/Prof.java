package com.epg.java.models;

import java.util.Objects;

public class Prof {
    private int id;
    private String nom;
    private String prenom;
    private String tel;
    private String email;
    
    
	public Prof() {
		super();
	}


	public Prof(int id, String nom, String prenom, String tel, String email) {
		super();
		this.id = id;
		this.nom = nom;
		this.prenom = prenom;
		this.tel = tel;
		this.email = email;
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


	public String getPrenom() {
		return prenom;
	}


	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}


	public String getTel() {
		return tel;
	}


	public void setTel(String tel) {
		this.tel = tel;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}

	
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Prof other = (Prof) obj;
		return id == other.id;
	}
    
    

	public String toString() {
		return id+" ,"+nom+" ,"+prenom+" ,"+email+"";
	}





}
