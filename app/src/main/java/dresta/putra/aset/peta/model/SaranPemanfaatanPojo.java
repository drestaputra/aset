package dresta.putra.aset.peta.model;

import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.google.gson.annotations.SerializedName;
import com.pchmn.materialchips.model.ChipInterface;

public class SaranPemanfaatanPojo  implements ChipInterface {
    @SerializedName("id")
    private String id;
    private Uri avatarUri;
    @SerializedName("name")
    private String name;
    private String phoneNumber;

    public SaranPemanfaatanPojo() {
    }

    public SaranPemanfaatanPojo(String id, Uri avatarUri, String name, String phoneNumber) {
        this.id = id;
        this.avatarUri = avatarUri;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public Object getId() {
        return id;
    }

    @Override
    public Uri getAvatarUri() {
        return avatarUri;
    }

    @Override
    public Drawable getAvatarDrawable() {
        return null;
    }

    @Override
    public String getLabel() {
        return name;
    }

    @Override
    public String getInfo() {
        return phoneNumber;
    }
}
