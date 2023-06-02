package com.example.duanmau_dangtrongtai_ps27144_md18202.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabase extends SQLiteOpenHelper {

    public MyDatabase (Context context){
        super(context, "QUAN_LY_THU_VIEN", null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String dbThuThu = "CREATE TABLE IF NOT EXISTS THUTHU(MATT TEXT PRIMARY KEY, HOTEN TEXT, MATKHAU TEXT)";
        db.execSQL(dbThuThu);

        String dbThanhVien = "CREATE TABLE IF NOT EXISTS THANHVIEN (MATV INTEGER PRIMARY KEY AUTOINCREMENT, HOTEN TEXT, NAMSINH TEXT)";
        db.execSQL(dbThanhVien);

        String dbLoaiSach = "CREATE TABLE IF NOT EXISTS LOAISACH (MALOAI INTEGER PRIMARY KEY AUTOINCREMENT, TENLOAI TEXT)";
        db.execSQL(dbLoaiSach);

        String dbSach = "CREATE TABLE IF NOT EXISTS SACH (MASACH INTEGER PRIMARY KEY AUTOINCREMENT, TENSACH TEXT, GIATHUE INTEGER, MALOAI INTEGER REFERENCES LOAISACH(MALOAI))";
        db.execSQL(dbSach);

        String dbPhieuMuon = "CREATE TABLE IF NOT EXISTS PHIEUMUON (MAPM INTEGER PRIMARY KEY AUTOINCREMENT, MATV INTEGER REFERENCES THANHVIEN(MATV), MATT REFERENCES THUTHU(MATT), MASACH INTEGER REFERENCES SACH(MASACH), NGAY TEXT, TRASACH INTEGER, TIENTHUE INTEGER)";
        db.execSQL(dbPhieuMuon);

        // Data mẫu
        db.execSQL("INSERT INTO LOAISACH VALUES (1, 'Thiếu nhi'),(2,'Tình cảm'),(3, 'Giáo khoa')");
        db.execSQL("INSERT INTO SACH VALUES (1, 'Hãy đợi đấy', 2500, 1), (2, 'Thằng cuội', 1000, 1), (3, 'Lập trình Android', 2000, 3)");
        db.execSQL("INSERT INTO THUTHU VALUES ('thuthu01','Nguyễn Văn Anh','abc123'),('thuthu02','Trần Văn Hùng','123abc')");
        db.execSQL("INSERT INTO THANHVIEN VALUES (1,'Cao Thu Trang','2000'),(2,'Trần Mỹ Kim','2000')");
        //trả sách: 1: đã trả - 0: chưa trả
        db.execSQL("INSERT INTO PHIEUMUON VALUES (1,1,'thuthu01', 1, '19/03/2022', 1, 2500),(2,1,'thuthu01', 3, '19/03/2022', 0, 2000),(3,2,'thuthu02', 1, '19/03/2022', 1, 2000)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion != newVersion){
            db.execSQL("DROP TABLE IF EXISTS THUTHU");
            db.execSQL("DROP TABLE IF EXISTS THANHVIEN");
            db.execSQL("DROP TABLE IF EXISTS LOAISACH");
            db.execSQL("DROP TABLE IF EXISTS SACH");
            db.execSQL("DROP TABLE IF EXISTS PHIEUMUON");
            onCreate(db);
        }
    }
}
