package com.epg.java.test;

import com.epg.java.service.*;
import com.epg.java.modelsDAO.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.epg.java.models.DomaineActivite;
import com.epg.java.models.Encadrant;
import com.epg.java.models.Entreprise;
import com.epg.java.models.Etudiant;
import com.epg.java.models.Prof;
import com.epg.java.models.Stage;

public class EntrepriseServicesTest {
	private static EtudiantDAO etudiantDAO = new EtudiantDAO();

    public static void main(String[] args) {
    	

            EntrepriseService entrepriseService = new EntrepriseService();
            
            
            DomaineActivite activite1=new DomaineActivite(1,"Marketing1");
            DomaineActivite activite2=new DomaineActivite(2,"Marketing2");
            List<DomaineActivite> domaineActivites = new ArrayList<>();
            domaineActivites.add(activite1);
            domaineActivites.add(activite2);
            
            DomaineActivite activite3=new DomaineActivite(3,"Lavage1");
            DomaineActivite activite4=new DomaineActivite(4,"Lavage2");
            List<DomaineActivite> domaineActivites2 = new ArrayList<>();
            domaineActivites.add(activite3);
            domaineActivites.add(activite4);
            
            Entreprise entreprise113 = new Entreprise(113, "ConstructionEntre", "Fes", "MOntfleuri", "www.entreprise4.com", domaineActivites);
            //entrepriseService.ajouterEntreprise(entreprise113);
            
            Entreprise entreprise1 = new Entreprise(9, "Entreprise 1", "Ville 1", "Adresse 1", "www.entreprise1.com", domaineActivites);
            //entrepriseService.ajouterEntreprise(entreprise1);

            Entreprise entreprise2 = new Entreprise(10, "Entreprise 2", "Ville 2", "Adresse 2", "www.entreprise2.com", domaineActivites2);
            //entrepriseService.ajouterEntreprise(entreprise2);

            Entreprise entreprise3 = new Entreprise(8, "Entreprise 3", "Ville 3", "Adresse 3", "www.entreprise3.com", domaineActivites);
            //entrepriseService.ajouterEntreprise(entreprise3);
            
            
            Etudiant etudiant=new Etudiant(122,"abdennour","boumaden",new Date());
            Etudiant etudiant1=new Etudiant(123,"reda","tali",new Date());
            //entrepriseService.ajouterEtudiant(etudiant);
            
            Encadrant encadrant=new Encadrant(3,"alae","lazrak","0697277104","alae@adada.com");
            //entrepriseService.ajouterEncadrant(encadrant);
            
            Prof prof=new Prof(3,"alae","lazrak","0697277104","alae@adada.com");
            //entrepriseService.ajouterProf(prof);
            
            List<Etudiant> etudiants = new ArrayList<>();
            etudiants.add(etudiant);
            
            List<String> technologiesDemandees = new ArrayList<>();
            technologiesDemandees.add("Java");
            technologiesDemandees.add("Php");
            
            
            Stage stage = new Stage();
            stage.setTheme("Développement app");
            stage.setEntreprise(entreprise113);
            stage.setEncadrantEntreprise(encadrant);
            stage.setProfEncadrant(prof);
            stage.setEtudiants(etudiants);
            stage.setTechnologiesDemandees(technologiesDemandees);
           // entrepriseService.ajouterStage(stage);
            
            // Ajoutez un domaine d'activité à l'entreprise
            DomaineActivite domaineActivite1 = new DomaineActivite(5,"Développement applicatio Spring");
          //entrepriseService.ajouterDomaineActivite(domaineActivite1);
            
            DomaineActivite domaineActivite = new DomaineActivite(6,"Construction");
            //entrepriseService.ajouterDomaineActivite(domaineActivite);
            
            //entrepriseService.ajouterDomaineActiviteEntreprise(entreprise13, domaineActivite);
            
            
            
            // Affichez les domaines d'activité de l'entreprise pour vérification
            System.out.println("Domaines d'activité de l'entreprise : " + entreprise113.getDomainesActivite());
            
       
  /*00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000*/
            
         // Recherchez un étudiant par son CNE
    	    Etudiant etudiant6 = etudiantDAO.trouverEtudiantParCne(159);
    	    
    	    if (etudiant6 != null) {
    	        List<Stage> stages = entrepriseService.trouverStagesParCneEtudiant(etudiant6.getCne());
    	        
    	        if (!stages.isEmpty()) {
    	            System.out.println("Stages de l'étudiant avec le CNE " + etudiant6.getCne() + ":");
    	            for (Stage stage6 : stages) {
    	                System.out.println("Theme: " + stage6.getTheme());
    	                System.out.println("Entreprise: " + stage6.getEntreprise().getRaisonSociale());
    	                System.out.println("Encadrant à l'entreprise: " + stage6.getEncadrantEntreprise().getNom());
    	                System.out.println("Prof Encadrant: " + stage6.getProfEncadrant().getNom());
    	                System.out.println("Technologies demandées: " + stage6.getTechnologiesDemandees());
    	                System.out.println("----------------------------------");
    	            }
    	        } else {
    	            System.out.println("Aucun stage trouvé pour l'étudiant avec le CNE " + etudiant6.getCne());
    	        }
    	    } else {
    	        System.out.println("Etudiant non trouvé.");
    	    }
    	
    	    System.out.println("/*/0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000/*/         \r\n"
    	    		+ ".");
           
    	    // Recherchez les stages bases sur une technologie donnee
    	    String technologie = "Java"; 
    	    List<Stage> stages = entrepriseService.trouverStagesParTechnologie(technologie);
    	    
    	    if (!stages.isEmpty()) {
    	        System.out.println("Stages basés sur la technologie " + technologie + ":");
    	        for (Stage stage6 : stages) {
    	            System.out.println("Theme: " + stage6.getTheme());
    	            System.out.println("Entreprise: " + stage6.getEntreprise().getRaisonSociale());
    	            System.out.println("Encadrant à l'entreprise: " + stage6.getEncadrantEntreprise().getNom());
    	            System.out.println("Prof Encadrant: " + stage6.getProfEncadrant().getNom());
    	            System.out.println("Technologies demandées: " + stage6.getTechnologiesDemandees());
    	            System.out.println("----------------------------------");
    	        }
    	    } else {
    	        System.out.println("Aucun stage trouvé pour la technologie " + technologie);
    	    }
    	
    	    System.out.println("/*/0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000/*/         \r\n"
    	    		+ ".");
    	    
    	    
    	    
    	    // Testez la liste des stagiaires dans une entreprise donnée
    	    String nomEntreprise = "epg";
    	    List<Etudiant> stagiaires = entrepriseService.listeStagiairesDansEntreprise(nomEntreprise);
    	    
    	    if (!stagiaires.isEmpty()) {
    	        System.out.println("Stagiaires dans l'entreprise " + nomEntreprise + ":");
    	        for (Etudiant etudiant7 : stagiaires) {
    	            System.out.println("CNE: " + etudiant7.getCne());
    	            System.out.println("Nom: " + etudiant7.getNom());
    	            System.out.println("Prénom: " + etudiant7.getPrenom());
    	            System.out.println("DateNaissance: " + etudiant7.getDateNaissance());
    	            System.out.println("----------------------------------");
    	        }
    	    } else {
    	        System.out.println("Aucun stagiaire trouvé dans l'entreprise " + nomEntreprise);
    	    }

    
}
  
}
