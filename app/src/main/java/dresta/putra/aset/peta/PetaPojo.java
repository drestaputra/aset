package dresta.putra.aset.peta;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;
import com.google.maps.android.clustering.ClusterItem;

import java.util.List;

public class PetaPojo {
    @SerializedName("id_aset")
    String id_aset;
    @SerializedName("nama_aset")
    String nama_aset;
    @SerializedName("kode_barang")
    String kode_barang;
    @SerializedName("register")
    String register;
    @SerializedName("luas_tanah")
    String luas_tanah;
    @SerializedName("thumbnail_aset")
    String thumbnail_aset;
    @SerializedName("tahun_perolehan")
    String tahun_perolehan;
    @SerializedName("alamat")
    String alamat;
    @SerializedName("jenis_hak")
    String jenis_hak;
    @SerializedName("tanggal_sertifikat")
    String tanggal_sertifikat;
    @SerializedName("nomor_sertifikat")
    String nomor_sertifikat;
    @SerializedName("penggunaan")
    String penggunaan;
    @SerializedName("asal_perolehan")
    String asal_perolehan;
    @SerializedName("harga_perolehan")
    String harga_perolehan;
    @SerializedName("keterangan")
    String keterangan;
    @SerializedName("latitude")
    String latitude;
    @SerializedName("longitude")
    String longitude;
    @SerializedName("status_aset")
    String status_aset;
    @SerializedName("created_datetime")
    String created_datetime;
    @SerializedName("created_by_id")
    String created_by_id;
    @SerializedName("created_by")
    String created_by;
    @SerializedName("foto_aset")
    List<FotoAsetPojo> foto_aset;

    public PetaPojo(String id_aset, String nama_aset, String kode_barang, String register, String luas_tanah, String thumbnail_aset, String tahun_perolehan, String alamat, String jenis_hak, String tanggal_sertifikat, String nomor_sertifikat, String penggunaan, String asal_perolehan, String harga_perolehan, String keterangan, String latitude, String longitude, String status_aset, String created_datetime, String created_by_id, String created_by, List<FotoAsetPojo> foto_aset) {
        this.id_aset = id_aset;
        this.nama_aset = nama_aset;
        this.kode_barang = kode_barang;
        this.register = register;
        this.luas_tanah = luas_tanah;
        this.thumbnail_aset = thumbnail_aset;
        this.tahun_perolehan = tahun_perolehan;
        this.alamat = alamat;
        this.jenis_hak = jenis_hak;
        this.tanggal_sertifikat = tanggal_sertifikat;
        this.nomor_sertifikat = nomor_sertifikat;
        this.penggunaan = penggunaan;
        this.asal_perolehan = asal_perolehan;
        this.harga_perolehan = harga_perolehan;
        this.keterangan = keterangan;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status_aset = status_aset;
        this.created_datetime = created_datetime;
        this.created_by_id = created_by_id;
        this.created_by = created_by;
        this.foto_aset = foto_aset;
    }

    public PetaPojo() {
    }

    public String getId_aset() {
        return id_aset;
    }

    public void setId_aset(String id_aset) {
        this.id_aset = id_aset;
    }

    public String getNama_aset() {
        return nama_aset;
    }

    public void setNama_aset(String nama_aset) {
        this.nama_aset = nama_aset;
    }

    public String getKode_barang() {
        return kode_barang;
    }

    public void setKode_barang(String kode_barang) {
        this.kode_barang = kode_barang;
    }

    public String getRegister() {
        return register;
    }

    public void setRegister(String register) {
        this.register = register;
    }

    public String getLuas_tanah() {
        return luas_tanah;
    }

    public void setLuas_tanah(String luas_tanah) {
        this.luas_tanah = luas_tanah;
    }

    public String getThumbnail_aset() {
        return thumbnail_aset;
    }

    public void setThumbnail_aset(String thumbnail_aset) {
        this.thumbnail_aset = thumbnail_aset;
    }

    public String getTahun_perolehan() {
        return tahun_perolehan;
    }

    public void setTahun_perolehan(String tahun_perolehan) {
        this.tahun_perolehan = tahun_perolehan;
    }

    public String getTanggal_sertifikat() {
        return tanggal_sertifikat;
    }

    public void setTanggal_sertifikat(String tanggal_sertifikat) {
        this.tanggal_sertifikat = tanggal_sertifikat;
    }

    public String getNomor_sertifikat() {
        return nomor_sertifikat;
    }

    public void setNomor_sertifikat(String nomor_sertifikat) {
        this.nomor_sertifikat = nomor_sertifikat;
    }

    public String getAlamat() {
        return String.valueOf(alamat);
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getJenis_hak() {
        return String.valueOf(jenis_hak);
    }

    public void setJenis_hak(String jenis_hak) {
        this.jenis_hak = jenis_hak;
    }

    public String getPenggunaan() {
        return String.valueOf(penggunaan);
    }

    public void setPenggunaan(String penggunaan) {
        this.penggunaan = penggunaan;
    }

    public String getAsal_perolehan() {
        return asal_perolehan;
    }

    public void setAsal_perolehan(String asal_perolehan) {
        this.asal_perolehan = asal_perolehan;
    }

    public String getHarga_perolehan() {
        return harga_perolehan;
    }

    public void setHarga_perolehan(String harga_perolehan) {
        this.harga_perolehan = harga_perolehan;
    }

    public String getKeterangan() {
        return String.valueOf(keterangan);
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getStatus_aset() {
        return status_aset;
    }

    public void setStatus_aset(String status_aset) {
        this.status_aset = status_aset;
    }

    public String getCreated_datetime() {
        return created_datetime;
    }

    public void setCreated_datetime(String created_datetime) {
        this.created_datetime = created_datetime;
    }

    public String getCreated_by_id() {
        return created_by_id;
    }

    public void setCreated_by_id(String created_by_id) {
        this.created_by_id = created_by_id;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public List<FotoAsetPojo> getFoto_aset() {
        return foto_aset;
    }

    public void setFoto_aset(List<FotoAsetPojo> foto_aset) {
        this.foto_aset = foto_aset;
    }

}
