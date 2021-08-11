package dresta.putra.aset;

import com.google.gson.annotations.SerializedName;

public class ResponsePojo {
    @SerializedName("status")
    Integer status;
    @SerializedName("msg")
    String msg;

    public ResponsePojo(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
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
}
