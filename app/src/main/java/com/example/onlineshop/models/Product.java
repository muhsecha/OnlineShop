package com.example.onlineshop.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {
    private String id, name, desc, price, stock, image, productCategoryId, nameCategory;
    private boolean show;

    public Product() {

    }

    protected Product(Parcel in) {
        id = in.readString();
        name = in.readString();
        desc = in.readString();
        price = in.readString();
        stock = in.readString();
        image = in.readString();
        productCategoryId = in.readString();
        nameCategory = in.readString();
        show = in.readByte() != 0;
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(String productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    public String getNameCategory() {
        return nameCategory;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(desc);
        parcel.writeString(price);
        parcel.writeString(stock);
        parcel.writeString(image);
        parcel.writeString(productCategoryId);
        parcel.writeString(nameCategory);
        parcel.writeByte((byte) (show ? 1 : 0));
    }
}
