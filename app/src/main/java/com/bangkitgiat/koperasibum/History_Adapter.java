package com.bangkitgiat.koperasibum;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class History_Adapter extends RecyclerView.Adapter<History_Adapter.MyViewHolder> implements Filterable {

    private Context mContext ;
    private List<Produk_Util> mData ;
    private List<Produk_Util> mDataFull ;



    public History_Adapter(Context mContext, List<Produk_Util> mData) {
        this.mContext = mContext;
        this.mData = mData;
        mDataFull = new ArrayList<>(mData);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.history_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

//        holder.tvNama.setText("INV #123456");
//        holder.tvTotal.setText(mData.get(position).getId().toString());
//        holder.tvPrice.setText(mData.get(position).getHarga_beli());
//        holder.img_book_thumbnail.setImageResource(mData.get(position).getGambar());
//        holder.belilagi.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(mContext, Checkout.class);
//                mContext.startActivity(intent);
//            }
//        });


//        holder.cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(mContext, Produk_Detail.class);
//
//                // passing data to the book activity
//                intent.putExtra("Title",mData.get(position).getNama());
//                intent.putExtra("Description",mData.get(position).getDeskripsi());
//                intent.putExtra("Thumbnail",mData.get(position).getGambar());
//                intent.putExtra("Harga",mData.get(position).getHarga_beli());
//                // start the activity
//                mContext.startActivity(intent);
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
        Button belilagi;
        public MyViewHolder(View itemView) {
            super(itemView);

            tvNama = (TextView) itemView.findViewById(R.id.tv_name);
            tvTotal = (TextView) itemView.findViewById(R.id.tv_rate);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_total);
            cardView = (CardView) itemView.findViewById(R.id.card_myevent);
            img_book_thumbnail = (ImageView) itemView.findViewById(R.id.produk_img_id);
            belilagi = (Button)itemView.findViewById(R.id.belilagi);



        }
    }


}
