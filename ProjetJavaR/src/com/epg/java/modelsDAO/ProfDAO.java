	package com.epg.java.modelsDAO;
	
	import java.sql.Connection;
	import java.sql.DriverManager;
	import java.sql.PreparedStatement;
	import java.sql.ResultSet;
	import java.sql.SQLException;
	import java.util.ArrayList;
	import java.util.List;
	import com.epg.java.models.Prof;
	
	public class ProfDAO {
	
	    private Connection getConnection() throws SQLException {

	    	String url = "jdbc:mysql://localhost:3306/stagiaires";
	        String utilisateur = "root";
	        String motDePasse = "";
	        return DriverManager.getConnection(url, utilisateur, motDePasse);
	    }
	
	    public void ajouterProf(Prof prof) {
	        try (Connection connection = getConnection();
	             PreparedStatement preparedStatement = connection.prepareStatement(
	                     "INSERT INTO prof (id, nom, prenom, tel, email) " +
	                             "VALUES (?, ?, ?, ?, ?)")) {
	            preparedStatement.setInt(1, prof.getId());
	            preparedStatement.setString(2, prof.getNom());
	            preparedStatement.setString(3, prof.getPrenom());
	            preparedStatement.setString(4, prof.getTel());
	            preparedStatement.setString(5, prof.getEmail());
	            preparedStatement.executeUpdate();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	
	    public Prof trouverProfParId(int id) {
	        try (Connection connection = getConnection();
	             PreparedStatement preparedStatement = connection.prepareStatement(
	                     "SELECT * FROM prof WHERE id = ?")) {
	            preparedStatement.setInt(1, id);
	            ResultSet resultSet = preparedStatement.executeQuery();
	            if (resultSet.next()) {
	                Prof prof = new Prof();
	                prof.setId(resultSet.getInt("id"));
	                prof.setNom(resultSet.getString("nom"));
	                prof.setPrenom(resultSet.getString("prenom"));
	                prof.setTel(resultSet.getString("tel"));
	                prof.setEmail(resultSet.getString("email"));
	                return prof;
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return null;
	    }
	
	    public void mettreAJourProf(Prof prof) {
	        try (Connection connection = getConnection();
	             PreparedStatement preparedStatement = connection.prepareStatement(
	                     "UPDATE prof SET nom = ?, prenom = ?, tel = ?, email = ? " +
	                             "WHERE id = ?")) {
	            preparedStatement.setString(1, prof.getNom());
	            preparedStatement.setString(2, prof.getPrenom());
	            preparedStatement.setString(3, prof.getTel());
	            preparedStatement.setString(4, prof.getEmail());
	            preparedStatement.setInt(5,	prof.getId());
	            preparedStatement.executeUpdate();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	
	    public void supprimerProf(int id) {
	        try (Connection connection = getConnection();
	             PreparedStatement preparedStatement = connection.prepareStatement(
	                     "DELETE FROM prof WHERE id = ?")) {
	            preparedStatement.setInt(1, id);
	            preparedStatement.executeUpdate();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

		public boolean profExiste(int id) {
			 try (Connection connection = getConnection();
		             PreparedStatement preparedStatement = connection.prepareStatement(
		                     "SELECT id FROM prof WHERE id = ?")) {
		            preparedStatement.setInt(1, id);
		            ResultSet resultSet = preparedStatement.executeQuery();
		            return resultSet.next();
		        } catch (SQLException e) {
		            e.printStackTrace();
		        }
		        return false;
		}

		public List<Prof> getListeProfs() {
			 List<Prof> profs = new ArrayList<>();
		        
		        try (Connection connection = getConnection();
		             PreparedStatement preparedStatement = connection.prepareStatement(
		                     "SELECT * FROM prof")) {
		            ResultSet resultSet = preparedStatement.executeQuery();
		            
		            while (resultSet.next()) {
		                Prof prof = new Prof();
		                prof.setId(resultSet.getInt("id"));
		                prof.setNom(resultSet.getString("nom"));
		                prof.setPrenom(resultSet.getString("prenom"));
		                prof.setTel(resultSet.getString("tel"));
		                prof.setEmail(resultSet.getString("email"));
		                profs.add(prof);
		            }
		        } catch (SQLException e) {
		            e.printStackTrace();
		        }
		        
		        return profs;
	}

		
		  public void supprimerStageParIdProf(int idProf) {
		        String query = "DELETE FROM stage WHERE prof_encadrant_id = ?";
		        try (Connection connection = getConnection();
		             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
		            preparedStatement.setInt(1, idProf);
		            preparedStatement.executeUpdate();
		        } catch (SQLException e) {
		            e.printStackTrace();
		        }
		    }
		  
		  public void supprimerStagesEtudiantParIdProf(int idProf) {
			    String query = "DELETE FROM stage_etudiant WHERE stage_id IN (SELECT id FROM stage WHERE prof_encadrant_id = ?)";
			    try (Connection connection = getConnection();
			    		PreparedStatement statement = connection.prepareStatement(query)) {
			        statement.setInt(1, idProf);
			        statement.executeUpdate();
			    } catch (SQLException e) {
			        e.printStackTrace();
			    }
			}



	}
