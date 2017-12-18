package com.example.zhaoxuan;

import android.view.View;
import me.ele.intimate.annotation.RefTarget;
import me.ele.intimate.annotation.GetField;

/**
 * Created by lizhaoxuan on 2017/12/15.
 */
@RefTarget(className = "android.view.View$ListenerInfo", isSystemClass = true)
public interface RefListenerInfo {

    @GetField("mOnClickListener")
    View.OnClickListener getListener();

}
