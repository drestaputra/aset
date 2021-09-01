package dresta.putra.aset.user;

import com.google.gson.annotations.SerializedName;

public class SummaryUserPojo {
    @SerializedName("nasabah")
    Integer nasabah;
    @SerializedName("pinjaman")
    Integer pinjaman;
    @SerializedName("simpanan")
    Integer simpanan;

    public SummaryUserPojo(Integer nasabah, Integer pinjaman, Integer simpanan) {
        this.nasabah = nasabah;
        this.pinjaman = pinjaman;
        this.simpanan = simpanan;
    }

    public SummaryUserPojo() {
    }

    public Integer getNasabah() {
        return nasabah;
    }

    public void setNasabah(Integer nasabah) {
        this.nasabah = nasabah;
    }

    public Integer getPinjaman() {
        return pinjaman;
    }

    public void setPinjaman(Integer pinjaman) {
        this.pinjaman = pinjaman;
    }

    public Integer getSimpanan() {
        return simpanan;
    }

    public void setSimpanan(Integer simpanan) {
        this.simpanan = simpanan;
    }
}
