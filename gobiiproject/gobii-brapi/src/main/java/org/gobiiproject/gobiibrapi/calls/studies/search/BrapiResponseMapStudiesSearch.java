package org.gobiiproject.gobiibrapi.calls.studies.search;

import org.gobiiproject.gobidomain.services.ProjectService;
import org.gobiiproject.gobiimodel.headerlesscontainer.ProjectDTO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;




/**
 * Created by Phil on 12/15/2016.
 */
public class BrapiResponseMapStudiesSearch {

    @Autowired
    private ProjectService projectService = null;

    private List<BrapiResponseStudiesSearchItem> getBrapiJsonResponseStudySearchItems(BrapiRequestStudiesSearch brapiRequestStudiesSearch) {


        List<BrapiResponseStudiesSearchItem> returnVal = new ArrayList<>();

        List<ProjectDTO> projectDTOS = projectService.getProjects();
        for( ProjectDTO projectDTO : projectDTOS ) {


            BrapiResponseStudiesSearchItem brapiResponseStudiesSearchItem = new BrapiResponseStudiesSearchItem();
            brapiResponseStudiesSearchItem.setStudyType("genotype");
            brapiResponseStudiesSearchItem.setName(projectDTO.getProjectName());
            brapiResponseStudiesSearchItem.setStudyDbId(projectDTO.getProjectId().toString());

            returnVal.add(brapiResponseStudiesSearchItem);
        }

        return returnVal;
    }


    public BrapiResponseStudiesSearch getBrapiResponseStudySearch(BrapiRequestStudiesSearch brapiRequestStudiesSearch) {

        BrapiResponseStudiesSearch returnVal = new BrapiResponseStudiesSearch();

        List<BrapiResponseStudiesSearchItem> searchItems = getBrapiJsonResponseStudySearchItems(brapiRequestStudiesSearch);

        returnVal.setData(searchItems );

        return returnVal ;

    }

}
