package dresta.putra.aset.request;

import com.google.gson.annotations.SerializedName;

public class RequestPojo {
    @SerializedName("id_request")
    String id_request;
    @SerializedName("username")
    String username;
    @SerializedName("email")
    String email;
    @SerializedName("password")
    String password;
    @SerializedName("no_hp")
    String no_hp;
    @SerializedName("id_paket")
    String id_paket;
    @SerializedName("nama_paket")
    String nama_paket;
    @SerializedName("id_rekening")
    String id_rekening;
    @SerializedName("no_invoice")
    String no_invoice;
    @SerializedName("total_tagihan_invoice")
    String total_tagihan_invoice;
    @SerializedName("tgl_request")
    String tgl_request;
    @SerializedName("status")
    String status;
    @SerializedName("device_id")
    String device_id;

    public RequestPojo() {
    }

    public RequestPojo(String id_request, String username, String email, String password, String no_hp, String id_paket, String nama_paket, String id_rekening, String no_invoice, String total_tagihan_invoice, String tgl_request, String status, String device_id) {
        this.id_request = id_request;
        this.username = username;
        this.email = email;
        this.password = password;
        this.no_hp = no_hp;
        this.id_paket = id_paket;
        this.nama_paket = nama_paket;
        this.id_rekening = id_rekening;
        this.no_invoice = no_invoice;
        this.total_tagihan_invoice = total_tagihan_invoice;
        this.tgl_request = tgl_request;
        this.status = status;
        this.device_id = device_id;
    }

    public String getId_request() {
        return id_request;
    }

    public void setId_request(String id_request) {
        this.id_request = id_request;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNo_hp() {
        return no_hp;
    }

    public void setNo_hp(String no_hp) {
        this.no_hp = no_hp;
    }

    public String getId_paket() {
        return id_paket;
    }

    public void setId_paket(String id_paket) {
        this.id_paket = id_paket;
    }

    public String getNama_paket() {
        return nama_paket;
    }

    public void setNama_paket(String nama_paket) {
        this.nama_paket = nama_paket;
    }

    public String getId_rekening() {
        return id_rekening;
    }

    public void setId_rekening(String id_rekening) {
        this.id_rekening = id_rekening;
    }

    public String getNo_invoice() {
        return no_invoice;
    }

    public void setNo_invoice(String no_invoice) {
        this.no_invoice = no_invoice;
    }

    public String getTotal_tagihan_invoice() {
        return total_tagihan_invoice;
    }

    public void setTotal_tagihan_invoice(String total_tagihan_invoice) {
        this.total_tagihan_invoice = total_tagihan_invoice;
    }

    public String getTgl_request() {
        return tgl_request;
    }

    public void setTgl_request(String tgl_request) {
        this.tgl_request = tgl_request;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }
}
