package com.epg.java.app;

import com.epg.java.models.*;
import com.epg.java.modelsDAO.EntrepriseDAO;
import com.epg.java.service.*;
import javax.swing.*;
import javax.swing.table.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class EspaceEntrepriseApp {
	
	private EntrepriseDAO entrepriseDAO =new EntrepriseDAO();


	private DefaultTableModel tableModel;
    private JTextField raisonSocialeField = new JTextField(30);
    private JTextField villeField = new JTextField(30);
    private JTextField adresseField = new JTextField(30);
    private JTextField siteWebField = new JTextField(30);
    private JTextField domainesActiviteField = new JTextField(30);





    public void AjouterEntreprise() {
        JFrame frame = new JFrame("Ajouter Entreprise");
        frame.setSize(500, 500);

        JPanel globalPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("Ajouter Entreprise"));

        JPanel raisonSocialePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        raisonSocialePanel.add(new JLabel("Raison Sociale:"));
        raisonSocialePanel.add(raisonSocialeField);
        panel.add(raisonSocialePanel);

        JPanel villePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        villePanel.add(new JLabel("Ville:"));
        villePanel.add(villeField);
        panel.add(villePanel);

        JPanel adressePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        adressePanel.add(new JLabel("Adresse:"));
        adressePanel.add(adresseField);
        panel.add(adressePanel);

        JPanel siteWebPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        siteWebPanel.add(new JLabel("Site Web:"));
        siteWebPanel.add(siteWebField);
        panel.add(siteWebPanel);

        JPanel domainesActivitePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        domainesActivitePanel.add(new JLabel("Domaines d'Activité:"));
        domainesActivitePanel.add(domainesActiviteField);
        panel.add(domainesActivitePanel);

        JButton ajouterButton = new JButton("Ajouter");
        ajouterButton.addActionListener(e -> {
          
        	 
                String raisonSociale = raisonSocialeField.getText();
                String ville = villeField.getText();
                String adresse = adresseField.getText();
                String siteweb = siteWebField.getText();
                String domainesActiviteText = domainesActiviteField.getText();

                String[] domainesActiviteArray = domainesActiviteText.split(",");
                List<DomaineActivite> domainesActivite = new ArrayList<>();
                for (String domaine : domainesActiviteArray) {
                    DomaineActivite da = new DomaineActivite();
                    da.setNom(domaine.trim());
                    domainesActivite.add(da);
                }

                Entreprise nouvelleEntreprise = new Entreprise();
                nouvelleEntreprise.setRaisonSociale(raisonSociale);
                nouvelleEntreprise.setVille(ville);
                nouvelleEntreprise.setAdresse(adresse);
                nouvelleEntreprise.setSiteWeb(siteweb);
                nouvelleEntreprise.setDomainesActivite(domainesActivite);

                EntrepriseService entrepriseService = new EntrepriseService();
                int dernierId = entrepriseService.ajouterEntreprise(nouvelleEntreprise);
                nouvelleEntreprise.setIdentificateur(dernierId);

                JOptionPane.showMessageDialog(frame, "Entreprise ajoutée avec succès !");
                actualiserTable();

                
                resetFields();
            
        });
        

        JButton quitterButton = new JButton("Quitter");
        quitterButton.addActionListener(e -> {
          
                frame.dispose();
            
        });
        
        
        
       
        
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Identifiant"); 
        tableModel.addColumn("Raison Sociale");
        tableModel.addColumn("Ville");
        tableModel.addColumn("Adresse");
        tableModel.addColumn("Site Web");
        tableModel.addColumn("Domaine d'Activité");

        JTable entrepriseTable = new JTable(tableModel);
        entrepriseTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        entrepriseTable.getColumnModel().getColumn(0).setMinWidth(0);
        entrepriseTable.getColumnModel().getColumn(0).setMaxWidth(0);
        
        TableColumn domaineActiviteColumn = entrepriseTable.getColumnModel().getColumn(5); 
        domaineActiviteColumn.setPreferredWidth(270); 

        TableColumn raison = entrepriseTable.getColumnModel().getColumn(1); 
        raison.setPreferredWidth(140);
        
        TableColumn adresse = entrepriseTable.getColumnModel().getColumn(3); 
        adresse.setPreferredWidth(250);
        
        TableColumn site = entrepriseTable.getColumnModel().getColumn(4); 
        site.setPreferredWidth(250);
        
        entrepriseTable.getSelectionModel().addListSelectionListener(e -> {
           
                int selectedRow = entrepriseTable.getSelectedRow();
                if (selectedRow >= 0) {
                    Object identifiantObj = entrepriseTable.getValueAt(selectedRow, 0);
                    if (identifiantObj instanceof String) {
                        int identifiant = Integer.parseInt((String) identifiantObj);
                        Entreprise entrepriseSelectionnee = entrepriseDAO.trouverEntrepriseParId(identifiant);
                        if (entrepriseSelectionnee != null) {

                        	raisonSocialeField.setText(entrepriseSelectionnee.getRaisonSociale());
                            villeField.setText(entrepriseSelectionnee.getVille());
                            adresseField.setText(entrepriseSelectionnee.getAdresse());
                            siteWebField.setText(entrepriseSelectionnee.getSiteWeb());

                            domainesActiviteField.setText(getDomainesActiviteString(entrepriseSelectionnee.getDomainesActivite()));
                        }
                    }
                }
            
        });

        

       
        JScrollPane tableScrollPane = new JScrollPane(entrepriseTable);

        JButton actualiserButton = new JButton("Actualiser");
        actualiserButton.addActionListener(e -> {
        	  resetFields();
         
                
                List<Entreprise> entreprises = entrepriseDAO.getEntreprises();
                tableModel.setRowCount(0); 
                for (Entreprise entreprise : entreprises) {
                    String identifiant = String.valueOf(entreprise.getIdentificateur()); 
                    String raisonSociale = entreprise.getRaisonSociale();
                    String ville = entreprise.getVille();
                    String adresses = entreprise.getAdresse();
                    String siteWeb = entreprise.getSiteWeb();
                    String domainesActivite = "";
                    for (DomaineActivite da : entreprise.getDomainesActivite()) {
                        domainesActivite += da.getNom() + ", ";
                    }
                    if (!domainesActivite.isEmpty()) {
                        domainesActivite = domainesActivite.substring(0, domainesActivite.length() - 2);
                    }

                    
                    tableModel.addRow(new Object[]{identifiant, raisonSociale, ville, adresses, siteWeb, domainesActivite});
                }
            
        });

        JButton supprimerButton = new JButton("Supprimer");
        supprimerButton.addActionListener(e -> {
        	  resetFields();
            int selectedRow = entrepriseTable.getSelectedRow();
            if (selectedRow >= 0) {
                int option = JOptionPane.showConfirmDialog(
                    frame, "Etes-vous sûr de supprimer cette entreprise ?", "Confirmation",
                    JOptionPane.YES_NO_OPTION);

                if (option == JOptionPane.YES_OPTION) {
                    Object identifiantObj = entrepriseTable.getValueAt(selectedRow, 0);
                    if (identifiantObj instanceof String) {
                        int identifiant = Integer.parseInt((String) identifiantObj);

                        entrepriseDAO.supprimerDomainesActiviteParIdEntreprise(identifiant);
                        entrepriseDAO.supprimerStageTechnologieParIdEntreprise(identifiant);
                        entrepriseDAO.supprimerStageEtudiantParIdEntreprise(identifiant);
                        entrepriseDAO.supprimerStagesParIdEntreprise(identifiant);
                        entrepriseDAO.supprimerEntrepriseParId(identifiant);

                        JOptionPane.showMessageDialog(frame, "Entreprise supprimée avec succès !");
                        actualiserTable();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Veuillez sélectionner une entreprise à supprimer.");
            }
        });


      

    
       

        
        JButton modifierButton = new JButton("Modifier");
        modifierButton.addActionListener(e -> {
            int selectedRow = entrepriseTable.getSelectedRow();
            if (selectedRow >= 0) {
                Object identifiantObj = entrepriseTable.getValueAt(selectedRow, 0);
                if (identifiantObj instanceof String) {
                    int identifiant = Integer.parseInt((String) identifiantObj);

                    String nouvelleRaisonSociale = raisonSocialeField.getText();
                    String nouvelleVille = villeField.getText();
                    String nouvelleAdresse = adresseField.getText();
                    String nouveauSiteWeb = siteWebField.getText();
                    String nouveauxDomainesActiviteText = domainesActiviteField.getText();

                    String[] nouveauxDomainesActiviteArray = nouveauxDomainesActiviteText.split(",");
                    List<DomaineActivite> nouveauxDomainesActivite = new ArrayList<>();
                    for (String domaine : nouveauxDomainesActiviteArray) {
                        DomaineActivite da = new DomaineActivite();
                        da.setNom(domaine.trim());
                        nouveauxDomainesActivite.add(da);
                    }

                    Entreprise entrepriseModifiee = new Entreprise();
                    entrepriseModifiee.setIdentificateur(identifiant);
                    entrepriseModifiee.setRaisonSociale(nouvelleRaisonSociale);
                    entrepriseModifiee.setVille(nouvelleVille);
                    entrepriseModifiee.setAdresse(nouvelleAdresse);
                    entrepriseModifiee.setSiteWeb(nouveauSiteWeb);
                    entrepriseModifiee.setDomainesActivite(nouveauxDomainesActivite);

                    entrepriseDAO.mettreAJourEntreprise(entrepriseModifiee);

                    JOptionPane.showMessageDialog(frame, "Entreprise modifiée avec succès !");
                    actualiserTable();
                    resetFields();
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Veuillez sélectionner une entreprise à modifier.");
                resetFields();
            }
        });

        
        
        
        


        JPanel entrepriseTablePanel = new JPanel(new BorderLayout());
        entrepriseTablePanel.add(tableScrollPane, BorderLayout.CENTER);

        JPanel entrepriseButtonsPanel = new JPanel();
        entrepriseButtonsPanel.add(actualiserButton);entrepriseButtonsPanel.add(supprimerButton); entrepriseButtonsPanel.add(quitterButton);
        entrepriseTablePanel.add(entrepriseButtonsPanel, BorderLayout.SOUTH);

        globalPanel.add(panel);
        globalPanel.add(entrepriseTablePanel);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(ajouterButton);
       
        buttonsPanel.add(modifierButton);

        

        panel.add(buttonsPanel);
        globalPanel.add(panel);
        
        actualiserTable();

        frame.setContentPane(globalPanel);
        frame.setLocationRelativeTo(null); 
        
        frame.pack();
        frame.setVisible(true);
    }
    


    private void actualiserTable() {
        List<Entreprise> entreprises = entrepriseDAO.getEntreprises();
        tableModel.setRowCount(0);
        for (Entreprise entreprise : entreprises) {
            String identifiant = String.valueOf(entreprise.getIdentificateur());
            String raisonSociale = entreprise.getRaisonSociale();
            String ville = entreprise.getVille();
            String adresse = entreprise.getAdresse();
            String siteWeb = entreprise.getSiteWeb();
            String domainesActivite = getDomainesActiviteString(entreprise.getDomainesActivite());

            tableModel.addRow(new Object[]{identifiant, raisonSociale, ville, adresse, siteWeb, domainesActivite});
        }
    }

    
    private String getDomainesActiviteString(List<DomaineActivite> domainesActivite) {
        StringBuilder sb = new StringBuilder();
        for (DomaineActivite domaine : domainesActivite) {
            sb.append(domaine.getNom()).append(", ");
        }
        if (sb.length() > 2) {
            sb.setLength(sb.length() - 2);
        }
        return sb.toString();
    }

    private void resetFields() {
        raisonSocialeField.setText("");
        villeField.setText("");
        adresseField.setText("");
        siteWebField.setText("");
        domainesActiviteField.setText("");
    }

}
