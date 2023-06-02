package com.example.duanmau_dangtrongtai_ps27144_md18202.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duanmau_dangtrongtai_ps27144_md18202.Adapter.PhieuMuonAdapter;
import com.example.duanmau_dangtrongtai_ps27144_md18202.DAO.PhieuMuonDAO;
import com.example.duanmau_dangtrongtai_ps27144_md18202.DAO.SachDAO;
import com.example.duanmau_dangtrongtai_ps27144_md18202.DAO.ThanhVienDAO;
import com.example.duanmau_dangtrongtai_ps27144_md18202.Models.PhieuMuon;
import com.example.duanmau_dangtrongtai_ps27144_md18202.Models.Sach;
import com.example.duanmau_dangtrongtai_ps27144_md18202.Models.ThanhVien;
import com.example.duanmau_dangtrongtai_ps27144_md18202.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class PhieuMuonFragment extends Fragment {

    RecyclerView rvQuanLyPhieuMuon;
    PhieuMuonDAO phieuMuonDAO;
    ArrayList<PhieuMuon> list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phieumuon, container, false);

        // Ánh xạ
        rvQuanLyPhieuMuon = view.findViewById(R.id.rvQuanLyPhieuMuon);
        FloatingActionButton floatAdd = view.findViewById(R.id.floatAdd);

        // load lại data
        loadData();

        // Nút floatAdd
        floatAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog();
            }
        });

        return view;
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_them_phieumuon, null);

        // Ánh xạ
        Spinner spnThanhVien = view.findViewById(R.id.spnThanhVien);
        Spinner spnSach = view.findViewById(R.id.spnSach);

        getDataThanhVien(spnThanhVien);
        getDataSach(spnSach);
        builder.setView(view);

        builder.setPositiveButton("Thêm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Lấy mã thành viên
                HashMap<String, Object> hsTV = (HashMap<String, Object>) spnThanhVien.getSelectedItem();
                int maTV = (int) hsTV.get("matv");

                // Lấy mã sách
                HashMap<String, Object> hsSach = (HashMap<String, Object>) spnSach.getSelectedItem();
                int maSach = (int) hsSach.get("masach");

                int tien = (int) hsSach.get("giathue");

                themPhieuMuon(maTV, maSach, tien);
            }
        });

        builder.setNegativeButton("Huỷ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();


    }

    private void getDataThanhVien(Spinner spnThanhVien) {
        ThanhVienDAO thanhVienDAO = new ThanhVienDAO(getContext());
        ArrayList<ThanhVien> list = thanhVienDAO.getDSThanhVien();

        ArrayList<HashMap<String, Object>> listHM = new ArrayList<>();
        for (ThanhVien tv : list) {
            HashMap<String, Object> hs = new HashMap<>();
            hs.put("matv", tv.getMaTV());
            hs.put("hoten", tv.getHoTen());
            listHM.add(hs);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(
                getContext(),
                listHM,
                android.R.layout.simple_list_item_1,
                new String[]{"hoten"},
                new int[]{android.R.id.text1});

        spnThanhVien.setAdapter(simpleAdapter);

    }
    private void getDataSach(Spinner spnSach) {
        SachDAO sachDAO = new SachDAO(getContext());
        ArrayList<Sach> list = sachDAO.getDSDauSach();

        ArrayList<HashMap<String, Object>> listHM = new ArrayList<>();
        for (Sach s : list) {
            HashMap<String, Object> hs = new HashMap<>();
            hs.put("masach", s.getMaSach());
            hs.put("tensach", s.getTenSach());
            hs.put("giathue", s.getGiaThue());
            listHM.add(hs);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(
                getContext(),
                listHM,
                android.R.layout.simple_list_item_1,
                new String[]{"tensach"},
                new int[]{android.R.id.text1});

        spnSach.setAdapter(simpleAdapter);

    }
    private void themPhieuMuon(int maTV, int maSach, int tien){
        // Lấy mã thủ thư
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("THONGTIN", Context.MODE_PRIVATE);
        String matt = sharedPreferences.getString("matt", "");

        // Lấy ngày hiện tại
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String ngay = simpleDateFormat.format(currentTime);

        PhieuMuon phieuMuon = new PhieuMuon(maTV, matt, maSach, ngay, 0, tien);
        boolean kiemTra = phieuMuonDAO.themPhieuMuon(phieuMuon);
        if(kiemTra){
            Toast.makeText(getContext(), "Thêm phiếu mượn thành công", Toast.LENGTH_SHORT).show();
            loadData();
        }else{
            Toast.makeText(getContext(), "Thêm phiếu mượn thất bại", Toast.LENGTH_SHORT).show();
        }

    }

    private void loadData(){
        // Đưa dữ liệu lên rvQuanLyPhieuMuon
        phieuMuonDAO = new PhieuMuonDAO(getContext());
        list = phieuMuonDAO.getDSPhieuMuon();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvQuanLyPhieuMuon.setLayoutManager(linearLayoutManager);
        PhieuMuonAdapter phieuMuonAdapter = new PhieuMuonAdapter(list, getContext());
        rvQuanLyPhieuMuon.setAdapter(phieuMuonAdapter);
    }

}
