package com.example.duanmau_dangtrongtai_ps27144_md18202.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.duanmau_dangtrongtai_ps27144_md18202.Database.MyDatabase;

public class ThuThuDAO {

    MyDatabase myDatabase;

    public ThuThuDAO(Context context) {

        myDatabase = new MyDatabase(context);
    }

    // Đăng nhập
    public boolean checkDangNhap(String matt, String matKhau) {
        SQLiteDatabase sqLiteDatabase = myDatabase.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM THUTHU WHERE MATT = ? AND MATKHAU = ?", new String[]{matt, matKhau});
        if (cursor.getCount() != 0) {
            return true;
        } else {
            return false;
        }
    }

    public int capNhatMatKhau(String userName, String oldPass, String newPass) {
        SQLiteDatabase sqLiteDatabase = myDatabase.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM THUTHU WHERE MATT = ? AND MATKHAU = ?", new String[]{userName, oldPass});
        if (cursor.getCount() > 0) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("matkhau", newPass);
            long check = sqLiteDatabase.update("THUTHU", contentValues, "MATT = ?", new String[]{userName});
            if (check == -1) {
                return -1;
            }
            return 1;
        }
        return 0;
    }
}
