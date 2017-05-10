package org.gobiiproject.gobiibrapi.calls.studies.search;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phil on 12/15/2016.
 */
public class BrapiResponseMapStudiesSearch {

    private List<BrapiResponseStudiesSearchItem> getBrapiJsonResponseStudySearchItems(BrapiRequestStudiesSearch brapiRequestStudiesSearch) {

        List<BrapiResponseStudiesSearchItem> returnVal = new ArrayList<>();

        BrapiResponseStudiesSearchItem brapiResponseStudiesSearchItem = new BrapiResponseStudiesSearchItem();
        brapiResponseStudiesSearchItem.setStudyType("genotype");
        brapiResponseStudiesSearchItem.setName("a dummy search result for testing");

        returnVal.add(brapiResponseStudiesSearchItem);


        return returnVal;
    }


    public BrapiResponseStudiesSearch getBrapiResponseStudySearch(BrapiRequestStudiesSearch brapiRequestStudiesSearch) {

        BrapiResponseStudiesSearch returnVal = new BrapiResponseStudiesSearch();

        List<BrapiResponseStudiesSearchItem> searchItems = getBrapiJsonResponseStudySearchItems(brapiRequestStudiesSearch);

        returnVal.setData(searchItems );

        return returnVal ;

    }

}
