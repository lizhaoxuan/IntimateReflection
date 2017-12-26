package me.ele.intimate.plugin;

import com.google.common.util.concurrent.ExecutionError;

/**
 * Created by lizhaoxuan on 2017/12/22.
 */

public class ThrowExecutionError {

    public static void throwError(String msg) {
        throw new ExecutionError(msg, new Error(msg))
    }

}
