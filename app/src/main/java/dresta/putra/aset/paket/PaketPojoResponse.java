package dresta.putra.aset.paket;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PaketPojoResponse {
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
    List<PaketPojo> data;

    public PaketPojoResponse(Integer status, String msg, Integer page, int totalPage, Integer recordsFiltered, Integer totalRecords, List<PaketPojo> data) {
        this.status = status;
        this.msg = msg;
        this.page = page;
        this.totalPage = totalPage;
        this.recordsFiltered = recordsFiltered;
        this.totalRecords = totalRecords;
        this.data = data;
    }

    public PaketPojoResponse() {
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

    public List<PaketPojo> getData() {
        return data;
    }

    public void setData(List<PaketPojo> data) {
        this.data = data;
    }
}
