package dresta.putra.aset.kontak;

import com.google.gson.annotations.SerializedName;

public class KontakPojo {
    @SerializedName("app_contact_address")
    String app_contact_address;
    @SerializedName("app_contact_fb")
    String app_contact_fb;
    @SerializedName("app_contact_ig")
    String app_contact_ig;
    @SerializedName("app_contact_mail")
    String app_contact_mail;
    @SerializedName("app_contact_phone")
    String app_contact_phone;
    @SerializedName("app_contact_twitter")
    String app_contact_twitter;
    @SerializedName("app_contact_wa")
    String app_contact_wa;
    @SerializedName("app_description")
    String app_description;
    @SerializedName("app_dev")
    String app_dev;
    @SerializedName("app_dev_web")
    String app_dev_web;
    @SerializedName("app_name")
    String app_names;
    @SerializedName("app_is_aset_show")
    String app_is_aset_show;

    public KontakPojo(String app_contact_address, String app_contact_fb, String app_contact_ig, String app_contact_mail, String app_contact_phone, String app_contact_twitter, String app_contact_wa, String app_description, String app_dev, String app_dev_web, String app_names, String app_is_aset_show) {
        this.app_contact_address = app_contact_address;
        this.app_contact_fb = app_contact_fb;
        this.app_contact_ig = app_contact_ig;
        this.app_contact_mail = app_contact_mail;
        this.app_contact_phone = app_contact_phone;
        this.app_contact_twitter = app_contact_twitter;
        this.app_contact_wa = app_contact_wa;
        this.app_description = app_description;
        this.app_dev = app_dev;
        this.app_dev_web = app_dev_web;
        this.app_names = app_names;
        this.app_is_aset_show = app_is_aset_show;
    }

    public KontakPojo() {
    }

    public String getApp_contact_address() {
        return app_contact_address;
    }

    public void setApp_contact_address(String app_contact_address) {
        this.app_contact_address = app_contact_address;
    }

    public String getApp_contact_fb() {
        return app_contact_fb;
    }

    public void setApp_contact_fb(String app_contact_fb) {
        this.app_contact_fb = app_contact_fb;
    }

    public String getApp_contact_ig() {
        return app_contact_ig;
    }

    public void setApp_contact_ig(String app_contact_ig) {
        this.app_contact_ig = app_contact_ig;
    }

    public String getApp_contact_mail() {
        return app_contact_mail;
    }

    public void setApp_contact_mail(String app_contact_mail) {
        this.app_contact_mail = app_contact_mail;
    }

    public String getApp_contact_phone() {
        return app_contact_phone;
    }

    public void setApp_contact_phone(String app_contact_phone) {
        this.app_contact_phone = app_contact_phone;
    }

    public String getApp_contact_twitter() {
        return app_contact_twitter;
    }

    public void setApp_contact_twitter(String app_contact_twitter) {
        this.app_contact_twitter = app_contact_twitter;
    }

    public String getApp_contact_wa() {
        return app_contact_wa;
    }

    public void setApp_contact_wa(String app_contact_wa) {
        this.app_contact_wa = app_contact_wa;
    }

    public String getApp_description() {
        return app_description;
    }

    public void setApp_description(String app_description) {
        this.app_description = app_description;
    }

    public String getApp_dev() {
        return app_dev;
    }

    public void setApp_dev(String app_dev) {
        this.app_dev = app_dev;
    }

    public String getApp_dev_web() {
        return app_dev_web;
    }

    public void setApp_dev_web(String app_dev_web) {
        this.app_dev_web = app_dev_web;
    }

    public String getApp_names() {
        return app_names;
    }

    public void setApp_names(String app_names) {
        this.app_names = app_names;
    }

    public String getApp_is_aset_show() {
        return app_is_aset_show;
    }

    public void setApp_is_aset_show(String app_is_aset_show) {
        this.app_is_aset_show = app_is_aset_show;
    }
}