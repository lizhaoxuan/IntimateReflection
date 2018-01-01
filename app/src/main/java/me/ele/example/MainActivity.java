package me.ele.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;

import com.google.gson.Gson;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import me.ele.example.lib.User;
import me.ele.example.mock.CarPrivate;
import me.ele.example.mock.InnerClazz;
import me.ele.example.ref.RefCarPrivate;
import me.ele.example.ref.RefFactoryImpl;
import me.ele.example.ref.RefGson;
import me.ele.example.ref.RefInnerClazz;
import me.ele.example.ref.RefPrivateInnerClass;
import me.ele.example.ref.RefPublicStaticInnerClass;
import me.ele.example.ref.RefRecyclerView;
import me.ele.example.ref.RefTextView;
import me.ele.example.ref.RefUser;
import me.ele.intimate.IntimateException;
import me.ele.intimate.RefImplFactory;

import static junit.framework.Assert.assertEquals;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    int type = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.text);

        RefImplFactory.setFactoryShell(new RefFactoryImpl());
        RefTextView refTextView = null;
        try {
            refTextView = RefImplFactory.getRefImplThrows(textView, RefTextView.class);
            refTextView.getText();
            Log.d("TAG", refTextView.getText().toString());
            Log.d("TAG", refTextView.getDesiredHeight() + " ");
        } catch (IntimateException e) {
            e.printStackTrace();
        }

        int type = 1000;

        User user = new User("kaka", "男", 19, "三年二班");

        RefUser refUser = RefImplFactory.getRefImpl(user, RefUser.class);
        Log.d("TAG", "getName:" + refUser.getName());
        Log.d("TAG", "getSexStr: " + refUser.getSexStr());
        refUser.setSexRef("你猜猜ss");
        Log.d("TAG", "getSex：" + refUser.getSex());
        refUser.setAge(1, 2);
        Log.d("TAG", "getAge: " + refUser.getAge());
        Log.d("TAG", "getAgeStr: " + refUser.getAgeStr());
        Log.d("TAG", "getClassName: " + refUser.getClassName());


        RefGson refGson = RefImplFactory.getRefImpl(new Gson(), RefGson.class);
        Log.d("TAG", "getDefaultLenient:" + refGson.getDefaultLenient());
        Log.d("TAG", "getDefaultPrettyPrint:" + refGson.getDefaultPrettyPrint());
        new CarPrivate();
        RefCarPrivate carPrivate = RefImplFactory.getRefImpl(new CarPrivate(), RefCarPrivate.class);
        Log.d("TAG", "carPrivate:" + carPrivate.getNameField());

        RecyclerView recyclerView = new RecyclerView(this);
        RefRecyclerView refRecyclerView = RefImplFactory.getRefImpl(recyclerView, RefRecyclerView.class);
        Log.d("TAG", "recyclerView:" + refRecyclerView.getLastTouchY());
        refRecyclerView.setLastTouchY(11);
        Log.d("TAG", "recyclerView:" + refRecyclerView.getLastTouchY());

        testPrivateInnerClass();
        testStaticInnerClass();
    }

    private boolean testPrivateInnerClass() {
        RefInnerClazz refInnerClazz = RefImplFactory.getRefImpl(new InnerClazz(), RefInnerClazz.class);
        if (refInnerClazz == null) {
            Log.d("TAG", "111:");
            return false;
        }
        RefPrivateInnerClass refPrivateInnerClass = RefImplFactory.getRefImpl(refInnerClazz.getPrivateInnerClass(),
                RefPrivateInnerClass.class);
        if (refPrivateInnerClass == null) {
            Log.d("TAG", "222:");
            return false;
        }
        assertEquals(refPrivateInnerClass.getName(), "defaultInnerClass");
        return true;
    }

    private boolean testStaticInnerClass() {
        RefInnerClazz refInnerClazz = RefImplFactory.getRefImpl(new InnerClazz(), RefInnerClazz.class);
        if (refInnerClazz == null) {
            return false;
        }
        RefPublicStaticInnerClass refPublicStaticInnerClass = RefImplFactory.getRefImpl(refInnerClazz.getPublicStaticInnerClass(),
                RefPublicStaticInnerClass.class);
        if (refPublicStaticInnerClass == null) {
            return false;
        }
        assertEquals(refPublicStaticInnerClass.getName(), "publicStaticInnerClass");
        return true;
    }

}
