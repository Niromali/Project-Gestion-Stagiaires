package com.epg.java.app;

import com.epg.java.models.*;
import com.epg.java.modelsDAO.DomaineActiviteDAO;
import com.epg.java.modelsDAO.StageDAO;
import com.epg.java.modelsDAO.TechnologieDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class EspaceTechnologiesApp {

    private TechnologieDAO technologieDAO = new TechnologieDAO();

    private JTextField nomField = new JTextField(15);

    private DefaultTableModel tableModel;

    public void EspaceTechnologies() {
        JFrame frame = new JFrame("Gestion des Technologies Demandee");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel globalPanel = new JPanel(new BorderLayout());

        JPanel leftPanel = new JPanel(new FlowLayout());
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
       

        JPanel rightPanel = new JPanel(new FlowLayout());

        // Tableau
        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Nom");

        JTable TechnologieTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(TechnologieTable);
        leftPanel.add(tableScrollPane);
        
        TechnologieTable.getColumnModel().getColumn(0).setMinWidth(0);
        TechnologieTable.getColumnModel().getColumn(0).setMaxWidth(0);

        // Formulaire
        JPanel Formulaire = new JPanel();
        Formulaire.setLayout(new BoxLayout(Formulaire, BoxLayout.Y_AXIS));
        Formulaire.setBorder(BorderFactory.createTitledBorder("Ajouter une Technologies"));

        JPanel nomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        nomPanel.add(new JLabel("Nom:"));
        nomPanel.add(nomField);
        Formulaire.add(nomPanel);

        JPanel buttonsPanelLeft = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton ajouterButton = new JButton("Ajouter");
        ajouterButton.addActionListener(e -> {
            String nom = nomField.getText();

            if (nom.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Veuillez entrer un nom pour la technologie.", "Erreur d'ajout", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (technologieDAO.technologieExisteDeja(nom)) {
                JOptionPane.showMessageDialog(frame, "La technologie existe déjà.", "Erreur d'ajout", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Technologie technologie = new Technologie();
            technologie.setNom(nom);
            technologieDAO.ajouterTechnologie(technologie);

            JOptionPane.showMessageDialog(frame, "Technologie ajoutée avec succès !");
            actualiserTable();
            resetFields();
        });


        buttonsPanel.add(ajouterButton);

        JButton quitterButton = new JButton("Quitter");
        quitterButton.addActionListener(e -> {
            frame.dispose();
        });
        buttonsPanelLeft.add(quitterButton);

        Formulaire.add(buttonsPanel);
        rightPanel.add(Formulaire);

        JButton actualiserButton = new JButton("Actualiser");
        actualiserButton.addActionListener(e -> {
            actualiserTable();
        });
        buttonsPanelLeft.add(actualiserButton);

        JButton supprimerButton = new JButton("Supprimer");
        supprimerButton.addActionListener(e -> {
            int selectedRow = TechnologieTable.getSelectedRow();
            if (selectedRow >= 0) {
                Object idObj = TechnologieTable.getValueAt(selectedRow, 0);
                if (idObj instanceof Integer) {
                    int id = (int) idObj;

                    int option = JOptionPane.showConfirmDialog(
                            frame, "Êtes-vous sûr de supprimer cette technologie ?", "Confirmation",
                            JOptionPane.YES_NO_OPTION);

                    if (option == JOptionPane.YES_OPTION) {
                        if (technologieDAO.isTechnologieUsedInStages(id)) {
                            int stageOption = JOptionPane.showConfirmDialog(
                                    frame, "Cette technologie est liée à un ou plusieurs stages. Voulez-vous supprimer ?",
                                    "Confirmation", JOptionPane.YES_NO_OPTION);

                            if (stageOption == JOptionPane.YES_OPTION) {
                                technologieDAO.supprimerStagesAvecTechnologie(id);
                                JOptionPane.showMessageDialog(frame, "technologie supprimés avec succès !");
                            } else {
                                JOptionPane.showMessageDialog(frame, "Annulation de la suppression de la technologie.");
                            }
                        } else {
                            technologieDAO.supprimerTechnologie(id);
                            JOptionPane.showMessageDialog(frame, "Technologie supprimée avec succès !");
                        }

                        actualiserTable();
                        resetFields();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Veuillez sélectionner une technologie à supprimer.");
            }
        });
        buttonsPanelLeft.add(supprimerButton);


        TechnologieTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = TechnologieTable.getSelectedRow();
            if (selectedRow >= 0) {
                Object idObj = TechnologieTable.getValueAt(selectedRow, 0);
                if (idObj instanceof Integer) {
                    int id = (int) idObj;
                    Technologie technologie = technologieDAO.trouverTechnologieParId(id);

                    if (technologie != null) {
                        nomField.setText(technologie.getNom());
                    }
                }
            }
        });
        
        JButton modifierButton = new JButton("Modifier");
        modifierButton.addActionListener(e -> {
            int selectedRow = TechnologieTable.getSelectedRow();
            if (selectedRow >= 0) {
                Object idObj = TechnologieTable.getValueAt(selectedRow, 0);
                if (idObj instanceof Integer) {
                    int id = (int) idObj;
                    String nouveauNom = nomField.getText();

                    Technologie technologieAModifier = technologieDAO.trouverTechnologieParId(id);
                    if (technologieAModifier != null) {
                        technologieAModifier.setNom(nouveauNom);
                        technologieDAO.mettreAJourTechnologie(technologieAModifier);

                        JOptionPane.showMessageDialog(frame, "Technologie modifié avec succèes !");
                        actualiserTable();
                        resetFields();
                    }
                }
            } else {
            	 JOptionPane.showMessageDialog(frame, "Veuillez sélectionner une Technologie à modifier.");
            }
        });


      

        buttonsPanel.add(modifierButton);
        
        leftPanel.add(buttonsPanelLeft);

        globalPanel.add(leftPanel, BorderLayout.CENTER);
        globalPanel.add(rightPanel, BorderLayout.EAST);

        actualiserTable();
        frame.setContentPane(globalPanel);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }

    private void actualiserTable() {
        List<Technologie> technologies = technologieDAO.getListeTechnologies();
        tableModel.setRowCount(0); 
        
        for (Technologie technologie : technologies) {
            tableModel.addRow(new Object[]{technologie.getId(), technologie.getNom()});
        }
    }


    private void resetFields() {
        nomField.setText("");
    }
}
