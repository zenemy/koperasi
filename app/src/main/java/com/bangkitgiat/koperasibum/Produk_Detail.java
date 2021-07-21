package com.bangkitgiat.koperasibum;

import android.app.ProgressDialog;
import android.content.ContentProviderClient;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

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

public class Produk_Detail extends AppCompatActivity {


    private TextView tvtitle,tvdescription, tvStok, tvHarga, tvKategori;
//    private TextView tvtitle,tvdescription,tvcategory;
    private ImageView img;
    private ImageButton btnTambah;
    private EditText jumlah;
    private int x=1;
    private Button btnkeranjang, btnBayar;
    String id;

    List<Produk_Util> produk;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.produk_detail);

        produk = new ArrayList<Produk_Util>();
        tvtitle = (TextView) findViewById(R.id.txttitle);
        tvdescription = (TextView) findViewById(R.id.txtDesc);
        tvStok = (TextView) findViewById(R.id.txtStok);
        tvKategori = (TextView) findViewById(R.id.txtCat);
        tvHarga = (TextView) findViewById(R.id.txtHarga);
        img = (ImageView) findViewById(R.id.bookthumbnail);
        btnTambah = (ImageButton)findViewById(R.id.btntambah);
        jumlah = (EditText)findViewById(R.id.jumlah);
        btnkeranjang = (Button)findViewById(R.id.btnkeranjang);
        btnBayar = (Button)findViewById(R.id.btnBayar);

        // Recieve data
        Intent intent = getIntent();
       id = intent.getExtras().getString("id");
        GetProductDetail p = new GetProductDetail();
        p.execute();

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Integer.parseInt(jumlah.getText().toString())>=Integer.parseInt(produk.get(0).getStok())){
Toast.makeText(Produk_Detail.this,"Anda tidak dapat melakukan pembelian melebihi stok yang ada. Terima kasih", Toast.LENGTH_LONG).show();
                }else{
                    x++;
                    jumlah.setText(""+x);
                }
            }
        });

        btnkeranjang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Produk_Detail.this, "Produk telah ditambahkan ke keranjang", Toast.LENGTH_SHORT).show();

                AddCart a = new AddCart();
                a.execute();
            }
        });

        btnBayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iBayar = new Intent(Produk_Detail.this, Bayar.class);
                iBayar.putExtra("id", produk.get(0).getId());
                iBayar.putExtra("jumlah", jumlah.getText().toString());
                startActivity(iBayar);
            }
        });
    }
    private class GetProductDetail extends AsyncTask<String, Void, String> {

        ProgressDialog pdLoading = new ProgressDialog(Produk_Detail.this);
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
                url = new URL(GlobalData.globalUrl + "produkdetil.php?id="+id);
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
                    //simpan data ke table pegawai
//                    JSONObject objProfil = json_obj.getJSONObject("profil");
                    arrayDetail = json_obj.getJSONArray("data");
                    for (int i = 0; i < arrayDetail.length(); i++) {
                        JSONObject dataKaryawan = arrayDetail.getJSONObject(i);

                        Produk_Util data = new Produk_Util();
                        data.setId(dataKaryawan.getString("id_produk"));
                        data.setKategori(dataKaryawan.getString("nama_kategori"));
                        data.setStok(dataKaryawan.getString("stok_produk"));
                        data.setNama(dataKaryawan.getString("nama"));
                        data.setAktif_produk(dataKaryawan.getString("aktif_produk"));
                        data.setKeterangan(dataKaryawan.getString("keterangan"));
                        data.setFoto_produk(dataKaryawan.getString("foto_produk"));
                        data.setHarga_jual(dataKaryawan.getString("harga_produk"));
                        data.setDiskon(dataKaryawan.getString("diskon_produk"));
                        data.setBerat(dataKaryawan.getString("berat_produk"));


                        produk.add(data);
                    }

                }else{
                    Toast.makeText(Produk_Detail.this, "Data tidak dapat diload, silahkan refresh", Toast.LENGTH_LONG).show();

                }

            }catch (JSONException e){
                e.printStackTrace();
                Log.e("JSON ERROR", e.toString());
            }
// Initialize contacts
//                        dataPos = Contact.createContactsList(20);
            // Create adapter passing in the sample user data
            Log.e("URL FOTO MAIN","data-->"+produk.toString());

            tvtitle.setText(produk.get(0).getNama());
            tvdescription.setText(produk.get(0).getKeterangan());
            tvStok.setText(": "+produk.get(0).getStok());
            tvKategori.setText(": "+produk.get(0).getKategori());
            Locale localeID = new Locale("in", "ID");
            NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
            tvHarga.setText(": "+formatRupiah.format((double)Integer.parseInt(produk.get(0).getHarga_jual())));
            new DownloadImageTask(img).execute(GlobalData.globalUrl+produk.get(0).getFoto_produk());
//            img.setImageResource(produk.get(0).getNama());
//            tvcategory.setText(harga);
            jumlah.setText(""+x);
        }
    }

    private class AddCart extends AsyncTask<String, Void, String> {

        ProgressDialog pdLoading = new ProgressDialog(Produk_Detail.this);
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
                url = new URL(GlobalData.globalUrl + "cart.php?iduser=1&op=add&produk="+id+"&jumlah="+jumlah.getText().toString());

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

            Log.e("Result", result);
            try {
                json_obj = new JSONObject(result);
                kode = json_obj.getString("kode");

                Log.e("JSON registrasi", kode);

            } catch (JSONException e) {
                e.printStackTrace();
            }

                if(kode.equalsIgnoreCase("200")){
                   Toast.makeText(Produk_Detail.this,"Produk telah ditambahkan kedalam keranjang belanja anda", Toast.LENGTH_LONG).show();


                }else{
                    Toast.makeText(Produk_Detail.this, "Produk tidak dapat ditambahkan ke keranjang belanja. Silahkan coba setelah beberapa saat", Toast.LENGTH_LONG).show();

                }

                Intent iMenu = new Intent(Produk_Detail.this, MainActivity.class);
                startActivity(iMenu);
                finish();
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
