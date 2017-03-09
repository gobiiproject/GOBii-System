package edu.cornell.gobii.gdi.services;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.RestUri;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;
import org.gobiiproject.gobiiclient.core.common.ClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiimodel.tobemovedtoapimodel.Header;
import org.gobiiproject.gobiimodel.tobemovedtoapimodel.HeaderStatusMessage;
import org.gobiiproject.gobiimodel.entity.TableColDisplay;
import org.gobiiproject.gobiimodel.headerlesscontainer.ContactDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.DisplayDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.ExperimentDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.NameIdDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.OrganizationDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.PlatformDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.ProtocolDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.VendorProtocolDTO;
import org.gobiiproject.gobiimodel.types.GobiiCropType;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
import org.gobiiproject.gobiimodel.types.GobiiFilterType;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.gobiiproject.gobiimodel.types.SystemUserDetail;
import org.gobiiproject.gobiimodel.types.SystemUserNames;
import org.gobiiproject.gobiimodel.types.SystemUsers;

import edu.cornell.gobii.gdi.forms.FrmProtocol;
import edu.cornell.gobii.gdi.main.App;
import edu.cornell.gobii.gdi.main.Main2;
import edu.cornell.gobii.gdi.utils.Utils;

public class Controller {
	private static Logger log = Logger.getLogger(Controller.class.getName());
public static String gobiiVersion = null;

	private static List<NameIdDTO> testNameRetrieval(GobiiEntityNameType gobiiEntityNameType,
			GobiiFilterType gobiiFilterType,
			String filterValue) throws Exception {
		List<NameIdDTO> returnVal = null;

		RestUri namesUri = App.INSTANCE.getUriFactory().nameIdListByQueryParams();
		GobiiEnvelopeRestResource<NameIdDTO> restResource = new GobiiEnvelopeRestResource<>(namesUri);

		namesUri.setParamValue("entity", gobiiEntityNameType.toString().toLowerCase());

		if (GobiiFilterType.NONE != gobiiFilterType) {
			namesUri.setParamValue("filterType", StringUtils.capitalize(gobiiFilterType.toString().toUpperCase()));
			namesUri.setParamValue("filterValue", filterValue);
		}


		PayloadEnvelope<NameIdDTO> resultEnvelope = restResource
				.get(NameIdDTO.class);

		String assertionErrorStem = "Error testing name-id retrieval of entity "
				+ gobiiEntityNameType.toString();

		if (GobiiFilterType.NONE != gobiiFilterType) {

			assertionErrorStem += " with filter type "
					+ gobiiFilterType.toString()
					+ " and filter value "
					+ filterValue;
		}

		assertionErrorStem += ": ";

		if(Controller.getDTOResponse(Display.getCurrent().getActiveShell(), resultEnvelope.getHeader(), null, false)){
			returnVal = resultEnvelope.getPayload().getData();
		}

		return returnVal;
	}

	public static PayloadEnvelope<ProtocolDTO> getProtocolDetailsByExperimentId(Integer experimentId) throws Exception{
		 RestUri restUriProtocolsForGetDetailsByExperimentId = ClientContext.getInstance(null, false)
	                .getUriFactory()
	                .resourceColl(ServiceRequestId.URL_EXPERIMENTS)
	                .addUriParam("experimentId")
	                .setParamValue("experimentId", Integer.toString(experimentId))
	                .appendSegment(ServiceRequestId.URL_PROTOCOL);

		GobiiEnvelopeRestResource<ProtocolDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(restUriProtocolsForGetDetailsByExperimentId);
		PayloadEnvelope<ProtocolDTO> resultEnvelope = gobiiEnvelopeRestResource
				.get(ProtocolDTO.class);
		
		return resultEnvelope;
	}

	public static List<NameIdDTO> getAnalysisNames() {
		// TODO Auto-generated method stub
		List<NameIdDTO> returnVal = null;
		try {
			returnVal = testNameRetrieval(GobiiEntityNameType.ANALYSES, GobiiFilterType.NONE, null);
		} catch (Exception err) {
			// TODO Auto-generated catch block

			Utils.showLog( log, "Error getting list of analysis names", err);
		}
		return returnVal;
	}

