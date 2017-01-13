package edu.cornell.gobii.gdi.forms;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.gobiiproject.gobiiclient.dtorequests.DtoRequestMarkerGroup;
import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.container.MarkerGroupDTO;
import org.gobiiproject.gobiimodel.dto.container.MarkerGroupMarkerDTO;

import edu.cornell.gobii.gdi.main.App;
import edu.cornell.gobii.gdi.services.Controller;
import edu.cornell.gobii.gdi.services.IDs;
import edu.cornell.gobii.gdi.utils.FormUtils;
import edu.cornell.gobii.gdi.utils.Utils;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class FrmMarkerGroups extends AbstractFrm {
	private static Logger log = Logger.getLogger(FrmPlatforms.class.getName());
	private Text txtName;
	private Text txtCode;
	private Text txtGermplasmGroup;
	private Table table;
	private TableViewer viewerParameters;
	private Button btnSelectAll;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public FrmMarkerGroups(Shell shell, Composite parent, int style) {
		super(shell, parent, style);
		cbList.setEnabled(false);
		GridLayout gridLayout = (GridLayout) cmpForm.getLayout();
		gridLayout.numColumns = 2;

		Label lblName = new Label(cmpForm, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblName.setText("Name:");

		txtName = new Text(cmpForm, SWT.BORDER);
		txtName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblCode = new Label(cmpForm, SWT.NONE);
		lblCode.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblCode.setText("Code:");

		txtCode = new Text(cmpForm, SWT.BORDER);
		txtCode.setEditable(false);
		txtCode.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
				Label lblGermplasmGroup = new Label(cmpForm, SWT.NONE);
				lblGermplasmGroup.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
				lblGermplasmGroup.setText("Germplasm group:");
		
				txtGermplasmGroup = new Text(cmpForm, SWT.BORDER);
				txtGermplasmGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(cmpForm, SWT.NONE);
		
		btnSelectAll = new Button(cmpForm, SWT.CHECK);
		btnSelectAll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectAllMarkers(btnSelectAll.getSelection());
			}
		});
		btnSelectAll.setText("Select all Markers");

		Label lblMarkers = new Label(cmpForm, SWT.NONE);
		lblMarkers.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblMarkers.setText("Markers:");

		viewerParameters = new TableViewer(cmpForm, SWT.BORDER | SWT.CHECK | SWT.FULL_SELECTION);
		table = viewerParameters.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		new Label(cmpForm, SWT.NONE);

		TableViewerColumn tableViewerColumn = new TableViewerColumn(viewerParameters, SWT.NONE);
		TableColumn tblclmnName = tableViewerColumn.getColumn();
		tblclmnName.setWidth(150);
		tblclmnName.setText("Platform");

		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(viewerParameters, SWT.NONE);
		TableColumn tblclmnDefinition = tableViewerColumn_2.getColumn();
		tblclmnDefinition.setWidth(100);
		tblclmnDefinition.setText("Marker");

		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(viewerParameters, SWT.NONE);
		TableColumn tblclmnRank = tableViewerColumn_1.getColumn();
		tblclmnRank.setWidth(100);
		tblclmnRank.setText("Favorable alleles");
		
		Button btnImportMarkers = new Button(cmpForm, SWT.NONE);
		btnImportMarkers.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(!txtName.getText().isEmpty()){
					FileDialog dlg = new FileDialog(shell, SWT.OPEN);

//					dlg.setFilterExtensions(new String[]{".txt"});
					dlg.setFilterNames(new String[]{"*.txt"});
					String fn = dlg.open();
					
					if(table.getItemCount() > 0){
						MessageBox dialog = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
						dialog.setMessage("Marker Group already contains markers. Would you like to replace existing markers with new list?\n\n Click Yes to replace existing list, or No to append to existing list.");
						if(dialog.open() == SWT.YES){
							cleanMarkerTable();
						}
					}
					
					if (fn != null) {
						File file = new File(fn);
						try {
							Scanner sc = new Scanner(file);
							while (sc.hasNextLine()) {
								String[] row = sc.nextLine().split("\t");
								TableItem item = new TableItem(table, SWT.NONE);
								item.setChecked(true);
//								item.setText(0, row[0]);
//								item.setText(1, row[1]);
//								item.setText(2, row[2]);
								item.setText(row);
							}
							sc.close();
						} 
						catch (FileNotFoundException ae) {
							ae.printStackTrace();
						}
					}
				}else{
					MessageBox dialog = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
					dialog.setMessage("Please select a Marker Group!");
					dialog.open();
				}
			}
		});
		btnImportMarkers.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnImportMarkers.setText("Import Markers");
		new Label(cmpForm, SWT.NONE);
		
		Button btnExportMarkers = new Button(cmpForm, SWT.NONE);
		btnExportMarkers.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FormUtils.exportTableAsTxt(shell, table);
			}
		});
		btnExportMarkers.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnExportMarkers.setText("Export Markers");
		new Label(cmpForm, SWT.NONE);

