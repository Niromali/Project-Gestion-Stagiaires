package com.epg.java.models;

import java.util.*;

public class Stage {
	
	private int id;
    private Entreprise entreprise;
    private String theme;
    private List<Etudiant> etudiants;
    private Encadrant encadrantEntreprise;
    private Prof profEncadrant;
    private List<String> technologiesDemandees;

    public Stage() {
		super();
	}

	public Stage(int id, Entreprise entreprise, String theme, List<Etudiant> etudiants, Encadrant encadrantEntreprise,
			Prof profEncadrant, List<String> technologiesDemandees) {
		super();
		this.id = id;
		this.entreprise = entreprise;
		this.theme = theme;
		this.etudiants = etudiants;
		this.encadrantEntreprise = encadrantEntreprise;
		this.profEncadrant = profEncadrant;
		this.technologiesDemandees = technologiesDemandees;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Entreprise getEntreprise() {
		return entreprise;
	}

	public void setEntreprise(Entreprise entreprise) {
		this.entreprise = entreprise;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public List<Etudiant> getEtudiants() {
		return etudiants;
	}

	public void setEtudiants(List<Etudiant> etudiants) {
		this.etudiants = etudiants;
	}

	public Encadrant getEncadrantEntreprise() {
		return encadrantEntreprise;
	}

	public void setEncadrantEntreprise(Encadrant encadrantEntreprise) {
		this.encadrantEntreprise = encadrantEntreprise;
	}

	public Prof getProfEncadrant() {
		return profEncadrant;
	}

	public void setProfEncadrant(Prof profEncadrant) {
		this.profEncadrant = profEncadrant;
	}

	public List<String> getTechnologiesDemandees() {
		return technologiesDemandees;
	}

	public void setTechnologiesDemandees(List<String> technologiesDemandees) {
		this.technologiesDemandees = technologiesDemandees;
	}

	@Override
	public String toString() {
		return "Stage [id=" + id + ", entreprise=" + entreprise + ", theme=" + theme + ", etudiants=" + etudiants
				+ ", encadrantEntreprise=" + encadrantEntreprise + ", profEncadrant=" + profEncadrant
				+ ", technologiesDemandees=" + technologiesDemandees + "]";
	}
    

}
