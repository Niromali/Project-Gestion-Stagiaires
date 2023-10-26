package com.epg.java.modelsDAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import com.epg.java.models.*;

public class DomaineActiviteDAO {

    private Connection getConnection() throws SQLException {

    	String url = "jdbc:mysql://localhost:3306/stagiaires";
        String utilisateur = "root";
        String motDePasse = "";
        return DriverManager.getConnection(url, utilisateur, motDePasse);
    }

    public void ajouterDomaineActiviteEntreprise(DomaineActivite domaineActivite, Entreprise entreprise) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO domaine_activite (nom, entreprise_id) VALUES (?, ?)")) {
            preparedStatement.setString(1, domaineActivite.getNom());
            preparedStatement.setInt(2, entreprise.getIdentificateur());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void ajouterDomaineActivite(DomaineActivite domaineActivite) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO domaine_activite (nom) VALUES (?)", Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, domaineActivite.getNom());

            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int domaineId = generatedKeys.getInt(1);
            
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    

    public DomaineActivite trouverDomaineActiviteParNom(DomaineActivite domaine) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM domaine_activite WHERE nom = ?")) {
            preparedStatement.setString(1, domaine.getNom());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                DomaineActivite domaineActivite = new DomaineActivite();
                domaineActivite.setNom(resultSet.getString("nom"));
                return domaineActivite;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



    public List<DomaineActivite> getListeDomainesActivite() {
        List<DomaineActivite> domainesActivite = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM domaine_activite")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                DomaineActivite domaineActivite = new DomaineActivite();
                domaineActivite.setId(resultSet.getInt("id"));
                domaineActivite.setNom(resultSet.getString("nom"));
                domainesActivite.add(domaineActivite);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return domainesActivite;
    }


    public DomaineActivite trouverDomaineActiviteParId(int id) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM domaine_activite WHERE id = ?")) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                DomaineActivite domaineActivite = new DomaineActivite();
                domaineActivite.setNom(resultSet.getString("nom"));
                return domaineActivite;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void supprimerDomaineActiviteParId(int id) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "DELETE FROM domaine_activite WHERE id = ?")) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean domaineActiviteExiste(int id) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM domaine_activite WHERE id = ?")) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void mettreAJourDomaineActivite(DomaineActivite domaineActivite) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE domaine_activite SET nom = ? WHERE id = ?")) {
            preparedStatement.setString(1, domaineActivite.getNom());
            preparedStatement.setInt(2, domaineActivite.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }







}