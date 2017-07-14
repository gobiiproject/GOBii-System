package org.gobiiproject.gobiibrapi.calls.studies.search;

import org.gobiiproject.gobiibrapi.core.derived.BrapiListResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phil on 12/15/2016.
 */
public class BrapiResponseMapStudiesSearch {

    public List<BrapiResponseStudiesSearchItem> getBrapiJsonResponseStudySearchItems(BrapiRequestStudiesSearch brapiRequestStudiesSearch) {

        List<BrapiResponseStudiesSearchItem> returnVal = new ArrayList<>();

        //when we implement we will do the real query from the DB using these criteria
        //brapiRequestStudiesSearch

        BrapiResponseStudiesSearchItem brapiResponseStudiesSearchItem = new BrapiResponseStudiesSearchItem();
        brapiResponseStudiesSearchItem.setStudyType("genotype");
        brapiResponseStudiesSearchItem.setName("a dummy search result for testing");

        returnVal.add(brapiResponseStudiesSearchItem);


        return returnVal;
    }

    public BrapiListResult<BrapiResponseStudiesSearchItem> getBrapiResponseStudySearchItems(BrapiRequestStudiesSearch brapiRequestStudiesSearch) {

        BrapiListResult<BrapiResponseStudiesSearchItem> returnVal = new BrapiListResult<>(BrapiResponseStudiesSearchItem.class);

        returnVal.setData(getBrapiJsonResponseStudySearchItems(brapiRequestStudiesSearch));

        return returnVal;

    }
}
