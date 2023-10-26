package com.epg.java.models;

import java.util.*;

public class Entreprise {

	    private int identificateur;
	    private String raisonSociale;
	    private String ville;
		private String adresse;
	    private String siteWeb;
	    private List<DomaineActivite> domainesActivite;
	    
	    public Entreprise() {
			super();
			domainesActivite = new ArrayList<>();
		}



		public Entreprise(int identificateur, String raisonSociale, String ville, String adresse, String siteWeb,
				List<DomaineActivite> domainesActivite) {
			super();
			this.identificateur = identificateur;
			this.raisonSociale = raisonSociale;
			this.ville = ville;
			this.adresse = adresse;
			this.siteWeb = siteWeb;
			this.domainesActivite = domainesActivite;
		}



		public int getIdentificateur() {
			return identificateur;
		}

		public void setIdentificateur(int identificateur) {
			this.identificateur = identificateur;
		}

		public String getRaisonSociale() {
			return raisonSociale;
		}

		public void setRaisonSociale(String raisonSociale) {
			this.raisonSociale = raisonSociale;
		}

		public String getVille() {
			return ville;
		}

		public void setVille(String ville) {
			this.ville = ville;
		}

		public String getAdresse() {
			return adresse;
		}

		public void setAdresse(String adresse) {
			this.adresse = adresse;
		}

		public String getSiteWeb() {
			return siteWeb;
		}

		public void setSiteWeb(String siteWeb) {
			this.siteWeb = siteWeb;
		}
		
	    public List<DomaineActivite> getDomainesActivite() {
	        return domainesActivite;
	    }

	    public void setDomainesActivite(List<DomaineActivite> domainesActivite) {
	        this.domainesActivite = domainesActivite;
	    }



		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Entreprise other = (Entreprise) obj;
			return identificateur == other.identificateur;
		}

		@Override
		public String toString() {
			return identificateur+" ,"+raisonSociale+" ,"+ville+" ,"+adresse+" ,"+siteWeb+"";
		}

	

}