//		Button btnUpdateMarkers = new Button(cmpForm, SWT.NONE);
//		btnUpdateMarkers.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//			}
//		});
//		btnUpdateMarkers.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
//		btnUpdateMarkers.setText("Update Markers");
//		new Label(cmpForm, SWT.NONE);

		Button btnDeleteMarkers = new Button(cmpForm, SWT.NONE);
		btnDeleteMarkers.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MessageBox dialog = new MessageBox(shell, SWT.ICON_WARNING | SWT.YES | SWT.NO);
				dialog.setMessage("Are you sure you want to delete selected Markers?\n\n Changes will still have to be applied in order to affect database.");
				if(dialog.open() == SWT.YES){
					for(TableItem item : table.getItems()){
						if(item.getChecked()) item.dispose();
					}
					selectAllMarkers(true);
				}
			}
		});
		btnDeleteMarkers.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnDeleteMarkers.setText("Delete Selected Marker(s)");
		new Label(cmpForm, SWT.NONE);
		
		Label label = new Label(cmpForm, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(cmpForm, SWT.NONE);

		Button btnSave = new Button(cmpForm, SWT.NONE);
		btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean isNew = IDs.MarkerGroupId == -1 ? true : false;
				if(IDs.MarkerGroupId > 0){
					MessageBox dialog = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
					dialog.setMessage("A MarkerGroup seems to be selected, do you want to update this group with information or save as new?\n\n Please click YES to update selected MarkerGrup, or NO to add new MarkerGroup!");
					isNew = dialog.open() == SWT.YES ? false : true;
				}
				if(!validate(isNew)) return;
				
				try{
					List<MarkerGroupMarkerDTO> markers = new ArrayList<MarkerGroupMarkerDTO>();
					for(TableItem item : table.getItems()){
						if(!item.getChecked()) return;
						MarkerGroupMarkerDTO marker = new MarkerGroupMarkerDTO();
						marker.setPlatformName(item.getText(0));
						marker.setMarkerName(item.getText(1));
						marker.setFavorableAllele(item.getText(2));
					}
					MarkerGroupDTO markerGroupDTORequest = new MarkerGroupDTO(isNew ? DtoMetaData.ProcessType.CREATE : DtoMetaData.ProcessType.UPDATE);
					if(!isNew) markerGroupDTORequest.setMarkerGroupId(IDs.MarkerGroupId);
					markerGroupDTORequest.setName(txtName.getText());
					markerGroupDTORequest.setCode(txtName.getText());
					markerGroupDTORequest.setGermplasmGroup(txtGermplasmGroup.getText());
					markerGroupDTORequest.setMarkers(markers);
					markerGroupDTORequest.setStatus(1);
					markerGroupDTORequest.setCreatedBy(App.INSTANCE.getUser().getUserId());
					markerGroupDTORequest.setModifiedBy(App.INSTANCE.getUser().getUserId());
					
					DtoRequestMarkerGroup dtoRequestMarkerGroup = new DtoRequestMarkerGroup();
					MarkerGroupDTO mgRequest = dtoRequestMarkerGroup.process(markerGroupDTORequest);
					if(Controller.getDTOResponse(shell, mgRequest, memInfo)){
						cleanDetails();
						populateMarkerGroupList();
					}
				}catch (Exception err) {
					Utils.log(log, "Error saving MarkerGroup", err);
				}
			}
		});
		btnSave.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnSave.setText("Save MarkerGroup");
		new Label(cmpForm, SWT.NONE);