	public static List<NameIdDTO> getAnalysisTypes() {
		// TODO Auto-generated method stub
		List<NameIdDTO> returnVal = null;
		try {
			returnVal =   testNameRetrieval(GobiiEntityNameType.CVTERMS, GobiiFilterType.BYTYPENAME, "analysis_type");
		} catch (Exception err) {
			// TODO Auto-generated catch block

			Utils.showLog( log, "Error getting list of analysis types", err);
		}
		return returnVal;
	}

	public static List<NameIdDTO> getReferenceNames() {
		// TODO Auto-generated method stub
		List<NameIdDTO> returnVal = null;
		try {
			returnVal =  testNameRetrieval(GobiiEntityNameType.REFERENCES, GobiiFilterType.NONE, null);
		} catch (Exception err) {
			// TODO Auto-generated catch block

			Utils.showLog( log, "Error getting list of Reference names", err);
		}
		return returnVal;
	}

	public static List<NameIdDTO> getContactNames() {
		// TODO Auto-generated method stub
		List<NameIdDTO> returnVal = null;
		try {
			returnVal =  testNameRetrieval(GobiiEntityNameType.CONTACTS, GobiiFilterType.NONE, null);
		} catch (Exception err) {
			// TODO Auto-generated catch block
			Utils.showLog( log, "Error getting list of Contact names", err);
		}
		return returnVal;
	}

	public static List<NameIdDTO> getPIContactNames() {
		// TODO Auto-generated method stub

		List<NameIdDTO> returnVal = null;
		try {
			returnVal =  testNameRetrieval(GobiiEntityNameType.CONTACTS, GobiiFilterType.BYTYPENAME, "PI");
		} catch (Exception err) {
			// TODO Auto-generated catch block
			Utils.showLog( log, "Error getting list of PI Contact names", err);
		}
		return returnVal;
	}

	public static List<NameIdDTO> getProjectNamesByContactId(Integer selectedContactId) {
		// TODO Auto-generated method stub

		List<NameIdDTO> returnVal = null;
		try {
			returnVal =  testNameRetrieval(GobiiEntityNameType.PROJECTS, GobiiFilterType.BYTYPEID, Integer.toString(selectedContactId));
		} catch (Exception err) {
			// TODO Auto-generated catch block
			Utils.showLog( log, "Error getting Project names by contact id.", err);
		}
		return returnVal;
	}

	public static List<NameIdDTO> getManifestNames() {
		// TODO Auto-generated method stub
		List<NameIdDTO> returnVal = null;
		try {
			returnVal =  testNameRetrieval(GobiiEntityNameType.MANIFESTS, GobiiFilterType.NONE,null);
		} catch (Exception err) {
			// TODO Auto-generated catch block
			Utils.showLog( log, "Error getting list of Manifest names", err);
		}
		return returnVal;
	}

	public static List<NameIdDTO> getPlatformNames() {
		// TODO Auto-generated method stub

		List<NameIdDTO> returnVal = null;
		try {
			returnVal =  testNameRetrieval(GobiiEntityNameType.PLATFORMS, GobiiFilterType.NONE,null);
		} catch (Exception err) {
			// TODO Auto-generated catch block
			Utils.showLog( log, "Error getting list of Platform names", err);
		}
		return returnVal;
	}

	public static List<NameIdDTO> getProtocolNames() {
		// TODO Auto-generated method stub

		List<NameIdDTO> returnVal = null;
		try {
			returnVal =  testNameRetrieval(GobiiEntityNameType.PROTOCOLS, GobiiFilterType.NONE,null);
		} catch (Exception err) {
			// TODO Auto-generated catch block
			Utils.showLog( log, "Error getting list of Protocol names", err);
		}
		return returnVal;
	}

