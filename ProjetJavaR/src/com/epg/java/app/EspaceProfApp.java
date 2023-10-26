package com.epg.java.app;

import com.epg.java.models.*;
import com.epg.java.modelsDAO.EncadrantDAO;
import com.epg.java.modelsDAO.ProfDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class EspaceProfApp {

    private ProfDAO ProfDAO = new ProfDAO();

    private JTextField idField = new JTextField(15);
    private JTextField nomField = new JTextField(15);
    private JTextField prenomField = new JTextField(15);
    private JTextField telField = new JTextField(15);
    private JTextField emailField = new JTextField(15);

    private DefaultTableModel tableModel;

    public void AjouterProf() {
        JFrame frame = new JFrame("Gestion des Profs");
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

        JTable profTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(profTable);
        leftPanel.add(tableScrollPane);
        
        profTable.getColumnModel().getColumn(0).setMinWidth(0);
        profTable.getColumnModel().getColumn(0).setMaxWidth(0);
        
        
        TableColumn domaineActiviteColumn = profTable.getColumnModel().getColumn(4); 
        domaineActiviteColumn.setPreferredWidth(200); 

        // Formulaire
        JPanel Formulaire = new JPanel();
        Formulaire.setLayout(new BoxLayout(Formulaire, BoxLayout.Y_AXIS));
        Formulaire.setBorder(BorderFactory.createTitledBorder("Ajouter un Prof"));

       

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
            Prof nouvelProf = new Prof(nouvelId, nom, prenom, tel, email);
            ProfDAO.ajouterProf(nouvelProf);

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
        actualiserButton.addActionListener(e -> {
            
                actualiserTable();
            
        });
        buttonsPanelLeft.add(actualiserButton);

        JButton suppButton = new JButton("Supprimer");
        suppButton.addActionListener(e -> {
            int selectedRow = profTable.getSelectedRow();
            if (selectedRow >= 0) {
                int option = JOptionPane.showConfirmDialog(
                        frame, "Etes-vous sûr de supprimer cet prof ?", "Confirmation",
                        JOptionPane.YES_NO_OPTION);

                if (option == JOptionPane.YES_OPTION) {
                    Object idObj = profTable.getValueAt(selectedRow, 0);
                    if (idObj instanceof Integer) {
                        int id = (int) idObj;

                        ProfDAO.supprimerStageParIdProf(id);
                        ProfDAO.supprimerStagesEtudiantParIdProf(id);
                        ProfDAO.supprimerProf(id);

                        JOptionPane.showMessageDialog(frame, "Prof supprimé avec succès !");
                        actualiserTable();
                        resetFields();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Veuillez sélectionner un prof à supprimer.");
            }
        });
        buttonsPanelLeft.add(suppButton);


        profTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = profTable.getSelectedRow();
            if (selectedRow >= 0) {
                Object idObj = profTable.getValueAt(selectedRow, 0);
                if (idObj instanceof Integer) {
                    int id = (int) idObj;
                    Prof prof = ProfDAO.trouverProfParId(id);

                    if (prof != null) {
                        idField.setText(Integer.toString(prof.getId()));
                        nomField.setText(prof.getNom());
                        prenomField.setText(prof.getPrenom());
                        telField.setText(prof.getTel());
                        emailField.setText(prof.getEmail());
                    }
                }
            }
        });

        JButton modifierButton = new JButton("Modifier");
        modifierButton.addActionListener(e -> {
            int selectedRow = profTable.getSelectedRow();
            if (selectedRow >= 0) {
                int id = Integer.parseInt(idField.getText());
                String nom = nomField.getText();
                String prenom = prenomField.getText();
                String tel = telField.getText();
                String email = emailField.getText();

                Prof profModifie = new Prof(id, nom, prenom, tel, email);
                ProfDAO.mettreAJourProf(profModifie);

                JOptionPane.showMessageDialog(frame, "Prof modifié avec succès !");
                actualiserTable();
                resetFields();
            } else {
                JOptionPane.showMessageDialog(frame, "Veuillez sélectionner un Prof à modifier.");
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
        List<Prof> profs = ProfDAO.getListeProfs();
        if (profs.isEmpty()) {
            return 1;
        } else {
            int dernierId = profs.get(profs.size() - 1).getId();
            return dernierId + 1;
        }
    }

	private void actualiserTable() {
        List<Prof> profs = ProfDAO.getListeProfs();
        tableModel.setRowCount(0);
        for (Prof prof : profs) {
            tableModel.addRow(new Object[]{prof.getId(), prof.getNom(), prof.getPrenom(), prof.getTel(), prof.getEmail()});
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
