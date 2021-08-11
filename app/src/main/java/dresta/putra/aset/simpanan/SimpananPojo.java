package dresta.putra.aset.simpanan;

import com.google.gson.annotations.SerializedName;

import dresta.putra.aset.nasabah.NasabahPojo;

public class SimpananPojo {
    @SerializedName("id_simpanan")
    String id_simpanan;
    @SerializedName("id_owner")
    String id_owner;
    @SerializedName("id_kolektor")
    String id_kolektor;
    @SerializedName("id_nasabah")
    String id_nasabah;
    @SerializedName("jumlah_simpanan")
    String jumlah_simpanan;
    @SerializedName("tgl_simpanan")
    String tgl_simpanan;
    @SerializedName("last_update")
    String last_update;
    @SerializedName("status_simpanan")
    String status_simpanan;
    @SerializedName("input_oleh")
    String input_oleh;
    @SerializedName("input_oleh_id")
    String input_oleh_id;
    @SerializedName("update_oleh")
    String update_oleh;
    @SerializedName("update_oleh_id")
    String update_oleh_id;
    @SerializedName("note")
    String note;
    @SerializedName("nasabah")
    NasabahPojo nasabah;

    public SimpananPojo(String id_simpanan, String id_owner, String id_kolektor, String id_nasabah, String jumlah_simpanan, String tgl_simpanan, String last_update, String status_simpanan, String input_oleh, String input_oleh_id, String update_oleh, String update_oleh_id, String note, NasabahPojo nasabah) {
        this.id_simpanan = id_simpanan;
        this.id_owner = id_owner;
        this.id_kolektor = id_kolektor;
        this.id_nasabah = id_nasabah;
        this.jumlah_simpanan = jumlah_simpanan;
        this.tgl_simpanan = tgl_simpanan;
        this.last_update = last_update;
        this.status_simpanan = status_simpanan;
        this.input_oleh = input_oleh;
        this.input_oleh_id = input_oleh_id;
        this.update_oleh = update_oleh;
        this.update_oleh_id = update_oleh_id;
        this.note = note;
        this.nasabah = nasabah;
    }

    public SimpananPojo() {
    }

    public String getId_simpanan() {
        return id_simpanan;
    }

    public void setId_simpanan(String id_simpanan) {
        this.id_simpanan = id_simpanan;
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

    public String getJumlah_simpanan() {
        return jumlah_simpanan;
    }

    public void setJumlah_simpanan(String jumlah_simpanan) {
        this.jumlah_simpanan = jumlah_simpanan;
    }

    public String getTgl_simpanan() {
        return tgl_simpanan;
    }

    public void setTgl_simpanan(String tgl_simpanan) {
        this.tgl_simpanan = tgl_simpanan;
    }

    public String getLast_update() {
        return last_update;
    }

    public void setLast_update(String last_update) {
        this.last_update = last_update;
    }

    public String getStatus_simpanan() {
        return status_simpanan;
    }

    public void setStatus_simpanan(String status_simpanan) {
        this.status_simpanan = status_simpanan;
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

    public String getUpdate_oleh() {
        return update_oleh;
    }

    public void setUpdate_oleh(String update_oleh) {
        this.update_oleh = update_oleh;
    }

    public String getUpdate_oleh_id() {
        return update_oleh_id;
    }

    public void setUpdate_oleh_id(String update_oleh_id) {
        this.update_oleh_id = update_oleh_id;
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
