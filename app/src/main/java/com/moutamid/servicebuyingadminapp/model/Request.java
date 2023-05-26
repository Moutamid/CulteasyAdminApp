package com.moutamid.servicebuyingadminapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Request implements Parcelable {

    private String id;
    private String serviceName;
    private String subServiceName;
    private String phone;
    private String userId;
    private String location;
    private String date;
    private String time;
    private String status;
    private String description;

    public Request(){

    }

    public Request(String id, String serviceName, String subServiceName, String phone, String userId, String location, String date, String time, String status, String description) {
        this.id = id;
        this.serviceName = serviceName;
        this.subServiceName = subServiceName;
        this.phone = phone;
        this.userId = userId;
        this.location = location;
        this.date = date;
        this.time = time;
        this.status = status;
        this.description = description;
    }

    protected Request(Parcel in) {
        id = in.readString();
        serviceName = in.readString();
        subServiceName = in.readString();
        phone = in.readString();
        userId = in.readString();
        location = in.readString();
        date = in.readString();
        time = in.readString();
        status = in.readString();
        description = in.readString();
    }

    public static final Creator<Request> CREATOR = new Creator<Request>() {
        @Override
        public Request createFromParcel(Parcel in) {
            return new Request(in);
        }

        @Override
        public Request[] newArray(int size) {
            return new Request[size];
        }
    };

    public String getSubServiceName() {
        return subServiceName;
    }

    public void setSubServiceName(String subServiceName) {
        this.subServiceName = subServiceName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(serviceName);
        dest.writeString(subServiceName);
        dest.writeString(phone);
        dest.writeString(userId);
        dest.writeString(location);
        dest.writeString(date);
        dest.writeString(time);
        dest.writeString(status);
        dest.writeString(description);
    }
}
