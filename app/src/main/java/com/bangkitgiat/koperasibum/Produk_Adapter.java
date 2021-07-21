package com.bangkitgiat.koperasibum;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Produk_Adapter extends RecyclerView.Adapter<Produk_Adapter.MyViewHolder> implements Filterable {

    private Context mContext ;
    private List<Produk_Util> mData ;
    private List<Produk_Util> mDataFull ;
    private Bitmap mIcon_val;


    public Produk_Adapter(Context mContext, List<Produk_Util> mData) {
        this.mContext = mContext;
        this.mData = mData;
        mDataFull = new ArrayList<>(mData);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_item_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        new DownloadImageTask(holder.img_book_thumbnail).execute(GlobalData.globalUrl+mData.get(position).getFoto_produk());
//        URL newurl = null;
//        try {
//            newurl = new URL(globalUrl+mData.get(position).getFoto_produk());
//
//            Log.e("URL API", newurl.toString());
//        } catch (MalformedURLException e) {
//            e.toString();
//        }
        holder.tv_book_title.setText(mData.get(position).getNama());
//        try {
//            mIcon_val = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
//            holder.img_book_thumbnail.setImageBitmap(mIcon_val);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, Produk_Detail.class);

                // passing data to the book activity
                intent.putExtra("id",mData.get(position).getId());
                // start the activity
                mContext.startActivity(intent);

            }
        });

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

        TextView tv_book_title;
        ImageView img_book_thumbnail;
        CardView cardView ;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_book_title = (TextView) itemView.findViewById(R.id.produk_title_id) ;
            img_book_thumbnail = (ImageView) itemView.findViewById(R.id.produk_img_id);
            cardView = (CardView) itemView.findViewById(R.id.cardview);


        }
    }


}
