package com.example.duanmau_dangtrongtai_ps27144_md18202.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.duanmau_dangtrongtai_ps27144_md18202.Models.PhieuMuon;
import com.example.duanmau_dangtrongtai_ps27144_md18202.Database.MyDatabase;

import java.util.ArrayList;

public class PhieuMuonDAO {

    MyDatabase myDatabase;

    public PhieuMuonDAO(Context context) {

        myDatabase = new MyDatabase(context);
    }

    public ArrayList<PhieuMuon> getDSPhieuMuon() {
        ArrayList<PhieuMuon> list = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = myDatabase.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT PM.MAPM, PM.MATV, TV.HOTEN, PM.MATT, TT.HOTEN, PM.MASACH, S.TENSACH, PM.NGAY, PM.TRASACH, PM.TIENTHUE FROM PHIEUMUON PM, THANHVIEN TV, THUTHU TT, SACH S WHERE PM.MATV = TV.MATV AND PM.MATT = TT.MATT AND PM.MASACH = S.MASACH ORDER BY PM.MAPM DESC", null);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            do {
                int mapm = cursor.getInt(0);
                int matv = cursor.getInt(1);
                String tentv = cursor.getString(2);
                String matt = cursor.getString(3);
                String tentt = cursor.getString(4);
                int masach = cursor.getInt(5);
                String tensach = cursor.getString(6);
                String ngay = cursor.getString(7);
                int trasach = cursor.getInt(8);
                int tienthue = cursor.getInt(9);
                PhieuMuon phieuMuon = new PhieuMuon(mapm, matv, tentv, matt, tentt, masach, tensach, ngay, trasach, tienthue);
                list.add(phieuMuon);
            } while (cursor.moveToNext());
        }

        return list;
    }

    public boolean thayDoiTrangThai(int maPM) {
        SQLiteDatabase sqLiteDatabase = myDatabase.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("trasach", 1);
        long check = sqLiteDatabase.update("PHIEUMUON", contentValues, "MAPM = ?", new String[]{String.valueOf(maPM)});
        if (check == -1) {
            return false;
        }
        return true;
    }

    public boolean themPhieuMuon(PhieuMuon phieuMuon) {
        SQLiteDatabase sqLiteDatabase = myDatabase.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
//        contentValues.put("mapm", phieuMuon.getMapm());
        contentValues.put("matv", phieuMuon.getMatv());
        contentValues.put("matt", phieuMuon.getMatt());
        contentValues.put("masach", phieuMuon.getMasach());
        contentValues.put("ngay", phieuMuon.getNgay());
        contentValues.put("trasach", phieuMuon.getTrasach());
        contentValues.put("tienthue", phieuMuon.getTienthue());

        long check = sqLiteDatabase.insert("PHIEUMUON", null, contentValues);
        if (check == -1) {
            return false;
        }
        return true;
    }

}
