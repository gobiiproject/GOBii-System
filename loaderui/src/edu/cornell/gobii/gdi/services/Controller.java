package edu.cornell.gobii.gdi.services;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
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
import org.gobiiproject.gobiiclient.dtorequests.DtoRequestDisplay;
import org.gobiiproject.gobiimodel.tobemovedtoapimodel.Header;
import org.gobiiproject.gobiimodel.tobemovedtoapimodel.HeaderStatusMessage;
import org.gobiiproject.gobiimodel.dto.container.DisplayDTO;
import org.gobiiproject.gobiimodel.entity.TableColDisplay;
import org.gobiiproject.gobiimodel.headerlesscontainer.ContactDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.NameIdDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.OrganizationDTO;
import org.gobiiproject.gobiimodel.types.GobiiCropType;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
import org.gobiiproject.gobiimodel.types.GobiiFilterType;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.gobiiproject.gobiimodel.types.SystemUserDetail;
import org.gobiiproject.gobiimodel.types.SystemUserNames;
import org.gobiiproject.gobiimodel.types.SystemUsers;

import edu.cornell.gobii.gdi.main.App;
import edu.cornell.gobii.gdi.main.Main2;
import edu.cornell.gobii.gdi.utils.Utils;

public class Controller {


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
	
	public static void getVendorProtocolsByProtocolId(Integer protocolId) throws Exception{
//		RestUri restUriProtocoLVendor = ClientContext.getInstance(null, false)
//                .getUriFactory()
//                .childResourceByUriIdParam(ServiceRequestId.URL_PROTOCOL,
//                        ServiceRequestId.URL_VENDORS);
//        restUriProtocoLVendor.setParamValue("id", protocolId.toString());
//        GobiiEnvelopeRestResource<OrganizationDTO> protocolVendorResource =
//                new GobiiEnvelopeRestResource<>(restUriProtocoLVendor);
//        PayloadEnvelope<OrganizationDTO> resultEnvelope = protocolVendorResource
//              .get(OrganizationDTO.class);
		RestUri restUriVendorsForProtocol = ClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(ServiceRequestId.URL_PROTOCOL)
                .addUriParam("protocolId")
                .setParamValue("protocolId", protocolId.toString())
                .appendSegment(ServiceRequestId.URL_VENDORS);

        GobiiEnvelopeRestResource<OrganizationDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(restUriVendorsForProtocol);
        PayloadEnvelope<OrganizationDTO> resultEnvelope = gobiiEnvelopeRestResource
                .get(OrganizationDTO.class);
        System.out.println();
	}

