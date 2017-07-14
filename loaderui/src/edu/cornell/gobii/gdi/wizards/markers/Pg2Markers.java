package edu.cornell.gobii.gdi.wizards.markers;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import edu.cornell.gobii.gdi.utils.Utils;


public class Pg2Markers extends WizardPage {
	private String config;
	private DTOmarkers dto = new DTOmarkers();
	private Table tbFieldHeaders;
	private Table tbMarkerProp;
	private Table tbMarker;
	private Table tbDSmarker;

	/**
	 * Create the wizard.
	 */
	public Pg2Markers(String config, DTOmarkers dto) {
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
		container.setLayout(new GridLayout(3, false));
		
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
		
		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		composite.setLayout(new GridLayout(1, false));
		
		tbMarker = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		tbMarker.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tbMarker.setHeaderVisible(true);
		tbMarker.setLinesVisible(true);
		
		TableColumn tblclmnIndex = new TableColumn(tbMarker, SWT.NONE);
		tblclmnIndex.setWidth(50);
		
		TableColumn tblclmnMarkerInformation = new TableColumn(tbMarker, SWT.NONE);
		tblclmnMarkerInformation.setWidth(150);
		tblclmnMarkerInformation.setText("Marker Information");
		
		TableColumn tblclmnHeader = new TableColumn(tbMarker, SWT.NONE);
		tblclmnHeader.setWidth(150);
		tblclmnHeader.setText("Header");
		
		TableColumn tblclmnFrom = new TableColumn(tbMarker, SWT.NONE);
		tblclmnFrom.setWidth(50);
		tblclmnFrom.setText("From");
		
		TableColumn tblclmnTo = new TableColumn(tbMarker, SWT.NONE);
		tblclmnTo.setWidth(50);
		tblclmnTo.setText("To");
		
		Utils.unmarshalColumns(tbMarker, config+"/xml/Marker.xml", dto.getMarkerFields(), dto.getSubMarkerFields());
		Utils.setDndColumnTarget(tbFieldHeaders, tbMarker, dto.getMarkerFields(), dto.getSubMarkerFields());
		Utils.setTableMouseLister(tbMarker, dto.getMarkerFields());
		
				tbMarkerProp = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
				tbMarkerProp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
				tbMarkerProp.setHeaderVisible(true);
				tbMarkerProp.setLinesVisible(true);
				
				TableColumn tblclmnIndex_2 = new TableColumn(tbMarkerProp, SWT.NONE);
				tblclmnIndex_2.setWidth(50);
				
				TableColumn tblclmnProperty = new TableColumn(tbMarkerProp, SWT.NONE);
				tblclmnProperty.setWidth(200);
				tblclmnProperty.setText("Property");
				
				TableColumn tblclmnValue = new TableColumn(tbMarkerProp, SWT.NONE);
				tblclmnValue.setWidth(100);
				tblclmnValue.setText("Value");
				
				Utils.loadTableProps(tbMarkerProp, "marker_prop", dto.getMarkerPropFields());
				Utils.setDndColumnTarget(tbFieldHeaders, tbMarkerProp, dto.getMarkerPropFields(), null);
				Utils.setTableMouseLister(tbMarkerProp, dto.getMarkerFields());
		
		tbDSmarker = new Table(container, SWT.BORDER | SWT.FULL_SELECTION);
		tbDSmarker.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tbDSmarker.setHeaderVisible(true);
		tbDSmarker.setLinesVisible(true);
		
		TableColumn tblclmnIndex_1 = new TableColumn(tbDSmarker, SWT.NONE);
		tblclmnIndex_1.setWidth(50);
		
		TableColumn tblclmnDsMarkerInformation = new TableColumn(tbDSmarker, SWT.NONE);
		tblclmnDsMarkerInformation.setWidth(150);
		tblclmnDsMarkerInformation.setText("DS Marker Information");
		
		TableColumn tblclmnHeader_1 = new TableColumn(tbDSmarker, SWT.NONE);
		tblclmnHeader_1.setWidth(150);
		tblclmnHeader_1.setText("Header");
		
		TableColumn tblclmnFrom_1 = new TableColumn(tbDSmarker, SWT.NONE);
		tblclmnFrom_1.setWidth(50);
		tblclmnFrom_1.setText("From");
		
		TableColumn tblclmnTo_1 = new TableColumn(tbDSmarker, SWT.NONE);
		tblclmnTo_1.setWidth(50);
		tblclmnTo_1.setText("To");
		
		Utils.unmarshalColumns(tbDSmarker, config+"/xml/DS_marker.xml", dto.getDsMarkerFields(), dto.getSubDsMarkerFields());
		Utils.setDndColumnTarget(tbFieldHeaders, tbDSmarker, dto.getDsMarkerFields(), dto.getSubDsMarkerFields());
		Utils.setTableMouseLister(tbDSmarker, dto.getDsMarkerFields());
		
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
//					item.setText(0, dto.getHeader().get(i)[0]);
				}
			}
			
		});
	}

}
