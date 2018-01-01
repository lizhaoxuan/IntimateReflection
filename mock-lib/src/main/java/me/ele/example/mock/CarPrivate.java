package me.ele.example.mock;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lizhaoxuan on 2017/12/29.
 */

public class CarPrivate {

    private String name;
    private int level;
    private Brand brand;
    private List<Wheel> wheels;

    public CarPrivate() {
        name = "我的Private小电驴";
        level = 7;
        brand = new Brand("宝驴", "源自德国的百年品牌");
        wheels = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            wheels.add(new Wheel("米其林", 10));
        }
    }

    private String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    private int getLevel() {
        return level;
    }

    private void setLevel(int level) {
        this.level = level;
    }

    private Brand getBrand() {
        return brand;
    }

    private void setBrand(Brand brand) {
        this.brand = brand;
    }

    private List<Wheel> getWheels() {
        return wheels;
    }

    private void setWheels(List<Wheel> wheels) {
        this.wheels = wheels;
    }
}
