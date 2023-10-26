package com.epg.java.app;

import com.epg.java.models.*;
import com.epg.java.modelsDAO.EtudiantDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;

public class EspaceEtudiantApp {

    private EtudiantDAO etudiantDAO = new EtudiantDAO();

    private JTextField cneField = new JTextField(15);
    private JTextField nomField = new JTextField(15);
    private JTextField prenomField = new JTextField(15);
    private JTextField dateNaissanceField = new JTextField(15);

    private DefaultTableModel tableModel;

    public void AjouterEtudiant(){
        JFrame frame = new JFrame("Gestion des étudiants");
        frame.setSize(800, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel globalPanel = new JPanel(new BorderLayout());

        JPanel leftPanel = new JPanel(new FlowLayout());
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        JPanel rightPanel = new JPanel(new FlowLayout());

        // Tableau
        tableModel = new DefaultTableModel();
        tableModel.addColumn("CNE");
        tableModel.addColumn("Nom");
        tableModel.addColumn("Prénom");
        tableModel.addColumn("Date de Naissance");

        JTable etudiantTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(etudiantTable);
        leftPanel.add(tableScrollPane);

        // Formulaire d'ajout
        JPanel Formulaire = new JPanel();
        Formulaire.setLayout(new BoxLayout(Formulaire, BoxLayout.Y_AXIS));
        Formulaire.setBorder(BorderFactory.createTitledBorder("Ajouter un étudiant"));

        JPanel cnePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        cnePanel.add(new JLabel("CNE:"));
        cnePanel.add(cneField);
        Formulaire.add(cnePanel);

        JPanel nomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        nomPanel.add(new JLabel("Nom:"));
        nomPanel.add(nomField);
        Formulaire.add(nomPanel);

        JPanel prenomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        prenomPanel.add(new JLabel("Prénom:"));
        prenomPanel.add(prenomField);
        Formulaire.add(prenomPanel);

        JPanel dateNaissancePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        dateNaissancePanel.add(new JLabel("Date de Naissance (yyyy-mm-dd):"));
        dateNaissancePanel.add(dateNaissanceField);
        Formulaire.add(dateNaissancePanel);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JPanel buttonsPanelLeft = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        JButton ajouterButton = new JButton("Ajouter");
        ajouterButton.addActionListener(e -> {
            String cneStr = cneField.getText();
            String nom = nomField.getText();
            String prenom = prenomField.getText();
            String dateNaissanceStr = dateNaissanceField.getText();

            if (cneStr.isEmpty() || nom.isEmpty() || prenom.isEmpty() || dateNaissanceStr.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Veuillez remplir tous les champs.", "Champs vides", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int cne = Integer.parseInt(cneStr);
                Date dateNaissance = java.sql.Date.valueOf(dateNaissanceStr);

                if (etudiantDAO.etudiantExiste(cne)) {
                    JOptionPane.showMessageDialog(frame, "Un étudiant avec le même CNE existe déjà.", "Erreur d'ajout", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Etudiant nouvelEtudiant = new Etudiant(cne, nom, prenom, dateNaissance);
                etudiantDAO.ajouterEtudiant(nouvelEtudiant);

                JOptionPane.showMessageDialog(frame, "Étudiant ajouté avec succès !");
                actualiserTable();
                resetFields();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Veuillez entrer un CNE valide.", "Erreur de format", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(frame, "Veuillez entrer une date de naissance valide (format : yyyy-mm-dd).", "Erreur de format", JOptionPane.ERROR_MESSAGE);
            }
        });



        buttonsPanel.add(ajouterButton); 

        JButton quitterButton = new JButton("Quitter");
        quitterButton.addActionListener( e-> {
            
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
        
        
        JButton suppButton =new JButton("Suppreimer");
    
        suppButton.addActionListener(e -> {
            int selectedRow = etudiantTable.getSelectedRow();
            if (selectedRow >= 0) {
                int option = JOptionPane.showConfirmDialog(
                    frame, "Etes-vous sûr de supprimer cet étudiant ?", "Confirmation",
                    JOptionPane.YES_NO_OPTION);

                if (option == JOptionPane.YES_OPTION) {
                    Object cneObj = etudiantTable.getValueAt(selectedRow, 0);
                    if (cneObj instanceof Integer) {
                        int cne = (int) cneObj;

                        etudiantDAO.supprimerEtudiant(cne);

                        JOptionPane.showMessageDialog(frame, "Étudiant supprimé avec succès !");
                        actualiserTable();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Veuillez sélectionner un étudiant à supprimer.");
            }
        });
        buttonsPanelLeft.add(suppButton);

        
        
        etudiantTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = etudiantTable.getSelectedRow();
            if (selectedRow >= 0) {
                Object cneObj = etudiantTable.getValueAt(selectedRow, 0);
                if (cneObj instanceof Integer) {
                    int cne = (int) cneObj;
                    Etudiant etudiant = etudiantDAO.trouverEtudiantParCne(cne);

                    if (etudiant != null) {
                        cneField.setText(Integer.toString(etudiant.getCne()));
                        nomField.setText(etudiant.getNom());
                        prenomField.setText(etudiant.getPrenom());
                        dateNaissanceField.setText(etudiant.getDateNaissance().toString());
                    }
                }
            }
        });

        JButton modifierButton = new JButton("Modifier");
        modifierButton.addActionListener(e -> {
            int selectedRow = etudiantTable.getSelectedRow();
            if (selectedRow >= 0) {
                int cne = Integer.parseInt(cneField.getText());
                String nom = nomField.getText();
                String prenom = prenomField.getText();
                Date dateNaissance = java.sql.Date.valueOf(dateNaissanceField.getText());

                Etudiant etudiantModifie = new Etudiant(cne, nom, prenom, dateNaissance);
                etudiantDAO.mettreAJourEtudiant(etudiantModifie);

                JOptionPane.showMessageDialog(frame, "Étudiant modifié avec succès !");
                actualiserTable();
                resetFields();
            } else {
                JOptionPane.showMessageDialog(frame, "Veuillez sélectionner un étudiant à modifier.");
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

    private void actualiserTable(){
        List<Etudiant> etudiants = etudiantDAO.getListeEtudiants();
        tableModel.setRowCount(0);
        for (Etudiant etudiant : etudiants) {
            tableModel.addRow(new Object[]{etudiant.getCne(), etudiant.getNom(), etudiant.getPrenom(), etudiant.getDateNaissance()});
        }
    }

    private void resetFields() {
        cneField.setText("");
        nomField.setText("");
        prenomField.setText("");
        dateNaissanceField.setText("");
    }


}
