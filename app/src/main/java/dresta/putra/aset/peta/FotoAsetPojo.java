package dresta.putra.aset.peta;

import com.google.gson.annotations.SerializedName;

public class FotoAsetPojo {
    @SerializedName("id_foto_aset")
    String id_foto_aset;
    @SerializedName("id_aset")
    String id_aset;
    @SerializedName("foto_aset")
    String foto_aset;
    @SerializedName("status_foto")
    String status_foto;
    @SerializedName("created_datetime")
    String created_datetime;

    public FotoAsetPojo() {
    }

    public FotoAsetPojo(String id_foto_aset, String id_aset, String foto_aset, String status_foto, String created_datetime) {
        this.id_foto_aset = id_foto_aset;
        this.id_aset = id_aset;
        this.foto_aset = foto_aset;
        this.status_foto = status_foto;
        this.created_datetime = created_datetime;
    }

    public String getId_foto_aset() {
        return id_foto_aset;
    }

    public void setId_foto_aset(String id_foto_aset) {
        this.id_foto_aset = id_foto_aset;
    }

    public String getId_aset() {
        return id_aset;
    }

    public void setId_aset(String id_aset) {
        this.id_aset = id_aset;
    }

    public String getFoto_aset() {
        return foto_aset;
    }

    public void setFoto_aset(String foto_aset) {
        this.foto_aset = foto_aset;
    }

    public String getStatus_foto() {
        return status_foto;
    }

    public void setStatus_foto(String status_foto) {
        this.status_foto = status_foto;
    }

    public String getCreated_datetime() {
        return created_datetime;
    }

    public void setCreated_datetime(String created_datetime) {
        this.created_datetime = created_datetime;
    }
}
