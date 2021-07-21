package com.bangkitgiat.koperasibum;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Cart_Adapter extends RecyclerView.Adapter<Cart_Adapter.MyViewHolder> implements Filterable {

    private Context mContext ;
    private List<CartUtil> mData ;
    private List<CartUtil> mDataFull ;
    private String hapus="0";


    public Cart_Adapter(Context mContext, List<CartUtil> mData) {
        this.mContext = mContext;
        this.mData = mData;
        mDataFull = new ArrayList<>(mData);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cart_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.tvNama.setText(mData.get(position).getNama());
        holder.tvJumlah.setText("x"+mData.get(position).getJumlah());
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        holder.tvPrice.setText(formatRupiah.format((double)Integer.parseInt(mData.get(position).getHarga())));
//        holder.img_book_thumbnail.setImageResource(mData.get(position).getGambar());
        new DownloadImageTask(holder.img_book_thumbnail).execute(GlobalData.globalUrl+mData.get(position).getFoto());

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                mData.remove(position);
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(mContext);
                //Uncomment the below code to Set the message and title from the strings.xml file
                builder.setMessage("Konfirmasi").setTitle("Dialog Konfirmasi");

                //Setting message manually and performing action on button click
                builder.setMessage("Apakah anda yakin menghapus "+mData.get(position).getNama()+" dari keranjang belanja anda?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

//
                                hapus = mData.get(position).getId();
                                DeleteCart d = new DeleteCart();
                                d.execute();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();
                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("AlertDialogExample");
                alert.show();


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
            List<CartUtil> filteredList= new ArrayList<>();

            if(constraint == null || constraint.length()==0){
                filteredList.addAll(mDataFull);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(CartUtil produk : mDataFull){
                    if(produk.getId_user().toLowerCase().contains(filterPattern)){
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

        TextView tvNama, tvPrice, tvJumlah ;
        ImageView img_book_thumbnail;
        ImageButton btnDelete;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvNama = (TextView) itemView.findViewById(R.id.tv_name);
            tvJumlah = (TextView) itemView.findViewById(R.id.tv_jumlah);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_rate);
            img_book_thumbnail = (ImageView) itemView.findViewById(R.id.produk_img_id);
            btnDelete = (ImageButton)itemView.findViewById(R.id.btn_delete);

        }
    }

    private class DeleteCart extends AsyncTask<String, Void, String> {

        ProgressDialog pdLoading = new ProgressDialog(mContext);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setMessage("Loading." +
                    "\nMohon ditunggu...");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {

                // Enter URL address where your json file resides
                // Even you can make call to php file which returns json data
                url = new URL(GlobalData.globalUrl + "cart.php?iduser=1&op=delete&idcart="+hapus);
                Log.e("URL Delete", url.toString());

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from
                // php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(GlobalData.READ_TIMEOUT);
                conn.setConnectTimeout(GlobalData.CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");

                // setDoOutput to true as we recieve data from json file
                conn.setDoOutput(true);

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }

        }

        @Override
        protected void onPostExecute(String result) {
            pdLoading.dismiss();

            String kode="";
            JSONObject json_obj = null;
            JSONArray arrayDetail = null;

            Log.e("Result", result);
            try {
                json_obj = new JSONObject(result);
                kode = json_obj.getString("kode");

                Log.e("JSON registrasi", kode);

            } catch (JSONException e) {
                e.printStackTrace();
            }

                if(kode.equalsIgnoreCase("200")){
                    Log.e("HAPUS DATA", "SUKSES");
                    Toast.makeText(mContext,"Data telah berhasil dihapus",
                                        Toast.LENGTH_SHORT).show();
                    Intent iCart = new Intent(mContext, ShopingCart.class);
                    iCart.putExtra("id","1");
                    mContext.startActivity(iCart);


                }else{
                    Log.e("HAPUS DATA", "GAGAL");
                    Toast.makeText(mContext, "Data tidak dapat diload, silahkan refresh", Toast.LENGTH_LONG).show();

                }

        }
    }
}
