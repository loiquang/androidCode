package com.example.duanmau_dangtrongtai_ps27144_md18202.DAO;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.duanmau_dangtrongtai_ps27144_md18202.Models.Sach;
import com.example.duanmau_dangtrongtai_ps27144_md18202.Database.MyDatabase;

import java.util.ArrayList;

public class SachDAO {

    MyDatabase myDatabase;

    public SachDAO(Context context){
        myDatabase = new MyDatabase(context);
    }

    public ArrayList<Sach> getDSDauSach (){
        ArrayList<Sach> list = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = myDatabase.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM SACH", null);
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            do {
                int maSach = cursor.getInt(0);
                String tenSach = cursor.getString(1);
                int giaThue = cursor.getInt(2);
                int maLoai = cursor.getInt(3);
                Sach sach = new Sach(maSach, tenSach, giaThue, maLoai);
                list.add(sach);
            }while(cursor.moveToNext());
        }


        return list;
    }

}
