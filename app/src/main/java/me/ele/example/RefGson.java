package me.ele.example;

import me.ele.intimate.annotation.GetField;
import me.ele.intimate.annotation.RefTarget;

/**
 * Created by lizhaoxuan on 2017/12/26.
 */
@RefTarget(className = "com.google.gson.Gson", needReflection = false)
public interface RefGson {

    @GetField("DEFAULT_ESCAPE_HTML")
    boolean getDefaultLenient();

    @GetField("DEFAULT_PRETTY_PRINT")
    boolean getDefaultPrettyPrint();

}
