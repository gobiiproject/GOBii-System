package edu.cornell.gobii.gdi.main;

import java.util.Set;
import java.util.Map.Entry;

import org.gobiiproject.gobiiclient.core.ClientContext;
import org.gobiiproject.gobiimodel.types.SystemUserDetail;
import org.gobiiproject.gobiimodel.types.SystemUserNames;
import org.gobiiproject.gobiimodel.types.SystemUsers;
import org.apache.commons.lang3.SystemUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.forms.widgets.ColumnLayout;

import edu.cornell.gobii.gdi.forms.FrmAnalyses;
import edu.cornell.gobii.gdi.forms.FrmCV;
import edu.cornell.gobii.gdi.forms.FrmContacts;
import edu.cornell.gobii.gdi.forms.FrmDatasets;
import edu.cornell.gobii.gdi.forms.FrmExperiments;
import edu.cornell.gobii.gdi.forms.FrmMapset;
import edu.cornell.gobii.gdi.forms.FrmMarkerGroups;
import edu.cornell.gobii.gdi.forms.FrmPlatforms;
import edu.cornell.gobii.gdi.forms.FrmProjects;
import edu.cornell.gobii.gdi.forms.FrmReferences;
import edu.cornell.gobii.gdi.forms.FrmTableDisplay;
import edu.cornell.gobii.gdi.services.Controller;
import edu.cornell.gobii.gdi.services.IDs;
import edu.cornell.gobii.gdi.utils.WizardUtils;
import swing2swt.layout.BorderLayout;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

public class Main {

	protected Shell shell;
	protected Display display;
	private CTabFolder tabContent;
	private Button btnProj;
	private Button btnPlatformExp;
	private Button btnDS;
	private Button btnCV;
	private Button btnAnalysis;
	private Button btnReferences;
	private Button btnContacts;
	private Button btnTableDisplay;
	private Button btnMarkerGroups;
	private Button btnMarkerWizard;
	private Button btnDNAsampleWizard;
	private Button btnDatasetWizard;
	
	private static String config;
	private Label lblWizards;
	private Label label;
	private Tree tree;
	private Label lblManage;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Main window = new Main();
			window.open();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
//		config = getClass().getProtectionDomain().getCodeSource().getLocation().getPath()+"/../config";
		config = System.getProperty("user.dir")+"/config";
		display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setMinimumSize(new Point(1280, 800));
//		shell.setSize(1024, 800);
		shell.setText("GOBII :: Genomic Data Integration");
		shell.setLayout(new BorderLayout(0, 0));
		
		createMenu();
		createHeader();
		createContent();
	}

	private void createMenu(){
		Composite cmpMenu = new Composite(shell, SWT.NONE);
		cmpMenu.setLayoutData(BorderLayout.WEST);
		
		CTabFolder tabMenu = new CTabFolder(cmpMenu, SWT.BORDER);
		tabMenu.setBorderVisible(false);
		tabMenu.setBounds(0, 0, 200, 600);
		tabMenu.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		
		CTabItem tbtmMenu = new CTabItem(tabMenu, SWT.NONE);
		tbtmMenu.setText("Menu");
		
		Composite composite_2 = new Composite(tabMenu, SWT.NONE);
		if(!SystemUtils.IS_OS_WINDOWS){
			composite_2.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_FOREGROUND));
		}
