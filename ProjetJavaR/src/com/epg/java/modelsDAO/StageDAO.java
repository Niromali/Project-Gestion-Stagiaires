package com.epg.java.modelsDAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import com.epg.java.models.Stage;
import com.epg.java.models.Encadrant;
import com.epg.java.models.Entreprise;
import com.epg.java.models.Etudiant;
import com.epg.java.models.Prof;

public class StageDAO {
	
    private EntrepriseDAO entrepriseDAO = new EntrepriseDAO();
    private EncadrantDAO encadrantDAO = new EncadrantDAO();
    private ProfDAO profDAO = new ProfDAO();
   
    private Connection getConnection() throws SQLException {

    	String url = "jdbc:mysql://localhost:3306/stagiaires";
        String utilisateur = "root";
        String motDePasse = "";
        return DriverManager.getConnection(url, utilisateur, motDePasse);
    }

    public void ajouterStage(Stage stage) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO stage (theme, entreprise_id, encadrant_entreprise_id, prof_encadrant_id) " +
                             "VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, stage.getTheme());

            int entrepriseId = getEntrepriseId(stage.getEntreprise().getIdentificateur(), connection);
            preparedStatement.setInt(2, entrepriseId);

            preparedStatement.setInt(3, stage.getEncadrantEntreprise().getId());
            preparedStatement.setInt(4, stage.getProfEncadrant().getId());

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int stageId = generatedKeys.getInt(1);

