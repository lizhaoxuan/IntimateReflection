package com.example.zhaoxuan;

import android.view.View;

import java.lang.reflect.Field;

import me.ele.intimate.BaseRefImpl;

/**
 * Created by lizhaoxuan on 2017/12/15.
 */

public class RefListenerInfo$$Impl extends BaseRefImpl<RefListenerInfo> implements RefListenerInfo {

    Field mOnClickListener;

    public RefListenerInfo$$Impl(Object mObject) throws ClassNotFoundException {
        super(mObject, Class.forName("android.view.View$ListenerInfo"));
    }

    @Override
    public View.OnClickListener getListener() {
        try {
            if (mOnClickListener == null) {
                mOnClickListener = getField("mOnClickListener");
            }
            return (View.OnClickListener) mOnClickListener.get(mObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