	public static List<NameIdDTO> getAnalysisNames() {
		// TODO Auto-generated method stub
		List<NameIdDTO> returnVal = null;
		try {
			returnVal = testNameRetrieval(GobiiEntityNameType.ANALYSES, GobiiFilterType.NONE, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnVal;
	}

	public static List<NameIdDTO> getAnalysisTypes() {
		// TODO Auto-generated method stub
		List<NameIdDTO> returnVal = null;
		try {
			returnVal =   testNameRetrieval(GobiiEntityNameType.CVTERMS, GobiiFilterType.BYTYPENAME, "analysis_type");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnVal;
	}

	public static List<NameIdDTO> getReferenceNames() {
		// TODO Auto-generated method stub
		List<NameIdDTO> returnVal = null;
		try {
			returnVal =  testNameRetrieval(GobiiEntityNameType.REFERENCES, GobiiFilterType.NONE, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnVal;
	}

	public static List<NameIdDTO> getContactNames() {
		// TODO Auto-generated method stub
		List<NameIdDTO> returnVal = null;
		try {
			returnVal =  testNameRetrieval(GobiiEntityNameType.CONTACTS, GobiiFilterType.NONE, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnVal;
	}

	public static List<NameIdDTO> getPIContactNames() {
		// TODO Auto-generated method stub

		List<NameIdDTO> returnVal = null;
		try {
			returnVal =  testNameRetrieval(GobiiEntityNameType.CONTACTS, GobiiFilterType.BYTYPENAME, "PI");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnVal;
	}

	public static List<NameIdDTO> getProjectNamesByContactId(Integer selectedContactId) {
		// TODO Auto-generated method stub

		List<NameIdDTO> returnVal = null;
		try {
			returnVal =  testNameRetrieval(GobiiEntityNameType.PROJECTS, GobiiFilterType.BYTYPEID, Integer.toString(selectedContactId));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnVal;
	}

	public static List<NameIdDTO> getManifestNames() {
		// TODO Auto-generated method stub
		List<NameIdDTO> returnVal = null;
		try {
			returnVal =  testNameRetrieval(GobiiEntityNameType.MANIFESTS, GobiiFilterType.NONE,null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnVal;
	}

	public static List<NameIdDTO> getPlatformNames() {
		// TODO Auto-generated method stub

		List<NameIdDTO> returnVal = null;
		try {
			returnVal =  testNameRetrieval(GobiiEntityNameType.PLATFORMS, GobiiFilterType.NONE,null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnVal;
	}

	public static List<NameIdDTO> getProtocolNames() {
		// TODO Auto-generated method stub

		List<NameIdDTO> returnVal = null;
		try {
			returnVal =  testNameRetrieval(GobiiEntityNameType.PROTOCOLS, GobiiFilterType.NONE,null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnVal;
	}
	
	public static List<NameIdDTO> getVendorProtocolNames(){
		List<NameIdDTO> returnVal = null;
		try{
			returnVal = testNameRetrieval(GobiiEntityNameType.VENDORS_PROTOCOLS, GobiiFilterType.NONE, null);
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnVal;
	}

	public static List<NameIdDTO> getProtocolNamesByPlatformId(Integer selectedId) {
		// TODO Auto-generated method stub

		List<NameIdDTO> returnVal = null;
		try {
			returnVal =  testNameRetrieval(GobiiEntityNameType.PROTOCOLS, GobiiFilterType.BYTYPEID, Integer.toString(selectedId));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnVal;
	}

	public static List<NameIdDTO> getVendorProtocolNamesByProtocolId(Integer selectedId) {
		// TODO Auto-generated method stub

		List<NameIdDTO> returnVal = null;
		try {
			returnVal =  testNameRetrieval(GobiiEntityNameType.VENDORS_PROTOCOLS, GobiiFilterType.BYTYPEID, Integer.toString(selectedId));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnVal;
	}



	public static List<NameIdDTO> getExperimentNamesByProjectId(Integer selectedId) {
		// TODO Auto-generated method stub

		List<NameIdDTO> returnVal = null;
		try {
			returnVal =  testNameRetrieval(GobiiEntityNameType.EXPERIMENTS, GobiiFilterType.BYTYPEID, Integer.toString(selectedId));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnVal;
	}

	public static List<NameIdDTO> getDataSetNamesByExperimentId(Integer selectedId) {
		// TODO Auto-generated method stub
		List<NameIdDTO> returnVal = null;
		try {
			returnVal =    testNameRetrieval(GobiiEntityNameType.DATASETS, GobiiFilterType.BYTYPEID, Integer.toString(selectedId));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnVal;
	}

	public static List<NameIdDTO> getMapNames() {
		// TODO Auto-generated method stub
		List<NameIdDTO> returnVal = null;
		try {
			returnVal =  testNameRetrieval(GobiiEntityNameType.MAPSETS, GobiiFilterType.NONE, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnVal;
	}

	public static List<NameIdDTO> getMapTypes() {
		List<NameIdDTO> returnVal = null;
		try {
			returnVal =  testNameRetrieval(GobiiEntityNameType.CVTERMS, GobiiFilterType.BYTYPENAME, "mapset_type");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnVal;
	}

	public static List<NameIdDTO> getMapNamesByTypeId(int mapTypeId) {
		// TODO Auto-generated method stub
		List<NameIdDTO> returnVal = null;
		try {
			returnVal =  testNameRetrieval(GobiiEntityNameType.MAPSETS, GobiiFilterType.BYTYPEID, Integer.toString(mapTypeId));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnVal;
	}

	public static List<NameIdDTO> getMarkerGroupNames() {
		// TODO Auto-generated method stub
		List<NameIdDTO> returnVal = null;
		try {
			returnVal =  testNameRetrieval(GobiiEntityNameType.MARKERGROUPS, GobiiFilterType.NONE, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnVal;
	}

	public static Set<Entry<String, List<TableColDisplay>>> getTableDisplayNames() {
		// TODO Auto-generated method stub
		DtoRequestDisplay dtoRequestDisplay = new DtoRequestDisplay();

		DisplayDTO displayDTORequest = new DisplayDTO(GobiiProcessType.READ);
		displayDTORequest.getTableNamesWithColDisplay();
		displayDTORequest.setIncludeDetailsList(true);

		DisplayDTO displayDTOResponse = null;
		try {
			displayDTOResponse = dtoRequestDisplay.process(displayDTORequest);
			return displayDTOResponse.getTableNamesWithColDisplay().entrySet();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static List<NameIdDTO> getAnalysisNamesByTypeId(int analysisTypeId) {
		// TODO Auto-generated method stub
		List<NameIdDTO> returnVal = null;
		try {
			returnVal =  testNameRetrieval(GobiiEntityNameType.ANALYSES, GobiiFilterType.BYTYPEID, Integer.toString(analysisTypeId));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnVal;
	}

	public static List<NameIdDTO> getContactNamesByType(String selected) {
		// TODO Auto-generated method stubNameIdListDTO nameIdListDTO = null;
		List<NameIdDTO> returnVal = null;
		try {
			returnVal =  testNameRetrieval(GobiiEntityNameType.CONTACTS, GobiiFilterType.BYTYPENAME, selected);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnVal;
	}

	public static List<NameIdDTO> getCVByGroup(String selectedGroup) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stubNameIdListDTO nameIdListDTO = null;
		List<NameIdDTO> returnVal = null;
		try {
			returnVal = testNameRetrieval(GobiiEntityNameType.CVTERMS, GobiiFilterType.BYTYPENAME, selectedGroup);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnVal;
	}

	public static List<NameIdDTO> getCvGroupNames() {
		// TODO Auto-generated method stub
		List<NameIdDTO> returnVal = null;
		try {
			returnVal =  testNameRetrieval(GobiiEntityNameType.CVGROUPS, GobiiFilterType.NONE, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnVal;
	}

	public static List<NameIdDTO> getCvNames() {
		// TODO Auto-generated method stub
		List<NameIdDTO> returnVal = null;
		try {
			returnVal =  testNameRetrieval(GobiiEntityNameType.CVTERMS, GobiiFilterType.NONE, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnVal;
	}

	public static List<NameIdDTO> getProjectNames() {
		// TODO Auto-generated method stub
		// Authenticator.authenticate
		List<NameIdDTO> returnVal = null;
		try {
			returnVal =  testNameRetrieval(GobiiEntityNameType.PROJECTS, GobiiFilterType.NONE, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnVal;
	}

	public static List<NameIdDTO> getExperimentNames() {
		// TODO Auto-generated method stub
		List<NameIdDTO> returnVal = null;
		try {
			returnVal =   testNameRetrieval(GobiiEntityNameType.EXPERIMENTS, GobiiFilterType.NONE, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnVal;
	}

	public static List<NameIdDTO> getDataSetNames() {
		// TODO Auto-generated method stub

		List<NameIdDTO> returnVal = null;
		try {
			returnVal =   testNameRetrieval(GobiiEntityNameType.DATASETS, GobiiFilterType.NONE, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

		return headerStatusIsSuccessful;
	}

	public static List<NameIdDTO> getPlatformNamesByTypeId(int platformTypeId) {
		// TODO Auto-generated method stub
		List<NameIdDTO> returnVal = null;
		try {
			returnVal =testNameRetrieval(GobiiEntityNameType.PLATFORMS, GobiiFilterType.BYTYPEID, Integer.toString(platformTypeId));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return isNewEmail;
	}

	public static List<NameIdDTO> getOrganizationNames() {
		// TODO Auto-generated method stub

		List<NameIdDTO> returnVal = null;
		try {
			returnVal =  testNameRetrieval(GobiiEntityNameType.ORGANIZATIONS, GobiiFilterType.NONE, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnVal;
	}
}
