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
    private String reg_date;
    private String license_plate;
    private String id_type;
    private String id_number;
    private String picture;
    private int service_type;
    private int fuel_type;
    private int vehicle_type;

    public Registration() {}

    protected Registration(Parcel in) {
        color = in.readString();
        brand = in.readString();
        model = in.readString();
        reg_date = in.readString();
        license_plate = in.readString();
        id_type = in.readString();
        id_number = in.readString();
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
        return reg_date;
    }

    public void setRegistrationDate(String regDate) {
        this.reg_date = regDate;
    }

    public String getLicensePlate() {
        return license_plate;
    }

    public void setLicensePlate(String lPlate) {
        this.license_plate = lPlate;
    }

    public String getIdType() {
        return id_type;
    }

    public void setIdType(String idType) {
        this.id_type = idType;
    }

    public String getIdNumber() {
        return id_number;
    }

    public void setIdNumber(String idNumber) {
        this.id_number = idNumber;
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
        dest.writeString(reg_date);
        dest.writeString(license_plate);
        dest.writeString(id_type);
        dest.writeString(id_number);
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

