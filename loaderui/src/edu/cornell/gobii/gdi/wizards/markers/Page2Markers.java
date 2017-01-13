package edu.cornell.gobii.gdi.wizards.markers;

import java.util.List;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import edu.cornell.gobii.gdi.objects.xml.Columns;
import edu.cornell.gobii.gdi.utils.Utils;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.TableEditor;

@Deprecated
public class Page2Markers extends WizardPage {
	private Table tbFileColumns;
	private Table tbMarker;
	private Table tbMarkerProps;
	private Table tbDatasetMarker;
	private Table tbLG;
	private Table tbMarkerLG;
	private Button btnLoad;
	private TableColumn tblclmnProperty;
	private TableColumn tblclmnValue;
	private Combo cbMap;
	private TableColumn tblclmnFieldHeaders;
	
	private List<String> header;
	private String config;
	private TableColumn tblclmnName;
	private TableColumn tblclmnHeader;
	private TableColumn tblclmnFrom;
	private TableColumn tblclmnTo;
	private TableColumn tblclmnName_1;
	private TableColumn tblclmnHeader_1;
	private TableColumn tblclmnFrom_1;
	private TableColumn tblclmnTo_1;
	private TableColumn tblclmnName_2;
	private TableColumn tblclmnHeader_2;
	private TableColumn tblclmnNewColumn;
	private TableColumn tblclmnTo_2;
	private TableColumn tblclmnName_3;
	private TableColumn tblclmnHeader_3;
	private TableColumn tblclmnFrom_2;
	private TableColumn tblclmnTo_3;

