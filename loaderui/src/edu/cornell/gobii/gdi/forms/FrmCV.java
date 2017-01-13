package edu.cornell.gobii.gdi.forms;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.widgets.Text;
import org.gobiiproject.gobiiclient.dtorequests.DtoRequestCv;
import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.container.CvDTO;
import org.gobiiproject.gobiimodel.entity.CvItem;

import edu.cornell.gobii.gdi.main.Main2;
import edu.cornell.gobii.gdi.services.Controller;
import edu.cornell.gobii.gdi.services.IDs;
import edu.cornell.gobii.gdi.utils.FormUtils;
import edu.cornell.gobii.gdi.utils.Utils;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class FrmCV extends AbstractFrm {
	private static Logger log = Logger.getLogger(Main2.class.getName());
	private CvDTO cvDTO;
	private Text txtGroup;
	private Table tbTerms;
//	private TableColumn tblclmnName_1;
	private TableViewer viewerParameters;
	private Button btnInsert;
	protected CvDTO cvDTOResponse;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public FrmCV(Shell shell, Composite parent, int style) {
		super(shell, parent, style);
		((GridData) cmpForm.getLayoutData()).widthHint = 330;
		GridLayout gridLayout = (GridLayout) cmpForm.getLayout();
		gridLayout.numColumns = 2;

		Label lblGroup = new Label(cmpForm, SWT.NONE);
		lblGroup.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblGroup.setText("Group:");

		txtGroup = new Text(cmpForm, SWT.BORDER | SWT.READ_ONLY);
		txtGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblTerms = new Label(cmpForm, SWT.NONE);
		lblTerms.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblTerms.setText("Terms:");

		viewerParameters = new TableViewer(cmpForm, SWT.BORDER | SWT.CHECK | SWT.FULL_SELECTION);
		tbTerms = viewerParameters.getTable();
		tbTerms.setLinesVisible(true);
		tbTerms.setHeaderVisible(true);
		tbTerms.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		TableViewerColumn tableViewerColumn = new TableViewerColumn(viewerParameters, SWT.NONE);
		TableColumn tblclmnName = tableViewerColumn.getColumn();
		tblclmnName.setWidth(150);
		tblclmnName.setText("Term");

		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(viewerParameters, SWT.NONE);
		TableColumn tblclmnDefinition = tableViewerColumn_2.getColumn();
		tblclmnDefinition.setWidth(200);
		tblclmnDefinition.setText("Definition");

		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(viewerParameters, SWT.NONE);
		TableColumn tblclmnRank = tableViewerColumn_1.getColumn();
		tblclmnRank.setWidth(100);
		tblclmnRank.setText("Rank");

		tbTerms.addListener(SWT.MouseDown, event -> {
			TableEditor editor = new TableEditor(tbTerms);
			// The editor must have the same size as the cell and must
			// not be any smaller than 50 pixels.
			editor.horizontalAlignment = SWT.LEFT;
			editor.grabHorizontal = true;
			editor.minimumWidth = 50;
			Control oldEditor = editor.getEditor();
			if (oldEditor != null)
				oldEditor.dispose(); 

			Point pt = new Point(event.x, event.y);
			TableItem item = tbTerms.getItem(pt);
			if (item == null)
				return;

			Text newEditor = new Text(tbTerms, SWT.NONE);
			int EDITABLECOLUMN = -1;
			for (int i = 0; i < tbTerms.getColumnCount(); i++) {
				Rectangle rect = item.getBounds(i);
				if (rect.contains(pt)) {
					EDITABLECOLUMN = i;
					break;
				}
			}

			final int col = EDITABLECOLUMN;
			if(col < 0) return;
			newEditor.setText(item.getText(col));
			Listener textListener = new Listener() {
				public void handleEvent(final Event e) {
					switch (e.type) {
					case SWT.FocusOut:
						item.setText(col, newEditor.getText());
						newEditor.dispose();
						break;
					case SWT.Traverse:
						switch (e.detail) {
						case SWT.TRAVERSE_RETURN:
							item.setText(col, newEditor.getText());
							// FALL THROUGH
						case SWT.TRAVERSE_ESCAPE:
							item.setText(col, newEditor.getText());
							newEditor.dispose();
							e.doit = false;
						}
						break;
					}
				}
			};
			newEditor.addListener(SWT.FocusOut, textListener);
			newEditor.addListener(SWT.Traverse, textListener);
			newEditor.selectAll();
			newEditor.setFocus();           
			editor.setEditor(newEditor, item, col); 
		});
		new Label(cmpForm, SWT.NONE);

		btnInsert = new Button(cmpForm, SWT.NONE);
		btnInsert.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnInsert.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem item = new TableItem(tbTerms, SWT.NONE);
				item.setChecked(true);
			}
		});
		btnInsert.setText("Insert New Record(s)");
		new Label(cmpForm, SWT.NONE);
		
		Button btnSaveSelectedRecords = new Button(cmpForm, SWT.NONE);
		btnSaveSelectedRecords.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnSaveSelectedRecords.setText("Save Selected Record(s)");
		btnSaveSelectedRecords.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e){
				try{
					if(!validate()) return;
					CvDTO cvDTO = null;
					boolean successful = true;
					for(TableItem item : tbTerms.getItems()){
						if(!item.getChecked()) continue;
						boolean isNew = item.getData() == null ? true : false;
						cvDTO = new CvDTO(isNew ? DtoMetaData.ProcessType.CREATE : DtoMetaData.ProcessType.UPDATE);
						if(!isNew) cvDTO.setCvId((Integer) item.getData());
						cvDTO.setGroup(txtGroup.getText());
						cvDTO.setTerm(item.getText(0));
						cvDTO.setDefinition(item.getText(1));
						cvDTO.setRank(Integer.parseInt(item.getText(2)));
						try{
							DtoRequestCv dtoRequestCv = new DtoRequestCv();
							cvDTOResponse = dtoRequestCv.process(cvDTO);
							successful = true;
						}catch(Exception err){
							//						Utils.log(shell, memInfo, log, "Error saving Term", err);
							break;
						}
					}
					if(successful){
						if(Controller.getDTOResponse(shell, cvDTOResponse, memInfo)){
							populateCVDetails(IDs.cvGroupId); 
						}
					}
				}catch(Exception err){
					Utils.log(shell, memInfo, log, "Error saving Term", err);
				}
			}
		});
		new Label(cmpForm, SWT.NONE);

