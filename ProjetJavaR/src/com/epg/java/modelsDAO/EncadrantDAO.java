package com.epg.java.modelsDAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.epg.java.models.Encadrant;

public class EncadrantDAO {

    private Connection getConnection() throws SQLException {
     
    	String url = "jdbc:mysql://localhost:3306/stagiaires";
        String utilisateur = "root";
        String motDePasse = "";
        return DriverManager.getConnection(url, utilisateur, motDePasse);
    }

    public void ajouterEncadrant(Encadrant encadrant) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO encadrant (id, nom, prenom, tel, email) " +
                             "VALUES (?, ?, ?, ?, ?)")) {
            preparedStatement.setInt(1, encadrant.getId());
            preparedStatement.setString(2, encadrant.getNom());
            preparedStatement.setString(3, encadrant.getPrenom());
            preparedStatement.setString(4, encadrant.getTel());
            preparedStatement.setString(5, encadrant.getEmail());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Encadrant trouverEncadrantParId(int id) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM encadrant WHERE id = ?")) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Encadrant encadrant = new Encadrant();
                encadrant.setId(resultSet.getInt("id"));
                encadrant.setNom(resultSet.getString("nom"));
                encadrant.setPrenom(resultSet.getString("prenom"));
                encadrant.setTel(resultSet.getString("tel"));
                encadrant.setEmail(resultSet.getString("email"));
                return encadrant;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void mettreAJourEncadrant(Encadrant encadrant) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE encadrant SET nom = ?, prenom = ?, tel = ?, email = ? " +
                             "WHERE id = ?")) {
            preparedStatement.setString(1, encadrant.getNom());
            preparedStatement.setString(2, encadrant.getPrenom());
            preparedStatement.setString(3, encadrant.getTel());
            preparedStatement.setString(4, encadrant.getEmail());
            preparedStatement.setInt(5, encadrant.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void supprimerEncadrant(int id) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "DELETE FROM encadrant WHERE id = ?")) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean encadrantExiste(int id) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT id FROM encadrant WHERE id = ?")) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Encadrant> getListeEncadrants() {
        List<Encadrant> encadrants = new ArrayList<>();
        
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM encadrant")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                Encadrant encadrant = new Encadrant();
                encadrant.setId(resultSet.getInt("id"));
                encadrant.setNom(resultSet.getString("nom"));
                encadrant.setPrenom(resultSet.getString("prenom"));
                encadrant.setTel(resultSet.getString("tel"));
                encadrant.setEmail(resultSet.getString("email"));
                encadrants.add(encadrant);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return encadrants;
    }
    
    
    
	  public void supprimerStageParIdEncadrant(int idEnc) {
	        String query = "DELETE FROM stage WHERE encadrant_entreprise_id = ?";
	        try (Connection connection = getConnection();
	             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
	            preparedStatement.setInt(1, idEnc);
	            preparedStatement.executeUpdate();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	  
	  public void supprimerStagesEtudiantParIdEncadrant(int idEnc) {
		    String query = "DELETE FROM stage_etudiant WHERE stage_id IN (SELECT id FROM stage WHERE encadrant_entreprise_id = ?)";
		    try (Connection connection = getConnection();
		    		PreparedStatement statement = connection.prepareStatement(query)) {
		        statement.setInt(1, idEnc);
		        statement.executeUpdate();
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		}


}
