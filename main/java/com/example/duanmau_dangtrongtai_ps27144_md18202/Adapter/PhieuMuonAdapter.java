package com.example.duanmau_dangtrongtai_ps27144_md18202.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duanmau_dangtrongtai_ps27144_md18202.DAO.PhieuMuonDAO;
import com.example.duanmau_dangtrongtai_ps27144_md18202.Models.PhieuMuon;
import com.example.duanmau_dangtrongtai_ps27144_md18202.R;

import java.util.ArrayList;

public class PhieuMuonAdapter extends RecyclerView.Adapter<PhieuMuonAdapter.ViewHoler> {

    private ArrayList<PhieuMuon> list;
    private Context context;

    public PhieuMuonAdapter(ArrayList<PhieuMuon> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_recycler_phieumuon, parent, false);

        return new ViewHoler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoler holder, int position) {
        holder.tvMaThuThu.setText("" + list.get(position).getMatt());
        holder.tvTenThuThu.setText("" + list.get(position).getTentt());
        holder.tvMaPhieuMuon.setText("" + list.get(position).getMapm());
        holder.tvMaThanhVien.setText("" + list.get(position).getMatv());
        holder.tvThanhVien.setText("" + list.get(position).getTentv());
        holder.tvMaSach.setText("" + list.get(position).getMasach());
        holder.tvSach.setText("" + list.get(position).getTensach());
        holder.tvNgay.setText("" + list.get(position).getNgay());
        holder.tvTienThue.setText("" + list.get(position).getTienthue());
        String trangThai = "";
        if(list.get(position).getTrasach() == 1){
            trangThai = "Đã trả sách";
            holder.btnTraSach.setVisibility(View.GONE);
        }else{
            trangThai = "Chưa trả sách";
            holder.btnTraSach.setVisibility(View.VISIBLE);
        }
        holder.tvTrangThai.setText("" + trangThai);

        holder.btnTraSach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhieuMuonDAO phieuMuonDAO = new PhieuMuonDAO(context);
                boolean kiemTra = phieuMuonDAO.thayDoiTrangThai(list.get(holder.getAdapterPosition()).getMapm());
                if(kiemTra){
                    list.clear();
                    list = phieuMuonDAO.getDSPhieuMuon();
                    notifyDataSetChanged();
                }else{
                    Toast.makeText(context, "Thay đổi trạng thái không thành công", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHoler extends RecyclerView.ViewHolder {

        TextView tvMaThuThu, tvTenThuThu, tvMaPhieuMuon, tvMaThanhVien, tvThanhVien, tvMaSach, tvSach, tvNgay, tvTienThue, tvTrangThai;
        Button btnTraSach;
        public ViewHoler(@NonNull View itemView) {
            super(itemView);

            tvMaThuThu = itemView.findViewById(R.id.tvMaThuThu);
            tvTenThuThu = itemView.findViewById(R.id.tvTenThuThu);
            tvMaPhieuMuon = itemView.findViewById(R.id.tvMaPhieuMuon);
            tvMaThanhVien = itemView.findViewById(R.id.tvMaThanhVien);
            tvThanhVien = itemView.findViewById(R.id.tvThanhVien);
            tvMaSach = itemView.findViewById(R.id.tvMaSach);
            tvSach = itemView.findViewById(R.id.tvSach);
            tvNgay = itemView.findViewById(R.id.tvNgay);
            tvTienThue = itemView.findViewById(R.id.tvTienThue);
            tvTrangThai = itemView.findViewById(R.id.tvTrangThai);
            btnTraSach = itemView.findViewById(R.id.btnTraSach);


        }
    }
}
