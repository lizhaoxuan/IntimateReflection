package me.ele.example;


import java.lang.reflect.Field;
import java.lang.reflect.Method;

import me.ele.intimate.BaseRefImpl;
import me.ele.intimate.IntimateException;

/**
 * Created by lizhaoxuan on 2017/12/15.
 */

public class RefTextViewImpl extends BaseRefImpl implements RefTextView {

    Field mText;
    Field mListenerInfo;
    Method getDesiredHeight;
    Method isDirectionalNavigationKey;

    public RefTextViewImpl(Object mTextView) {
        super(mTextView, android.widget.TextView.class);
    }


    @Override
    public CharSequence getText() {
        try {
            if (mText == null) {
                mText = getField("mText");
            }
            return (CharSequence) mText.get(mObject);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void setText(CharSequence result) {
        try {
            if (mText == null) {
                mText = getField("mText");
            }
            mText.set(mObject, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object getListenerInfo() throws IntimateException {
        if (mListenerInfo == null) {
            try {
                mListenerInfo = getField("mListenerInfo");
                return mListenerInfo.get(mObject);
            } catch (Exception e) {
                throw new IntimateException(e);
            }
        }
        return null;
    }

    //不需要抛出异常
    @Override
    public int getDesiredHeight() {
        try {
            if (getDesiredHeight == null) {
                getDesiredHeight = getMethod("getDesiredHeight");
            }
            return (int) getDesiredHeight.invoke(mObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    //需要抛出异常
    @Override
    public int isDirectionalNavigationKey(int keyCode) throws IntimateException {
        try {
            if (isDirectionalNavigationKey == null) {
                isDirectionalNavigationKey = getMethod("isDirectionalNavigationKey", int.class);
            }
            return (int) isDirectionalNavigationKey.invoke(mObject, keyCode);
        } catch (Exception e) {
            throw new IntimateException(e);
        }
    }
}
