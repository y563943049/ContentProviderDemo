package com.example.contentprovidertest;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private MyDBOpenHelper helper = new MyDBOpenHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void click(View view){
        SQLiteDatabase db = helper.getWritableDatabase();
        int number = 1520000000;
        for(int i = 0; i < 100;i++){
            ContentValues values = new ContentValues();
            values.put("name","用户："+i);
            values.put("phone",String.valueOf(number + i));
            db.insert("contactinfo",null,values);
        }
        db.close();
        Toast.makeText(this,"添加数据成功。",Toast.LENGTH_SHORT).show();
    }
}
