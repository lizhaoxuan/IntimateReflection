package me.ele.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;

import me.ele.example.lib.User;
import me.ele.intimate.IntimateException;
import me.ele.intimate.RefImplFactory;

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
    }

}