	/**
	 * Create the wizard.
	 */
	public Page2Markers(String config, List<String> header) {
		super("wizardPage");
		setTitle("Wizard :: Marker Information");
		setDescription("");
		this.header = header;
		this.config = config;
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		container.setLayout(new GridLayout(5, false));
		
		Group grpInformation = new Group(container, SWT.NONE);
		grpInformation.setLayout(new GridLayout(2, false));
		GridData gd_grpInformation = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
		gd_grpInformation.heightHint = 422;
		gd_grpInformation.widthHint = 265;
		grpInformation.setLayoutData(gd_grpInformation);
		grpInformation.setText("Data file");
		
		Label lblFieldHeaders = new Label(grpInformation, SWT.NONE);
		lblFieldHeaders.setText("Field Headers");
		
		tbFileColumns = new Table(grpInformation, SWT.BORDER | SWT.FULL_SELECTION);
		GridData gd_tbFileColumns = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_tbFileColumns.widthHint = 159;
		tbFileColumns.setLayoutData(gd_tbFileColumns);
		tbFileColumns.setHeaderVisible(true);
		tbFileColumns.setLinesVisible(true);
		
		tblclmnFieldHeaders = new TableColumn(tbFileColumns, SWT.NONE);
		tblclmnFieldHeaders.setWidth(136);
		tblclmnFieldHeaders.setText("Field Headers");
		new Label(grpInformation, SWT.NONE);
		
		tbFileColumns.addListener(SWT.Activate, new Listener() {
			
			@Override
			public void handleEvent(Event arg0) {
				tbFileColumns.clearAll();
				tbFileColumns.removeAll();
				for(int i=0; i<header.size(); i++){
					TableItem item = new TableItem(tbFileColumns, SWT.NONE);
					item.setText(0, header.get(i));
				}
			}
		});
		Utils.setDndColumnSource(tbFileColumns);
		
		btnLoad = new Button(grpInformation, SWT.NONE);
		btnLoad.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnLoad.setText("Load from file");
		
		Group grpMarkerInformation = new Group(container, SWT.NONE);
		grpMarkerInformation.setLayout(new GridLayout(1, false));
		GridData gd_grpMarkerInformation = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_grpMarkerInformation.widthHint = 202;
		grpMarkerInformation.setLayoutData(gd_grpMarkerInformation);
		grpMarkerInformation.setText("Marker Information");
		
		tbMarker = new Table(grpMarkerInformation, SWT.BORDER | SWT.FULL_SELECTION);
		tbMarker.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tbMarker.setHeaderVisible(true);
		tbMarker.setLinesVisible(true);
//		Columns columns = (Columns) Utils.unmarshalColumns(config+"/xmlMarker.xml");
//		TableEditor editor = new TableEditor(tbMarker);
//		editor.horizontalAlignment = SWT.LEFT;
//		Text text = new Text(tbMarker, SWT.NONE);
//		text.setText("haha");
//		editor.grabHorizontal = true;
//		for(String col : columns.getColumn()){
//			TableItem item = new TableItem(tbMarker, SWT.NONE);
//			item.setText(col);
//			editor.setEditor(text, item, 2);
//			editor.setEditor(text, item, 3);
//		}
//		Utils.setDdnColumnTarget(tbMarker);
		
		tblclmnName = new TableColumn(tbMarker, SWT.NONE);
		tblclmnName.setWidth(70);
		tblclmnName.setText("Name");
		
		tblclmnHeader = new TableColumn(tbMarker, SWT.NONE);
		tblclmnHeader.setWidth(50);
		tblclmnHeader.setText("Header");
		
		tblclmnFrom = new TableColumn(tbMarker, SWT.NONE);
		tblclmnFrom.setWidth(30);
		tblclmnFrom.setText("From");
		
		tblclmnTo = new TableColumn(tbMarker, SWT.NONE);
		tblclmnTo.setWidth(30);
		tblclmnTo.setText("To");
		
		tbMarkerProps = new Table(grpMarkerInformation, SWT.BORDER | SWT.FULL_SELECTION);
		tbMarkerProps.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tbMarkerProps.setHeaderVisible(true);
		tbMarkerProps.setLinesVisible(true);
//		columns = (Columns) Utils.unmarshalColumns(config+"/xmlMarkerProps.xml");
//		for(String col : columns.getColumn()){
//			TableItem item = new TableItem(tbMarkerProps, SWT.NONE);
//			item.setText(col);
//		}
//		Utils.setDdnColumnTarget(tbMarkerProps);
		
		tblclmnProperty = new TableColumn(tbMarkerProps, SWT.NONE);
		tblclmnProperty.setWidth(100);
		tblclmnProperty.setText("Property");
		
		tblclmnValue = new TableColumn(tbMarkerProps, SWT.NONE);
		tblclmnValue.setWidth(100);
		tblclmnValue.setText("Value");
		
		Group grpInstructionFile = new Group(container, SWT.NONE);
		grpInstructionFile.setText("Map Information");
		grpInstructionFile.setLayout(new GridLayout(1, false));
		GridData gd_grpInstructionFile = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_grpInstructionFile.widthHint = 276;
		grpInstructionFile.setLayoutData(gd_grpInstructionFile);
		
		cbMap = new Combo(grpInstructionFile, SWT.NONE);
		cbMap.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Group grpLinkageGroup = new Group(grpInstructionFile, SWT.NONE);
		grpLinkageGroup.setLayout(new FillLayout(SWT.HORIZONTAL));
		GridData gd_grpLinkageGroup = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_grpLinkageGroup.heightHint = 159;
		grpLinkageGroup.setLayoutData(gd_grpLinkageGroup);
		grpLinkageGroup.setText("Linkage Group");
		
		tbLG = new Table(grpLinkageGroup, SWT.BORDER | SWT.FULL_SELECTION);
		tbLG.setHeaderVisible(true);
		tbLG.setLinesVisible(true);
//		columns = (Columns) Utils.unmarshalColumns(config+"/xmlLG.xml");
//		editor = new TableEditor(tbLG);
//		text = new Text(tbLG, SWT.NONE);
//		for(String col : columns.getColumn()){
//			TableItem item = new TableItem(tbLG, SWT.NONE);
//			item.setText(col);
//			editor.setEditor(text, item, 2);
//			editor.setEditor(text, item, 3);
//		}
//		Utils.setDdnColumnTarget(tbLG);
		
		tblclmnName_1 = new TableColumn(tbLG, SWT.NONE);
		tblclmnName_1.setWidth(70);
		tblclmnName_1.setText("Name");
		
		tblclmnHeader_1 = new TableColumn(tbLG, SWT.NONE);
		tblclmnHeader_1.setWidth(50);
		tblclmnHeader_1.setText("Header");
		
		tblclmnFrom_1 = new TableColumn(tbLG, SWT.NONE);
		tblclmnFrom_1.setWidth(30);
		tblclmnFrom_1.setText("From");
		
		tblclmnTo_1 = new TableColumn(tbLG, SWT.NONE);
		tblclmnTo_1.setWidth(30);
		tblclmnTo_1.setText("To");
		
		Group grpMarkerLinkage = new Group(grpInstructionFile, SWT.NONE);
		grpMarkerLinkage.setLayout(new FillLayout(SWT.HORIZONTAL));
		GridData gd_grpMarkerLinkage = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_grpMarkerLinkage.heightHint = 193;
		grpMarkerLinkage.setLayoutData(gd_grpMarkerLinkage);
		grpMarkerLinkage.setText("Marker - Linkage Group");
		
		tbMarkerLG = new Table(grpMarkerLinkage, SWT.BORDER | SWT.FULL_SELECTION);
		tbMarkerLG.setHeaderVisible(true);
		tbMarkerLG.setLinesVisible(true);
//		columns = (Columns) Utils.unmarshalColumns(config+"/xmlMarker_LG.xml");
//		editor = new TableEditor(tbMarkerLG);
//		text = new Text(tbMarkerLG, SWT.NONE);
//		for(String col : columns.getColumn()){
//			TableItem item = new TableItem(tbMarkerLG, SWT.NONE);
//			item.setText(col);
//			editor.setEditor(text, item, 2);
//			editor.setEditor(text, item, 3);
//		}
//		Utils.setDdnColumnTarget(tbMarkerLG);
		
		tblclmnName_2 = new TableColumn(tbMarkerLG, SWT.NONE);
		tblclmnName_2.setWidth(70);
		tblclmnName_2.setText("Name");
		
		tblclmnHeader_2 = new TableColumn(tbMarkerLG, SWT.NONE);
		tblclmnHeader_2.setWidth(50);
		tblclmnHeader_2.setText("Header");
		
		tblclmnNewColumn = new TableColumn(tbMarkerLG, SWT.NONE);
		tblclmnNewColumn.setWidth(30);
		tblclmnNewColumn.setText("From");
		
		tblclmnTo_2 = new TableColumn(tbMarkerLG, SWT.NONE);
		tblclmnTo_2.setWidth(30);
		tblclmnTo_2.setText("To");
		
		Group grpDatasetMarkerInformation = new Group(container, SWT.NONE);
		grpDatasetMarkerInformation.setText("Dataset marker information");
		grpDatasetMarkerInformation.setLayout(new GridLayout(1, false));
		GridData gd_grpDatasetMarkerInformation = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_grpDatasetMarkerInformation.widthHint = 216;
		grpDatasetMarkerInformation.setLayoutData(gd_grpDatasetMarkerInformation);
		
		tbDatasetMarker = new Table(grpDatasetMarkerInformation, SWT.BORDER | SWT.FULL_SELECTION);
		tbDatasetMarker.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tbDatasetMarker.setHeaderVisible(true);
		tbDatasetMarker.setLinesVisible(true);
//		columns = (Columns) Utils.unmarshalColumns(config+"/xmlDS_marker.xml");
//		for(String col : columns.getColumn()){
//			TableItem item = new TableItem(tbDatasetMarker, SWT.NONE);
//			item.setText(col);
//		}
		
		tblclmnName_3 = new TableColumn(tbDatasetMarker, SWT.NONE);
		tblclmnName_3.setWidth(70);
		tblclmnName_3.setText("Name");
		
		tblclmnHeader_3 = new TableColumn(tbDatasetMarker, SWT.NONE);
		tblclmnHeader_3.setWidth(50);
		tblclmnHeader_3.setText("Header");
		
		tblclmnFrom_2 = new TableColumn(tbDatasetMarker, SWT.NONE);
		tblclmnFrom_2.setWidth(30);
		tblclmnFrom_2.setText("From");
		
		tblclmnTo_3 = new TableColumn(tbDatasetMarker, SWT.NONE);
		tblclmnTo_3.setWidth(30);
		tblclmnTo_3.setText("To");
	}
}
