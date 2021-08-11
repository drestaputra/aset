package dresta.putra.aset.oper_berkas;

import com.google.gson.annotations.SerializedName;

public class OperBerkasPojo {
    @SerializedName("id_oper_berkas")
    String id_oper_berkas;
    @SerializedName("id_nasabah")
    String id_nasabah;
    @SerializedName("nama_nasabah")
    String nama_nasabah;
    @SerializedName("id_kolektor_dari")
    String id_kolektor_dari;
    @SerializedName("id_kolektor_ke")
    String id_kolektor_ke;
    @SerializedName("status")
    String status;
    @SerializedName("tgl_oper_berkas")
    String tgl_oper_berkas;
    @SerializedName("username_kolektor_dari")
    String username_kolektor_dari;
    @SerializedName("username_kolektor_ke")
    String username_kolektor_ke;

    public OperBerkasPojo(String id_oper_berkas, String id_nasabah, String nama_nasabah, String id_kolektor_dari, String id_kolektor_ke, String status, String tgl_oper_berkas, String username_kolektor_dari, String username_kolektor_ke) {
        this.id_oper_berkas = id_oper_berkas;
        this.id_nasabah = id_nasabah;
        this.nama_nasabah = nama_nasabah;
        this.id_kolektor_dari = id_kolektor_dari;
        this.id_kolektor_ke = id_kolektor_ke;
        this.status = status;
        this.tgl_oper_berkas = tgl_oper_berkas;
        this.username_kolektor_dari = username_kolektor_dari;
        this.username_kolektor_ke = username_kolektor_ke;
    }

    public OperBerkasPojo() {
    }

    public String getId_oper_berkas() {
        return id_oper_berkas;
    }

    public void setId_oper_berkas(String id_oper_berkas) {
        this.id_oper_berkas = id_oper_berkas;
    }

    public String getId_nasabah() {
        return id_nasabah;
    }

    public void setId_nasabah(String id_nasabah) {
        this.id_nasabah = id_nasabah;
    }

    public String getNama_nasabah() {
        return nama_nasabah;
    }

    public void setNama_nasabah(String nama_nasabah) {
        this.nama_nasabah = nama_nasabah;
    }

    public String getId_kolektor_dari() {
        return id_kolektor_dari;
    }

    public void setId_kolektor_dari(String id_kolektor_dari) {
        this.id_kolektor_dari = id_kolektor_dari;
    }

    public String getId_kolektor_ke() {
        return id_kolektor_ke;
    }

    public void setId_kolektor_ke(String id_kolektor_ke) {
        this.id_kolektor_ke = id_kolektor_ke;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTgl_oper_berkas() {
        return tgl_oper_berkas;
    }

    public void setTgl_oper_berkas(String tgl_oper_berkas) {
        this.tgl_oper_berkas = tgl_oper_berkas;
    }

    public String getUsername_kolektor_dari() {
        return username_kolektor_dari;
    }

    public void setUsername_kolektor_dari(String username_kolektor_dari) {
        this.username_kolektor_dari = username_kolektor_dari;
    }

    public String getUsername_kolektor_ke() {
        return username_kolektor_ke;
    }

    public void setUsername_kolektor_ke(String username_kolektor_ke) {
        this.username_kolektor_ke = username_kolektor_ke;
    }
}