                for (Etudiant etudiant : stage.getEtudiants()) {
                    try (PreparedStatement etudiantStatement = connection.prepareStatement(
                            "INSERT INTO stage_etudiant (stage_id, etudiant_id) VALUES (?, ?)")) {
                        etudiantStatement.setInt(1, stageId);
                        etudiantStatement.setInt(2, etudiant.getCne());
                        etudiantStatement.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                try (PreparedStatement technologieStatement = connection.prepareStatement(
                        "INSERT INTO stage_technologie (stage_id, technologie_id) VALUES (?, ?)",
                        Statement.RETURN_GENERATED_KEYS)) {
                    for (String technologie : stage.getTechnologiesDemandees()) {
                        technologieStatement.setInt(1, stageId);
                        
                        int technologieId = obtenirTechnologieId(technologie, connection);
                        technologieStatement.setInt(2, technologieId);
                        
                        technologieStatement.executeUpdate();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    private int obtenirTechnologieId(String nomTechnologie, Connection connection) throws SQLException {
        int technologieId = -1;
        
        try (PreparedStatement technologieQuery = connection.prepareStatement(
                "SELECT id FROM technologie WHERE nom = ?")) {
            technologieQuery.setString(1, nomTechnologie);
            
            try (ResultSet resultSet = technologieQuery.executeQuery()) {
                if (resultSet.next()) {
                    technologieId = resultSet.getInt("id");
                }
            }
        }
        
        return technologieId;
    }

	private int getEntrepriseId(int identificateur, Connection connection) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT identificateur FROM entreprise WHERE identificateur = ?")) {
            preparedStatement.setInt(1, identificateur);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("identificateur");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new IllegalArgumentException("L'identificateur d'entreprise n'existe pas dans la base de données.");
    }

    

public List<Stage> trouverStagesParCneEtudiant(int cne) {
        List<Stage> stages = new ArrayList<>();
        
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT s.* FROM stage s " +
                     "INNER JOIN stage_etudiant se ON s.id = se.stage_id " +
                     "INNER JOIN etudiant e ON se.etudiant_id = e.cne " +
                     "WHERE e.cne = ?")) {
            preparedStatement.setInt(1, cne);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                Stage stage = new Stage();
                stage.setTheme(resultSet.getString("theme"));
                
                int entrepriseId = resultSet.getInt("entreprise_id");
                Entreprise entreprise = entrepriseDAO.trouverEntrepriseParId(entrepriseId);
                stage.setEntreprise(entreprise);
                
                int encadrantId = resultSet.getInt("encadrant_entreprise_id");
                Encadrant encadrantEntreprise = encadrantDAO.trouverEncadrantParId(encadrantId);
                stage.setEncadrantEntreprise(encadrantEntreprise);
                
                int profId = resultSet.getInt("prof_encadrant_id");
                Prof profEncadrant = profDAO.trouverProfParId(profId);
                stage.setProfEncadrant(profEncadrant);
                
                List<String> technologiesDemandees = new ArrayList<>();
                try (PreparedStatement technologieStatement = connection.prepareStatement(
                        "SELECT t.nom FROM technologie t " +
                        "INNER JOIN stage_technologie st ON t.id = st.technologie_id " +
                        "WHERE st.stage_id = ?")) {
                    technologieStatement.setInt(1, resultSet.getInt("id"));
                    ResultSet techResultSet = technologieStatement.executeQuery();
                    while (techResultSet.next()) {
                        String technologie = techResultSet.getString("nom");
                        technologiesDemandees.add(technologie);
                    }
                }
                stage.setTechnologiesDemandees(technologiesDemandees);
                
                stages.add(stage);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return stages;
    }



public List<Stage> trouverStagesParTechnologie(String technologie) {
    List<Stage> stages = new ArrayList<>();
    
    try (Connection connection = getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(
                 "SELECT s.* FROM stage s " +
                 "INNER JOIN stage_technologie st ON s.id = st.stage_id " +
                 "INNER JOIN technologie t ON st.technologie_id = t.id " +
                 "WHERE t.nom = ?")) {
        preparedStatement.setString(1, technologie);
        ResultSet resultSet = preparedStatement.executeQuery();
        
        while (resultSet.next()) {
            Stage stage = new Stage();
            stage.setTheme(resultSet.getString("theme"));
            
            int entrepriseId = resultSet.getInt("entreprise_id");
            Entreprise entreprise = entrepriseDAO.trouverEntrepriseParId(entrepriseId);
            stage.setEntreprise(entreprise);
            
            int encadrantId = resultSet.getInt("encadrant_entreprise_id");
            Encadrant encadrantEntreprise = encadrantDAO.trouverEncadrantParId(encadrantId);
            stage.setEncadrantEntreprise(encadrantEntreprise);
            
            int profId = resultSet.getInt("prof_encadrant_id");
            Prof profEncadrant = profDAO.trouverProfParId(profId);
            stage.setProfEncadrant(profEncadrant);
            
            List<String> technologiesDemandees = new ArrayList<>();
            try (PreparedStatement technologieStatement = connection.prepareStatement(
                    "SELECT t.nom FROM technologie t " +
                    "INNER JOIN stage_technologie st ON t.id = st.technologie_id " +
                    "WHERE st.stage_id = ?")) {
                technologieStatement.setInt(1, resultSet.getInt("id"));
                ResultSet techResultSet = technologieStatement.executeQuery();
                while (techResultSet.next()) {
                    String tech = techResultSet.getString("nom");
                    technologiesDemandees.add(tech);
                }
            }
            stage.setTechnologiesDemandees(technologiesDemandees);
            
            stages.add(stage);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    
    return stages;
}

public List<Stage> getListeStages() {
    List<Stage> stages = new ArrayList<>();

    try (Connection connection = getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(
                 "SELECT * FROM stage")) {
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            Stage stage = new Stage();
            stage.setId(resultSet.getInt("id"));
            stage.setTheme(resultSet.getString("theme"));

            int entrepriseId = resultSet.getInt("entreprise_id");
            Entreprise entreprise = entrepriseDAO.trouverEntrepriseParId(entrepriseId);
            stage.setEntreprise(entreprise);

            int encadrantId = resultSet.getInt("encadrant_entreprise_id");
            Encadrant encadrantEntreprise = encadrantDAO.trouverEncadrantParId(encadrantId);
            stage.setEncadrantEntreprise(encadrantEntreprise);

            int profId = resultSet.getInt("prof_encadrant_id");
            Prof profEncadrant = profDAO.trouverProfParId(profId);
            stage.setProfEncadrant(profEncadrant);

            List<String> technologiesDemandees = new ArrayList<>();
            try (PreparedStatement technologieStatement = connection.prepareStatement(
                    "SELECT t.nom FROM technologie t " +
                            "INNER JOIN stage_technologie st ON t.id = st.technologie_id " +
                            "WHERE st.stage_id = ?")) {
                technologieStatement.setInt(1, resultSet.getInt("id"));
                ResultSet techResultSet = technologieStatement.executeQuery();
                while (techResultSet.next()) {
                    String technologie = techResultSet.getString("nom");
                    technologiesDemandees.add(technologie);
                }
            }
            stage.setTechnologiesDemandees(technologiesDemandees);

            List<Etudiant> etudiants = new ArrayList<>();
            try (PreparedStatement etudiantStatement = connection.prepareStatement(
                    "SELECT e.* FROM etudiant e " +
                    "INNER JOIN stage_etudiant se ON e.cne = se.etudiant_id " +
                    "WHERE se.stage_id = ?")) {
                etudiantStatement.setInt(1, resultSet.getInt("id"));
                ResultSet etudiantResultSet = etudiantStatement.executeQuery();
                while (etudiantResultSet.next()) {
                    Etudiant etudiant = new Etudiant();
                    etudiant.setCne(etudiantResultSet.getInt("cne"));
                    etudiant.setNom(etudiantResultSet.getString("nom"));
                    etudiant.setPrenom(etudiantResultSet.getString("prenom"));
                    etudiant.setDateNaissance(etudiantResultSet.getDate("dateNaissance"));
                    etudiants.add(etudiant);
                }
            }
            stage.setEtudiants(etudiants);

            stages.add(stage);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return stages;
}


    public void supprimerStageParId(int stageId) {
        String deleteStageQuery = "DELETE FROM stage WHERE id = ?";
        String deleteStageEtudiantQuery = "DELETE FROM stage_etudiant WHERE stage_id = ?";
        String deleteStageTechnologieQuery = "DELETE FROM stage_technologie WHERE stage_id = ?";
        
        try (Connection connection = getConnection();
             PreparedStatement deleteStageStmt = connection.prepareStatement(deleteStageQuery);
             PreparedStatement deleteStageEtudiantStmt = connection.prepareStatement(deleteStageEtudiantQuery);
             PreparedStatement deleteStageTechnologieStmt = connection.prepareStatement(deleteStageTechnologieQuery)) {
            
            deleteStageEtudiantStmt.setInt(1, stageId);
            deleteStageEtudiantStmt.executeUpdate();
            
            deleteStageTechnologieStmt.setInt(1, stageId);
            deleteStageTechnologieStmt.executeUpdate();
            
            deleteStageStmt.setInt(1, stageId);
            deleteStageStmt.executeUpdate();
            
            System.out.println("Stage supprimé avec succès !");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    

    public Stage trouverStageParId(int id) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM stage WHERE id = ?")) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Stage stage = new Stage();
                stage.setId(resultSet.getInt("id"));
                stage.setTheme(resultSet.getString("theme"));
                
                int entrepriseId = resultSet.getInt("entreprise_id");
                Entreprise entreprise = entrepriseDAO.trouverEntrepriseParId(entrepriseId);
                stage.setEntreprise(entreprise);

                int encadrantId = resultSet.getInt("encadrant_entreprise_id");
                Encadrant encadrantEntreprise = encadrantDAO.trouverEncadrantParId(encadrantId);
                stage.setEncadrantEntreprise(encadrantEntreprise);

                int profId = resultSet.getInt("prof_encadrant_id");
                Prof profEncadrant = profDAO.trouverProfParId(profId);
                stage.setProfEncadrant(profEncadrant);

                List<String> technologiesDemandees = new ArrayList<>();
                try (PreparedStatement technologieStatement = connection.prepareStatement(
                        "SELECT t.nom FROM technologie t " +
                                "INNER JOIN stage_technologie st ON t.id = st.technologie_id " +
                                "WHERE st.stage_id = ?")) {
                    technologieStatement.setInt(1, resultSet.getInt("id"));
                    ResultSet techResultSet = technologieStatement.executeQuery();
                    while (techResultSet.next()) {
                        String technologie = techResultSet.getString("nom");
                        technologiesDemandees.add(technologie);
                    }
                }
                stage.setTechnologiesDemandees(technologiesDemandees);

                List<Etudiant> etudiants = new ArrayList<>();
                try (PreparedStatement etudiantStatement = connection.prepareStatement(
                        "SELECT e.* FROM etudiant e " +
                        "INNER JOIN stage_etudiant se ON e.cne = se.etudiant_id " +
                        "WHERE se.stage_id = ?")) {
                    etudiantStatement.setInt(1, resultSet.getInt("id"));
                    ResultSet etudiantResultSet = etudiantStatement.executeQuery();
                    while (etudiantResultSet.next()) {
                        Etudiant etudiant = new Etudiant();
                        etudiant.setCne(etudiantResultSet.getInt("cne"));
                        etudiant.setNom(etudiantResultSet.getString("nom"));
                        etudiant.setPrenom(etudiantResultSet.getString("prenom"));
                        etudiant.setDateNaissance(etudiantResultSet.getDate("dateNaissance"));
                        etudiants.add(etudiant);
                    }
                }
                stage.setEtudiants(etudiants);

                return stage;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    
    

    
    public void modifierStage(Stage stage) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE stage SET theme = ?, entreprise_id = ?, encadrant_entreprise_id = ?, prof_encadrant_id = ? " +
                             "WHERE id = ?")) {
            preparedStatement.setString(1, stage.getTheme());

            int entrepriseId = getEntrepriseId(stage.getEntreprise().getIdentificateur(), connection);
            preparedStatement.setInt(2, entrepriseId);

            preparedStatement.setInt(3, stage.getEncadrantEntreprise().getId());
            preparedStatement.setInt(4, stage.getProfEncadrant().getId());
            preparedStatement.setInt(5, stage.getId());

            // Mise à jour des associations entre le stage et les étudiants
            try (PreparedStatement deleteEtudiantsStmt = connection.prepareStatement(
                    "DELETE FROM stage_etudiant WHERE stage_id = ?")) {
                deleteEtudiantsStmt.setInt(1, stage.getId());
                deleteEtudiantsStmt.executeUpdate();

                for (Etudiant etudiant : stage.getEtudiants()) {
                    try (PreparedStatement etudiantStatement = connection.prepareStatement(
                            "INSERT INTO stage_etudiant (stage_id, etudiant_id) VALUES (?, ?)")) {
                        etudiantStatement.setInt(1, stage.getId());
                        etudiantStatement.setInt(2, etudiant.getCne());
                        etudiantStatement.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try (PreparedStatement deleteTechnologiesStmt = connection.prepareStatement(
                    "DELETE FROM stage_technologie WHERE stage_id = ?")) {
                deleteTechnologiesStmt.setInt(1, stage.getId());
                deleteTechnologiesStmt.executeUpdate();

                for (String technologie : stage.getTechnologiesDemandees()) {
                    try (PreparedStatement technologieStatement = connection.prepareStatement(
                            "INSERT INTO technologie (nom) VALUES (?)", Statement.RETURN_GENERATED_KEYS)) {
                        technologieStatement.setString(1, technologie);
                        technologieStatement.executeUpdate();

                        ResultSet techGeneratedKeys = technologieStatement.getGeneratedKeys();
                        if (techGeneratedKeys.next()) {
                            int technologieId = techGeneratedKeys.getInt(1);

                            try (PreparedStatement stageTechnoStatement = connection.prepareStatement(
                                    "INSERT INTO stage_technologie (stage_id, technologie_id) VALUES (?, ?)")) {
                                stageTechnoStatement.setInt(1, stage.getId());
                                stageTechnoStatement.setInt(2, technologieId);
                                stageTechnoStatement.executeUpdate();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}

