package dresta.putra.aset.pinjaman;

import com.google.gson.annotations.SerializedName;

import dresta.putra.aset.nasabah.NasabahPojo;

public class PinjamanPojo {
    @SerializedName("id_pinjaman")
    String id_pinjaman;
    @SerializedName("id_owner")
    String id_owner;
    @SerializedName("id_kolektor")
    String id_kolektor;
    @SerializedName("id_nasabah")
    String id_nasabah;
    @SerializedName("jumlah_pinjaman")
    String jumlah_pinjaman;
    @SerializedName("jumlah_diterima")
    String jumlah_diterima;
    @SerializedName("persentase_bunga")
    String persentase_bunga;
    @SerializedName("persentase_biaya_admin")
    String persentase_biaya_admin;
    @SerializedName("persentase_biaya_simpanan")
    String persentase_biaya_simpanan;
    @SerializedName("periode_angsuran")
    String periode_angsuran;
    @SerializedName("lama_angsuran")
    String lama_angsuran;
    @SerializedName("jumlah_pinjaman_setelah_bunga")
    String jumlah_pinjaman_setelah_bunga;
    @SerializedName("jumlah_perangsuran")
    String jumlah_perangsuran;
    @SerializedName("jumlah_terbayar")
    String jumlah_terbayar;
    @SerializedName("input_oleh")
    String input_oleh;
    @SerializedName("input_oleh_id")
    String input_oleh_id;
    @SerializedName("tgl_pinjaman")
    String tgl_pinjaman;
    @SerializedName("tgl_terakhir_angsuran")
    String tgl_terakhir_angsuran;
    @SerializedName("angsuran_ke")
    String angsuran_ke;
    @SerializedName("last_update")
    String last_update;
    @SerializedName("status_pinjaman")
    String status_pinjaman;
    @SerializedName("note")
    String note;
    @SerializedName("nasabah")
    NasabahPojo nasabah;

    public PinjamanPojo() {
    }

    public PinjamanPojo(String id_pinjaman, String id_owner, String id_kolektor, String id_nasabah, String jumlah_pinjaman, String jumlah_diterima, String persentase_bunga, String persentase_biaya_admin, String persentase_biaya_simpanan, String periode_angsuran, String lama_angsuran, String jumlah_pinjaman_setelah_bunga, String jumlah_perangsuran, String jumlah_terbayar, String input_oleh, String input_oleh_id, String tgl_pinjaman, String tgl_terakhir_angsuran, String angsuran_ke, String last_update, String status_pinjaman, String note, NasabahPojo nasabah) {
        this.id_pinjaman = id_pinjaman;
        this.id_owner = id_owner;
        this.id_kolektor = id_kolektor;
        this.id_nasabah = id_nasabah;
        this.jumlah_pinjaman = jumlah_pinjaman;
        this.jumlah_diterima = jumlah_diterima;
        this.persentase_bunga = persentase_bunga;
        this.persentase_biaya_admin = persentase_biaya_admin;
        this.persentase_biaya_simpanan = persentase_biaya_simpanan;
        this.periode_angsuran = periode_angsuran;
        this.lama_angsuran = lama_angsuran;
        this.jumlah_pinjaman_setelah_bunga = jumlah_pinjaman_setelah_bunga;
        this.jumlah_perangsuran = jumlah_perangsuran;
        this.jumlah_terbayar = jumlah_terbayar;
        this.input_oleh = input_oleh;
        this.input_oleh_id = input_oleh_id;
        this.tgl_pinjaman = tgl_pinjaman;
        this.tgl_terakhir_angsuran = tgl_terakhir_angsuran;
        this.angsuran_ke = angsuran_ke;
        this.last_update = last_update;
        this.status_pinjaman = status_pinjaman;
        this.note = note;
        this.nasabah = nasabah;
    }

    public String getId_pinjaman() {
        return id_pinjaman;
    }

    public void setId_pinjaman(String id_pinjaman) {
        this.id_pinjaman = id_pinjaman;
    }