//		Button btnAddNew = new Button(cmpForm, SWT.NONE);
//		btnAddNew.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				try{
//					boolean successful = true;
//					if(!validate()) return;
//					CvDTO cvDTO = null;
//					for(TableItem item : tbTerms.getItems()){
//						if(item.getChecked()){
//							cvDTO = new CvDTO(DtoMetaData.ProcessType.CREATE);
//							cvDTO.setGroup(txtGroup.getText());
//							cvDTO.setTerm(item.getText(0));
//							cvDTO.setDefinition(item.getText(1));
//							cvDTO.setRank(Integer.parseInt(item.getText(2)));
//							try{
//								if(Integer.toString((Integer) item.getData()) != null){
//									MessageBox dialog = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
//									dialog.setMessage("'"+cvDTO.getTerm()+" is recognized as an existing value, please use update instead.");
//									dialog.open();
//									successful=false;
//								}
//							}catch(NullPointerException npe){
//								try{
//									DtoRequestCv dtoRequestCv = new DtoRequestCv();
//									cvDTOResponse = dtoRequestCv.process(cvDTO);
//									successful = true;
//								}catch(Exception err){
////									Utils.log(shell, memInfo, log, "Error saving Term", err);
//									break;
//								}
//							}
//						}
//					}
//					if(successful){
//						if(Controller.getDTOResponse(shell, cvDTOResponse, memInfo)){
//							populateCVDetails(IDs.cvGroupId); 
//						}
//					}
//				}catch(Exception err){
//					Utils.log(shell, memInfo, log, "Error saving Term", err);
//				}
//			}
//		});
//		btnAddNew.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
//		btnAddNew.setText("Add Selected Record(s)");
//		new Label(cmpForm, SWT.NONE);
		
