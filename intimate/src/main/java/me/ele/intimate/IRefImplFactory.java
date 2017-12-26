package me.ele.intimate;

/**
 * Created by lizhaoxuan on 2017/12/26.
 */

public interface IRefImplFactory {

    BaseRefImpl createRefImpl(Object object, String name) throws ClassNotFoundException;

}
