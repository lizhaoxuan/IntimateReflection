package com.example.zhaoxuan;

import com.example.zhaoxuan.lib.RefUserImpl;

import me.ele.intimate.BaseRefImpl;

/**
 * Created by lizhaoxuan on 2017/12/15.
 */

public class RefImplFactory {

    public static <T> T getRefImplThrows(Object object, Class<T> clazz) throws ClassNotFoundException {
        String name = clazz.getCanonicalName();
        BaseRefImpl baseRef = null;
        switch (name) {
            case "com.example.zhaoxuan.RefTextView":
                baseRef = new RefTextViewImpl(object);
                break;
            case "com.example.zhaoxuan.RefListenerInfo":
                baseRef = new RefListenerInfoImpl(object);
                break;
            case "com.example.zhaoxuan.RefUser":
                baseRef = new RefUserImpl(object);
                break;
        }
        return (T) baseRef;
    }

    public static <T> T getRefImpl(Object object, Class<T> clazz) {
        try {
            return getRefImplThrows(object, clazz);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