//		Button btnUpdate = new Button(cmpForm, SWT.NONE);
//		btnUpdate.addSelectionListener(new SelectionAdapter() {
//
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				try{
//					cvDTO = null;
//					if(!validate()) return;
//					for(TableItem item : tbTerms.getItems()){
//						if(item.getChecked()){
//							cvDTO = new CvDTO(DtoMetaData.ProcessType.UPDATE);
//							cvDTO.setGroup(txtGroup.getText());
//							cvDTO.setTerm(item.getText(0));
//							cvDTO.setDefinition(item.getText(1));
//							cvDTO.setRank(Integer.parseInt(item.getText(2)));
//							cvDTO.setCvId((Integer) item.getData());
//							try{
//								if(!cvDTO.getCvId().equals(null)){
//									try{
//										DtoRequestCv dtoRequestCv = new DtoRequestCv();
//										cvDTOResponse = dtoRequestCv.process(cvDTO);
//									}catch(Exception err){
//										Utils.log(shell, memInfo, log, "Error saving Term", err);
//										break;
//									}
//								}
//							}catch(NullPointerException npe){
//
//								MessageBox dialog = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
//								dialog.setMessage("'"+cvDTO.getTerm()+"' is recognized as a new value, please Add it as a new record.");
//								dialog.open();
//							}
//						}
//					}
//					if(Controller.getDTOResponse(shell, cvDTOResponse, memInfo)){
//						populateCVDetails(IDs.cvGroupId); 
//					}
//				}catch(Exception err){
//					Utils.log(shell, memInfo, log, "Error saving Terms", err);
//				}
//			}
//		});
//		btnUpdate.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
//		btnUpdate.setText("Update Selected Record(s)");
//		new Label(cmpForm, SWT.NONE);

		Button btnDelete = new Button(cmpForm, SWT.NONE);
		btnDelete.setEnabled(false);
		btnDelete.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try{
					MessageBox messageBox = new MessageBox(shell, SWT.ICON_WARNING | SWT.YES | SWT.NO);
					messageBox.setMessage("Deleting this TERM will affect data that is associated with it. Do you want to continue?");
					int msg = messageBox.open();
					if(msg == SWT.NO) return;
					cvDTO = null;
					if(!validate()) return;
					for(TableItem item : tbTerms.getItems()){
						if(item.getChecked()){
							cvDTO = new CvDTO(DtoMetaData.ProcessType.DELETE);
							cvDTO.setCvId((Integer) item.getData());
							cvDTO.setGroup(txtGroup.getText());
							cvDTO.setTerm(item.getText(0));
							cvDTO.setDefinition(item.getText(1));
							cvDTO.setRank(Integer.parseInt(item.getText(2)));
							try{
								if(!cvDTO.getCvId().equals(null)){
									try{
										DtoRequestCv dtoRequestCv = new DtoRequestCv();
										cvDTOResponse = dtoRequestCv.process(cvDTO);
									}catch(Exception err){
										Utils.log(shell, memInfo, log, "Error saving Term", err);
										break;
									}
								}
							}catch(NullPointerException npe){

								MessageBox dialog = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
								dialog.setMessage("Failed to delete '"+cvDTO.getTerm()+"'. It is recognized as a new value.");
								dialog.open();
							}
						}
					}
					if(Controller.getDTOResponse(shell, cvDTOResponse, memInfo)){
						populateCVDetails(IDs.cvGroupId); 
					}
				}catch(Exception err){
					Utils.log(shell, memInfo, log, "Error saving Terms", err);
				}
				
				cleanParameterDetails(false);
			
			}
		});
		btnDelete.setText("Delete Selected Record(s)");

	}

	protected void clearCvDetails() {
		tbTerms.removeAll();
		txtGroup.setText("");

	}

	protected void cleanParameterDetails(boolean deleteAll) {
		// TODO Auto-generated method stub
		for(TableItem item : tbTerms.getItems()){
			if(deleteAll || item.getChecked()){
				item.dispose();
			}
		}
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	@Override
	protected void createContent() {
		// TODO Auto-generated method stub
		populateCvGroupTable(tbList);
		
		TableColumn tblclmnCvGroups = new TableColumn(tbList, SWT.NONE);
		tblclmnCvGroups.setWidth(300);
		tblclmnCvGroups.setText("CV Groups:");
		cbList.setEnabled(false);
		tbList.addListener (SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				try{
					String selected = tbList.getSelection()[0].getText(); //single selection
					IDs.cvGroupId= Integer.parseInt((String) tbList.getSelection()[0].getData(selected));
					populateCVDetails(IDs.cvGroupId); //retrieve and display projects by contact Id
				}catch(Exception err){
					Utils.log(shell, memInfo, log, "Error retrieving CVs", err);
				}
			}
		});
	}

	protected void populateCVDetails(int cvGroupId) {
		try{
			DtoRequestCv dtoRequestCv = new DtoRequestCv();
			CvDTO cvDTORequest = new CvDTO();
			cvDTORequest.setIncludeDetailsList(true);
			cvDTORequest.setCvId(cvGroupId);

			CvDTO cvDTOResponse = null;
			try {
				cvDTOResponse = dtoRequestCv.process(cvDTORequest);
				txtGroup.setText(cvDTOResponse.getGroup());

				populateCvTableByGroup(cvDTOResponse.getGroupCvItems().get(txtGroup.getText()), tbTerms);
			} catch(Exception err){
				Utils.log(shell, memInfo, log, "Error retrieving CVs", err);
			}
		}catch(Exception err){
			Utils.log(shell, memInfo, log, "Error retrieving CVs", err);
		}
	}

	private void populateCvTableByGroup(List<CvItem> list, Table table) {
		try{
			table.removeAll();

			TableItem item = null;
			for (CvItem c : list){ //add project on list
				item= new TableItem(table, SWT.NONE); 
				item.setText(0, c.getTerm()); //index zero - first column 
				item.setText(1, c.getDefinition());
				item.setText(2, Integer.toString(c.getRank())); //index zero - first column 
				item.setData(c.getCvId());
			}
		}catch(Exception err){
			Utils.log(shell, memInfo, log, "Error retrieving CVs", err);
		}
	}

	private void populateCvGroupTable(Table tbl) {
		try{
			clearTable(tbl);
			FormUtils.entrySetToTable(Controller.getCvGroupNames(), tbl);
		}catch(Exception err){
			Utils.log(shell, memInfo, log, "Error retrieving CVs", err);
		}
	}

	private void clearTable(Table tbl) {
		try{
			for(TableItem item : tbl.getItems()){
				item.dispose();
			}
		}catch(Exception err){
			Utils.log(shell, memInfo, log, "Error clearing fields", err);
		}
	}

	private boolean validate(){
		boolean successful = true;
		String message = null;
		MessageBox dialog = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
		if(txtGroup.getText().isEmpty()){//
			message = "Group field is required!";
			successful = false;
		}else if(txtGroup.getText().isEmpty()){
			message = "Group definition is required!";
			successful = false;
		}else{
			int checked = 0;
			for(int i=0; i<viewerParameters.getTable().getItemCount(); i++){
//			for(TableItem tbItem : viewerParameters.getTable().getItems()){
				TableItem tbItem = viewerParameters.getTable().getItem(i);
				
				if(tbItem.getChecked()){
					checked++;
					if(tbItem.getText(0).isEmpty()){
						message = "Column 'term' is required!";
						successful = false;
						break;
					}
					if(tbItem.getText(1).isEmpty()){message = "Column 'definition' is required!";
					successful = false;
					break;

					}
					if(tbItem.getText(2).isEmpty()){message = "Column 'rank' is required!";
					successful = false;
					break;
					}else{
						try{
							if(Integer.parseInt(tbItem.getText(2)) >0) continue;
							
						}catch(NumberFormatException npe){
							message = "Column 'rank' should be an integer!";
							successful = false;
							break;
						}
					}
					for(int j=0; j<i; j++){
						if(tbItem.getText(0).equalsIgnoreCase(viewerParameters.getTable().getItem(j).getText(0))){
							message = "Term \""+tbItem.getText(0)+"\" is duplicated, please change the name of this term!";
							successful = false;
							break;
						}
					}
				}
			}
			if (checked==0){
				message = "Please select a record!";
				successful = false;
			}

		}
		if(!successful){
			dialog.setMessage(message);
			dialog.open();
		}
		return successful;
	}
}
