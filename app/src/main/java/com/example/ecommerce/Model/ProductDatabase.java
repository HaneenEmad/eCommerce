package com.example.ecommerce.Model;

public class ProductDatabase {

    private String name, phone, address, email, sid, productState;
    private String categoryName, description, price, pname, saveCurrentDate, saveCurrentTime,downloadImageUrl, productId;

    public ProductDatabase() {
    }

    public ProductDatabase(String name, String phone, String address, String email, String sid, String productState, String categoryName, String description, String price, String pname, String saveCurrentDate, String saveCurrentTime, String downloadImageUrl, String productId) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.email = email;
        this.sid = sid;
        this.productState = productState;
        this.categoryName = categoryName;
        this.description = description;
        this.price = price;
        this.pname = pname;
        this.saveCurrentDate = saveCurrentDate;
        this.saveCurrentTime = saveCurrentTime;
        this.downloadImageUrl = downloadImageUrl;
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }



    public void setEmail(String email) {
        this.email = email;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getProductState() {
        return productState;
    }

    public void setProductState(String productState) {
        this.productState = productState;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getSaveCurrentDate() {
        return saveCurrentDate;
    }

    public void setSaveCurrentDate(String saveCurrentDate) {
        this.saveCurrentDate = saveCurrentDate;
    }

    public String getSaveCurrentTime() {
        return saveCurrentTime;
    }

    public void setSaveCurrentTime(String saveCurrentTime) {
        this.saveCurrentTime = saveCurrentTime;
    }

    public String getDownloadImageUrl() {
        return downloadImageUrl;
    }

    public void setDownloadImageUrl(String downloadImageUrl) {
        this.downloadImageUrl = downloadImageUrl;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    @Override
    public String toString() {
        return "ProductDatabase{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", sid='" + sid + '\'' +
                ", productState='" + productState + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", description='" + description + '\'' +
                ", price='" + price + '\'' +
                ", pname='" + pname + '\'' +
                ", saveCurrentDate='" + saveCurrentDate + '\'' +
                ", saveCurrentTime='" + saveCurrentTime + '\'' +
                '}';
    }
}