	public static List<NameIdDTO> getVendorProtocolNames(){
		List<NameIdDTO> returnVal = null;
		try{
			returnVal = testNameRetrieval(GobiiEntityNameType.VENDORS_PROTOCOLS, GobiiFilterType.NONE, null);
		}catch (Exception err) {
			// TODO Auto-generated catch block
			Utils.showLog( log, "Error getting list of Vendor-Protocol names", err);
		}
		return returnVal;
	}

	public static List<NameIdDTO> getProtocolNamesByPlatformId(Integer selectedId) {
		// TODO Auto-generated method stub

		List<NameIdDTO> returnVal = null;
		try {
			returnVal =  testNameRetrieval(GobiiEntityNameType.PROTOCOLS, GobiiFilterType.BYTYPEID, Integer.toString(selectedId));
		} catch (Exception err) {
			// TODO Auto-generated catch block
			Utils.showLog( log, "Error getting list of Protocol names by Platform id", err);
		}
		return returnVal;
	}

	public static List<NameIdDTO> getVendorProtocolNamesByProtocolId(Integer selectedId) {
		// TODO Auto-generated method stub

		List<NameIdDTO> returnVal = null;
		try {
			returnVal =  testNameRetrieval(GobiiEntityNameType.VENDORS_PROTOCOLS, GobiiFilterType.BYTYPEID, Integer.toString(selectedId));
		} catch (Exception err) {
			// TODO Auto-generated catch block
			Utils.showLog( log, "Error getting list of Vendor-Protocol names by Protocol id.", err);
		}
		return returnVal;
	}



	public static List<NameIdDTO> getExperimentNamesByProjectId(Integer selectedId) {
		// TODO Auto-generated method stub

		List<NameIdDTO> returnVal = null;
		try {
			returnVal =  testNameRetrieval(GobiiEntityNameType.EXPERIMENTS, GobiiFilterType.BYTYPEID, Integer.toString(selectedId));
		} catch (Exception err) {
			// TODO Auto-generated catch block
			Utils.showLog( log, "Error getting list of Experiment names by Project id", err);
		}
		return returnVal;
	}

	public static List<NameIdDTO> getDataSetNamesByExperimentId(Integer selectedId) {
		// TODO Auto-generated method stub
		List<NameIdDTO> returnVal = null;
		try {
			returnVal =    testNameRetrieval(GobiiEntityNameType.DATASETS, GobiiFilterType.BYTYPEID, Integer.toString(selectedId));
		} catch (Exception err) {
			// TODO Auto-generated catch block
			Utils.showLog( log, "Error getting list of Dataset names by Experiment id.", err);
		}
		return returnVal;
	}

	public static List<NameIdDTO> getMapNames() {
		// TODO Auto-generated method stub
		List<NameIdDTO> returnVal = null;
		try {
			returnVal =  testNameRetrieval(GobiiEntityNameType.MAPSETS, GobiiFilterType.NONE, null);
		} catch (Exception err) {
			// TODO Auto-generated catch block
			Utils.showLog( log, "Error getting list of Map names", err);
		}
		return returnVal;
	}

	public static List<NameIdDTO> getMapTypes() {
		List<NameIdDTO> returnVal = null;
		try {
			returnVal =  testNameRetrieval(GobiiEntityNameType.CVTERMS, GobiiFilterType.BYTYPENAME, "mapset_type");
		} catch (Exception err) {
			// TODO Auto-generated catch block
			Utils.showLog( log, "Error getting list of Map types", err);
		}
		return returnVal;
	}

	public static List<NameIdDTO> getMapNamesByTypeId(int mapTypeId) {
		// TODO Auto-generated method stub
		List<NameIdDTO> returnVal = null;
		try {
			returnVal =  testNameRetrieval(GobiiEntityNameType.MAPSETS, GobiiFilterType.BYTYPEID, Integer.toString(mapTypeId));
		} catch (Exception err) {
			// TODO Auto-generated catch block
			Utils.showLog( log, "Error getting list of Map names by type id.", err);
		}
		return returnVal;
	}

