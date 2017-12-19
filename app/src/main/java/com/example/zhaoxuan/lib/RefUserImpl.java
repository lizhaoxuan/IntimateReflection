package com.example.zhaoxuan.lib;

import com.example.zhaoxuan.RefUser;

import me.ele.intimate.BaseRefImpl;

/**
 * Created by lizhaoxuan on 2017/12/18.
 */

public class RefUserImpl extends BaseRefImpl implements RefUser {

    User mData;

    public RefUserImpl(Object mObject) {
        super(mObject, User.class);
        mData = (User) mObject;
    }

    public User getObject() {
        return (User) mObject;
    }

    @Override
    public String getName() {
        return (String) mData.getName$IntimateField();
    }

    @Override
    public String getSex() {
        return mData.sex;
    }

    @Override
    public String setSex(String sex) {
        return mData.sex = sex;
    }

    @Override
    public String getAgeStr() {
        return mData.getAgeStr();
    }

    @Override
    public String getSexStr() {
        return mData.getSexStr();
    }

    @Override
    public String getClassName() {
        return (String) mData.getClassName$IntimateMethod();
    }

    @Override
    public void println() {
        mData.println();
    }
}
