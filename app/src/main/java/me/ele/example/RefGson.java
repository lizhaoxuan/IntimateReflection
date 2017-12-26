package me.ele.example;

import me.ele.intimate.annotation.GetField;
import me.ele.intimate.annotation.RefTarget;
import me.ele.intimate.annotation.SetField;

/**
 * Created by lizhaoxuan on 2017/12/26.
 */
@RefTarget(className = "com.google.gson.Gson", isSystemClass = false)
public interface RefGson {

    @GetField("DEFAULT_ESCAPE_HTML")
    boolean getDefaultLenient();

}