	public static List<NameIdDTO> getMarkerGroupNames() {
		// TODO Auto-generated method stub
		List<NameIdDTO> returnVal = null;
		try {
			returnVal =  testNameRetrieval(GobiiEntityNameType.MARKERGROUPS, GobiiFilterType.NONE, null);
		} catch (Exception err) {
			// TODO Auto-generated catch block
			Utils.showLog( log, "Error getting list of Marker names", err);
		}
		return returnVal;
	}

	public static Set<Entry<String, List<TableColDisplay>>> getTableDisplayNames() {
		// TODO Auto-generated method stub

		DisplayDTO displayDTORequest = new DisplayDTO();
		displayDTORequest.getTableNamesWithColDisplay();
		displayDTORequest.setIncludeDetailsList(true);

		DisplayDTO displayDTOResponse = null;
		try {
			RestUri restUriDisplay = ClientContext.getInstance(null,false)
					.getUriFactory()
					.resourceColl(ServiceRequestId.URL_DISPLAY);
			GobiiEnvelopeRestResource<DisplayDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(restUriDisplay);
			PayloadEnvelope<DisplayDTO> resultEnvelope = gobiiEnvelopeRestResource.get(DisplayDTO.class);
			displayDTOResponse = resultEnvelope.getPayload().getData().get(0);

			return displayDTOResponse.getTableNamesWithColDisplay().entrySet();
		} catch (Exception err) {
			// TODO Auto-generated catch block
			Utils.showLog( log, "Error getting list of Table Display names", err);
		}
		return null;
	}

	public static List<NameIdDTO> getAnalysisNamesByTypeId(int analysisTypeId) {
		// TODO Auto-generated method stub
		List<NameIdDTO> returnVal = null;
		try {
			returnVal =  testNameRetrieval(GobiiEntityNameType.ANALYSES, GobiiFilterType.BYTYPEID, Integer.toString(analysisTypeId));
		} catch (Exception err) {
			// TODO Auto-generated catch block
			Utils.showLog( log, "Error getting list of Analysis names by type id.", err);
		}
		return returnVal;
	}

	public static List<NameIdDTO> getAnalysisNamesByType(String string) {
		// TODO Auto-generated method stub
		List<NameIdDTO> types = getAnalysisTypes();
		int typeId = 0;
		for (NameIdDTO entry : types) {
			if (entry.getName().equals(string)) {
				typeId = entry.getId();
				break;
			}
		}

		return getAnalysisNamesByTypeId(typeId);
	}

	public static List<NameIdDTO> getRoleNames() {
		// TODO Auto-generated method stub
		List<NameIdDTO> returnVal = null;
		try {
			returnVal =  testNameRetrieval(GobiiEntityNameType.ROLES,GobiiFilterType.NONE,null);
		} catch (Exception err) {
			// TODO Auto-generated catch block
			Utils.showLog( log, "Error getting list of Role names", err);
		}
		return returnVal;
	}

	public static List<NameIdDTO> getContactNamesByType(String selected) {
		// TODO Auto-generated method stubNameIdListDTO nameIdListDTO = null;
		List<NameIdDTO> returnVal = null;
		try {
			returnVal =  testNameRetrieval(GobiiEntityNameType.CONTACTS, GobiiFilterType.BYTYPENAME, selected);
		} catch (Exception err) {
			// TODO Auto-generated catch block
			Utils.showLog( log, "Error getting list of Contact names by type.", err);
		}
		return returnVal;
	}

