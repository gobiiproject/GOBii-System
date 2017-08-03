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
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiapimodel.types.GobiiServiceRequestId;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiimodel.headerlesscontainer.MarkerGroupDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.MarkerGroupMarkerDTO;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;

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

	private int platformCol = -1;
	private int markerCol= -1;
	private int  alleleCol = -1;

	private Text txtName;
	private Text txtCode;
	private Text txtGermplasmGroup;
	private Table table;
	private TableViewer viewerParameters;
	private int currentMarkerGroupId=-1;
	private Button btnMarkerGroupAdd;
	private Button btnUpdate;
	ArrayList<String> tableItemMarkerNames = new ArrayList<String>();

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public FrmMarkerGroups(Shell shell, Composite parent, int style) {
		super(shell, parent, style);
		cbList.setEnabled(false);
		selectedName = ""; 
		GridLayout gridLayout = (GridLayout) cmpForm.getLayout();
		gridLayout.numColumns = 3;

		Label lblName = new Label(cmpForm, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblName.setText("*Marker Group Name:");

		txtName = new Text(cmpForm, SWT.BORDER);
		txtName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		Label lblCode = new Label(cmpForm, SWT.NONE);
		lblCode.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblCode.setText("Code:");

		txtCode = new Text(cmpForm, SWT.BORDER);
		txtCode.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtCode.setEditable(false);
		txtCode.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		Label lblGermplasmGroup = new Label(cmpForm, SWT.NONE);
		lblGermplasmGroup.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblGermplasmGroup.setText("Germplasm group:");

		txtGermplasmGroup = new Text(cmpForm, SWT.BORDER);
		txtGermplasmGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		new Label(cmpForm, SWT.NONE);

		viewerParameters = new TableViewer(cmpForm, SWT.BORDER);
		table = viewerParameters.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 4));

		TableViewerColumn tableViewerColumn = new TableViewerColumn(viewerParameters, SWT.NONE);
		TableColumn tblclmnName = tableViewerColumn.getColumn();
		tblclmnName.setWidth(90);
		tblclmnName.setText("Markers");

		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(viewerParameters, SWT.NONE);
		TableColumn tblclmnDefinition = tableViewerColumn_2.getColumn();
		tblclmnDefinition.setWidth(90);
		tblclmnDefinition.setText("Platforms");

		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(viewerParameters, SWT.NONE);
		TableColumn tblclmnRank = tableViewerColumn_1.getColumn();
		tblclmnRank.setWidth(150);
		tblclmnRank.setText("Favorable alleles");
		new Label(cmpForm, SWT.NONE);
		new Label(cmpForm, SWT.NONE);

		Label label_1 = new Label(cmpForm, SWT.NONE);
		label_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_1.setText("Markers:");


		Button btnImportMarkers = new Button(cmpForm, SWT.NONE);
		btnImportMarkers.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(!txtName.getText().isEmpty()){
					FileDialog dlg = new FileDialog(shell, SWT.OPEN);

					//					dlg.setFilterExtensions(new String[]{".txt"});
					dlg.setFilterNames(new String[]{"*.txt"});
					String fn = dlg.open();

					if (fn != null) {
						ArrayList<String>  newLines = new ArrayList<String>();
						ArrayList<String>  newMarkerPlatforms = new ArrayList<String>();

						if(table.getItemCount() > 0){
							MessageDialog dg = new MessageDialog(
									shell,
									"Warning",
									null,
									"Would you like to overwrite the existing markers with the new list?",
									MessageDialog.QUESTION_WITH_CANCEL, 
									new String[]{
											"Yes", 
											"Append only"
									},
									0
									);

							switch(dg.open()) {
							case 0:   //overwrite
								cleanMarkerTable();
								tableItemMarkerNames  = new ArrayList<String>();
								break;
							case 1: //append 
								//do not add new value to table
								break;
							}
						}
						File file = new File(fn);
						try {
							

							platformCol = -1;
							markerCol= -1;
							alleleCol = -1;
							
							Scanner sc = new Scanner(file);
							String[] firstLine = sc.nextLine().split("\t");
							int ctr = 0;

							for(String s: firstLine){

								if (s.toLowerCase().equals("platform_name")) platformCol=ctr;
								else if(s.toLowerCase().equals("marker_name")) markerCol=ctr;
								else if(s.toLowerCase().equals("alleles"))  alleleCol=ctr;

								ctr++;

							}

							if(firstLine.length<2){ // Must have atleast 2 columns for platform and marker

								MessageDialog.openError(shell, "Error", "Platform and Marker Columns are required");
								sc.close();
								return;
							}
							else{

								if(platformCol==-1 || markerCol == -1){ //if the required column names are not found, assume proper order.

									platformCol = 1;
									markerCol=0;
									alleleCol = 2;
								}

								tableItemMarkerNames = getTableItems();

								while (sc.hasNextLine()) { //read file
									String readline = sc.nextLine();
									String[] row = readline.split("\t");
									String platform=null;
									String marker=null;

									//check if platformName exists
									if(!readline.replaceAll("\t", "").isEmpty()){ // ignore empty lines with just tabs
										try {platform = row[platformCol].toLowerCase();
										if(platform.isEmpty()){
											MessageDialog.openError(shell, "Error: Platform is required", "Some entries does not have a platform name.");
											sc.close();
											return;}
										}
										catch(Exception e2){
										}

										//check if markerName exists
										try{marker = row[markerCol].toLowerCase();
										if(marker.isEmpty()){
											MessageDialog.openError(shell, "Error: Marker is required", "Some entries does not have a Marker name.");
											sc.close();
											return;}
										}
										catch(Exception e2){
										}

										String platformMarker = platform + marker;
										if(newMarkerPlatforms.contains(platformMarker)){
											MessageDialog.openError(shell, "Error: Duplicates found ", "There were duplicate markers found in the file with the same platform name.");
											sc.close();
											return;
										}else{
											newMarkerPlatforms.add(platformMarker);
											newLines.add(readline);
										}
									}
								}

								sc.close();
								if (newLines.size()>200) MessageDialog.openError(shell, "Error", "Please upload up to 200 markers only");
								else{

									int replace=-1;
									for(String s: newLines){
										String[] row = s.split("\t");

										if (tableItemMarkerNames.size()>0){
											String platformMarker = row[markerCol].toLowerCase()+row[platformCol].toLowerCase();

											if(tableItemMarkerNames.contains(platformMarker)){

												if(replace<0){
													MessageDialog dg = new MessageDialog(
															shell,
															"WARNING: Duplicates found",
															null,
															"Some of the markers already exist in the table.\n\n Would you like to overwrite the favorable alleles for these markers?",
															MessageDialog.QUESTION_WITH_CANCEL, 
															new String[]{
																	"Overwrite", 
															"Append new markers"},
															0
															);
													switch(dg.open()) {
													case 0:   //overwrite
														replace=1;
														int index = getTableIndex(platformMarker);
														try{table.getItem(index).setText(2, row[alleleCol].replaceAll(" ", "").replaceAll("\"", ""));}catch(Exception e1){}
														break;
													case 1: //append new
														//do not add new value to table
														replace=2;
														break;
													}
												}
												else if (replace == 1){

													int index = getTableIndex(platformMarker);
													try{
														table.getItem(index).setText(2, row[alleleCol].replaceAll(" ", "").replaceAll("\"", ""));}catch(Exception e1){}
												}

											}else{
												TableItem item = new TableItem(table, SWT.NONE);
												try{
													item.setText(1, row[platformCol]);
													item.setText(0, row[markerCol]);
													item.setText(2, row[alleleCol].replaceAll(" ", "").replaceAll("\"", ""));
												}catch(Exception e2){}
											}
										}
										else{ // just keep adding
											TableItem item = new TableItem(table, SWT.NONE);
											try{
												item.setText(1, row[platformCol]);
												item.setText(0, row[markerCol]);
												item.setText(2, row[alleleCol].replaceAll(" ", "").replaceAll("\"", ""));
											}catch(Exception e3){}
										}

									}
								}
							}
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
		btnImportMarkers.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		btnImportMarkers.setText("Import Markers");

		Button btnExportMarkers = new Button(cmpForm, SWT.NONE);
		btnExportMarkers.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FormUtils.exportTableAsTxt(shell, table, "Markers");
			}
		});
		btnExportMarkers.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		btnExportMarkers.setText("Export Markers");

		Label label = new Label(cmpForm, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));

		btnMarkerGroupAdd = new Button(cmpForm, SWT.FLAT);
		btnMarkerGroupAdd.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN));
		btnMarkerGroupAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				if(!validate(true)) return;

				try{
					List<MarkerGroupMarkerDTO> markers = new ArrayList<MarkerGroupMarkerDTO>();
					for(TableItem item : table.getItems()){
						MarkerGroupMarkerDTO marker = new MarkerGroupMarkerDTO();
						marker.setPlatformName(item.getText(1));
						marker.setMarkerName(item.getText(0));
						marker.setFavorableAllele(item.getText(2));
						markers.add(marker);
					}
					MarkerGroupDTO markerGroupDTORequest = new MarkerGroupDTO(); //isNew ? Header.ProcessType.CREATE : Header.ProcessType.UPDATE
					markerGroupDTORequest.setName(txtName.getText());
					markerGroupDTORequest.setCode(txtName.getText()+"_"+txtGermplasmGroup.getText());
					markerGroupDTORequest.setGermplasmGroup(txtGermplasmGroup.getText());
					markerGroupDTORequest.setMarkers(markers);
					markerGroupDTORequest.setStatusId(1);
					markerGroupDTORequest.setCreatedBy(App.INSTANCE.getUser().getUserId());
					markerGroupDTORequest.setModifiedBy(App.INSTANCE.getUser().getUserId());

					PayloadEnvelope<MarkerGroupDTO> markerGroupDTOResponseEnvelope = null;
					PayloadEnvelope<MarkerGroupDTO> payloadEnvelope = new PayloadEnvelope<>(markerGroupDTORequest, GobiiProcessType.CREATE);
					GobiiEnvelopeRestResource<MarkerGroupDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(GobiiClientContext.getInstance(null, false)
							.getUriFactory()
							.resourceColl(GobiiServiceRequestId.URL_MARKERGROUP));
					markerGroupDTOResponseEnvelope = gobiiEnvelopeRestResource.post(MarkerGroupDTO.class,
							payloadEnvelope);

					if(!markerGroupDTOResponseEnvelope.getHeader().getStatus().isSucceeded()){
						MessageDialog.openError(shell, "Error", " Error saving Marker Group.\n\nPlease check if the marker(s) and platform(s) you are trying to save is already in the database.");
					}
					else if(Controller.getDTOResponse(shell, markerGroupDTOResponseEnvelope.getHeader(), memInfo, true)){
						currentMarkerGroupId = markerGroupDTOResponseEnvelope.getPayload().getData().get(0).getMarkerGroupId();
						populateMarkerGroupList();
						FormUtils.selectRowById(tbList, currentMarkerGroupId);
						txtCode.setText(markerGroupDTOResponseEnvelope.getPayload().getData().get(0).getCode());
					}


				}catch (Exception err) {
					Utils.log(log, "Error saving MarkerGroup", err);
				}
			}
		});
		btnMarkerGroupAdd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		btnMarkerGroupAdd.setText("Add New MarkerGroup");

		btnUpdate = new Button(cmpForm, SWT.NONE);
		btnUpdate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				if(!validate(false)) return;

				try{
					List<MarkerGroupMarkerDTO> markers = new ArrayList<MarkerGroupMarkerDTO>();
					for(TableItem item : table.getItems()){
						MarkerGroupMarkerDTO marker = new MarkerGroupMarkerDTO();
						marker.setPlatformName(item.getText(1));
						marker.setMarkerName(item.getText(0));
						marker.setFavorableAllele(item.getText(2));
						markers.add(marker);
					}

					MarkerGroupDTO markerGroupDTORequest = new MarkerGroupDTO(); 
					markerGroupDTORequest.setMarkerGroupId(currentMarkerGroupId);
					markerGroupDTORequest.setName(txtName.getText());
					markerGroupDTORequest.setCode(txtName.getText()+"_"+txtGermplasmGroup.getText());
					markerGroupDTORequest.setGermplasmGroup(txtGermplasmGroup.getText());
					markerGroupDTORequest.setMarkers(markers);
					markerGroupDTORequest.setStatusId(1);
					markerGroupDTORequest.setCreatedBy(App.INSTANCE.getUser().getUserId());
					markerGroupDTORequest.setModifiedBy(App.INSTANCE.getUser().getUserId());

					PayloadEnvelope<MarkerGroupDTO> markerGroupDTOResponseEnvelope = null;

					RestUri restUriMapsetForGetById = GobiiClientContext.getInstance(null, false)
							.getUriFactory()
							.resourceByUriIdParam(GobiiServiceRequestId.URL_MARKERGROUP);
					restUriMapsetForGetById.setParamValue("id", markerGroupDTORequest.getMarkerGroupId().toString());
					GobiiEnvelopeRestResource<MarkerGroupDTO> gobiiEnvelopeRestResourceForGetById = new GobiiEnvelopeRestResource<>(restUriMapsetForGetById);
					gobiiEnvelopeRestResourceForGetById.setParamValue("id", markerGroupDTORequest.getMarkerGroupId().toString());
					markerGroupDTOResponseEnvelope = gobiiEnvelopeRestResourceForGetById.put(MarkerGroupDTO.class,
							new PayloadEnvelope<>(markerGroupDTORequest, GobiiProcessType.UPDATE));

					if(!markerGroupDTOResponseEnvelope.getHeader().getStatus().isSucceeded()){
						MessageDialog.openError(shell, "Error", " Error saving Marker Group.\n\nPlease make sure that the marker(s) and platform(s) you are trying to save is already in the database.");
					}
					else if(Controller.getDTOResponse(shell, markerGroupDTOResponseEnvelope.getHeader(), memInfo, true)){
						populateMarkerGroupList();
						FormUtils.selectRowById(tbList, currentMarkerGroupId);
						txtCode.setText(markerGroupDTOResponseEnvelope.getPayload().getData().get(0).getCode());
					}
				}catch (Exception err) {
					Utils.log(log, "Error saving MarkerGroup", err);
				}
			}
		});
		btnUpdate.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		btnUpdate.setText("Update MarkerGroup");

		Button btnClearFields = new Button(cmpForm, SWT.NONE);
		btnClearFields.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				cleanDetails();
			}
		});
		btnClearFields.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		btnClearFields.setText("Clear Fields");

		

		btnRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				cleanDetails();
				currentMarkerGroupId = -1;
				selectedName = "";
				tbList.removeAll();
				FormUtils.entrySetToTable(Controller.getMarkerGroupNames(), tbList);
			}
		});
	}

	protected int getTableIndex(String platformMarker) {
		// TODO Auto-generated method stub
		int ctr=0;
		for(String s: tableItemMarkerNames){
			if(s.equals(platformMarker)) break;
			ctr++;
		}
		return ctr;
	}

	protected ArrayList<String> getTableItems() {
		// TODO Auto-generated method stub
		tableItemMarkerNames = new ArrayList<String>();

		for(TableItem item : table.getItems()){
			String s = item.getText(markerCol).toLowerCase()+item.getText(platformCol).toLowerCase();
			tableItemMarkerNames.add(s);
		}
		return tableItemMarkerNames;
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	@Override
	protected void createContent() {
		TableColumn tblclmnNewColumn = new TableColumn(tbList, SWT.NONE);
		tblclmnNewColumn.setWidth(300);
		tblclmnNewColumn.setText("Marker Groups:");

		populateMarkerGroupList();
	}

	private void populateMarkerGroupList() {
		// TODO Auto-generated method stub
		tbList.removeAll();
		FormUtils.entrySetToTable(Controller.getMarkerGroupNames(), tbList);

		tbList.addListener (SWT.Selection, new Listener() {

			public void handleEvent(Event e) {
				cleanDetails();
				if(tbList.getSelectionIndex() < 0) return;
				TableItem item = tbList.getItem(tbList.getSelectionIndex());
				if(item == null) return;
				String selected = item.getText(); //single selection
				currentMarkerGroupId = Integer.parseInt((String) item.getData(selected));
				populateMarkerGroupDetails(currentMarkerGroupId); //retrieve and display projects by contact Id
			}

			protected void populateMarkerGroupDetails(int markerGroupId) {
				try{
					MarkerGroupDTO MarkerGroupDTORequest = new MarkerGroupDTO();
					MarkerGroupDTORequest.setMarkerGroupId(markerGroupId);
					try {
						RestUri restUriMapsetForGetById = GobiiClientContext.getInstance(null, false)
								.getUriFactory()
								.resourceByUriIdParam(GobiiServiceRequestId.URL_MARKERGROUP);
						restUriMapsetForGetById.setParamValue("id", Integer.toString(markerGroupId));
						GobiiEnvelopeRestResource<MarkerGroupDTO> gobiiEnvelopeRestResourceForGetById = new GobiiEnvelopeRestResource<>(restUriMapsetForGetById);
						PayloadEnvelope<MarkerGroupDTO> markerGroupDTOResponseEnvelope = gobiiEnvelopeRestResourceForGetById
								.get(MarkerGroupDTO.class);

						MarkerGroupDTO markerGroupDTOResponse = markerGroupDTOResponseEnvelope.getPayload().getData().get(0);
						selectedName = markerGroupDTOResponse.getName();
						txtName.setText(selectedName);
						txtCode.setText(markerGroupDTOResponse.getCode());
						txtGermplasmGroup.setText(markerGroupDTOResponse.getGermplasmGroup());

						populateTableFromStringList(markerGroupDTOResponse.getMarkers(), table);
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
				item.setText(1, property.getPlatformName()); //index zero - first column 
				item.setText(0,property.getMarkerName());
				item.setText(2, property.getFavorableAllele()); //index zero - first column 
				item.setData(property.getMarkerId());
			}
		}catch(Exception err){
			Utils.log(shell, memInfo, log, "Error retrieving CVs", err);
		}
	}
	protected void cleanDetails() {
		try{
			txtName.setText("");
			txtCode.setText("");
			txtGermplasmGroup.setText("");
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

	protected boolean validate(boolean isNew){
		boolean successful = true;
		String message = null;
		if(txtName.getText().isEmpty()){
			message = "Name field is required";
			successful = false;
		}else if(table.getItemCount() == 0){
			message = "There are no markers in group, please import a list of markers";
			successful = false;
		}else if(!isNew && currentMarkerGroupId<=0){
			message = "'"+txtName.getText()+"' is recognized as a new value. Please use Add instead.";
			successful = false;
		}
		else if(isNew || !txtName.getText().equalsIgnoreCase(selectedName)){
			for(int i=0; i<tbList.getItemCount(); i++){
				if(tbList.getItem(i).getText(0).equalsIgnoreCase(txtName.getText())){
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
