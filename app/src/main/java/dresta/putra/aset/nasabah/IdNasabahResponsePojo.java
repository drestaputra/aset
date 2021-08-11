package dresta.putra.aset.nasabah;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class IdNasabahResponsePojo{
    @SerializedName("status")
    Integer status;
    @SerializedName("msg")
    String msg;
    @SerializedName("data")
    List<NasabahPojo> data;

    public IdNasabahResponsePojo(Integer status, String msg, List<NasabahPojo> data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
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

    public List<NasabahPojo> getData() {
        return data;
    }

    public void setData(List<NasabahPojo> data) {
        this.data = data;
    }
}
