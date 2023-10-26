package com.epg.java.modelsDAO;

import java.sql.*;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.epg.java.models.Etudiant;

public class EtudiantDAO {
	  private Connection getConnection() throws SQLException {
	    
		  String url = "jdbc:mysql://localhost:3306/stagiaires";
	        String utilisateur = "root";
	        String motDePasse = "";
	        return DriverManager.getConnection(url, utilisateur, motDePasse);
	    }

	    public void ajouterEtudiant(Etudiant etudiant) {
	        try (Connection connection = getConnection();
	             PreparedStatement preparedStatement = connection.prepareStatement(
	                     "INSERT INTO etudiant (cne, nom, prenom, dateNaissance) " +
	                             "VALUES (?, ?, ?, ?)")) {
	            preparedStatement.setInt(1, etudiant.getCne());
	            preparedStatement.setString(2, etudiant.getNom());
	            preparedStatement.setString(3, etudiant.getPrenom());
	            preparedStatement.setDate(4, new java.sql.Date(etudiant.getDateNaissance().getTime()));
	            preparedStatement.executeUpdate();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    public Etudiant trouverEtudiantParCne(int code) {
	        try (Connection connection = getConnection();
	             PreparedStatement preparedStatement = connection.prepareStatement(
	                     "SELECT * FROM etudiant WHERE cne = ?")) {
	            preparedStatement.setInt(1, code);
	            ResultSet resultSet = preparedStatement.executeQuery();
	            if (resultSet.next()) {
	                Etudiant etudiant = new Etudiant();
	                etudiant.setCne(resultSet.getInt("cne"));
	                etudiant.setNom(resultSet.getString("nom"));
	                etudiant.setPrenom(resultSet.getString("prenom"));
	                etudiant.setDateNaissance(resultSet.getDate("dateNaissance"));
	                return etudiant;
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return null;
	    }
	    
	    
	    public void mettreAJourEtudiant(Etudiant etudiant) {
	        try (Connection connection = getConnection();
	             PreparedStatement preparedStatement = connection.prepareStatement(
	                     "UPDATE etudiant SET nom = ?, prenom = ?, dateNaissance = ? " +
	                             "WHERE cne = ?")) {
	            preparedStatement.setString(1, etudiant.getNom());
	            preparedStatement.setString(2, etudiant.getPrenom());
	            preparedStatement.setDate(3, new java.sql.Date(etudiant.getDateNaissance().getTime()));
	            preparedStatement.setInt(4, etudiant.getCne());
	            preparedStatement.executeUpdate();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    public void supprimerEtudiant(int cne) {
	        try (Connection connection = getConnection();
	             PreparedStatement preparedStatement = connection.prepareStatement(
	                     "DELETE FROM etudiant WHERE cne = ?")) {
	            preparedStatement.setInt(1, cne);
	            preparedStatement.executeUpdate();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	
	    
	    public List<Etudiant> listeStagiairesDansEntreprise(String nomEntreprise) {
	        List<Etudiant> stagiaires = new ArrayList<>();
	        
	        try (Connection connection = getConnection();
	             PreparedStatement preparedStatement = connection.prepareStatement(
	                     "SELECT e.* FROM etudiant e " +
	                     "INNER JOIN stage_etudiant se ON e.cne = se.etudiant_id " +
	                     "INNER JOIN stage s ON se.stage_id = s.id " +
	                     "INNER JOIN entreprise en ON s.entreprise_id = en.identificateur " +
	                     "WHERE en.raisonSociale = ?")) {
	            preparedStatement.setString(1, nomEntreprise);
	            ResultSet resultSet = preparedStatement.executeQuery();
	            
	            while (resultSet.next()) {
	                Etudiant etudiant = new Etudiant();
	                etudiant.setCne(resultSet.getInt("cne"));
	                etudiant.setNom(resultSet.getString("nom"));
	                etudiant.setPrenom(resultSet.getString("prenom"));
	                etudiant.setDateNaissance(resultSet.getDate("dateNaissance"));
	                stagiaires.add(etudiant);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        
	        return stagiaires;
	    }

	    public List<Etudiant> getListeEtudiants() {
	        List<Etudiant> etudiants = new ArrayList<>();

	        try (Connection connection = getConnection();
	             PreparedStatement preparedStatement = connection.prepareStatement(
	                     "SELECT * FROM etudiant")) {
	            ResultSet resultSet = preparedStatement.executeQuery();

	            while (resultSet.next()) {
	                int cne = resultSet.getInt("cne");
	                String nom = resultSet.getString("nom");
	                String prenom = resultSet.getString("prenom");
	                Date dateNaissance = resultSet.getDate("dateNaissance");
	                
	                Etudiant etudiant = new Etudiant(cne, nom, prenom, dateNaissance);
	                etudiants.add(etudiant);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        return etudiants;
	    }

	    public boolean etudiantExiste(int cne) {
	        try (Connection connection = getConnection();
	             PreparedStatement preparedStatement = connection.prepareStatement(
	                     "SELECT * FROM etudiant WHERE cne = ?")) {
	            preparedStatement.setInt(1, cne);
	            ResultSet resultSet = preparedStatement.executeQuery();
	            return resultSet.next();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return false;
	    }



}
