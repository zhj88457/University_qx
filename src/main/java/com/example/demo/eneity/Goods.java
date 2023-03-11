package com.example.demo.eneity;

import lombok.ToString;

@ToString
public class Goods implements Comparable<Goods> {
    private int sellerId;
    private String name;
    private int id;
    private double price;
    private String detail;
    private String upload_date;
    private int status;
    private String picture;
    private int score;

    public Goods() {

    }

    public Goods(int id, int score) {
        this.id = id;
        this.score = score;
    }

    public int getSellerId() {
        return sellerId;
    }
    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getUpload_date() {
        return upload_date;
    }

    public void setUpload_date(String upload_date) {
        this.upload_date = upload_date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object obj){
        if(obj instanceof Goods) {
            //若是，强转成Student对象，并比较属性的值
            Goods s = (Goods) obj;
            if(this.id==s.id) {
                //若属性的值相同，则返回true
                return true;
            }
        }
        return false;
    }
    @Override
    public int hashCode(){
        /*hashCode方法返回值是int类型，所以重写时需要找到int类型的数据返回，还要保证此方法的返回值与对象的所有属性都相关,所以返回姓名属性的字符串的长度*/
        return this.id;
    }
    @Override
    public int compareTo(Goods o) {
        return score -o.score ;
    }
}
