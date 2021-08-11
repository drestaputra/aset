package dresta.putra.aset.informasi_program;

import com.google.gson.annotations.SerializedName;

public class InformasiProgramPojo {
    @SerializedName("id_informasi_program")
    String id_informasi_program;
    @SerializedName("id_owner")
    String id_owner;
    @SerializedName("judul_informasi_program")
    String judul_informasi_program;
    @SerializedName("deskripsi_informasi_program")
    String deskripsi_informasi_program;
    @SerializedName("tgl_informasi_program")
    String tgl_informasi_program;
    @SerializedName("status")
    String status;
    @SerializedName("foto_informasi_program")
    String foto_informasi_program;

    public InformasiProgramPojo(String id_informasi_program, String id_owner, String judul_informasi_program, String deskripsi_informasi_program, String tgl_informasi_program, String status, String foto_informasi_program) {
        this.id_informasi_program = id_informasi_program;
        this.id_owner = id_owner;
        this.judul_informasi_program = judul_informasi_program;
        this.deskripsi_informasi_program = deskripsi_informasi_program;
        this.tgl_informasi_program = tgl_informasi_program;
        this.status = status;
        this.foto_informasi_program = foto_informasi_program;
    }

    public InformasiProgramPojo() {
    }

    public String getId_informasi_program() {
        return id_informasi_program;
    }

    public void setId_informasi_program(String id_informasi_program) {
        this.id_informasi_program = id_informasi_program;
    }

    public String getId_owner() {
        return id_owner;
    }

    public void setId_owner(String id_owner) {
        this.id_owner = id_owner;
    }

    public String getJudul_informasi_program() {
        return judul_informasi_program;
    }

    public void setJudul_informasi_program(String judul_informasi_program) {
        this.judul_informasi_program = judul_informasi_program;
    }

    public String getDeskripsi_informasi_program() {
        return deskripsi_informasi_program;
    }

    public void setDeskripsi_informasi_program(String deskripsi_informasi_program) {
        this.deskripsi_informasi_program = deskripsi_informasi_program;
    }

    public String getTgl_informasi_program() {
        return tgl_informasi_program;
    }

    public void setTgl_informasi_program(String tgl_informasi_program) {
        this.tgl_informasi_program = tgl_informasi_program;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFoto_informasi_program() {
        return foto_informasi_program;
    }

    public void setFoto_informasi_program(String foto_informasi_program) {
        this.foto_informasi_program = foto_informasi_program;
    }
}
