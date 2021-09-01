package dresta.putra.aset.login;

import com.google.gson.annotations.SerializedName;

import dresta.putra.aset.user.UserPojo;

public class LoginResponsePojo {
    @SerializedName("status")
    int status;
    @SerializedName("msg")
    String msg;
    @SerializedName("data")
    UserPojo data;

    public LoginResponsePojo(int status, String msg, UserPojo data) {
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

    public UserPojo getData() {
        return data;
    }

    public void setData(UserPojo data) {
        this.data = data;
    }
}
