package com.example.zhaoxuan;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.zhaoxuan.lib.User;

public class MainActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.text);

        try {
            RefTextView refTextView = RefImplFactory.getRefImplThrows(textView, RefTextView.class);
            refTextView.getText();
            Log.d("TAG", refTextView.getText().toString());
            Log.d("TAG", refTextView.getDesiredHeight() + " ");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        User user = new User("kaka", "男", 19, "三年二班");

        RefUser refUser = RefImplFactory.getRefImpl(user, RefUser.class);
        Log.d("TAG", "getName:" + refUser.getName());
        Log.d("TAG", "getSexStr: " + refUser.getSexStr());
        refUser.setSex("你猜猜");
        Log.d("TAG", "getSex：" + refUser.getSex());
        Log.d("TAG", "getAgeStr: " + refUser.getAgeStr());
        Log.d("TAG", "getClassName: " + refUser.getClassName());


    }
}
