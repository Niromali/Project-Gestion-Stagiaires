package com.epg.java.app;

import com.epg.java.models.*;
import com.epg.java.modelsDAO.EncadrantDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class EspaceEncadrantApp {

    private EncadrantDAO encadrantDAO = new EncadrantDAO();

    private JTextField idField = new JTextField(15);
    private JTextField nomField = new JTextField(15);
    private JTextField prenomField = new JTextField(15);
    private JTextField telField = new JTextField(15);
    private JTextField emailField = new JTextField(15);

    private DefaultTableModel tableModel;

    public void AjouterEncadrant() {
        JFrame frame = new JFrame("Gestion des encadrants");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel globalPanel = new JPanel(new BorderLayout());

        JPanel leftPanel = new JPanel(new FlowLayout());
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        JPanel rightPanel = new JPanel(new FlowLayout());

        // Tableau
        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Nom");
        tableModel.addColumn("Prénom");
        tableModel.addColumn("Téléphone");
        tableModel.addColumn("Email");

        JTable encadrantTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(encadrantTable);
        leftPanel.add(tableScrollPane);
        
        encadrantTable.getColumnModel().getColumn(0).setMinWidth(0);
        encadrantTable.getColumnModel().getColumn(0).setMaxWidth(0);
        
        
        TableColumn domaineActiviteColumn = encadrantTable.getColumnModel().getColumn(4); 
        domaineActiviteColumn.setPreferredWidth(200); 

        // Formulaire
        JPanel Formulaire = new JPanel();
        Formulaire.setLayout(new BoxLayout(Formulaire, BoxLayout.Y_AXIS));
        Formulaire.setBorder(BorderFactory.createTitledBorder("Ajouter un encadrant"));

        
        JPanel nomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        nomPanel.add(new JLabel("Nom:"));
        nomPanel.add(nomField);
        Formulaire.add(nomPanel);

        JPanel prenomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        prenomPanel.add(new JLabel("Prénom:"));
        prenomPanel.add(prenomField);
        Formulaire.add(prenomPanel);

        JPanel telPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        telPanel.add(new JLabel("Téléphone:"));
        telPanel.add(telField);
        Formulaire.add(telPanel);

        JPanel emailPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        emailPanel.add(new JLabel("Email:"));
        emailPanel.add(emailField);
        Formulaire.add(emailPanel);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JPanel buttonsPanelLeft = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton ajouterButton = new JButton("Ajouter");
        ajouterButton.addActionListener(e -> {
            String nom = nomField.getText();
            String prenom = prenomField.getText();
            String tel = telField.getText();
            String email = emailField.getText();

            if (nom.isEmpty() || prenom.isEmpty() || tel.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Veuillez remplir tous les champs.", "Erreur d'ajout", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int nouvelId = genererNouvelId();
            Encadrant nouvelEncadrant = new Encadrant(nouvelId, nom, prenom, tel, email);
            encadrantDAO.ajouterEncadrant(nouvelEncadrant);

            JOptionPane.showMessageDialog(frame, "Encadrant ajouté avec succès !");
            actualiserTable();
            resetFields();
        });
        buttonsPanel.add(ajouterButton);
        
        
        

        JButton quitterButton = new JButton("Quitter");
        quitterButton.addActionListener(e -> {
            frame.dispose();
        });
        buttonsPanelLeft.add(quitterButton);

        JButton actualiserButton = new JButton("Actualiser");
        actualiserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualiserTable();
            }
        });
        buttonsPanelLeft.add(actualiserButton);

        JButton suppButton = new JButton("Supprimer");
        suppButton.addActionListener(e -> {
            int selectedRow = encadrantTable.getSelectedRow();
            if (selectedRow >= 0) {
                int option = JOptionPane.showConfirmDialog(
                        frame, "Etes-vous sûr de supprimer cet encadrant ?", "Confirmation",
                        JOptionPane.YES_NO_OPTION);

                if (option == JOptionPane.YES_OPTION) {
                    Object idObj = encadrantTable.getValueAt(selectedRow, 0);
                    if (idObj instanceof Integer) {
                        int id = (int) idObj;

                        encadrantDAO.supprimerStageParIdEncadrant(id);
                        encadrantDAO.supprimerStagesEtudiantParIdEncadrant(id);
                        encadrantDAO.supprimerEncadrant(id);

                        JOptionPane.showMessageDialog(frame, "Encadrant supprimé avec succès !");
                        actualiserTable();
                        resetFields();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Veuillez sélectionner un encadrant à supprimer.");
            }
        });
        buttonsPanelLeft.add(suppButton);

        encadrantTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = encadrantTable.getSelectedRow();
            if (selectedRow >= 0) {
                Object idObj = encadrantTable.getValueAt(selectedRow, 0);
                if (idObj instanceof Integer) {
                    int id = (int) idObj;
                    Encadrant encadrant = encadrantDAO.trouverEncadrantParId(id);

                    if (encadrant != null) {
                        idField.setText(Integer.toString(encadrant.getId()));
                        nomField.setText(encadrant.getNom());
                        prenomField.setText(encadrant.getPrenom());
                        telField.setText(encadrant.getTel());
                        emailField.setText(encadrant.getEmail());
                    }
                }
            }
        });

        JButton modifierButton = new JButton("Modifier");
        modifierButton.addActionListener(e -> {
            int selectedRow = encadrantTable.getSelectedRow();
            if (selectedRow >= 0) {
                int id = Integer.parseInt(idField.getText());
                String nom = nomField.getText();
                String prenom = prenomField.getText();
                String tel = telField.getText();
                String email = emailField.getText();

                Encadrant encadrantModifie = new Encadrant(id, nom, prenom, tel, email);
                encadrantDAO.mettreAJourEncadrant(encadrantModifie);

                JOptionPane.showMessageDialog(frame, "Encadrant modifié avec succès !");
                actualiserTable();
                resetFields();
            } else {
                JOptionPane.showMessageDialog(frame, "Veuillez sélectionner un encadrant à modifier.");
            }
        });

        buttonsPanel.add(modifierButton);

        Formulaire.add(buttonsPanel);
        rightPanel.add(Formulaire);

        leftPanel.add(buttonsPanelLeft);

        globalPanel.add(leftPanel, BorderLayout.CENTER);
        globalPanel.add(rightPanel, BorderLayout.EAST);

        actualiserTable();
        frame.setContentPane(globalPanel);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }

    private int genererNouvelId() {
        List<Encadrant> encadrants = encadrantDAO.getListeEncadrants();
        if (encadrants.isEmpty()) {
            return 1;
        } else {
            int dernierId = encadrants.get(encadrants.size() - 1).getId();
            return dernierId + 1;
        }
    }

	private void actualiserTable() {
        List<Encadrant> encadrants = encadrantDAO.getListeEncadrants();
        tableModel.setRowCount(0);
        for (Encadrant encadrant : encadrants) {
            tableModel.addRow(new Object[]{encadrant.getId(), encadrant.getNom(), encadrant.getPrenom(), encadrant.getTel(), encadrant.getEmail()});
        }
    }

    private void resetFields() {
        idField.setText("");
        nomField.setText("");
        prenomField.setText("");
        telField.setText("");
        emailField.setText("");
    }


}
