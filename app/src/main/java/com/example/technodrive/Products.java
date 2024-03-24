package com.example.technodrive;

public class Products {
    private  String title,count,mark,desck,price,imageProduct,category,time,data,pId;
    public Products(){

    }

    public Products(String title, String count, String mark, String desck, String price, String imageProduct, String category, String time, String data, String pId) {
        this.title = title;
        this.count = count;
        this.mark = mark;
        this.desck = desck;
        this.price = price;
        this.imageProduct = imageProduct;
        this.category = category;
        this.time = time;
        this.data = data;
        this.pId = pId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getDesck() {
        return desck;
    }

    public void setDesck(String desck) {
        this.desck = desck;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImageProduct() {
        return imageProduct;
    }

    public void setImageProduct(String imageProduct) {
        this.imageProduct = imageProduct;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }
}
