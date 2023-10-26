package com.epg.java.app;


import javax.swing.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.table.*;
import com.epg.java.models.Encadrant;
import com.epg.java.models.Entreprise;
import com.epg.java.models.Etudiant;
import com.epg.java.models.Prof;
import com.epg.java.models.Stage;
import com.epg.java.models.Technologie;
import com.epg.java.modelsDAO.EncadrantDAO;
import com.epg.java.modelsDAO.EntrepriseDAO;
import com.epg.java.modelsDAO.EtudiantDAO;
import com.epg.java.modelsDAO.ProfDAO;
import com.epg.java.modelsDAO.StageDAO;
import com.epg.java.modelsDAO.TechnologieDAO;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MainApplication {
	

	

	
    private static StageDAO stageDAO = new StageDAO();
    private static EntrepriseDAO entrepriseDAO = new EntrepriseDAO();
    private static EtudiantDAO etudiantDAO = new EtudiantDAO();
    private static EncadrantDAO encadrantDAO = new EncadrantDAO();
    private static ProfDAO profDAO = new ProfDAO();
    private static TechnologieDAO technologieDAO =new TechnologieDAO();
    private static JComboBox<Entreprise> entrepriseComboBox;
    private static JTextField themeField;
    private static JComboBox<Encadrant> encadrantComboBox;
    private static JComboBox<Prof> profComboBox;
    private static JList<String> technologiesList = new JList<>();
    private static JList<Etudiant> etudiantsList   = new JList<>();;
    private static JList<Technologie> TechnoliesList;
	private static DefaultTableModel stageTableModel =new DefaultTableModel();
	private static JTable stageTable;



	
    public static void main(String[] args) {
    	
    	
    	
       
        
        JFrame frame = new JFrame("Gestion des Stagaires");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);
        JPanel globalPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

      

      
        
	        JPanel buttonPanel = new JPanel();
	        globalPanel.add(buttonPanel);
            

            JButton addButton = new JButton("Espace Entreprise");
            buttonPanel.add(addButton);
            
            addButton.addActionListener(e -> {
                
                 
                    EspaceEntrepriseApp app = new EspaceEntrepriseApp();
                    app.AjouterEntreprise();
                
            });

            
            JButton etudiantButon=new JButton("Espace Etudiant ");
            buttonPanel.add(etudiantButon);
            etudiantButon.addActionListener(e -> { 
            	EspaceEtudiantApp aje=new EspaceEtudiantApp();
            	aje.AjouterEtudiant();
            	
            });
            
            JButton encadrantButon=new JButton("espace Encadrant");
            buttonPanel.add(encadrantButon);
            encadrantButon.addActionListener(e -> { 
            	EspaceEncadrantApp ajen=new EspaceEncadrantApp();
            	ajen.AjouterEncadrant();
            	
            });
            
            JButton profButon=new JButton("espace Prof");
            buttonPanel.add(profButon);
            profButon.addActionListener(e -> { 
            	EspaceProfApp esP=new EspaceProfApp();
            	esP.AjouterProf();
            	
            });
            
            
            JButton DomaineButon=new JButton("espace Domaines Activite");
            buttonPanel.add(DomaineButon);
            DomaineButon.addActionListener(e -> { 
            	EspaceDomaineActiviteApp esP=new EspaceDomaineActiviteApp();
            	esP.EspaceDomaineActivite();
            	
            });
            
            
            JButton TechnoButon=new JButton("espace Technologies");
            buttonPanel.add(TechnoButon);
            TechnoButon.addActionListener(e -> { 
            	EspaceTechnologiesApp st =new EspaceTechnologiesApp();
            	st.EspaceTechnologies();
            	
            });
            
            
            frame.add(globalPanel);
            
            
            JPanel tablePanel = new JPanel();
            tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
            globalPanel.add(tablePanel);

            stageTableModel = new DefaultTableModel();
                     
            stageTableModel.addColumn("ID");
            stageTableModel.addColumn("Theme");
            stageTableModel.addColumn("Entreprise");
            stageTableModel.addColumn("Etudiant");
            stageTableModel.addColumn("Encadrant Entreprise");
            stageTableModel.addColumn("Prof Encadrant");
            stageTableModel.addColumn("Technologies Demandées");


            stageTable = new JTable(stageTableModel);
            JScrollPane tableScrollPane = new JScrollPane(stageTable);
            stageTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            stageTable.getColumnModel().getColumn(0).setMinWidth(0);
            stageTable.getColumnModel().getColumn(0).setMaxWidth(0);
            
            tablePanel.add(tableScrollPane, BorderLayout.CENTER);
            actualiserTable(stageTableModel);
            
            TableColumn ThemeColumn = stageTable.getColumnModel().getColumn(1); 
            ThemeColumn.setPreferredWidth(250); 
            
            TableColumn EntreColumn = stageTable.getColumnModel().getColumn(2); 
            EntreColumn.setPreferredWidth(120); 
            
            TableColumn EtudColumn = stageTable.getColumnModel().getColumn(3); 
            EtudColumn.setPreferredWidth(270); 
            
            TableColumn EncColumn = stageTable.getColumnModel().getColumn(4); 
            EncColumn.setPreferredWidth(120); 
            
            TableColumn profColumn = stageTable.getColumnModel().getColumn(5); 
            profColumn.setPreferredWidth(120); 
            
            TableColumn TechnoColumn = stageTable.getColumnModel().getColumn(6); 
            TechnoColumn.setPreferredWidth(200);
            

            // Panel pour le formulaire d'ajout 
            JPanel formPanel = new JPanel();
            formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
            formPanel.setBorder(BorderFactory.createTitledBorder("Ajouter Stage ->"));
            globalPanel.add(formPanel);
            
            JPanel entrepriseP = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            List<Entreprise> entreprises = entrepriseDAO.getEntreprises();
            entrepriseComboBox = new JComboBox<>(entreprises.toArray(new Entreprise[0]));
            entrepriseComboBox.setPreferredSize(new Dimension(300, entrepriseComboBox.getPreferredSize().height));
            entrepriseP.add(new JLabel("Entreprise:"));
            entrepriseP.add(entrepriseComboBox);
            formPanel.add(entrepriseP);


            JPanel themeFieldP = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            themeField = new JTextField(27);
            themeFieldP.add(new JLabel("Theme:"));
            themeFieldP.add(themeField);
            formPanel.add(themeFieldP);
            

            JPanel etudiantsP = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            List<Etudiant> etudiants = etudiantDAO.getListeEtudiants();
            etudiantsList = new JList<>(etudiants.toArray(new Etudiant[0]));
            etudiantsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            etudiantsList.setVisibleRowCount(3);
            JScrollPane etudiantsScrollPane = new JScrollPane(etudiantsList);
            etudiantsP.add(new JLabel("Etudiants:"));
            etudiantsP.add(etudiantsScrollPane);
            formPanel.add(etudiantsP);


            
            JPanel encadrantsP = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            List<Encadrant> encadrants = encadrantDAO.getListeEncadrants();
            encadrantComboBox = new JComboBox<>(encadrants.toArray(new Encadrant[0]));
            encadrantComboBox.setPreferredSize(new Dimension(300, encadrantComboBox.getPreferredSize().height));
            encadrantsP.add(new JLabel("Encadrant:"));
            encadrantsP.add(encadrantComboBox);
            formPanel.add(encadrantsP);
            
            

            JPanel profsP = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            List<Prof> profs = profDAO.getListeProfs();
            profComboBox = new JComboBox<>(profs.toArray(new Prof[0]));
            profComboBox.setPreferredSize(new Dimension(300, profComboBox.getPreferredSize().height));
            profsP.add(new JLabel("Prof:"));
            profsP.add(profComboBox);
            formPanel.add(profsP);
            
            JPanel technologiesListModelP = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            List<Technologie> technos = technologieDAO.getListeTechnologies();
            TechnoliesList = new JList<>(technos.toArray(new Technologie[0]));
            TechnoliesList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            TechnoliesList.setVisibleRowCount(5);
            JScrollPane TechnoScrollPane = new JScrollPane(TechnoliesList);
            technologiesListModelP.add(new JLabel("Technologies :"));
            technologiesListModelP.add(TechnoScrollPane);
            formPanel.add(technologiesListModelP);
            
            
			 
		

            JPanel ajouterButtonP = new JPanel(new FlowLayout(FlowLayout.CENTER));
            
            
            JButton ajouterButton = new JButton("Ajouter");
            
            ajouterButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ajouterStage();
                    actualiserTable(stageTableModel);
                }
            });
            
            ajouterButtonP.add(ajouterButton);
            formPanel.add(ajouterButtonP);
            
            
            FlowLayout fl= new FlowLayout(FlowLayout.CENTER);
            JPanel pa=new JPanel(fl);
            
            
            JButton actualiser=new JButton("Actualiser");
            actualiser.addActionListener(e -> {
            	 actualiserTable(stageTableModel);
            	 actualiserComboBoxesEtList();
            	 resetFields();
            });
            pa.add(actualiser);
            
            
            JButton quitte=new JButton("Quitter");
            quitte.addActionListener(f ->{
            	System.exit(0);
            });
            pa.add(quitte);
            tablePanel.add(pa);
            
            
            
            JButton supprimerButton = new JButton("Supprimer");
            supprimerButton.addActionListener(e -> {
               
                    int selectedRow = stageTable.getSelectedRow();
                    if (selectedRow != -1) {
                        int selectedStageId = (int) stageTable.getValueAt(selectedRow, 0);
                        
                        int rep = JOptionPane.showConfirmDialog(
                            null,
                            "Êtes-vous sûr de vouloir supprimer ce stage ?",
                            "Confirmation de suppression",
                            JOptionPane.YES_NO_OPTION
                        );
                        
                        if (rep == JOptionPane.YES_OPTION) {
                            stageDAO.supprimerStageParId(selectedStageId);
                            JOptionPane.showMessageDialog(null, "stage supprimé avec succès");
                            actualiserTable(stageTableModel);
                            
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Veuillez sélectionner un stage à supprimer.");
                    }
                
            });
            pa.add(supprimerButton);


            
            stageTable.getSelectionModel().addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = stageTable.getSelectedRow();
                    if (selectedRow >= 0) {
                        int selectedStageId = (int) stageTable.getValueAt(selectedRow, 0);
                        Stage selectedStage = stageDAO.trouverStageParId(selectedStageId);
                        remplirChampsAvecStage(selectedStage);
                    }
                }
            });




            
            JButton modifierButton = new JButton("Modifier");
            modifierButton.addActionListener(e -> {
                modifierStage();
                actualiserTable(stageTableModel);
                actualiserComboBoxesEtList();
            });

            ajouterButtonP.add(modifierButton);

           
            

            frame.setVisible(true);
        }
    
    

    private static void ajouterStage() {
        Entreprise selectedEntreprise = (Entreprise) entrepriseComboBox.getSelectedItem();
        String theme = themeField.getText();
        List<Etudiant> selectedEtudiants = etudiantsList.getSelectedValuesList();

        Encadrant selectedEncadrant = (Encadrant) encadrantComboBox.getSelectedItem();
        Prof selectedProf = (Prof) profComboBox.getSelectedItem();
        
        List<Technologie> selectedTechnologies = TechnoliesList.getSelectedValuesList();

        Stage nouveauStage = new Stage();
        nouveauStage.setEntreprise(selectedEntreprise);
        nouveauStage.setTheme(theme);
        nouveauStage.setEtudiants(selectedEtudiants);
        nouveauStage.setEncadrantEntreprise(selectedEncadrant);
        nouveauStage.setProfEncadrant(selectedProf);
        
        List<String> technologyNames = selectedTechnologies.stream()
            .map(Technologie::getNom)
            .collect(Collectors.toList());
        nouveauStage.setTechnologiesDemandees(technologyNames);

        stageDAO.ajouterStage(nouveauStage);

        actualiserTable(stageTableModel);

        JOptionPane.showMessageDialog(null, "Stage ajouté avec succès !");
    }



    private static void modifierStage() {
        int selectedRow = stageTable.getSelectedRow();
        if (selectedRow >= 0) {
            int selectedStageId = (int) stageTable.getValueAt(selectedRow, 0);
            Stage selectedStage = stageDAO.trouverStageParId(selectedStageId);

            selectedStage.setEntreprise((Entreprise) entrepriseComboBox.getSelectedItem());
            selectedStage.setTheme(themeField.getText());
            selectedStage.setEtudiants(etudiantsList.getSelectedValuesList());
            selectedStage.setEncadrantEntreprise((Encadrant) encadrantComboBox.getSelectedItem());
            selectedStage.setProfEncadrant((Prof) profComboBox.getSelectedItem());
            
            List<Technologie> selectedTechnologies = TechnoliesList.getSelectedValuesList();
            
            List<String> technologyNames = selectedTechnologies.stream()
                .map(Technologie::getNom)
                .collect(Collectors.toList());
            selectedStage.setTechnologiesDemandees(technologyNames);

            stageDAO.modifierStage(selectedStage);
            JOptionPane.showMessageDialog(null, "Stage modifié avec succès !");
        } else {
            JOptionPane.showMessageDialog(null, "Veuillez sélectionner un stage à modifier.");
        }
    }





    private static void actualiserTable(DefaultTableModel tableModel) {
        List<Stage> stages = stageDAO.getListeStages();
        
        tableModel.setRowCount(0);
        
        for (Stage stage : stages) {
            StringBuilder etudiantsString = new StringBuilder();
            if (stage.getEtudiants() != null) {
                for (Etudiant etudiant : stage.getEtudiants()) {
                    etudiantsString.append(etudiant.getPrenom()).append(" ").append(etudiant.getNom()).append(", ");
                }
                if (etudiantsString.length() > 0) {
                    etudiantsString.setLength(etudiantsString.length() - 2);
                }
            }

            String technologiesString = stage.getTechnologiesDemandees() != null ? stage.getTechnologiesDemandees().toString() : "";


            
            tableModel.addRow(new Object[]{
                stage.getId(),
                stage.getTheme(),
                stage.getEntreprise().getRaisonSociale(),
                etudiantsString.toString(),
                stage.getEncadrantEntreprise().getPrenom() + " " + stage.getEncadrantEntreprise().getNom(),
                stage.getProfEncadrant().getPrenom() + " " + stage.getProfEncadrant().getNom(),
                technologiesString
            });
        }
    }




    private static void actualiserComboBoxesEtList() {
        List<Entreprise> entreprises = entrepriseDAO.getEntreprises();
        entrepriseComboBox.setModel(new DefaultComboBoxModel<>(entreprises.toArray(new Entreprise[0])));

        List<Encadrant> encadrants = encadrantDAO.getListeEncadrants();
        encadrantComboBox.setModel(new DefaultComboBoxModel<>(encadrants.toArray(new Encadrant[0])));

        List<Prof> profs = profDAO.getListeProfs();
        profComboBox.setModel(new DefaultComboBoxModel<>(profs.toArray(new Prof[0])));
        
        List<Etudiant> etudiants = etudiantDAO.getListeEtudiants();
        etudiantsList.setListData(etudiants.toArray(new Etudiant[0]));
        


        List<Technologie> technos = technologieDAO.getListeTechnologies();
        TechnoliesList.setListData(technos.toArray(new Technologie[0]));


    }


    
    private static void remplirChampsAvecStage(Stage stage) {
        entrepriseComboBox.setSelectedItem(stage.getEntreprise());
        themeField.setText(stage.getTheme());

        List<Etudiant> etudiants = etudiantDAO.getListeEtudiants();
        etudiantsList.setListData(etudiants.toArray(new Etudiant[0]));
        
        for (int i = 0; i < etudiants.size(); i++) {
            if (stage.getEtudiants().contains(etudiants.get(i))) {
                etudiantsList.addSelectionInterval(i, i);
            }
        }

        encadrantComboBox.setSelectedItem(stage.getEncadrantEntreprise());
        profComboBox.setSelectedItem(stage.getProfEncadrant());

        List<Technologie> technos = technologieDAO.getListeTechnologies();
        TechnoliesList.setListData(technos.toArray(new Technologie[0]));

        DefaultListModel<String> technologiesListModel = new DefaultListModel<>();
        technologiesList.setModel(technologiesListModel);
        for (String technologie : stage.getTechnologiesDemandees()) {
            technologiesListModel.addElement(technologie);
        }
    }

    
    
    
    
    private static void resetFields() {
        themeField.setText("");
    }

}


