package me.ele.example.mock;

/**
 * Created by lizhaoxuan on 2017/12/29.
 */

public class Brand {

    private String name;
    private String des;

    public Brand(String name, String des) {
        this.name = name;
        this.des = des;
    }

    public String getName() {
        return name;
    }

    public String getDes() {
        return des;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
