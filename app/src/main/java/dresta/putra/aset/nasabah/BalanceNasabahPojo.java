package dresta.putra.aset.nasabah;

import com.google.gson.annotations.SerializedName;

public class BalanceNasabahPojo {
    @SerializedName("angsuran")
    Float angsuran;
    @SerializedName("pinjaman")
    Float pinjaman;
    @SerializedName("simpanan")
    Float simpanan;

    public BalanceNasabahPojo(Float angsuran, Float pinjaman, Float simpanan) {
        this.angsuran = angsuran;
        this.pinjaman = pinjaman;
        this.simpanan = simpanan;
    }

    public BalanceNasabahPojo() {
    }

    public Float getAngsuran() {
        return angsuran;
    }

    public void setAngsuran(Float angsuran) {
        this.angsuran = angsuran;
    }

    public Float getPinjaman() {
        return pinjaman;
    }

    public void setPinjaman(Float pinjaman) {
        this.pinjaman = pinjaman;
    }

    public Float getSimpanan() {
        return simpanan;
    }

    public void setSimpanan(Float simpanan) {
        this.simpanan = simpanan;
    }
}
