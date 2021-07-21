package com.bangkitgiat.koperasibum;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
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

public class ShopingCart extends AppCompatActivity {

    List<CartUtil> produk;
    public Cart_Adapter myAdapter;
    Button bayar;
    String id;
    TextView tvTotal;

    int totalbelanja=0, totalproduk=0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart);

        // Recieve data
        Intent intent = getIntent();
        id = intent.getExtras().getString("id");
        GetCart c = new GetCart();
        c.execute();

        tvTotal = (TextView)findViewById(R.id.tv_total);

        bayar = (Button)findViewById(R.id.btn_placeorder);
        bayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("ShopingCart", "Total Belanja = "+totalbelanja);
                Log.e("ShopingCart", "Total Produk = "+totalproduk);

                Intent iPesan = new Intent(ShopingCart.this, Checkout.class);
                iPesan.putExtra("totalbelanja", ""+totalbelanja);
                iPesan.putExtra("totalproduk" , ""+totalproduk );
                startActivity(iPesan);
//                Toast.makeText(ShopingCart.this, "Produk-> "+totalproduk+" & belanja-> "+totalbelanja, Toast.LENGTH_LONG).show();
            }
        });
        produk = new ArrayList<CartUtil>();
//        produk.add(new Produk_Util("1","SBK-01","Beras Premium Sania","Rp. 62.500","Rp. 58.000","Beras Premium Berat 5Kg","",R.drawable.sania));
//        produk.add(new Produk_Util("1","SBK-09","Gulaku","Rp. 62.500","Rp. 12.000","Gulaku","",R.drawable.gulaku));


    }

    private class GetCart extends AsyncTask<String, Void, String> {

        ProgressDialog pdLoading = new ProgressDialog(ShopingCart.this);
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
                url = new URL(GlobalData.globalUrl + "cart.php?iduser="+id+"&op=view");
                Log.e("URL Service", url.toString());

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

            try{
                if(kode.equalsIgnoreCase("200")){

                    arrayDetail = json_obj.getJSONArray("data");
                    for (int i = 0; i < arrayDetail.length(); i++) {
                        JSONObject dataKaryawan = arrayDetail.getJSONObject(i);

                        CartUtil data = new CartUtil();

                        data.setId(dataKaryawan.getString("id_cart"));
                        data.setId_user(dataKaryawan.getString("id_user"));
                        data.setId_produk(dataKaryawan.getString("id_produk"));
                        data.setJumlah(dataKaryawan.getString("jumlah_produk"));
                        data.setTgl(dataKaryawan.getString("tgl_keranjang"));
                        data.setJam(dataKaryawan.getString("jam_keranjang"));
                        data.setStatus(dataKaryawan.getString("status"));
                        data.setNama(dataKaryawan.getString("nama"));
                        data.setHarga(dataKaryawan.getString("harga_produk"));
                        data.setFoto(dataKaryawan.getString("foto_produk"));
                        data.setCek(true);

                        produk.add(data);
                    }

                }else{
                    Toast.makeText(ShopingCart.this, "Data tidak dapat diload, silahkan refresh", Toast.LENGTH_LONG).show();

                }

            }catch (JSONException e){
                e.printStackTrace();
                Log.e("JSON ERROR", e.toString());
            }

            for(int i=0; i<produk.size(); i++){
                if(produk.get(i).isCek()) {
                    totalbelanja += Integer.parseInt(produk.get(i).getHarga()) * Integer.parseInt(produk.get(i).getJumlah());
                    totalproduk += Integer.parseInt(produk.get(i).getJumlah());
                }
            }
            Locale localeID = new Locale("in", "ID");
            NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
            tvTotal.setText(formatRupiah.format((double)totalbelanja));

            RecyclerView myrv = (RecyclerView) findViewById(R.id.recycler_cart);
            myAdapter = new Cart_Adapter(ShopingCart.this, produk);
            myrv.setLayoutManager(new LinearLayoutManager(ShopingCart.this));
            myrv.setAdapter(myAdapter);
        }
    }
}
