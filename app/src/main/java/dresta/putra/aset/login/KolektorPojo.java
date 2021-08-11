package dresta.putra.aset.login;

import com.google.gson.annotations.SerializedName;

public class KolektorPojo {
    @SerializedName("id_kolektor")
    String id_kolektor;
    @SerializedName("no_pegawai")
    String no_pegawai;
    @SerializedName("no_ktp")
    String no_ktp;
    @SerializedName("nama_lengkap")
    String nama_lengkap;
    @SerializedName("username")
    String username;
    @SerializedName("id_koperasi")
    String id_koperasi;
    @SerializedName("no_hp")
    String no_hp;
    @SerializedName("email")
    String email;
    @SerializedName("alamat")
    String alamat;
    @SerializedName("kecamatan")
    String kecamatan;
    @SerializedName("kabupaten")
    String kabupaten;
    @SerializedName("provinsi")
    String provinsi;
    @SerializedName("warga_negara")
    String warga_negara;
    @SerializedName("status")
    String status;
    @SerializedName("tgl_bergabung")
    String tgl_bergabung;

    public KolektorPojo(String id_kolektor, String no_pegawai, String no_ktp, String nama_lengkap, String username, String id_koperasi, String no_hp, String email, String alamat, String kecamatan, String kabupaten, String provinsi, String warga_negara, String status, String tgl_bergabung) {
        this.id_kolektor = id_kolektor;
        this.no_pegawai = no_pegawai;
        this.no_ktp = no_ktp;
        this.nama_lengkap = nama_lengkap;
        this.username = username;
        this.id_koperasi = id_koperasi;
        this.no_hp = no_hp;
        this.email = email;
        this.alamat = alamat;
        this.kecamatan = kecamatan;
        this.kabupaten = kabupaten;
        this.provinsi = provinsi;
        this.warga_negara = warga_negara;
        this.status = status;
        this.tgl_bergabung = tgl_bergabung;
    }

    public KolektorPojo() {
    }

    public String getId_kolektor() {
        return id_kolektor;
    }

    public void setId_kolektor(String id_kolektor) {
        this.id_kolektor = id_kolektor;
    }

    public String getNo_pegawai() {
        return no_pegawai;
    }

    public void setNo_pegawai(String no_pegawai) {
        this.no_pegawai = no_pegawai;
    }

    public String getNo_ktp() {
        return no_ktp;
    }

    public void setNo_ktp(String no_ktp) {
        this.no_ktp = no_ktp;
    }

    public String getNama_lengka() {
        return nama_lengkap;
    }

    public void setNama_lengka(String nama_lengkap) {
        this.nama_lengkap = nama_lengkap;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId_koperasi() {
        return id_koperasi;
    }

    public void setId_koperasi(String id_koperasi) {
        this.id_koperasi = id_koperasi;
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

    public String getKecamatan() {
        return kecamatan;
    }

    public void setKecamatan(String kecamatan) {
        this.kecamatan = kecamatan;
    }

    public String getKabupaten() {
        return kabupaten;
    }

    public void setKabupaten(String kabupaten) {
        this.kabupaten = kabupaten;
    }

    public String getProvinsi() {
        return provinsi;
    }

    public void setProvinsi(String provinsi) {
        this.provinsi = provinsi;
    }

    public String getWarga_negara() {
        return warga_negara;
    }

    public void setWarga_negara(String warga_negara) {
        this.warga_negara = warga_negara;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTgl_bergabung() {
        return tgl_bergabung;
    }

    public void setTgl_bergabung(String tgl_bergabung) {
        this.tgl_bergabung = tgl_bergabung;
    }
}
