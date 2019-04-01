/*
 * Copyright (c) 2019 Created by Adit Chauhan
 */

package tutorial.adit.com.onelabsdemo.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SearchResultmodel {
    @SerializedName("results") private ArrayList<ResultsArray> results;
    @SerializedName("total") private int total;
    @SerializedName("total_pages") private int totalPages;

    public int getTotal() {
        return total;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public ArrayList<ResultsArray> getResults() {
        return results;
    }
}
