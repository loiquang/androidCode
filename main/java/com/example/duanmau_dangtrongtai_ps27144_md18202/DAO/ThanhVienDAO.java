package com.example.duanmau_dangtrongtai_ps27144_md18202.DAO;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.duanmau_dangtrongtai_ps27144_md18202.Database.MyDatabase;
import com.example.duanmau_dangtrongtai_ps27144_md18202.Models.ThanhVien;

import java.util.ArrayList;

public class ThanhVienDAO {

    MyDatabase myDatabase;

    public ThanhVienDAO(Context context) {
        myDatabase = new MyDatabase(context);
    }

    public ArrayList<ThanhVien> getDSThanhVien() {
        ArrayList<ThanhVien> list = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = myDatabase.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM THANHVIEN", null);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            do {
                int maTV = cursor.getInt(0);
                String hoTen = cursor.getString(1);
                String namSinh = cursor.getString(2);
                ThanhVien thanhVien = new ThanhVien(maTV, hoTen, namSinh);
                list.add(thanhVien);
            } while (cursor.moveToNext());
        }

        return list;
    }

}
