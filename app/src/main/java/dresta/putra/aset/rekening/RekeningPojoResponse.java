package dresta.putra.aset.rekening;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RekeningPojoResponse {
    @SerializedName("status")
    Integer status;
    @SerializedName("msg")
    String msg;
    @SerializedName("page")
    Integer page;
    @SerializedName("totalPage")
    int totalPage;
    @SerializedName("recordsFiltered")
    Integer recordsFiltered;
    @SerializedName("totalRecords")
    Integer totalRecords;
    @SerializedName("data")
    List<RekeningPojo> data;

    public RekeningPojoResponse(Integer status, String msg, Integer page, int totalPage, Integer recordsFiltered, Integer totalRecords, List<RekeningPojo> data) {
        this.status = status;
        this.msg = msg;
        this.page = page;
        this.totalPage = totalPage;
        this.recordsFiltered = recordsFiltered;
        this.totalRecords = totalRecords;
        this.data = data;
    }

    public RekeningPojoResponse() {
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public Integer getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(Integer recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    public Integer getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(Integer totalRecords) {
        this.totalRecords = totalRecords;
    }

    public List<RekeningPojo> getData() {
        return data;
    }

    public void setData(List<RekeningPojo> data) {
        this.data = data;
    }
}
