package me.ele.example;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import me.ele.example.mock.Brand;
import me.ele.example.mock.CarProtected;
import me.ele.example.mock.CarPublic;
import me.ele.example.mock.Wheel;
import me.ele.example.ref.RefCarProtected;
import me.ele.example.ref.RefCarPublic;
import me.ele.example.ref.RefFactoryImpl;
import me.ele.intimate.RefImplFactory;

import static org.junit.Assert.assertEquals;

/**
 * Test "reflection" Private field and method.
 * Created by lizhaoxuan on 2017/12/31.
 */
@RunWith(AndroidJUnit4.class)
public class PublicRefTest {

    @Test
    public void testUnUseShell() throws Exception {
        //利用反射创建RefFactory类
        RefImplFactory.setFactoryShell(null);

        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals(true, testPublicRefField());
        assertEquals(true, testPublicRefMethod());
    }

    @Test
    public void testUseShell() throws Exception {
        //编译期向FactoryShell注入代码，免去一次反射操作
        RefImplFactory.setFactoryShell(new RefFactoryImpl());
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals(true, testPublicRefField());
        assertEquals(true, testPublicRefMethod());
    }

    private boolean testPublicRefField() {
        CarPublic car = new CarPublic();
        RefCarPublic refCarPublic = RefImplFactory.getRefImpl(car, RefCarPublic.class);
        if (refCarPublic == null) {
            return false;
        }
        //test String field
        assertEquals(refCarPublic.getNameField(), "我的Public小电驴");
        refCarPublic.setNameField("changeName");
        assertEquals(refCarPublic.getNameField(), "changeName");

        //test Primitive field
        assertEquals(refCarPublic.getLevelField(), 7);
        refCarPublic.setLevelField(12);
        assertEquals(refCarPublic.getLevelField(), 12);

        //test Object field
        assertEquals(refCarPublic.getBrandField(), new Brand("宝驴", "源自德国的百年品牌"));
        refCarPublic.setBrandField(new Brand("宝骡", "源自德国的千年品牌"));
        assertEquals(refCarPublic.getBrandField(), new Brand("宝骡", "源自德国的千年品牌"));

        //test set field
        assertEquals(refCarPublic.getWhellListField().size(), 4);
        List<Wheel> wheels_field = refCarPublic.getWhellListField();
        wheels_field.add(new Wheel("new Wheels", 4));
        refCarPublic.setWhellListField(wheels_field);
        assertEquals(refCarPublic.getWhellListField().size(), 5);

        return true;
    }

    private boolean testPublicRefMethod() {
        CarProtected car = new CarProtected();
        RefCarProtected refCarProtected = RefImplFactory.getRefImpl(car, RefCarProtected.class);
        if (refCarProtected == null) {
            return false;
        }

        //test String method
        assertEquals(refCarProtected.getName(), "我的Protected小电驴");
        refCarProtected.setName("changeName");
        assertEquals(refCarProtected.getName(), "changeName");

        //test Primitive method
        assertEquals(refCarProtected.getLevel(), 7);
        refCarProtected.setLevel(12);
        assertEquals(refCarProtected.getLevel(), 12);

        //test Object method
        assertEquals(refCarProtected.getBrand(), new Brand("宝驴", "源自德国的百年品牌"));
        refCarProtected.setBrand(new Brand("宝骡", "源自德国的千年品牌"));
        assertEquals(refCarProtected.getBrand(), new Brand("宝骡", "源自德国的千年品牌"));

        //test set method
        assertEquals(refCarProtected.getWheels().size(), 4);
        List<Wheel> wheels_method = refCarProtected.getWheels();
        wheels_method.add(new Wheel("new Wheels", 4));
        refCarProtected.setWheels(wheels_method);
        assertEquals(refCarProtected.getWheels().size(), 5);

        return true;
    }

}
