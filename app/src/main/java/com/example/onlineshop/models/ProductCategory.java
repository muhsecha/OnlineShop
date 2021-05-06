package com.example.onlineshop.models;

import android.os.Parcel;
import android.os.Parcelable;

public class ProductCategory implements Parcelable {
    private String id, name;

    public ProductCategory() {

    }

    protected ProductCategory(Parcel in) {
        id = in.readString();
        name = in.readString();
    }

    public static final Creator<ProductCategory> CREATOR = new Creator<ProductCategory>() {
        @Override
        public ProductCategory createFromParcel(Parcel in) {
            return new ProductCategory(in);
        }

        @Override
        public ProductCategory[] newArray(int size) {
            return new ProductCategory[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
    }

    @Override
    public String toString() {
        return name;
    }
}