//		else{
//			shell.setSize(1280, 1024);
//		}
		tbtmMenu.setControl(composite_2);
		composite_2.setLayout(new ColumnLayout());
		
		btnProj = new Button(composite_2, SWT.NONE);
		btnProj.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FrmProjects frm = new FrmProjects(shell, tabContent, SWT.NONE, config);
				createContentTab(frm, "Projects");
			}
		});
		btnProj.setText("Projects");
		
		btnPlatformExp = new Button(composite_2, SWT.NONE);
		btnPlatformExp.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FrmExperiments frm = new FrmExperiments(shell, tabContent, SWT.NONE, config);
				createContentTab(frm, "Experiments");
				
			}
		});
		btnPlatformExp.setText("Platform Experiments");
		
		btnDS = new Button(composite_2, SWT.NONE);
		btnDS.setEnabled(true);
		btnDS.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FrmDatasets frm = new FrmDatasets(shell, tabContent, SWT.NONE, config);
				createContentTab(frm, "Datasets");
			}
		});
		btnDS.setText("Analyses");
		
		Button btnMaps = new Button(composite_2, SWT.NONE);
		btnMaps.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FrmMapset frm = new FrmMapset(shell, tabContent, SWT.NONE, config);
				createContentTab(frm, "Maps");
			}
		});
		btnMaps.setText("Mapsets");
		
		btnMarkerGroups = new Button(composite_2, SWT.NONE);
		btnMarkerGroups.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FrmMarkerGroups frm = new FrmMarkerGroups(shell, tabContent, SWT.NONE);
				createContentTab(frm, "Marker Groups");
			}
		});
		btnMarkerGroups.setText("Marker Groups");
		
		lblManage = new Label(composite_2, SWT.SEPARATOR | SWT.HORIZONTAL);
		lblManage.setText("Manage");
		
		btnAnalysis = new Button(composite_2, SWT.NONE);
		btnAnalysis.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FrmAnalyses frm = new FrmAnalyses(shell, tabContent, SWT.NONE, config);
				createContentTab(frm, "Analyses");
			}
		});
		btnAnalysis.setText("Manage Analyses");
		
		Button btnPlatforms = new Button(composite_2, SWT.NONE);
		btnPlatforms.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FrmPlatforms frm = new FrmPlatforms(shell, tabContent, SWT.NONE, config);
				createContentTab(frm, "Platforms");
			}
		});
		btnPlatforms.setText("Manage Platforms");
		
		btnCV = new Button(composite_2, SWT.NONE);
		btnCV.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FrmCV frm = new FrmCV(shell, tabContent, SWT.NONE);
				createContentTab(frm, "Controlled Vocabulary");
			}
		});
		btnCV.setText("Manage CVs");
		
		btnTableDisplay = new Button(composite_2, SWT.NONE);
		btnTableDisplay.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FrmTableDisplay frm = new FrmTableDisplay(shell, tabContent, SWT.NONE);
				createContentTab(frm, "Table Display");
			}
		});
		btnTableDisplay.setText("Manage Table Display");
		
		btnContacts = new Button(composite_2, SWT.NONE);
		btnContacts.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FrmContacts frm = new FrmContacts(shell, tabContent, SWT.NONE);
				createContentTab(frm, "Contacts");
			}
		});
		btnContacts.setText("Mange Contacts");
		
		btnReferences = new Button(composite_2, SWT.NONE);
		btnReferences.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FrmReferences frm = new FrmReferences(shell, tabContent, SWT.NONE);
				createContentTab(frm, "Reference Genomes");
			}
		});
		btnReferences.setText("Manage References");
		
		lblWizards = new Label(composite_2, SWT.SEPARATOR | SWT.HORIZONTAL);
		lblWizards.setText("Wizards");
		
		btnMarkerWizard = new Button(composite_2, SWT.FLAT);
		btnMarkerWizard.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN));
		btnMarkerWizard.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				WizardUtils.CreateMarkerWizard(shell, config);
			}
		});
		btnMarkerWizard.setText("Marker Wizard");
		
		btnDNAsampleWizard = new Button(composite_2, SWT.FLAT);
		btnDNAsampleWizard.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN));
		btnDNAsampleWizard.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				WizardUtils.createDNASampleWizard(shell, config);
			}
		});
		btnDNAsampleWizard.setText("DNA Sample Wizard");
		
		btnDatasetWizard = new Button(composite_2, SWT.FLAT);
		btnDatasetWizard.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN));
		btnDatasetWizard.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				WizardUtils.CreateDatasetWizard(shell, config);
			}
		});
		btnDatasetWizard.setText("Dataset Wizard");
		
		CTabItem tbtmProjects = new CTabItem(tabMenu, SWT.NONE);
		tbtmProjects.setText("Projects");
		
		Composite composite_1 = new Composite(tabMenu, SWT.NONE);
		tbtmProjects.setControl(composite_1);
		composite_1.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		tree = new Tree(composite_1, SWT.BORDER);
		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				Point point = new Point(e.x, e.y);
				TreeItem item = tree.getItem(point);
				Integer projId = item.getData("project") != null ? Integer.parseInt((String) item.getData("project").toString()) : null;
				Integer expId = item.getData("experiment") != null ? Integer.parseInt((String) item.getData("experiment").toString()) : null;
				Integer dsId = item.getData("dataset") != null ? Integer.parseInt((String) item.getData("dataset").toString()) : null;
				if(dsId != null && dsId > 0){
					// To nothing
				}else if(expId != null && expId > 0){
					FrmDatasets frm = new FrmDatasets(shell, tabContent, SWT.NONE, config);
					createContentTab(frm, "Datasets");
					IDs.experimentId = expId;
					frm.populateDatasetListFromSelectedExperiment(expId);
				}else if(projId != null && projId > 0){
					FrmExperiments frm = new FrmExperiments(shell, tabContent, SWT.NONE, config);
					createContentTab(frm, "Experiments");
					IDs.projectId = projId;
					frm.populateExperimentsListFromSelectedProject(projId);
				}
			}
			@Override
			public void mouseDown(MouseEvent e) {
				Point point = new Point(e.x, e.y);
				TreeItem item = tree.getItem(point);
				if(item == null || (boolean) item.getData("clicked")) return;
				Integer projId = item.getData("project") != null ? Integer.parseInt((String) item.getData("project").toString()) : null;
				Integer expId = item.getData("experiment") != null ? Integer.parseInt((String) item.getData("experiment").toString()) : null;
				Integer dsId = item.getData("dataset") != null ? Integer.parseInt((String) item.getData("dataset").toString()) : null;
				if(dsId != null && dsId > 0){
					// Do nothing
				}else if(expId != null && expId > 0){
					Set<Entry<String, String>> itemDS = Controller.getDataSetNamesByExperimentId(expId);
					for(Entry<String, String> entryDS : itemDS){
						TreeItem ds = new TreeItem(item, 0);
						ds.setText(entryDS.getValue());
						ds.setData("project", projId);
						ds.setData("experiment", expId);
						ds.setData("dataset", entryDS.getKey());
						ds.setData("clicked", true);
						ds.setBackground(0, display.getSystemColor(SWT.COLOR_GRAY));
					}
					item.setData("clicked", true);
				}else if(projId != null && projId > 0){
					Set<Entry<String, String>> itemExps = Controller.getExperimentNamesByProjectId(projId);
					for(Entry<String, String> entryExp : itemExps){
						TreeItem exp = new TreeItem(item, 0);
						exp.setText(entryExp.getValue());
						exp.setData("project", projId);
						exp.setData("experiment", entryExp.getKey());
						exp.setData("clicked", false);
						exp.setBackground(0, display.getSystemColor(SWT.COLOR_CYAN));
					}
					item.setData("clicked", true);
				}
			}
		});
		tree.setLinesVisible(true);
		getTreeItems();
	}
	
	private void getTreeItems(){
		tree.removeAll();
		Set<Entry<String, String>> itemProjs = Controller.getProjectNames();
		for(Entry<String, String> entryProj : itemProjs){
			TreeItem proj = new TreeItem(tree, 0);
			proj.setText(entryProj.getValue());
			proj.setData("project", entryProj.getKey());
			proj.setData("clicked", false);
		}
		
//		Random r_exp = new Random();
//		Random r_ds = new Random();
//		for(int i=1; i<=100; i++){
//			TreeItem trProj = new TreeItem(tree, 0);
//			trProj.setText("Project :: "+i);
//			for(int j=0; j<r_exp.nextInt(1000); j++){
//				TreeItem trExp = new TreeItem(trProj, 0);
//				trExp.setText("Experiment :: "+j);
//				for(int k=0; k<r_ds.nextInt(1000); k++){
//					TreeItem trDs = new TreeItem(trExp, 0);
//					trDs.setText("Dataset :: "+k);
//				}
//			}
//		}
	}
	
	private void createHeader(){
		Composite cmpHeader = new Composite(shell, SWT.NONE);
		cmpHeader.setLayoutData(BorderLayout.NORTH);
		cmpHeader.setLayout(new GridLayout(2, false));
		
		Label lblogo = new Label(cmpHeader, SWT.NONE);
		lblogo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		lblogo.setText("         ");
		Image imglogo = new Image(Display.getCurrent(), config+"/logo.png");
		lblogo.setImage(imglogo);
		
		label = new Label(cmpHeader, SWT.NONE);
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		label.setText("GLyPh");
		label.setFont(SWTResourceManager.getFont(".SF NS Text", 60, SWT.NORMAL));
		
	}
	
	private void createContent(){
		tabContent = new CTabFolder(shell, SWT.BORDER);
		tabContent.setLayoutData(BorderLayout.CENTER);
		tabContent.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
	}
	
	private void createContentTab(Composite frm, String title){
		CTabItem item = new CTabItem(tabContent, SWT.NONE);
		item.setText(title);
		item.setShowClose(true);
		item.setControl(frm);
		tabContent.setSelection(item);
	}
}
