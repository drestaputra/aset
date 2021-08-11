package dresta.putra.aset.login;

import com.google.gson.annotations.SerializedName;

public class LoginResponsePojo {
    @SerializedName("status")
    int status;
    @SerializedName("msg")
    String msg;
    @SerializedName("data")
    KolektorPojo data;

    public LoginResponsePojo(int status, String msg, KolektorPojo data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public KolektorPojo getData() {
        return data;
    }

    public void setData(KolektorPojo data) {
        this.data = data;
    }
}
