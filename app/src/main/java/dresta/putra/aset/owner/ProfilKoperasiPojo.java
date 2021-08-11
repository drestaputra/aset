package dresta.putra.aset.owner;

import com.google.gson.annotations.SerializedName;

public class ProfilKoperasiPojo {
    @SerializedName("id_profil_koperasi")
    String id_profil_koperasi;
    @SerializedName("id_owner")
    String id_owner;
    @SerializedName("deskripsi_koperasi")
    String deskripsi_koperasi;
    @SerializedName("alamat")
    String alamat;
    @SerializedName("visi")
    String visi;
    @SerializedName("misi")
    String misi;
    @SerializedName("no_telp")
    String no_telp;
    @SerializedName("no_wa")
    String no_wa;
    @SerializedName("facebook")
    String facebook;
    @SerializedName("instagram")
    String instagram;
    @SerializedName("email")
    String email;
    @SerializedName("twitter")
    String twitter;
    @SerializedName("foto")
    String foto;
    @SerializedName("nama_koperasi")
    String nama_koperasi;

    public ProfilKoperasiPojo() {
    }

    public ProfilKoperasiPojo(String id_profil_koperasi, String id_owner, String deskripsi_koperasi, String alamat, String visi, String misi, String no_telp, String no_wa, String facebook, String instagram, String email, String twitter, String foto, String nama_koperasi) {
        this.id_profil_koperasi = id_profil_koperasi;
        this.id_owner = id_owner;
        this.deskripsi_koperasi = deskripsi_koperasi;
        this.alamat = alamat;
        this.visi = visi;
        this.misi = misi;
        this.no_telp = no_telp;
        this.no_wa = no_wa;
        this.facebook = facebook;
        this.instagram = instagram;
        this.email = email;
        this.twitter = twitter;
        this.foto = foto;
        this.nama_koperasi = nama_koperasi;
    }

    public String getId_profil_koperasi() {
        return id_profil_koperasi;
    }

    public void setId_profil_koperasi(String id_profil_koperasi) {
        this.id_profil_koperasi = id_profil_koperasi;
    }

    public String getId_owner() {
        return id_owner;
    }

    public void setId_owner(String id_owner) {
        this.id_owner = id_owner;
    }

    public String getDeskripsi_koperasi() {
        return deskripsi_koperasi;
    }

    public void setDeskripsi_koperasi(String deskripsi_koperasi) {
        this.deskripsi_koperasi = deskripsi_koperasi;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getVisi() {
        return visi;
    }

    public void setVisi(String visi) {
        this.visi = visi;
    }

    public String getMisi() {
        return misi;
    }

    public void setMisi(String misi) {
        this.misi = misi;
    }

    public String getNo_telp() {
        return no_telp;
    }

    public void setNo_telp(String no_telp) {
        this.no_telp = no_telp;
    }

    public String getNo_wa() {
        return no_wa;
    }

    public void setNo_wa(String no_wa) {
        this.no_wa = no_wa;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getNama_koperasi() {
        return nama_koperasi;
    }

    public void setNama_koperasi(String nama_koperasi) {
        this.nama_koperasi = nama_koperasi;
    }
}
