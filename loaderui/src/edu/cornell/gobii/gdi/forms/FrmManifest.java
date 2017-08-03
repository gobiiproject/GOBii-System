package edu.cornell.gobii.gdi.forms;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiapimodel.types.GobiiServiceRequestId;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiimodel.headerlesscontainer.AnalysisDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.ManifestDTO;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;

import edu.cornell.gobii.gdi.main.App;
import edu.cornell.gobii.gdi.services.Controller;
import edu.cornell.gobii.gdi.services.IDs;
import edu.cornell.gobii.gdi.utils.FormUtils;
import edu.cornell.gobii.gdi.utils.Utils;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.wb.swt.SWTResourceManager;

public class FrmManifest extends AbstractFrm {
	private static Logger log = Logger.getLogger(FrmManifest.class.getName());
	private Text txtName;
	private Text txtCode;
	private Text txtFilePath;
	protected Integer currentManifestId=0;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public FrmManifest(Shell shell, Composite parent, int style) {
		super(shell, parent, style);
		cbList.setEnabled(false);
		GridLayout gridLayout = (GridLayout) cmpForm.getLayout();
		gridLayout.numColumns = 2;
		
		Label lblName = new Label(cmpForm, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblName.setText("*Manifest Name:");
		
		txtName = new Text(cmpForm, SWT.BORDER);
		txtName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblCode = new Label(cmpForm, SWT.NONE);
		lblCode.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblCode.setText("Code:");
		
		txtCode = new Text(cmpForm, SWT.BORDER | SWT.READ_ONLY);
		txtCode.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtCode.setEditable(false);
		txtCode.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblFilePath = new Label(cmpForm, SWT.NONE);
		lblFilePath.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFilePath.setText("File Path:");
		
		txtFilePath = new Text(cmpForm, SWT.BORDER);
		txtFilePath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(cmpForm, SWT.NONE);
		
		Button btnAddNew = new Button(cmpForm, SWT.NONE);
		btnAddNew.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try{
					if(!validate(true)) return;

					ManifestDTO manifestDTORequest = new ManifestDTO();
					String name = txtName.getText();
					manifestDTORequest.setName(name);
					manifestDTORequest.setCode(name);
					manifestDTORequest.setFilePath(txtFilePath.getText());

					try {
						PayloadEnvelope<ManifestDTO> payloadEnvelope = new PayloadEnvelope<>(manifestDTORequest,
								GobiiProcessType.CREATE);
						GobiiEnvelopeRestResource<ManifestDTO> restResource = new GobiiEnvelopeRestResource<>( GobiiClientContext.getInstance(null, false).getUriFactory().resourceColl(GobiiServiceRequestId.URL_MANIFEST));
						PayloadEnvelope<ManifestDTO> manifestDTOResponse = restResource.post(ManifestDTO.class,
								payloadEnvelope);
						
						if(Controller.getDTOResponse(shell, manifestDTOResponse.getHeader(), memInfo, true)){
							currentManifestId = manifestDTOResponse.getPayload().getData().get(0).getManifestId();
							populateManifestTable();
							FormUtils.selectRowById(tbList,currentManifestId);
							selectedName = txtName.getText();

							txtCode.setText(manifestDTOResponse.getPayload().getData().get(0).getCode());
						};
					} catch (Exception err) {
						Utils.log(shell, memInfo, log, "Error retrieving Manifests", err);
					}
				}catch(Exception err){
					Utils.log(shell, memInfo, log, "Error retrieving Manifests", err);
				}
			}
		});
		btnAddNew.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnAddNew.setText("Add New");
		new Label(cmpForm, SWT.NONE);
		
		Button btnUpdate = new Button(cmpForm, SWT.NONE);
		btnUpdate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try{
					if(!validate(false)) return;
					if(!FormUtils.updateForm(getShell(), "Manifest", selectedName)) return;
					ManifestDTO manifestDTORequest = new ManifestDTO();
					manifestDTORequest.setManifestId(currentManifestId);
					String name = txtName.getText();
					manifestDTORequest.setName(name);
					manifestDTORequest.setCode(name);
					manifestDTORequest.setFilePath(txtFilePath.getText());

					try {
						RestUri restUri =  GobiiClientContext.getInstance(null, false).getUriFactory().resourceByUriIdParam(GobiiServiceRequestId.URL_MANIFEST);
						restUri.setParamValue("id", Integer.toString(currentManifestId));
						GobiiEnvelopeRestResource<ManifestDTO> restResourceById = new GobiiEnvelopeRestResource<>(restUri);
						restResourceById.setParamValue("id", manifestDTORequest.getManifestId().toString());
						PayloadEnvelope<ManifestDTO> manifestDTOResponse = restResourceById.put(
								ManifestDTO.class, new PayloadEnvelope<>(manifestDTORequest, GobiiProcessType.UPDATE));
						
						if(Controller.getDTOResponse(shell, manifestDTOResponse.getHeader(), memInfo, true)){
							populateManifestTable();
							FormUtils.selectRowById(tbList,currentManifestId);
							txtCode.setText(manifestDTOResponse.getPayload().getData().get(0).getCode());
						};
					} catch (Exception err) {
						Utils.log(shell, memInfo, log, "Error retrieving Manifests", err);
					}
				}catch(Exception err){
					Utils.log(shell, memInfo, log, "Error retrieving Manifests", err);
				}
			}
		});
		btnUpdate.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnUpdate.setText("Update");
		new Label(cmpForm, SWT.NONE);
		
		Button btnClearFields = new Button(cmpForm, SWT.NONE);
		btnClearFields.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				clearDetails();
			}
		});
		btnClearFields.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnClearFields.setText("Clear Fields");

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	@Override
	protected void createContent() {

		TableColumn tblclmnManifests = new TableColumn(tbList, SWT.NONE);
		tblclmnManifests.setWidth(300);
		tblclmnManifests.setText("Manifests:");
		
		populateManifestTable();
		
		btnRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				populateManifestTable();
				clearDetails();
				currentManifestId=0;
			}
		});
		
		tbList.addListener (SWT.Selection, new Listener() {

			public void handleEvent(Event e) {
				String selected = tbList.getSelection()[0].getText(); //single selection
				selectedName = selected;
				currentManifestId = Integer.parseInt((String) tbList.getSelection()[0].getData(selected));
				populateManifestDetails(currentManifestId); 
			}


			protected void populateManifestDetails(int manifestId) {
				try{
					ManifestDTO manifestDTO = new ManifestDTO();
					manifestDTO.setManifestId(manifestId);
					try {
						RestUri restUri =  GobiiClientContext.getInstance(null, false).getUriFactory().resourceByUriIdParam(GobiiServiceRequestId.URL_MANIFEST);
						restUri.setParamValue("id", Integer.toString(manifestId));
						GobiiEnvelopeRestResource<ManifestDTO> restResource = new GobiiEnvelopeRestResource<>(restUri);
						PayloadEnvelope<ManifestDTO> dtoRequestManifest = restResource.get(ManifestDTO.class);
						manifestDTO = dtoRequestManifest.getPayload().getData().get(0);
						
						//displayDetails
						txtName.setText(manifestDTO.getName());
						txtCode.setText(manifestDTO.getCode());
						txtFilePath.setText(manifestDTO.getFilePath()==null ? "" : manifestDTO.getFilePath());
						selectedName = manifestDTO.getName();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				
				}catch(Exception err){
					Utils.log(shell, memInfo, log, "Error retrieving Manifests", err);
				}
			}
		});
	}
	private void populateManifestTable() {
		try{
			tbList.removeAll();
			FormUtils.entrySetToTable(Controller.getManifestNames(), tbList);

		}catch(Exception err){
			Utils.log(shell, memInfo, log, "Error retrieving Manifests", err);
		}
	}
	
	private void clearDetails(){
		try{
			txtName.setText("");
			txtCode.setText("");
			txtFilePath.setText("");
		}catch(Exception err){
			Utils.log(shell, memInfo, log, "Error clearing fields", err);
		}
	}
	
	private boolean validate(boolean isNew){
		boolean successful = true;
		String message = null;
		if(txtName.getText().isEmpty()){
			successful = false;
			message = "Name is required field!";
		}else if(!isNew && currentManifestId==0){
			message = "'"+txtName.getText()+"' is recognized as a new value. Please use Add instead.";
			successful = false;
		}else if(isNew || !txtName.getText().equalsIgnoreCase(selectedName)){
			for(TableItem item : tbList.getItems()){
				if(item.getText().equalsIgnoreCase(txtName.getText())){
					successful = false;
					message = "Manifest name already exists!";
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
