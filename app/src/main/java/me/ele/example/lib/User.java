package me.ele.example.lib;

/**
 * Created by lizhaoxuan on 2017/12/18.
 */

public class User {

    private String name;
    protected String sex;
    private int age;

    private String className;

    public User(String name, String sex, int age, String className) {
        this.name = name;
        this.sex = sex;
        this.age = age;
        this.className = className;
    }

    String getAgeStr() {
        return String.valueOf(age + 1);
    }

    protected String getSexStr() {
        return sex;
    }

    private String getClassName() {
        return className;
    }

    public Object getName$IntimateField() {
        return name;
    }

    //生成的方法
    public Object getAge$IntimateField() {
        return age;
    }

    public Object getClassName$IntimateMethod() {
        return getClassName();
    }

    public void println() {
        System.out.println("ssss");
    }
}
