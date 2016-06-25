package io.github.ryanhoo.firFlight.network;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 3/19/16
 * Time: 9:21 PM
 * Desc: MultiPageResponse
 */
/*
{
  "total_count": 4,
  "total_pages": 1,
  "current_page": 1,
  "datas": [object, object]
}
*/
public class MultiPageResponse<T> {

    @SerializedName("total_count")
    private int totalCount;

    @SerializedName("total_pages")
    private int totalPages;

    @SerializedName("current_page")
    private int currentPage;

    @SerializedName("datas")
    private List<T> data;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
