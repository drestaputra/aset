package dresta.putra.aset.slider;

import com.google.gson.annotations.SerializedName;

public class SliderPojo {
    @SerializedName("id_slider")
    String id_slider;
    @SerializedName("judul_slider")
    String judul_slider;
    @SerializedName("deskripsi_slider")
    String deskripsi_slider;
    @SerializedName("link_slider")
    String link_slider;
    @SerializedName("image_slider")
    String image_slider;
    @SerializedName("is_active")
    String is_actice;

    public SliderPojo(String id_slider, String judul_slider, String deskripsi_slider, String link_slider, String image_slider, String is_actice) {
        this.id_slider = id_slider;
        this.judul_slider = judul_slider;
        this.deskripsi_slider = deskripsi_slider;
        this.link_slider = link_slider;
        this.image_slider = image_slider;
        this.is_actice = is_actice;
    }

    public SliderPojo() {
    }

    public String getId_slider() {
        return id_slider;
    }

    public void setId_slider(String id_slider) {
        this.id_slider = id_slider;
    }

    public String getJudul_slider() {
        return judul_slider;
    }

    public void setJudul_slider(String judul_slider) {
        this.judul_slider = judul_slider;
    }

    public String getDeskripsi_slider() {
        return deskripsi_slider;
    }

    public void setDeskripsi_slider(String deskripsi_slider) {
        this.deskripsi_slider = deskripsi_slider;
    }

    public String getLink_slider() {
        return link_slider;
    }

    public void setLink_slider(String link_slider) {
        this.link_slider = link_slider;
    }

    public String getImage_slider() {
        return image_slider;
    }

    public void setImage_slider(String image_slider) {
        this.image_slider = image_slider;
    }

    public String getIs_actice() {
        return is_actice;
    }

    public void setIs_actice(String is_actice) {
        this.is_actice = is_actice;
    }
}
