package com.bangkitgiat.koperasibum;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Kirim_Adapter extends RecyclerView.Adapter<Kirim_Adapter.MyViewHolder> implements Filterable, ActivityCompat.OnRequestPermissionsResultCallback  {

    private Context mContext ;
    private List<Produk_Util> mData ;
    private List<Produk_Util> mDataFull ;


    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =1 ;
    public Kirim_Adapter(Context mContext, List<Produk_Util> mData) {
        this.mContext = mContext;
        this.mData = mData;
        mDataFull = new ArrayList<>(mData);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.kirim_row,parent,false);
        return new MyViewHolder(view);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    String posted_by = "+6285720009138";

                    String uri = "tel:" + posted_by.trim() ;
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse(uri));
                    mContext.startActivity(intent);

                } else {
                    Toast.makeText(mContext,
                            "Telepon gagal terhubung", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
//
//        holder.tvNama.setText(mData.get(position).getNama());
//        holder.tvTotal.setText(mData.get(position).getId().toString());
//        holder.tvPrice.setText(mData.get(position).getHarga_beli());
//        holder.img_book_thumbnail.setImageResource(mData.get(position).getGambar());
//        holder.call.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                String posted_by = "+6285720009138";
//
//                Log.e("Permission Call","0");
//                if (ContextCompat.checkSelfPermission(mContext,
//                        Manifest.permission.CALL_PHONE)
//                        != PackageManager.PERMISSION_GRANTED) {
//                    Log.e("Permission Call","1");
//                    if (ActivityCompat.shouldShowRequestPermissionRationale((AppCompatActivity) mContext,
//                            Manifest.permission.CALL_PHONE)) {
//
//                        Log.e("Permission Call","2");
//
//                        String uri = "tel:" + posted_by ;
//                        Intent intent = new Intent(Intent.ACTION_CALL);
//                        intent.setData(Uri.parse(uri));
//                        mContext.startActivity(intent);
//                        intent.setData(Uri.parse(uri));
//                        mContext.startActivity(intent);
//                    } else {
//
//                        Log.e("Permission Call","3");
//                        ActivityCompat.requestPermissions((AppCompatActivity) mContext,
//                                new String[]{Manifest.permission.CALL_PHONE},
//                                MY_PERMISSIONS_REQUEST_SEND_SMS);
//                    }
//                }else{
//
//                    String uri = "tel:" + posted_by ;
//                    Intent intent = new Intent(Intent.ACTION_CALL);
//                    intent.setData(Uri.parse(uri));
//                    mContext.startActivity(intent);
//                    intent.setData(Uri.parse(uri));
//                    mContext.startActivity(intent);
//                    Log.e("Permission Call","4");
//                }
//
//            }
//        });


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public Filter getFilter() {
        return mDataFilter;
    }

    private Filter mDataFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Produk_Util> filteredList= new ArrayList<>();

            if(constraint == null || constraint.length()==0){
                filteredList.addAll(mDataFull);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(Produk_Util produk : mDataFull){
                    if(produk.getNama().toLowerCase().contains(filterPattern)){
                        filteredList.add(produk);
                    }
                }
            }

            FilterResults result =  new FilterResults();
            result.values = filteredList;

            return result;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            mData.clear();
            mData.addAll((List) results.values);
            notifyDataSetChanged();

        }
    };

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvNama, tvTotal, tvPrice;
       CardView cardView ;
        ImageView img_book_thumbnail;

        Button call;
        public MyViewHolder(View itemView) {
            super(itemView);

            tvNama = (TextView) itemView.findViewById(R.id.tv_name);
            tvTotal = (TextView) itemView.findViewById(R.id.tv_rate);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_total);
            cardView = (CardView) itemView.findViewById(R.id.card_myevent);
            img_book_thumbnail = (ImageView) itemView.findViewById(R.id.produk_img_id);
            call = (Button)itemView.findViewById(R.id.callkurir);



        }
    }


}
