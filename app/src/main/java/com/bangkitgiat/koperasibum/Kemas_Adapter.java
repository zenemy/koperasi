package com.bangkitgiat.koperasibum;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
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
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Kemas_Adapter extends RecyclerView.Adapter<Kemas_Adapter.MyViewHolder> implements Filterable{

    private Context mContext ;
    private List<OrderUtil> mData ;
    private List<OrderUtil> mDataFull ;


    public Kemas_Adapter(Context mContext, List<OrderUtil> mData) {
        this.mContext = mContext;
        this.mData = mData;
        mDataFull = new ArrayList<>(mData);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.kemas_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.tvNama.setText(mData.get(position).getInvoice());
        holder.tvTotal.setText(mData.get(position).getJumlah()+" Produk");
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        holder.tvPrice.setText(formatRupiah.format((double)Integer.parseInt(mData.get(position).getJumlah_belanja())));
        new DownloadImageTask(holder.img_book_thumbnail).execute(GlobalData.globalUrl+mData.get(position).getThumb());

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
            List<OrderUtil> filteredList= new ArrayList<>();

            if(constraint == null || constraint.length()==0){
                filteredList.addAll(mDataFull);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(OrderUtil produk : mDataFull){
                    if(produk.getInvoice().toLowerCase().contains(filterPattern)){
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
        ImageView img_book_thumbnail;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvNama = (TextView) itemView.findViewById(R.id.tv_name);
            tvTotal = (TextView) itemView.findViewById(R.id.tv_rate);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_total);
            img_book_thumbnail = (ImageView) itemView.findViewById(R.id.produk_img_id);


        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap bmp = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bmp = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bmp;
        }
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }


}
