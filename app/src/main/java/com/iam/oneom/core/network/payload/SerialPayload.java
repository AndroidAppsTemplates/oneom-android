package com.iam.oneom.core.network.payload;

import com.google.gson.annotations.SerializedName;
import com.iam.oneom.core.entities.model.Serial;

public class SerialPayload {

    @SerializedName("serial")
    private Serial serial;

    public Serial getSerial() {
        return serial;
    }

    public void setSerial(Serial serial) {
        this.serial = serial;
    }
}
