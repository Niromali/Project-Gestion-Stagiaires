package com.epg.java.modelsDAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.epg.java.models.Technologie;

public class TechnologieDAO {

   
    private Connection getConnection() throws SQLException {

    	String url = "jdbc:mysql://localhost:3306/stagiaires";
        String utilisateur = "root";
        String motDePasse = "";
        return DriverManager.getConnection(url, utilisateur, motDePasse);
    }

    
    public void ajouterTechnologie(Technologie technologie) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO technologie (nom) VALUES (?)")) {
            preparedStatement.setString(1, technologie.getNom());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Technologie> getListeTechnologies() {
        List<Technologie> technologies = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT id, nom FROM technologie")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nom = resultSet.getString("nom");
                technologies.add(new Technologie(id, nom));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return technologies;
    }

    public Technologie trouverTechnologieParId(int id) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT id, nom FROM technologie WHERE id = ?")) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String nom = resultSet.getString("nom");
                return new Technologie(id, nom);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void mettreAJourTechnologie(Technologie technologie) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE technologie SET nom = ? WHERE id = ?")) {
            preparedStatement.setString(1, technologie.getNom());
            preparedStatement.setInt(2, technologie.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public boolean technologieExisteDeja(String nom) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT COUNT(*) FROM technologie WHERE nom = ?")) {
            preparedStatement.setString(1, nom);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public void supprimerTechnologie(int id) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "DELETE FROM technologie WHERE id = ?")) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public boolean isTechnologieUsedInStages(int technologieId) {
        boolean isUsed = false;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT COUNT(*) FROM stage_technologie WHERE technologie_id = ?")) {
            preparedStatement.setInt(1, technologieId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    isUsed = count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isUsed;
    }

    public void supprimerStagesAvecTechnologie(int technologieId) {
        try (Connection connection = getConnection();
             PreparedStatement supprimerStageTechnologieStatement = connection.prepareStatement(
                     "DELETE FROM stage_technologie WHERE technologie_id = ?");
             PreparedStatement supprimerStageEtudiantStatement = connection.prepareStatement(
                     "DELETE FROM stage_etudiant WHERE stage_id IN (SELECT stage_id FROM stage_technologie WHERE technologie_id = ?)");
             PreparedStatement supprimerStageStatement = connection.prepareStatement(
                     "DELETE FROM stage WHERE id IN (SELECT stage_id FROM stage_technologie WHERE technologie_id = ?)")) {

            supprimerStageTechnologieStatement.setInt(1, technologieId);
            supprimerStageTechnologieStatement.executeUpdate();

            supprimerStageEtudiantStatement.setInt(1, technologieId);
            supprimerStageEtudiantStatement.executeUpdate();

            supprimerStageStatement.setInt(1, technologieId);
            supprimerStageStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }





}
