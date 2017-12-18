package com.example.zhaoxuan;

import me.ele.intimate.annotation.GetField;
import me.ele.intimate.annotation.Method;
import me.ele.intimate.annotation.RefTarget;
import me.ele.intimate.annotation.SetField;

/**
 * Created by lizhaoxuan on 2017/12/18.
 */
@RefTarget(className = "com.example.zhaoxuan.lib.User", isSystemClass = false)
public interface RefUser {

    @GetField("name")
    String getName();

    @GetField("sex")
    String getSex();

    @SetField("sex")
    String setSex(String sex);

    @Method
    String getAgeStr();

    @Method
    String getSexStr();

    @Method
    String getClassName();

}
