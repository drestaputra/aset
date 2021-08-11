package dresta.putra.aset.angsuran;

import com.google.gson.annotations.SerializedName;

public class RiwayatPinjamanPojo {
    @SerializedName("id_riwayat")
    String id_riwayat;
    @SerializedName("id_pinjaman")
    String id_pinjaman;
    @SerializedName("angsuran_ke")
    String angsuran_ke;
    @SerializedName("jumlah_riwayat_pembayaran")
    String jumlah_riwayat_pembayaran;
    @SerializedName("input_oleh")
    String input_oleh;
    @SerializedName("input_oleh_id")
    String input_oleh_id;
    @SerializedName("tgl_riwayat_pinjaman")
    String tgl_riwayat_pinjaman;
    @SerializedName("keterangan_riwayat")
    String keterangan_riwayat;

    public RiwayatPinjamanPojo(String id_riwayat, String id_pinjaman, String angsuran_ke, String jumlah_riwayat_pembayaran, String input_oleh, String input_oleh_id, String tgl_riwayat_pinjaman, String keterangan_riwayat) {
        this.id_riwayat = id_riwayat;
        this.id_pinjaman = id_pinjaman;
        this.angsuran_ke = angsuran_ke;
        this.jumlah_riwayat_pembayaran = jumlah_riwayat_pembayaran;
        this.input_oleh = input_oleh;
        this.input_oleh_id = input_oleh_id;
        this.tgl_riwayat_pinjaman = tgl_riwayat_pinjaman;
        this.keterangan_riwayat = keterangan_riwayat;
    }

    public RiwayatPinjamanPojo() {
    }

    public String getId_riwayat() {
        return id_riwayat;
    }

    public void setId_riwayat(String id_riwayat) {
        this.id_riwayat = id_riwayat;
    }

    public String getId_pinjaman() {
        return id_pinjaman;
    }

    public void setId_pinjaman(String id_pinjaman) {
        this.id_pinjaman = id_pinjaman;
    }

    public String getAngsuran_ke() {
        return angsuran_ke;
    }

    public void setAngsuran_ke(String angsuran_ke) {
        this.angsuran_ke = angsuran_ke;
    }

    public String getJumlah_riwayat_pembayaran() {
        return jumlah_riwayat_pembayaran;
    }

    public void setJumlah_riwayat_pembayaran(String jumlah_riwayat_pembayaran) {
        this.jumlah_riwayat_pembayaran = jumlah_riwayat_pembayaran;
    }

    public String getInput_oleh() {
        return input_oleh;
    }

    public void setInput_oleh(String input_oleh) {
        this.input_oleh = input_oleh;
    }

    public String getInput_oleh_id() {
        return input_oleh_id;
    }

    public void setInput_oleh_id(String input_oleh_id) {
        this.input_oleh_id = input_oleh_id;
    }

    public String getTgl_riwayat_pinjaman() {
        return tgl_riwayat_pinjaman;
    }

    public void setTgl_riwayat_pinjaman(String tgl_riwayat_pinjaman) {
        this.tgl_riwayat_pinjaman = tgl_riwayat_pinjaman;
    }

    public String getKeterangan_riwayat() {
        return keterangan_riwayat;
    }

    public void setKeterangan_riwayat(String keterangan_riwayat) {
        this.keterangan_riwayat = keterangan_riwayat;
    }
}
