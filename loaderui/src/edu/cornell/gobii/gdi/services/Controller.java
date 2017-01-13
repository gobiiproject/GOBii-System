package edu.cornell.gobii.gdi.services;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.gobiiproject.gobiiclient.core.ClientContext;
import org.gobiiproject.gobiiclient.dtorequests.DtoRequestDisplay;
import org.gobiiproject.gobiiclient.dtorequests.DtoRequestNameIdList;
import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.container.DisplayDTO;
import org.gobiiproject.gobiimodel.dto.container.NameIdListDTO;
import org.gobiiproject.gobiimodel.dto.header.HeaderStatusMessage;
import org.gobiiproject.gobiimodel.entity.TableColDisplay;
import org.gobiiproject.gobiimodel.types.GobiiCropType;
import org.gobiiproject.gobiimodel.types.SystemUserDetail;
import org.gobiiproject.gobiimodel.types.SystemUserNames;
import org.gobiiproject.gobiimodel.types.SystemUsers;

import edu.cornell.gobii.gdi.main.App;
import edu.cornell.gobii.gdi.utils.Utils;

public class Controller {

	public static Set<Entry<String, String>> getAnalysisNames() {
		// TODO Auto-generated method stub
		DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
		NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
		nameIdListDTORequest.setEntityName("analysis");
		try {
			NameIdListDTO nameIdListDtoResponse = dtoRequestNameIdList.process(nameIdListDTORequest); //for platforms, just get all names
			return nameIdListDtoResponse.getNamesById().entrySet();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static Set<Entry<String, String>> getAnalysisTypes() {
		// TODO Auto-generated method stub
		DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
		NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
		nameIdListDTORequest.setEntityName("cvgroupterms");
		nameIdListDTORequest.setFilter("analysis_type");
		try {
			NameIdListDTO nameIdListDTO = dtoRequestNameIdList.process(nameIdListDTORequest);
			return nameIdListDTO.getNamesById().entrySet();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static Set<Entry<String, String>> getReferenceNames() {
		// TODO Auto-generated method stub

		NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
		NameIdListDTO nameIdListDtoResponse = null;
		nameIdListDTORequest.setEntityName("reference");
		DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
		try {
			nameIdListDtoResponse = dtoRequestNameIdList.process(nameIdListDTORequest);
			return nameIdListDtoResponse.getNamesById().entrySet();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public static Set<Entry<String, String>> getContactNames() {
		// TODO Auto-generated method stub
		NameIdListDTO nameIdListDTOResponse = null;
		NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
		nameIdListDTORequest.setEntityName("allContacts");
		DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
		try {
			
			nameIdListDTOResponse = dtoRequestNameIdList.process(nameIdListDTORequest);
			return nameIdListDTOResponse.getNamesById().entrySet();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
	
	public static Set<Entry<String, String>> getPIContactNames() {
		// TODO Auto-generated method stub
		NameIdListDTO nameIdListDTOResponse = null;
		NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
		nameIdListDTORequest.setEntityName("contact");
		nameIdListDTORequest.setFilter("PI");
		DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
		try {
			
			nameIdListDTOResponse = dtoRequestNameIdList.process(nameIdListDTORequest);
			return nameIdListDTOResponse.getNamesById().entrySet();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public static Set<Entry<String, String>> getProjectNamesByContactId(Integer selectedContactId) {
		// TODO Auto-generated method stub
		NameIdListDTO nameIdListDTOResponse = null;
		NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
		nameIdListDTORequest.setEntityName("project");
		nameIdListDTORequest.setFilter(Integer.toString(selectedContactId));
		DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
		try {
			
			nameIdListDTOResponse = dtoRequestNameIdList.process(nameIdListDTORequest);
			return nameIdListDTOResponse.getNamesById().entrySet();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public static Set<Entry<String, String>> getManifestNames() {
		// TODO Auto-generated method stub

		NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
		NameIdListDTO nameIdListDtoResponse = null;
		nameIdListDTORequest.setEntityName("manifest");
		DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
		try {
			nameIdListDtoResponse = dtoRequestNameIdList.process(nameIdListDTORequest);
			return nameIdListDtoResponse.getNamesById().entrySet();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static Set<Entry<String, String>> getPlatformNames() {
		// TODO Auto-generated method stub
		NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
		NameIdListDTO nameIdListDtoResponse = null;
		nameIdListDTORequest.setEntityName("platform");
		DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
		try {
			nameIdListDtoResponse = dtoRequestNameIdList.process(nameIdListDTORequest); //for platforms, just get all names
			return nameIdListDtoResponse.getNamesById().entrySet();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Set<Entry<String, String>> getExperimentNamesByProjectId(Integer selectedId) {
		// TODO Auto-generated method stub
		NameIdListDTO nameIdListDTO = null;
		NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
		DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
		nameIdListDTORequest.setEntityName("experiment");
		nameIdListDTORequest.setFilter(Integer.toString(selectedId));
		try {
			nameIdListDTO = dtoRequestNameIdList.process(nameIdListDTORequest);
			return nameIdListDTO.getNamesById().entrySet();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static Set<Entry<String, String>> getDataSetNamesByExperimentId(Integer selectedId) {
		// TODO Auto-generated method stub
		NameIdListDTO nameIdListDTO = null;
		NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
		DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
		nameIdListDTORequest.setEntityName("datasetnamesbyexperimentid");
		nameIdListDTORequest.setFilter(Integer.toString(selectedId));
		try {
			nameIdListDTO = dtoRequestNameIdList.process(nameIdListDTORequest);
			return nameIdListDTO.getNamesById().entrySet();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static Set<Entry<String, String>> getMapNames() {
		// TODO Auto-generated method stub
		NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
		NameIdListDTO nameIdListDtoResponse = null;
		nameIdListDTORequest.setEntityName("mapset");
		DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
		try {
			nameIdListDtoResponse = dtoRequestNameIdList.process(nameIdListDTORequest); //for platforms, just get all names
			return nameIdListDtoResponse.getNamesById().entrySet();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static Set<Entry<String, String>> getMapTypes() {
		// TODO Auto-generated method stub
		DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
		NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
		nameIdListDTORequest.setEntityName("cvgroupterms");
		nameIdListDTORequest.setFilter("mapset_type");
		NameIdListDTO nameIdListDTO;
		try {
			nameIdListDTO = dtoRequestNameIdList.process(nameIdListDTORequest);
			return nameIdListDTO.getNamesById().entrySet();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static Set<Entry<String, String>> getMapNamesByTypeId(int mapTypeId) {
		// TODO Auto-generated method stub

		NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
		nameIdListDTORequest.setEntityName("mapNameByTypeId");
		nameIdListDTORequest.setFilter(Integer.toString(mapTypeId));
		DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();

		try {
			NameIdListDTO nameIdListDtoResponse = dtoRequestNameIdList.process(nameIdListDTORequest);
			return nameIdListDtoResponse.getNamesById().entrySet();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static Set<Entry<String, String>> getMarkerGroupNames() {
		// TODO Auto-generated method stub

		NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
		nameIdListDTORequest.setEntityName("markergroup");
		DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();

		try {
			NameIdListDTO nameIdListDtoResponse = dtoRequestNameIdList.process(nameIdListDTORequest);
			return nameIdListDtoResponse.getNamesById().entrySet();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static Set<Entry<String, List<TableColDisplay>>> getTableDisplayNames() {
		// TODO Auto-generated method stub
		DtoRequestDisplay dtoRequestDisplay = new DtoRequestDisplay();

		DisplayDTO displayDTORequest = new DisplayDTO();
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

	public static Set<Entry<String, String>> getAnalysisNamesByTypeId(int analysisTypeId) {
		// TODO Auto-generated method stub
		NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
		nameIdListDTORequest.setEntityName("analysisNameByTypeId");
		nameIdListDTORequest.setFilter(Integer.toString(analysisTypeId));
		DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
		try {
			NameIdListDTO nameIdListDtoResponse = dtoRequestNameIdList.process(nameIdListDTORequest);
			return nameIdListDtoResponse.getNamesById().entrySet();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static Set<Entry<String, String>> getAnalysisNamesByType(String string) {
		// TODO Auto-generated method stub
		Set<Entry<String, String>> types = getAnalysisTypes();
		int typeId =0;
		for (Entry entry : types){
			if(entry.getValue().equals(string)){
				typeId = Integer.parseInt((String) entry.getKey());
				break;
			}
		}

		return getAnalysisNamesByTypeId(typeId);
	}

	public static Set<Entry<String, String>> getRoleNames() {
		// TODO Auto-generated method stub
		NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
		nameIdListDTORequest.setEntityName("role");
		DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
		try {
			NameIdListDTO nameIdListDtoResponse = dtoRequestNameIdList.process(nameIdListDTORequest);
			return nameIdListDtoResponse.getNamesById().entrySet();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static Set<Entry<String, String>> getContactNamesByType(String selected) {
		// TODO Auto-generated method stubNameIdListDTO nameIdListDTO = null;
		NameIdListDTO nameIdListDTOResponse = null;
		NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
		nameIdListDTORequest.setEntityName("contact");
		nameIdListDTORequest.setFilter(selected);
		DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
		try {
			
			nameIdListDTOResponse = dtoRequestNameIdList.process(nameIdListDTORequest);
			return nameIdListDTOResponse.getNamesById().entrySet();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public static Set<Entry<String, String>> getCVByGroup(String selectedGroup) {
		// TODO Auto-generated method stub
		DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
		NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
		nameIdListDTORequest.setEntityName("cvgroupterms");
		nameIdListDTORequest.setFilter(selectedGroup);
		try {
			NameIdListDTO nameIdListDTO = dtoRequestNameIdList.process(nameIdListDTORequest);
			return nameIdListDTO.getNamesById().entrySet();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static Set<Entry<String, String>> getCvGroupNames() {
		// TODO Auto-generated method stub

		NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
		nameIdListDTORequest.setEntityName("cvgroups");
		DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
		NameIdListDTO nameIdListDtoResponse;
		try {
			nameIdListDtoResponse = dtoRequestNameIdList.process(nameIdListDTORequest);
			return nameIdListDtoResponse.getNamesById().entrySet();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static Set<Entry<String, String>> getCvNames() {
		// TODO Auto-generated method stub
		NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
		nameIdListDTORequest.setEntityName("cvnames");
		DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
		try {
			NameIdListDTO nameIdListDtoResponse = dtoRequestNameIdList.process(nameIdListDTORequest);
			return nameIdListDtoResponse.getNamesById().entrySet();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static Set<Entry<String, String>> getProjectNames() {
		// TODO Auto-generated method stub
		//Authenticator.authenticate
		SystemUsers systemUsers = new SystemUsers();
        SystemUserDetail userDetail = systemUsers.getDetail(SystemUserNames.USER_READER.toString());
		
		NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
		nameIdListDTORequest.setEntityName("projectnames");
		DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
		try {
			System.out.println(ClientContext.getInstance(null, false).getCurrentCropContextRoot());
			ClientContext.getInstance(null, false).login(userDetail.getUserName(), userDetail.getPassword());
			NameIdListDTO nameIdListDtoResponse = dtoRequestNameIdList.process(nameIdListDTORequest);
			return nameIdListDtoResponse.getNamesById().entrySet();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static Set<Entry<String, String>> getExperimentNames() {
		// TODO Auto-generated method stub
		NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
		nameIdListDTORequest.setEntityName("experimentnames");
		DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
		try {
			NameIdListDTO nameIdListDtoResponse = dtoRequestNameIdList.process(nameIdListDTORequest);
			return nameIdListDtoResponse.getNamesById().entrySet();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static Set<Entry<String, String>> getDataSetNames() {
		// TODO Auto-generated method stub

		NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
		nameIdListDTORequest.setEntityName("datasetnames");
		DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
		try {
			NameIdListDTO nameIdListDtoResponse = dtoRequestNameIdList.process(nameIdListDTORequest);
			return nameIdListDtoResponse.getNamesById().entrySet();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static boolean getDTOResponse(Shell shell, DtoMetaData dtoMetaData, StyledText memo){
		MessageBox dialog;
		boolean bool;
		if (!dtoMetaData.getDtoHeaderResponse().isSucceeded()) {
			dialog = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
			dialog.setText("Request information");
			dialog.setMessage("ERROR processing request. Please refer to message box for details");
			String message = "";
			for (HeaderStatusMessage currentStatusMesage : dtoMetaData.getDtoHeaderResponse().getStatusMessages()) {
				message += currentStatusMesage.getMessage() + "\n";
            }
			if(memo == null){
				dialog.setMessage(message);
			}else{
				memo.setText(message);
			}
			bool = false;
		}else{
			dialog = new MessageBox(shell, SWT.ICON_INFORMATION | SWT.OK);
			dialog.setText("Request information");
			dialog.setMessage("Request processed successfully!");
			bool = true;
		}
		dialog.open();
		return bool;
	}

	public static Set<Entry<String, String>> getPlatformNamesByTypeId(int platformTypeId) {
		// TODO Auto-generated method stub
		NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
		NameIdListDTO nameIdListDtoResponse = null;
        nameIdListDTORequest.setEntityName("platformByTypeId");
        nameIdListDTORequest.setFilter(Integer.toString(platformTypeId));
		DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
		try {
			nameIdListDtoResponse = dtoRequestNameIdList.process(nameIdListDTORequest); //for platforms, just get all names
			return nameIdListDtoResponse.getNamesById().entrySet();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static boolean authenticate(Logger log, boolean refresh){
		SystemUsers systemUsers = new SystemUsers();
        SystemUserDetail userDetail = systemUsers.getDetail(SystemUserNames.USER_READER.toString());
		try {
			if(refresh){
				ClientContext.resetConfiguration();
				ClientContext.getInstance(App.INSTANCE.getService(), true).getDefaultCropType();
				if(App.INSTANCE.getService().contains("localhost")){
					String hostPort = Utils.getFromTo(App.INSTANCE.getService(), "localhost:", "/");
					Integer port = Integer.parseInt(hostPort);
					ClientContext.setSshOverride("localhost", port);
				}
			}
//			System.out.println(ClientContext.getInstance(null, false).getCurrentClientCropType().toString());
			if(App.INSTANCE.getCrop() == null)
				ClientContext.getInstance(null,false).setCurrentClientCrop(ClientContext.getInstance(null,false).getDefaultCropType());
			else{
				GobiiCropType crop = GobiiCropType.valueOf(App.INSTANCE.getCrop());
				ClientContext.getInstance(null, false).setCurrentClientCrop(crop);
			}
//			System.out.println(ClientContext.getInstance(null, false).getCurrentClientCropType().toString());
			ClientContext.getInstance(null, false).login(userDetail.getUserName(), userDetail.getPassword());
		} catch (Exception err) {
			// TODO Auto-generated catch block
			Utils.log(log, "Error connecting to service", err);
			return false;
		}
		return true;
	}
}
