package com.epg.java.modelsDAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.epg.java.models.*;
import com.epg.java.modelsDAO.*;

public class EntrepriseDAO {
	private DomaineActiviteDAO domaineActiviteDAO=new DomaineActiviteDAO();

    private Connection getConnection() throws SQLException {
        
    	String url = "jdbc:mysql://localhost:3306/stagiaires";
        String utilisateur = "root";
        String motDePasse = "";
        return DriverManager.getConnection(url, utilisateur, motDePasse);
    }



    public int ajouterEntreprise(Entreprise entreprise) {
    	int entrepriseId = -1;
        try (Connection connection = getConnection()) {
        	try (PreparedStatement preparedStatement = connection.prepareStatement(
        	        "INSERT INTO entreprise (raisonSociale, ville, adresse, siteWeb, domaineActivite) " +
        	        "VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
        	    preparedStatement.setString(1, entreprise.getRaisonSociale());
        	    preparedStatement.setString(2, entreprise.getVille());
        	    preparedStatement.setString(3, entreprise.getAdresse());
        	    preparedStatement.setString(4, entreprise.getSiteWeb());
                
        	    try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
        	        if (generatedKeys.next()) {
        	            entrepriseId = generatedKeys.getInt(1);
        	        }
        	    }

                List<String> domainesActiviteNames = new ArrayList<>();
                for (DomaineActivite domaineActivite : entreprise.getDomainesActivite()) {
                    domainesActiviteNames.add(domaineActivite.getNom());
                }
                String domainesActivite = String.join(",", domainesActiviteNames);
                preparedStatement.setString(5, domainesActivite);

                preparedStatement.executeUpdate();
            }

            
            for (DomaineActivite domaineActivite : entreprise.getDomainesActivite()) {
            		domaineActiviteDAO.ajouterDomaineActivite(domaineActivite);
                
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entrepriseId;
    }
    
    public int obtenirDernierIdEntreprise() {
        int dernierId = -1;
        
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            String query = "SELECT MAX(identificateur) AS dernierId FROM entreprise";
            ResultSet resultSet = statement.executeQuery(query);
            
            if (resultSet.next()) {
                dernierId = resultSet.getInt("dernierId");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return dernierId;
        
    }




    
    public void mettreAJourEntreprise(Entreprise entreprise) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE entreprise SET raisonSociale = ?, ville = ?, adresse = ?, siteWeb = ?, domaineActivite = ? " +
                             "WHERE identificateur = ?")) {
            preparedStatement.setString(1, entreprise.getRaisonSociale());
            preparedStatement.setString(2, entreprise.getVille());
            preparedStatement.setString(3, entreprise.getAdresse());
            preparedStatement.setString(4, entreprise.getSiteWeb());

            List<String> domainesActiviteNames = new ArrayList<>();
            for (DomaineActivite domaineActivite : entreprise.getDomainesActivite()) {
                domainesActiviteNames.add(domaineActivite.getNom());
            }
            String domainesActivite = String.join(",", domainesActiviteNames);
            preparedStatement.setString(5, domainesActivite);

            preparedStatement.setInt(6, entreprise.getIdentificateur());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    public Entreprise trouverEntrepriseParId(int identificateur) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM entreprise WHERE identificateur = ?")) {
            preparedStatement.setInt(1, identificateur);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Entreprise entreprise = new Entreprise();
                entreprise.setIdentificateur(resultSet.getInt("identificateur"));
                entreprise.setRaisonSociale(resultSet.getString("raisonSociale"));
                entreprise.setVille(resultSet.getString("ville"));
                entreprise.setAdresse(resultSet.getString("adresse"));
                entreprise.setSiteWeb(resultSet.getString("siteWeb"));
                
                String domaineActiviteString = resultSet.getString("domaineActivite");
                List<String> domainesActiviteNames = Arrays.asList(domaineActiviteString.split(","));
                List<DomaineActivite> domainesActivite = new ArrayList<>();
                for (String domaineName : domainesActiviteNames) {
                	DomaineActivite domaine = new DomaineActivite();
                    domaine.setNom(domaineName);
                    domainesActivite.add(domaine);
                }
                entreprise.setDomainesActivite(domainesActivite);
                
                return entreprise;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



    public void supprimerEntrepriseParId(int identifiant) {
        try {
            Connection conn = getConnection();
            String query = "DELETE FROM entreprise WHERE identificateur = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, identifiant);
            statement.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    public void supprimerEntreprise(Entreprise entreprise) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = getConnection();

            String sql = "DELETE FROM entreprise WHERE identificateur = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, entreprise.getIdentificateur());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } 
    }


    public List<Entreprise> getEntreprises() {
        List<Entreprise> entreprises = new ArrayList<>();

        try (Connection connection = getConnection()) {
            String query = "SELECT * FROM entreprise";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Entreprise entreprise = new Entreprise();
                        entreprise.setIdentificateur(resultSet.getInt("identificateur"));
                        entreprise.setRaisonSociale(resultSet.getString("raisonSociale"));
                        entreprise.setVille(resultSet.getString("ville"));
                        entreprise.setAdresse(resultSet.getString("adresse"));
                        entreprise.setSiteWeb(resultSet.getString("siteWeb"));

                        String domainesActivite = resultSet.getString("domaineactivite");
                        if (domainesActivite != null) {
                            List<DomaineActivite> domainesList = new ArrayList<>();
                            String[] domainesArray = domainesActivite.split(", ");
                            for (String domaine : domainesArray) {
                                DomaineActivite da = new DomaineActivite();
                                da.setNom(domaine);
                                domainesList.add(da);
                            }
                            entreprise.setDomainesActivite(domainesList);
                        }

                        entreprises.add(entreprise);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return entreprises;
    }



    public void supprimerDomainesActiviteParIdEntreprise(int entrepriseId) {
       
        String sql = "DELETE FROM domaine_activite WHERE entreprise_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, entrepriseId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public void supprimerStagesParIdEntreprise(int idEntreprise) {
        String query = "DELETE FROM stage WHERE entreprise_id = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, idEntreprise);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public void supprimerStageEtudiantParIdEntreprise(int idEntreprise) {
        String query = "DELETE se FROM stage_etudiant se " +
                       "JOIN stage s ON se.stage_id = s.id " +
                       "WHERE s.entreprise_id = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, idEntreprise);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public void supprimerStageTechnologieParIdEntreprise(int idEntreprise) {
        String query = "DELETE FROM stage_technologie WHERE stage_id IN (SELECT id FROM stage WHERE entreprise_id = ?)";
        try (Connection connection = getConnection();
        		PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idEntreprise);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}
