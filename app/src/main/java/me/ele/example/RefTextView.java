package me.ele.example;


import android.widget.TextView;

import me.ele.intimate.IntimateException;
import me.ele.intimate.annotation.GetField;
import me.ele.intimate.annotation.Method;
import me.ele.intimate.annotation.RefTarget;
import me.ele.intimate.annotation.SetField;

/**
 * Created by lizhaoxuan on 2017/12/15.
 */
@RefTarget(clazz = TextView.class, optimizationRef = false)
public interface RefTextView {

    @GetField("mText")
    CharSequence getText();

    @SetField("mText")
    String setText(CharSequence result);

    @GetField("mListenerInfo")
    Object getListenerInfo() throws IllegalAccessException, NoSuchFieldException, IntimateException;

    @Method("getDesiredHeight")
    int getDesiredHeight();

    @Method("isDirectionalNavigationKey")
    int isDirectionalNavigationKey(int keyCode) throws IntimateException;

}
