package com.autollow.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Radsen on 7/25/17.
 */

public class Registration implements Parcelable {

    private String color;
    private String brand;
    private String model;
    private String regDate;
    private String lPlate;
    private String idType;
    private String idNumber;
    private String picture;
    private int service_type;
    private int fuel_type;
    private int vehicle_type;

    public Registration() {}

    protected Registration(Parcel in) {
        color = in.readString();
        brand = in.readString();
        model = in.readString();
        regDate = in.readString();
        lPlate = in.readString();
        idType = in.readString();
        idNumber = in.readString();
        picture = in.readString();
        service_type = in.readInt();
        fuel_type = in.readInt();
        vehicle_type = in.readInt();
    }

    public static final Creator<Registration> CREATOR = new Creator<Registration>() {
        @Override
        public Registration createFromParcel(Parcel in) {
            return new Registration(in);
        }

        @Override
        public Registration[] newArray(int size) {
            return new Registration[size];
        }
    };

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getRegistrationDate() {
        return regDate;
    }

    public void setRegistrationDate(String regDate) {
        this.regDate = regDate;
    }

    public String getLicensePlate() {
        return lPlate;
    }

    public void setLicensePlate(String lPlate) {
        this.lPlate = lPlate;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(color);
        dest.writeString(brand);
        dest.writeString(model);
        dest.writeString(regDate);
        dest.writeString(lPlate);
        dest.writeString(idType);
        dest.writeString(idNumber);
        dest.writeString(picture);
        dest.writeInt(service_type);
        dest.writeInt(fuel_type);
        dest.writeInt(vehicle_type);
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setServiceType(int serviceType) {
        this.service_type = serviceType;
    }

    public int getServiceType() {
        return service_type;
    }

    public void setFuelType(int fuelType) {
        this.fuel_type = fuelType;
    }

    public int getFuelType() {
        return fuel_type;
    }

    public void setVehicleType(int vehicleType) {
        this.vehicle_type = vehicleType;
    }

    public int getVehicleType() {
        return vehicle_type;
    }
}
