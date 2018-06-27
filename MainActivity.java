package com.example.sqlitedemo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText etName;
    private EditText etSex;
    private EditText etAddr;
    private EditText etList;
    private Button btnAdd;
    private Button btnQuery;
    private Button btnList;

    private SQLiteDatabase mFriendDb;
    //下述代码为声明常量
    private static final String DB_FILE = "friend.db";
    private static final String DB_TABLE = "friends";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FriendDbOpenHelper friendopenDbHelper = new FriendDbOpenHelper(getApplication(), DB_FILE, null, 1);//（上下文，数据库名字，工厂，版本）建立了数据库
        mFriendDb = friendopenDbHelper.getWritableDatabase();

        Cursor cursor = mFriendDb.rawQuery("select distinct tbl_name from sqlite_master where tbl_name='" + DB_TABLE + "'", null);//设置游标定位查找是否有值
        if (cursor != null) {
            if (cursor.getCount() == 0) {//无数据包，建立数据表
                mFriendDb.execSQL("create table " + DB_TABLE + " (_id integer primary key, name text not null, sex text, address text);");
                cursor.close();//释放资源
            }
        }

        initViews();
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues newRows = new ContentValues();//建键值对↓
                newRows.put("name", etName.getText().toString());//把值给name
                newRows.put("sex", etSex.getText().toString());
                newRows.put("address", etAddr.getText().toString());
                mFriendDb.insert(DB_TABLE, null, newRows);//把值插入表内
            }
        });
        btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor c = null;
                if (!etName.getText().toString().equals(" ")) {
                    c = mFriendDb.query(true, DB_TABLE, new String[]{"name", "sex", "address"}, "name=" + "'" + etName.getText().toString() + "'", null, null, null, null, null);//代表转义符→、“
                } else if (!etSex.getText().toString().equals("")) {
                    c = mFriendDb.query(true, DB_TABLE, new String[]{"name", "sex", "address"}, "sex=" + "'" + etSex.getText().toString() + "'", null, null, null, null, null);
                } else if (!etAddr.getText().toString().equals("")) {
                    c = mFriendDb.query(true, DB_TABLE, new String[]{"name", "sex", "address"}, "address" + "'" + etAddr.getText().toString() + "'", null, null, null, null, null);
                } else if (!etList.getText().toString().equals("")) {
                    c = mFriendDb.query(true, DB_TABLE, new String[]{"name", "sex", "address"}, "list" + "'" + etList.getText().toString() + "'", null, null, null, null, null);
                }
            }
        });
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor c = mFriendDb.query(true, DB_TABLE, new String[]{"name", "sex", "address"}, null, null, null, null, null, null);
                if (c == null) {
                    return;
                }
                if (c.getCount() == 0) {
                    etList.setText("");
                    Toast.makeText(MainActivity.this, "数据表中无记录", Toast.LENGTH_LONG).show();
                } else {
                    c.moveToFirst();
                    etList.setText(c.getString(0) + "  " + c.getString(1) + "  " + c.getString(2));

                    while (c.moveToNext()) {
                        etList.append("\n" + c.getString(0) + " " + c.getString(1) + "  " + c.getString(2));
                    }
                }
            }
        });
    }

    private void initViews() {
        etName = (EditText) findViewById(R.id.et_name);
        etAddr = (EditText) findViewById(R.id.et_address);
        etSex = (EditText) findViewById(R.id.et_sex);
        etList = (EditText) findViewById(R.id.et_list);
        btnAdd = (Button) findViewById(R.id.btn_add);
        btnList = (Button) findViewById(R.id.btn_list);
        btnQuery = (Button) findViewById(R.id.btn_query);
    }
}
