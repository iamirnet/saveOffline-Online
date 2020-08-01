package net.iamir.siadat.handel.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Record implements Parcelable {

    @SerializedName("id")
    private String RecordId;
    @SerializedName("local_id")
    private String RecordLocalId;
    @SerializedName("name")
    private String RecordName;
    @SerializedName("family")
    private String RecordFamily;
    @SerializedName("level")
    private String RecordLevel;
    @SerializedName("status")
    private String RecordStatus;

    public String getRecordId() {
        return RecordId;
    }

    public void setRecordId(String recordId) {
        RecordId = recordId;
    }

    public String getRecordLocalId() {
        return RecordLocalId;
    }

    public void setRecordLocalId(String recordLocalId) {
      RecordLocalId = recordLocalId;
    }

    public String getRecordName() {
        return RecordName;
    }

    public void setRecordName(String recordName) {
        RecordName = recordName;
    }

    public String getRecordFamily() {
        return RecordFamily;
    }

    public void setRecordFamily(String recordFamily) {
        RecordFamily = recordFamily;
    }

    public String getRecordLevel() {
        return RecordLevel;
    }

    public void setRecordLevel(String recordLevel) {
        RecordLevel = recordLevel;
    }

    public String getRecordStatus() {
        return RecordStatus;
    }

    public void setRecordStatus(String recordStatus) {
        RecordStatus = recordStatus;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.RecordId);
        dest.writeString(this.RecordName);
        dest.writeString(this.RecordFamily);
        dest.writeString(this.RecordLevel);
        dest.writeString(this.RecordStatus);
        dest.writeString(this.RecordLocalId);
    }

    public Record() {
    }

    protected Record(Parcel in) {
        this.RecordId = in.readString();
        this.RecordName = in.readString();
        this.RecordFamily = in.readString();
        this.RecordLevel = in.readString();
        this.RecordStatus = in.readString();
        this.RecordLocalId = in.readString();
    }

    public static final Creator<Record> CREATOR = new Creator<Record>() {
        @Override
        public Record createFromParcel(Parcel source) {
            return new Record(source);
        }

        @Override
        public Record[] newArray(int size) {
            return new Record[size];
        }
    };


}