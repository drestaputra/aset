package dresta.putra.aset.kolektor;

import com.google.gson.annotations.SerializedName;

public class KolektorPojo {
    @SerializedName("id_kolektor")
    String id_kolektor;
    @SerializedName("no_ktp")
    String no_ktp;
    @SerializedName("nama")
    String nama;
    @SerializedName("username")
    String username;
    @SerializedName("password")
    String password;
    @SerializedName("id_owner")
    String id_owner;
    @SerializedName("no_hp")
    String no_hp;
    @SerializedName("email")
    String email;
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
    @SerializedName("warga_negara")
    String warga_negara;
    @SerializedName("status")
    String status;

    public KolektorPojo() {
    }

    public KolektorPojo(String id_kolektor, String no_ktp, String nama, String username, String password, String id_owner, String no_hp, String email, String alamat, String provinsi, String label_provinsi, String kabupaten, String label_kabupaten, String kecamatan, String label_kecamatan, String warga_negara, String status) {
        this.id_kolektor = id_kolektor;
        this.no_ktp = no_ktp;
        this.nama = nama;
        this.username = username;
        this.password = password;
        this.id_owner = id_owner;
        this.no_hp = no_hp;
        this.email = email;
        this.alamat = alamat;
        this.provinsi = provinsi;
        this.label_provinsi = label_provinsi;
        this.kabupaten = kabupaten;
        this.label_kabupaten = label_kabupaten;
        this.kecamatan = kecamatan;
        this.label_kecamatan = label_kecamatan;
        this.warga_negara = warga_negara;
        this.status = status;
    }

    public String getId_kolektor() {
        return id_kolektor;
    }

    public void setId_kolektor(String id_kolektor) {
        this.id_kolektor = id_kolektor;
    }

    public String getNo_ktp() {
        return no_ktp;
    }

    public void setNo_ktp(String no_ktp) {
        this.no_ktp = no_ktp;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
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

    public String getId_owner() {
        return id_owner;
    }

    public void setId_owner(String id_owner) {
        this.id_owner = id_owner;
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

    public String getwarga_negara() {
        return warga_negara;
    }

    public void setwarga_negara(String warga_negara) {
        this.warga_negara = warga_negara;
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
