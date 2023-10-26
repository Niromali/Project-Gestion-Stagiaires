package com.epg.java.models;


import java.util.Date;
import java.util.Objects;

public class Etudiant {
    private int cne;
    private String nom;
    private String prenom;
    private Date dateNaissance;
    
    
	public Etudiant() {
		super();
	}

	


	public Etudiant(int cne, String nom, String prenom, Date dateNaissance) {
		super();
		this.cne = cne;
		this.nom = nom;
		this.prenom = prenom;
		this.dateNaissance = dateNaissance;
	}




	public int getCne() {
		return cne;
	}




	public void setCne(int cne) {
		this.cne = cne;
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




	public Date getDateNaissance() {
		return dateNaissance;
	}




	public void setDateNaissance(Date dateNaissance) {
		this.dateNaissance = dateNaissance;
	}




	 
	    public String toString() {
	        return cne+" ,"+prenom + " ," + nom +","+dateNaissance+"";
	    }
	
}