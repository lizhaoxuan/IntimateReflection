package me.ele.example;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import me.ele.example.mock.Brand;
import me.ele.example.mock.CarPrivate;
import me.ele.example.mock.Wheel;
import me.ele.example.ref.RefCarPrivate;
import me.ele.example.ref.RefFactoryImpl;
import me.ele.intimate.RefImplFactory;

import static org.junit.Assert.assertEquals;

/**
 * Test "reflection" Private field and method.
 * Created by lizhaoxuan on 2017/12/31.
 */
@RunWith(AndroidJUnit4.class)
public class PrivateRefTest {

    @Test
    public void testUnUseShell() throws Exception {
        //利用反射创建RefFactory类
        RefImplFactory.setFactoryShell(null);

        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals(true, testPrivateRefField());
        assertEquals(true, testPrivateRefMethod());
    }

    @Test
    public void testUseShell() throws Exception {
        //编译期向FactoryShell注入代码，免去一次反射操作
        RefImplFactory.setFactoryShell(new RefFactoryImpl());
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals(true, testPrivateRefField());
        assertEquals(true, testPrivateRefMethod());
    }

    private boolean testPrivateRefField() {
        CarPrivate car = new CarPrivate();
        RefCarPrivate refCarPrivate = RefImplFactory.getRefImpl(car, RefCarPrivate.class);
        if (refCarPrivate == null) {
            return false;
        }
        //test String field
        assertEquals(refCarPrivate.getNameField(), "我的Private小电驴");
        refCarPrivate.setNameField("changeName");
        assertEquals(refCarPrivate.getNameField(), "changeName");

        //test Primitive field
        assertEquals(refCarPrivate.getLevelField(), 7);
        refCarPrivate.setLevelField(12);
        assertEquals(refCarPrivate.getLevelField(), 12);

        //test Object field
        assertEquals(refCarPrivate.getBrandField(), new Brand("宝驴", "源自德国的百年品牌"));
        refCarPrivate.setBrandField(new Brand("宝骡", "源自德国的千年品牌"));
        assertEquals(refCarPrivate.getBrandField(), new Brand("宝骡", "源自德国的千年品牌"));

        //test set field
        assertEquals(refCarPrivate.getWhellListField().size(), 4);
        List<Wheel> wheels_field = refCarPrivate.getWhellListField();
        wheels_field.add(new Wheel("new Wheels", 4));
        refCarPrivate.setWhellListField(wheels_field);
        assertEquals(refCarPrivate.getWhellListField().size(), 5);

        return true;
    }

    private boolean testPrivateRefMethod() {
        CarPrivate car = new CarPrivate();
        RefCarPrivate refCarPrivate = RefImplFactory.getRefImpl(car, RefCarPrivate.class);
        if (refCarPrivate == null) {
            return false;
        }

        //test String method
        assertEquals(refCarPrivate.getName(), "我的Private小电驴");
        refCarPrivate.setName("changeName");
        assertEquals(refCarPrivate.getName(), "changeName");

        //test Primitive method
        assertEquals(refCarPrivate.getLevel(), 7);
        refCarPrivate.setLevel(12);
        assertEquals(refCarPrivate.getLevel(), 12);

        //test Object method
        assertEquals(refCarPrivate.getBrand(), new Brand("宝驴", "源自德国的百年品牌"));
        refCarPrivate.setBrand(new Brand("宝骡", "源自德国的千年品牌"));
        assertEquals(refCarPrivate.getBrand(), new Brand("宝骡", "源自德国的千年品牌"));

        //test set method
        assertEquals(refCarPrivate.getWheels().size(), 4);
        List<Wheel> wheels_method = refCarPrivate.getWheels();
        wheels_method.add(new Wheel("new Wheels", 4));
        refCarPrivate.setWheels(wheels_method);
        assertEquals(refCarPrivate.getWheels().size(), 5);

        return true;
    }

}
