package com.epg.java.service;



import java.util.*;
import com.epg.java.models.*;
import com.epg.java.modelsDAO.*;


public class EntrepriseService {
    private EntrepriseDAO entrepriseDAO = new EntrepriseDAO();
    private EtudiantDAO etudiantDAO = new EtudiantDAO();
    private EncadrantDAO encadrantDAO = new EncadrantDAO();
    private ProfDAO profDAO = new ProfDAO();
    private StageDAO stageDAO = new StageDAO();
    private DomaineActiviteDAO domaineActiviteDAO =new DomaineActiviteDAO();
    

    public int ajouterEntreprise(Entreprise entreprise) {
        return entrepriseDAO.ajouterEntreprise(entreprise);
    }
    

    public void ajouterEtudiant(Etudiant etudiant) {
        etudiantDAO.ajouterEtudiant(etudiant);
    }
    

    public void ajouterProf(Prof prof) {
        profDAO.ajouterProf(prof);
    }
    

    public void ajouterEncadrant(Encadrant encadrant) {
        encadrantDAO.ajouterEncadrant(encadrant);
    }
    
    


    public void ajouterStage(Stage stage) {

        stageDAO.ajouterStage(stage);
    }

    
    public void ajouterDomaineActiviteEntreprise(Entreprise entreprise, DomaineActivite domaineActivite) {
        domaineActiviteDAO.ajouterDomaineActiviteEntreprise(domaineActivite,entreprise);

        entreprise.getDomainesActivite().add(domaineActivite);
        entrepriseDAO.mettreAJourEntreprise(entreprise);
    }
    
    public void ajouterDomaineActivite(DomaineActivite domaineActivite) {
    	domaineActiviteDAO.ajouterDomaineActivite(domaineActivite);
    	
    }
    
	public List<Stage> trouverStagesParCneEtudiant(int cne) {
		
		return stageDAO.trouverStagesParCneEtudiant(cne);
	}

    
    public List<Stage> trouverStagesParTechnologie(String technologie) {
        return stageDAO.trouverStagesParTechnologie(technologie);
    }


	public List<Etudiant> listeStagiairesDansEntreprise(String nomEntreprise) {
		return etudiantDAO.listeStagiairesDansEntreprise(nomEntreprise);
		
	}




}

