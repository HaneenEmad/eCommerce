package com.example.ecommerce.Model;

public class Users {
    private String userName, Phone, Password, image, Address;

    public Users() {
    }

    public Users(String name, String phone, String password, String image, String address) {
        this.userName = name;
        this.Phone = phone;
        this.Password = password;
        this.image = image;
        this.Address = address;
    }

    public Users(String name, String phone, String image, String address) {
        this.userName = name;
        this.Phone = phone;
        this.image = image;
        this.Address = address;
    }
    public Users(String name, String phone, String address) {
        this.userName = name;
        this.Phone = phone;
        this.Address = address;
    }



    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        this.Phone = phone;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        this.Password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        this.Address = address;
    }

    @Override
    public String toString() {
        return "Users{" +
                "userName='" + userName + '\'' +
                ", Phone='" + Phone + '\'' +
                ", Password='" + Password + '\'' +
                ", image='" + image + '\'' +
                ", Address='" + Address + '\'' +
                '}';
    }
}
