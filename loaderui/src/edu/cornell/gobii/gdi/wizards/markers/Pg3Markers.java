package edu.cornell.gobii.gdi.wizards.markers;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import edu.cornell.gobii.gdi.utils.Utils;

import org.eclipse.swt.layout.FillLayout;

public class Pg3Markers extends WizardPage {
	private String config;
	private DTOmarkers dto = new DTOmarkers();
	private Table tbFieldHeaders;
	private Table tbLG;
	private Table tbLGmarker;

	/**
	 * Create the wizard.
	 */
	public Pg3Markers(String config, DTOmarkers dto) {
		super("wizardPage");
		setTitle("Wizard :: Marker Information");
		setDescription("");
		this.config = config;
		this.dto = dto;
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		container.setLayout(new GridLayout(2, false));
		
		Group grpDataFileFields = new Group(container, SWT.NONE);
		GridData gd_grpInformation = new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 1);
		gd_grpInformation.widthHint = 350;
		grpDataFileFields.setLayoutData(gd_grpInformation);
		grpDataFileFields.setLayout(new GridLayout(1, false));
		grpDataFileFields.setText("Data file fields");
		
		tbFieldHeaders = new Table(grpDataFileFields, SWT.BORDER | SWT.FULL_SELECTION);
		tbFieldHeaders.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tbFieldHeaders.setHeaderVisible(true);
		tbFieldHeaders.setLinesVisible(true);
		
		TableColumn tblclmnFieldHeaders = new TableColumn(tbFieldHeaders, SWT.NONE);
		tblclmnFieldHeaders.setWidth(200);
		tblclmnFieldHeaders.setText("Field Headers");
		
		Utils.setDndColumnSource(tbFieldHeaders);
		
		Group group_1 = new Group(container, SWT.NONE);
		group_1.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
		group_1.setText("Map Information");
		group_1.setLayout(new GridLayout(1, false));
		
		Group group_2 = new Group(group_1, SWT.NONE);
		GridData gd_group_2 = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_group_2.heightHint = 159;
		group_2.setLayoutData(gd_group_2);
		group_2.setText("Linkage Group");
		group_2.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		tbLG = new Table(group_2, SWT.BORDER | SWT.FULL_SELECTION);
		tbLG.setLinesVisible(true);
		tbLG.setHeaderVisible(true);
		
		TableColumn tblclmnIndex = new TableColumn(tbLG, SWT.NONE);
		tblclmnIndex.setWidth(60);
		
		TableColumn tableColumn_1 = new TableColumn(tbLG, SWT.NONE);
		tableColumn_1.setWidth(150);
		tableColumn_1.setText("Name");
		
		TableColumn tableColumn_2 = new TableColumn(tbLG, SWT.NONE);
		tableColumn_2.setWidth(150);
		tableColumn_2.setText("Header");
		
		TableColumn tableColumn_3 = new TableColumn(tbLG, SWT.NONE);
		tableColumn_3.setWidth(50);
		tableColumn_3.setText("From");
		
		TableColumn tableColumn_4 = new TableColumn(tbLG, SWT.NONE);
		tableColumn_4.setWidth(50);
		tableColumn_4.setText("To");
		
		Utils.unmarshalColumns(tbLG, config+"/xml/LG.xml", dto.getLgFields());
		Utils.setDndColumnTarget(tbFieldHeaders, tbLG, dto.getLgFields());
		Utils.setTableMouseLister(tbLG, dto.getLgFields());
		
		Group group_3 = new Group(group_1, SWT.NONE);
		GridData gd_group_3 = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_group_3.heightHint = 193;
		group_3.setLayoutData(gd_group_3);
		group_3.setText("Marker - Linkage Group");
		group_3.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		tbLGmarker = new Table(group_3, SWT.BORDER | SWT.FULL_SELECTION);
		tbLGmarker.setLinesVisible(true);
		tbLGmarker.setHeaderVisible(true);
		
		TableColumn tblclmnIndex_1 = new TableColumn(tbLGmarker, SWT.NONE);
		tblclmnIndex_1.setWidth(60);
		
		TableColumn tableColumn_5 = new TableColumn(tbLGmarker, SWT.NONE);
		tableColumn_5.setWidth(150);
		tableColumn_5.setText("Name");
		
		TableColumn tableColumn_6 = new TableColumn(tbLGmarker, SWT.NONE);
		tableColumn_6.setWidth(150);
		tableColumn_6.setText("Header");
		
		TableColumn tableColumn_7 = new TableColumn(tbLGmarker, SWT.NONE);
		tableColumn_7.setWidth(50);
		tableColumn_7.setText("From");
		
		TableColumn tableColumn_8 = new TableColumn(tbLGmarker, SWT.NONE);
		tableColumn_8.setWidth(50);
		tableColumn_8.setText("To");

		Utils.unmarshalColumns(tbLGmarker, config+"/xml/Marker_LG.xml", dto.getLgMarkerFields());
		Utils.setDndColumnTarget(tbFieldHeaders, tbLGmarker, dto.getLgMarkerFields());
		Utils.setTableMouseLister(tbLGmarker, dto.getLgMarkerFields());
		
		TableColumn tblclmnPreview = new TableColumn(tbFieldHeaders, SWT.NONE);
		tblclmnPreview.setWidth(100);
		tblclmnPreview.setText("Preview");
		
		container.addListener(SWT.Show, new Listener(){

			@Override
			public void handleEvent(Event arg0) {
				tbFieldHeaders.clearAll();
				tbFieldHeaders.removeAll();
				for(int i=0; i<dto.getHeader().size(); i++){
					TableItem item = new TableItem(tbFieldHeaders, SWT.NONE);
					item.setText(dto.getHeader().get(i));
//					item.setText(0, dto.getHeader().get(i));
				}
			}
			
		});
	}

}
