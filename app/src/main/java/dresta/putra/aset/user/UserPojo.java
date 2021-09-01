package dresta.putra.aset.user;

import com.google.gson.annotations.SerializedName;

public class UserPojo {
    @SerializedName("id_user")
    String id_user;
    @SerializedName("nama_lengkap")
    String nama_lengkap;
    @SerializedName("username")
    String username;
    @SerializedName("password")
    String password;
    @SerializedName("no_hp")
    String no_hp;
    @SerializedName("email")
    String email;
    @SerializedName("instansi")
    String instansi;
    @SerializedName("alamat")
    String alamat;
    @SerializedName("provinsi")
    String provinsi;
    @SerializedName("label_provinsi")
    String label_provinsi;
    @SerializedName("kabupaten")
    String kabupaten;
    @SerializedName("label_kabupaten")
    String label_kabupaten;
    @SerializedName("kecamatan")
    String kecamatan;
    @SerializedName("label_kecamatan")
    String label_kecamatan;

    @SerializedName("status")
    String status;

    public UserPojo() {
    }

    public UserPojo(String id_user, String nama_lengkap, String username, String password, String no_hp, String email, String instansi, String alamat, String provinsi, String label_provinsi, String kabupaten, String label_kabupaten, String kecamatan, String label_kecamatan, String status) {
        this.id_user = id_user;
        this.nama_lengkap = nama_lengkap;
        this.username = username;
        this.password = password;
        this.no_hp = no_hp;
        this.email = email;
        this.instansi = instansi;
        this.alamat = alamat;
        this.provinsi = provinsi;
        this.label_provinsi = label_provinsi;
        this.kabupaten = kabupaten;
        this.label_kabupaten = label_kabupaten;
        this.kecamatan = kecamatan;
        this.label_kecamatan = label_kecamatan;
        this.status = status;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getNamaLengkap() {
        return nama_lengkap;
    }

    public void setNama_lengkap(String nama_lengkap) {
        this.nama_lengkap = nama_lengkap;
    }


    public String getInstansi() {
        return instansi;
    }

    public void setInstansi(String instansi) {
        this.instansi = instansi;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getProvinsi() {
        return provinsi;
    }

    public void setProvinsi(String provinsi) {
        this.provinsi = provinsi;
    }

    public String getKabupaten() {
        return kabupaten;
    }

    public void setKabupaten(String kabupaten) {
        this.kabupaten = kabupaten;
    }

    public String getKecamatan() {
        return kecamatan;
    }

    public void setKecamatan(String kecamatan) {
        this.kecamatan = kecamatan;
    }



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLabel_provinsi() {
        return label_provinsi;
    }

    public void setLabel_provinsi(String label_provinsi) {
        this.label_provinsi = label_provinsi;
    }

    public String getLabel_kabupaten() {
        return label_kabupaten;
    }

    public void setLabel_kabupaten(String label_kabupaten) {
        this.label_kabupaten = label_kabupaten;
    }

    public String getLabel_kecamatan() {
        return label_kecamatan;
    }

    public void setLabel_kecamatan(String label_kecamatan) {
        this.label_kecamatan = label_kecamatan;
    }

}
