package com.example.demo.eneity;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;


@Data
@ToString
public class User {
    private Integer id;
    private String username;
    private String relname;
    private String stunum;
    private String  school;
    private Integer status;
    private String collection;
    private String followee;
    private String sessionkey;
    private String openid;
    private String tel;
    private String password;
    private String headurl;
    private String salt;
    public List<Goods> goodList = new ArrayList<>();

    public User() {
    }


    public User set(int goodId, int score) {
        this.goodList.add(new Goods(goodId, score));
        return this;
    }

    public Goods find() {
        for (Goods movie : goodList) {
            if (movie.getId()==id) {
                return movie;
            }
        }
        return null;
    }
    @Override
    public int hashCode(){
        /*hashCode方法返回值是int类型，所以重写时需要找到int类型的数据返回，还要保证此方法的返回值与对象的所有属性都相关,所以返回姓名属性的字符串的长度*/
        return this.id;
    }
    @Override
    public boolean equals(Object obj){
        if(obj instanceof User) {
            //若是，强转成Student对象，并比较属性的值
            User s = (User) obj;
            if(this.id==s.id) {
                //若属性的值相同，则返回true
                return true;
            }
        }
        return false;
    }

}