//		Button btnUpdate = new Button(cmpForm, SWT.NONE);
//		btnUpdate.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
//		btnUpdate.setText("Update");
//		new Label(cmpForm, SWT.NONE);
		
		Button btnClearFields = new Button(cmpForm, SWT.NONE);
		btnClearFields.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				cleanDetails();
			}
		});
		btnClearFields.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnClearFields.setText("Clear Fields");
		new Label(cmpForm, SWT.NONE);

		Button btnMarkerGroupWiz = new Button(cmpForm, SWT.FLAT);
		btnMarkerGroupWiz.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN));
		btnMarkerGroupWiz.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnMarkerGroupWiz.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnMarkerGroupWiz.setText("Marker Group Wizard");

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	@Override
	protected void createContent() {
		populateMarkerGroupList();
	}

	private void populateMarkerGroupList() {
		// TODO Auto-generated method stub
		tbList.removeAll();
		FormUtils.entrySetToTable(Controller.getMarkerGroupNames(), tbList);
		
		TableColumn tblclmnNewColumn = new TableColumn(tbList, SWT.NONE);
		tblclmnNewColumn.setWidth(300);
		tblclmnNewColumn.setText("Marker Groups");

		tbList.addListener (SWT.Selection, new Listener() {

			public void handleEvent(Event e) {
				cleanDetails();
				if(tbList.getSelectionIndex() < 0) return;
				TableItem item = tbList.getItem(tbList.getSelectionIndex());
				if(item == null) return;
				String selected = item.getText(); //single selection
				IDs.MarkerGroupId = Integer.parseInt((String) item.getData(selected));
				populateMarkerGroupDetails(IDs.MarkerGroupId); //retrieve and display projects by contact Id
			}

			protected void populateMarkerGroupDetails(int MarkerGroupId) {
				try{
					DtoRequestMarkerGroup dtoRequestMarkerGroup = new DtoRequestMarkerGroup();
					MarkerGroupDTO MarkerGroupDTORequest = new MarkerGroupDTO();
					MarkerGroupDTORequest.setMarkerGroupId(MarkerGroupId);
					try {
						MarkerGroupDTO MarkerGroupDTOResponse = dtoRequestMarkerGroup.process(MarkerGroupDTORequest);
						txtName.setText(MarkerGroupDTOResponse.getName());
						txtCode.setText(MarkerGroupDTOResponse.getCode());
						txtGermplasmGroup.setText(MarkerGroupDTOResponse.getGermplasmGroup());

						populateTableFromStringList(MarkerGroupDTOResponse.getMarkers(), table);
					} catch (Exception err) {
						Utils.log(shell, memInfo, log, "Error retrieving MarkerGroups", err);
					}
				}catch(Exception err){
					Utils.log(shell, memInfo, log, "Error retrieving MarkerGroups", err);
				}

			}
		});
	}

	private void populateTableFromStringList(List<MarkerGroupMarkerDTO> list, Table table) {
		try{
			table.removeAll();

			TableItem item = null;
			for(MarkerGroupMarkerDTO property : list){
				item= new TableItem(table, SWT.NONE); 
				item.setText(0, property.getPlatformName()); //index zero - first column 
				item.setText(1,property.getMarkerName());
				item.setText(2, property.getFavorableAllele()); //index zero - first column 
				item.setData(property.getMarkerId());
			}
		}catch(Exception err){
			Utils.log(shell, memInfo, log, "Error retrieving CVs", err);
		}
	}
	protected void cleanDetails() {
		try{
			IDs.MarkerGroupId = -1;
			txtName.setText("");
			txtCode.setText("");
			txtGermplasmGroup.setText("");
			btnSelectAll.setSelection(false);
			cleanMarkerTable();
		}catch(Exception err){
			Utils.log(shell, memInfo, log, "Error clearing fields", err);
		}
	}
	
	protected void cleanMarkerTable(){
		for(TableItem item : table.getItems()){
			item.dispose();
		}
	}

	protected void selectAllMarkers(boolean check){
		for(TableItem item : table.getItems())
			item.setChecked(check);
	}
	
	protected boolean validate(boolean isNew){
		boolean successful = true;
		String message = null;
		if(txtName.getText().isEmpty()){
			message = "Name field is required!";
			successful = false;
		}else if(table.getItemCount() == 0){
			message = "There are no markers in group, please import a list of markers!";
			successful = false;
		}
		if(isNew){
			for(TableItem item : tbList.getItems()){
				if(item.getText(0).equals(txtName.getText())){
					successful = false;
					message = "Marker Group name already exists, kindly change name and try saving again.";
					break;
				}
			}
		}
		
		if(!successful){
			MessageBox dialog = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
			dialog.setMessage(message);
			dialog.open();
		}
		return successful;
	}
}
