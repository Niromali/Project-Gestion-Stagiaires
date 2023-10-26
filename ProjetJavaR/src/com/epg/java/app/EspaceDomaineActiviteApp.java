package com.epg.java.app;

import com.epg.java.models.*;
import com.epg.java.modelsDAO.DomaineActiviteDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class EspaceDomaineActiviteApp {

    private DomaineActiviteDAO domaineActiviteDAO = new DomaineActiviteDAO();

    private JTextField nomField = new JTextField(15);

    private DefaultTableModel tableModel;

    public void EspaceDomaineActivite() {
        JFrame frame = new JFrame("Gestion des domaines d'activité");
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

        JTable domaineTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(domaineTable);
        leftPanel.add(tableScrollPane);
        
        domaineTable.getColumnModel().getColumn(0).setMinWidth(0);
        domaineTable.getColumnModel().getColumn(0).setMaxWidth(0);

        // Formulaire
        JPanel Formulaire = new JPanel();
        Formulaire.setLayout(new BoxLayout(Formulaire, BoxLayout.Y_AXIS));
        Formulaire.setBorder(BorderFactory.createTitledBorder("Ajouter un domaine d'activité"));

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
                JOptionPane.showMessageDialog(frame, "Veuillez entrer un nom pour le domaine d'activité.", "Erreur d'ajout", JOptionPane.ERROR_MESSAGE);
                return;
            }

            DomaineActivite nouveauDomaine = new DomaineActivite();
            nouveauDomaine.setNom(nom);
            domaineActiviteDAO.ajouterDomaineActivite(nouveauDomaine);

            JOptionPane.showMessageDialog(frame, "Domaine d'activité ajouté avec succès !");
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
            int selectedRow = domaineTable.getSelectedRow();
            if (selectedRow >= 0) {
                int option = JOptionPane.showConfirmDialog(
                        frame, "Etes-vous sûr de supprimer ce domaine d'activité ?", "Confirmation",
                        JOptionPane.YES_NO_OPTION);

                if (option == JOptionPane.YES_OPTION) {
                    Object idObj = domaineTable.getValueAt(selectedRow, 0);
                    if (idObj instanceof Integer) {
                        int id = (int) idObj;

                        domaineActiviteDAO.supprimerDomaineActiviteParId(id);

                        JOptionPane.showMessageDialog(frame, "Domaine d'activité supprimé avec succès !");
                        actualiserTable();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Veuillez sélectionner un domaine d'activité à supprimer.");
            }
        });
        buttonsPanelLeft.add(supprimerButton);

        domaineTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = domaineTable.getSelectedRow();
            if (selectedRow >= 0) {
                Object idObj = domaineTable.getValueAt(selectedRow, 0);
                if (idObj instanceof Integer) {
                    int id = (int) idObj;
                    DomaineActivite domaine = domaineActiviteDAO.trouverDomaineActiviteParId(id);

                    if (domaine != null) {
                        nomField.setText(domaine.getNom());
                    }
                }
            }
        });

        JButton modifierButton = new JButton("Modifier");
        modifierButton.addActionListener(e -> {
            int selectedRow = domaineTable.getSelectedRow();
            if (selectedRow >= 0) {
                Object idObj = domaineTable.getValueAt(selectedRow, 0);
                if (idObj instanceof Integer) {
                    int id = (int) idObj;
                    String nouveauNom = nomField.getText();

                    DomaineActivite domaineModifie = new DomaineActivite();
                    domaineModifie.setId(id);
                    domaineModifie.setNom(nouveauNom);

                    domaineActiviteDAO.mettreAJourDomaineActivite(domaineModifie);

                    JOptionPane.showMessageDialog(frame, "Domaine d'activité modifié avec succès !");
                    actualiserTable();
                    resetFields();
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Veuillez sélectionner un domaine d'activité à modifier.");
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
        List<DomaineActivite> domainesActivite = domaineActiviteDAO.getListeDomainesActivite();
        tableModel.setRowCount(0);
        for (DomaineActivite domaine : domainesActivite) {
            tableModel.addRow(new Object[]{domaine.getId(), domaine.getNom()});
        }
    }

    private void resetFields() {
        nomField.setText("");
    }
}