    public String getId_owner() {
        return id_owner;
    }

    public void setId_owner(String id_owner) {
        this.id_owner = id_owner;
    }

    public String getId_kolektor() {
        return id_kolektor;
    }

    public void setId_kolektor(String id_kolektor) {
        this.id_kolektor = id_kolektor;
    }

    public String getId_nasabah() {
        return id_nasabah;
    }

    public void setId_nasabah(String id_nasabah) {
        this.id_nasabah = id_nasabah;
    }

    public String getJumlah_pinjaman() {
        return jumlah_pinjaman;
    }

    public void setJumlah_pinjaman(String jumlah_pinjaman) {
        this.jumlah_pinjaman = jumlah_pinjaman;
    }

    public String getJumlah_diterima() {
        return jumlah_diterima;
    }

    public void setJumlah_diterima(String jumlah_diterima) {
        this.jumlah_diterima = jumlah_diterima;
    }

    public String getPersentase_bunga() {
        return persentase_bunga;
    }

    public void setPersentase_bunga(String persentase_bunga) {
        this.persentase_bunga = persentase_bunga;
    }

    public String getPersentase_biaya_admin() {
        return persentase_biaya_admin;
    }

    public void setPersentase_biaya_admin(String persentase_biaya_admin) {
        this.persentase_biaya_admin = persentase_biaya_admin;
    }

    public String getPersentase_biaya_simpanan() {
        return persentase_biaya_simpanan;
    }

    public void setPersentase_biaya_simpanan(String persentase_biaya_simpanan) {
        this.persentase_biaya_simpanan = persentase_biaya_simpanan;
    }

    public String getPeriode_angsuran() {
        return periode_angsuran;
    }

    public void setPeriode_angsuran(String periode_angsuran) {
        this.periode_angsuran = periode_angsuran;
    }

    public String getLama_angsuran() {
        return lama_angsuran;
    }

    public void setLama_angsuran(String lama_angsuran) {
        this.lama_angsuran = lama_angsuran;
    }

    public String getJumlah_pinjaman_setelah_bunga() {
        return jumlah_pinjaman_setelah_bunga;
    }

    public void setJumlah_pinjaman_setelah_bunga(String jumlah_pinjaman_setelah_bunga) {
        this.jumlah_pinjaman_setelah_bunga = jumlah_pinjaman_setelah_bunga;
    }

    public String getJumlah_perangsuran() {
        return jumlah_perangsuran;
    }

    public void setJumlah_perangsuran(String jumlah_perangsuran) {
        this.jumlah_perangsuran = jumlah_perangsuran;
    }

    public String getJumlah_terbayar() {
        return jumlah_terbayar;
    }

    public void setJumlah_terbayar(String jumlah_terbayar) {
        this.jumlah_terbayar = jumlah_terbayar;
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

    public String getTgl_pinjaman() {
        return tgl_pinjaman;
    }

    public void setTgl_pinjaman(String tgl_pinjaman) {
        this.tgl_pinjaman = tgl_pinjaman;
    }

    public String getTgl_terakhir_angsuran() {
        return tgl_terakhir_angsuran;
    }

    public void setTgl_terakhir_angsuran(String tgl_terakhir_angsuran) {
        this.tgl_terakhir_angsuran = tgl_terakhir_angsuran;
    }

    public String getAngsuran_ke() {
        return angsuran_ke;
    }

    public void setAngsuran_ke(String angsuran_ke) {
        this.angsuran_ke = angsuran_ke;
    }

    public String getLast_update() {
        return last_update;
    }

    public void setLast_update(String last_update) {
        this.last_update = last_update;
    }

    public String getStatus_pinjaman() {
        return status_pinjaman;
    }

    public void setStatus_pinjaman(String status_pinjaman) {
        this.status_pinjaman = status_pinjaman;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public NasabahPojo getNasabah() {
        return nasabah;
    }

    public void setNasabah(NasabahPojo nasabah) {
        this.nasabah = nasabah;
    }
}