	public static List<NameIdDTO> getCVByGroup(String selectedGroup) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stubNameIdListDTO nameIdListDTO = null;
		List<NameIdDTO> returnVal = null;
		try {
			returnVal = testNameRetrieval(GobiiEntityNameType.CVTERMS, GobiiFilterType.BYTYPENAME, selectedGroup);
		} catch (Exception err) {
			// TODO Auto-generated catch block
			Utils.showLog( log, "Error getting list of CV terms by group.", err);
		}
		return returnVal;
	}

	public static List<NameIdDTO> getCvGroupNames() {
		// TODO Auto-generated method stub
		List<NameIdDTO> returnVal = null;
		try {
			returnVal =  testNameRetrieval(GobiiEntityNameType.CVGROUPS, GobiiFilterType.NONE, null);
		} catch (Exception err) {
			// TODO Auto-generated catch block
			Utils.showLog( log, "Error getting list of Cv Group names", err);
		}
		return returnVal;
	}

	public static List<NameIdDTO> getCvNames() {
		// TODO Auto-generated method stub
		List<NameIdDTO> returnVal = null;
		try {
			returnVal =  testNameRetrieval(GobiiEntityNameType.CVTERMS, GobiiFilterType.NONE, null);
		} catch (Exception err) {
			// TODO Auto-generated catch block
			Utils.showLog( log, "Error getting list of Cv names", err);
		}
		return returnVal;
	}

	public static List<NameIdDTO> getProjectNames() {
		// TODO Auto-generated method stub
		// Authenticator.authenticate
		List<NameIdDTO> returnVal = null;
		try {
			returnVal =  testNameRetrieval(GobiiEntityNameType.PROJECTS, GobiiFilterType.NONE, null);
		} catch (Exception err) {
			// TODO Auto-generated catch block
			Utils.showLog( log, "Error getting list of Project names", err);
		}
		return returnVal;
	}

	public static List<NameIdDTO> getExperimentNames() {
		// TODO Auto-generated method stub
		List<NameIdDTO> returnVal = null;
		try {
			returnVal =   testNameRetrieval(GobiiEntityNameType.EXPERIMENTS, GobiiFilterType.NONE, null);
		} catch (Exception err) {
			// TODO Auto-generated catch block
			Utils.showLog( log, "Error getting list of Experiment names", err);
		}
		return returnVal;
	}

	public static List<NameIdDTO> getDataSetNames() {
		// TODO Auto-generated method stub

		List<NameIdDTO> returnVal = null;
		try {
			returnVal =   testNameRetrieval(GobiiEntityNameType.DATASETS, GobiiFilterType.NONE, null);
		} catch (Exception err) {
			// TODO Auto-generated catch block
			Utils.showLog( log, "Error getting list of Dataset names", err);
		}
		return returnVal;
	}

	public static void showException(Shell shell, StyledText memo, Throwable throwable) {

		MessageBox dialog = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
		dialog.setText("Exception");
		dialog.setMessage("An Exception ocurred. Please refer to message box for details");
		String message = throwable.getMessage() + ": " + throwable.getStackTrace().toString();
		if (memo == null) {
			dialog.setMessage(message);
		} else {
			memo.setText(message);
		}
		dialog.open();


	}

	public static boolean getDTOResponse(Shell shell, Header header, StyledText memo, Boolean showSuccess) {
		MessageBox dialog;
		boolean headerStatusIsSuccessful=false;
		if (!header.getStatus().isSucceeded()) {
			dialog = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
			dialog.setText("Request information");
			dialog.setMessage("ERROR processing request. Please refer to message box for details");
			String message = "";
			for (HeaderStatusMessage currentStatusMesage : header.getStatus().getStatusMessages()) {
				message += currentStatusMesage.getMessage() + "\n";
			}
			if (memo == null) {
				dialog.setMessage(message);
			} else {
				memo.setText(message);
			}
			dialog.open();
		} else {
			dialog = new MessageBox(shell, SWT.ICON_INFORMATION | SWT.OK);
			dialog.setText("Request information");
			dialog.setMessage("Request processed successfully!");
			headerStatusIsSuccessful = true;
			if(showSuccess) dialog.open();
		}
		setGobiiVersion(header.getGobiiVersion(), showSuccess);
		
		return headerStatusIsSuccessful;
	}

	public static List<NameIdDTO> getPlatformNamesByTypeId(int platformTypeId) {
		// TODO Auto-generated method stub
		List<NameIdDTO> returnVal = null;
		try {
			returnVal =testNameRetrieval(GobiiEntityNameType.PLATFORMS, GobiiFilterType.BYTYPEID, Integer.toString(platformTypeId));
		} catch (Exception err) {
			// TODO Auto-generated catch block
			Utils.showLog( log, "Error getting list of Platform names by type id.", err);
		}
		return returnVal;
	}

	public static boolean authenticate(Logger log, boolean refresh, boolean isSSH){
		return authenticate(log, refresh, isSSH, true);
	}

	public static boolean authenticate(Logger log, boolean refresh, boolean isSSH, boolean showUserDialog) {
		SystemUsers systemUsers = new SystemUsers();
		SystemUserDetail userDetail = systemUsers.getDetail(SystemUserNames.USER_READER.toString());
		try {
			if (refresh) {
				ClientContext.resetConfiguration();
				ClientContext.getInstance(App.INSTANCE.getService(), true).getDefaultCropType();
				if (App.INSTANCE.getService().contains("localhost") && isSSH) {
					String hostPort = Utils.getFromTo(App.INSTANCE.getService(), "localhost:", "/");
					Integer port = Integer.parseInt(hostPort);
					ClientContext.setSshOverride("localhost", port);
				}
				if(showUserDialog) App.INSTANCE.setCrop(null);
			}

			if (App.INSTANCE.getCrop() == null)
				ClientContext.getInstance(null, false)
				.setCurrentClientCrop(ClientContext.getInstance(null, false).getDefaultCropType());
			else {
				String crop = App.INSTANCE.getCrop();
				ClientContext.getInstance(null, false).setCurrentClientCrop(crop);
			}

			ClientContext.getInstance(null, false).login(userDetail.getUserName(), userDetail.getPassword());
		} catch (Exception err) {
			// TODO Auto-generated catch block
			Utils.log(log, "Error connecting to service", err);
			return false;
		}
		return true;
	}

	public static boolean isNewContactEmail(String email) {
		boolean isNewEmail = true;
		try {
			RestUri restUriContact = App.INSTANCE.getUriFactory().contactsByQueryParams();
			restUriContact.setParamValue("email", email);
			GobiiEnvelopeRestResource<ContactDTO> restResource = new GobiiEnvelopeRestResource<>(restUriContact);
			PayloadEnvelope<ContactDTO> resultEnvelope = restResource
					.get(ContactDTO.class);

			if(Controller.getDTOResponse(Display.getCurrent().getActiveShell(), resultEnvelope.getHeader(), null, false)){
				ContactDTO contactDTO = resultEnvelope.getPayload().getData().get(0);
				if (contactDTO.getEmail() != null)
					isNewEmail = false;
			}

		} catch (IndexOutOfBoundsException IOE) {
			// TODO Auto-generated catch block
			//nothing was returned
			isNewEmail = true;
		}catch (Exception err) {
			// TODO Auto-generated catch block
			Utils.showLog( log, "Error getting conact email status.", err);
		}

		return isNewEmail;
	}

	public static List<NameIdDTO> getOrganizationNames() {
		// TODO Auto-generated method stub

		List<NameIdDTO> returnVal = null;
		try {
			returnVal =  testNameRetrieval(GobiiEntityNameType.ORGANIZATIONS, GobiiFilterType.NONE, null);
		} catch (Exception err) {
			// TODO Auto-generated catch block
			Utils.showLog( log, "Error getting list of Organization names", err);
		}
		return returnVal;
	}

	public static Integer getPlatformIdByExperimentId(Integer experimentID) {
		// TODO Auto-generated method stub
		ProtocolDTO protocolDTO = null;
		PayloadEnvelope<ProtocolDTO> protocolDetails;
		try {
			protocolDetails = getProtocolDetailsByExperimentId(experimentID);
			protocolDTO = protocolDetails.getPayload().getData().get(0);
		} catch (Exception err) {
			// TODO Auto-generated catch block
			Utils.showLog( log, "Error getting protocol details by experiment id", err);
		}
		
		
		return protocolDTO.getPlatformId();
	}
	
	public static PayloadEnvelope<ExperimentDTO> getExperimentDetails(int experimentId) {
		// TODO Auto-generated method stub
		PayloadEnvelope<ExperimentDTO> resultEnvelope = null;
		RestUri experimentsUri;
		try {
			experimentsUri = App.INSTANCE.getUriFactory().resourceByUriIdParam(ServiceRequestId.URL_EXPERIMENTS);
			experimentsUri.setParamValue("id", Integer.toString(experimentId));
			GobiiEnvelopeRestResource<ExperimentDTO> restResourceForExperiments = new GobiiEnvelopeRestResource<>(experimentsUri);
			resultEnvelope = restResourceForExperiments.get(ExperimentDTO.class);
		} catch (Exception err) {
			// TODO Auto-generated catch block
			Utils.showLog( log, "Error getting experiment details by experiment id", err);
		}

		return resultEnvelope;
	}

	public static PayloadEnvelope<ProtocolDTO> getProtocolDetails(int protocolId) {
		// TODO Auto-generated method stub

		PayloadEnvelope<ProtocolDTO> resultEnvelopeForGetByID = null;
		RestUri restUriProtocolForGetById;
		try {
			restUriProtocolForGetById = App
					.INSTANCE.getUriFactory()
					.resourceByUriIdParam(ServiceRequestId.URL_PROTOCOL);

			restUriProtocolForGetById.setParamValue("id", Integer.toString(protocolId));
			GobiiEnvelopeRestResource<ProtocolDTO> restResourceForGetById = new GobiiEnvelopeRestResource<>(restUriProtocolForGetById);

			resultEnvelopeForGetByID = restResourceForGetById
					.get(ProtocolDTO.class);
		} catch (Exception err) {
			// TODO Auto-generated catch block

			Utils.showLog( log, "Error getting protocol details by protocol id", err);
		}

		return resultEnvelopeForGetByID;
	}

	public static PayloadEnvelope<PlatformDTO> getPlatformDetails(int platformId) {
		// TODO Auto-generated method stub
		PayloadEnvelope<PlatformDTO> resultEnvelopeForGetByID = null;
		RestUri restUriPlatformForGetById;
		try {
			restUriPlatformForGetById = App
					.INSTANCE.getUriFactory()
					.resourceByUriIdParam(ServiceRequestId.URL_PLATFORM);
			restUriPlatformForGetById.setParamValue("id", Integer.toString(platformId));
			GobiiEnvelopeRestResource<PlatformDTO> restResourceForGetById = new GobiiEnvelopeRestResource<>(restUriPlatformForGetById);
			
			resultEnvelopeForGetByID = restResourceForGetById
						.get(PlatformDTO.class);
		} catch (Exception err) {
			// TODO Auto-generated catch block

			Utils.showLog( log, "Error getting platform details by platform id", err);
		}
		
		return resultEnvelopeForGetByID;
	}

	public static String getGobiiVersion() {
		// TODO Auto-generated method stub
		 String version = gobiiVersion.split("-")[0];
		return version;
	}
	
	public static void setGobiiVersion(String gobiiVersion, Boolean showSuccess) {
		Controller.gobiiVersion = gobiiVersion;
		checkVersionCompatibility(showSuccess);
	}
	
	public static boolean checkVersionCompatibility(Boolean showSuccess) {
		// TODO Auto-generated method stub
		boolean isCompatible = true;
		String minimumGobiiVersion = Main2.getLoaderUiVersion();
		String headerVersion = getGobiiVersion();
		if(minimumGobiiVersion.compareTo(headerVersion)>0){
			if(showSuccess) MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "ERROR in Loader", "The version from gobii-web is less than the minimum acceptable version for this GDI.");
			isCompatible = false;
		}

		Main2.updateVersionsOnMainWindow();
		return isCompatible;
	}
}
