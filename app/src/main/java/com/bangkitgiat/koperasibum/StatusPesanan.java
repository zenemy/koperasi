package com.bangkitgiat.koperasibum;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import java.util.ArrayList;
import java.util.List;


public class StatusPesanan extends AppCompatActivity implements View.OnClickListener {

    private Button btnKemas, btnKirim, btnCall;
    List<OrderUtil> produk;
    Kemas_Adapter myAdapter;
    Kirim_Adapter kirimAdapter;
    RecyclerView myrv;
    private String totalProduk, totalBelanja, iduser, inv;

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =1 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_pesanan);


        Intent intent = getIntent();
        totalBelanja = intent.getExtras().getString("totalbelanja");
        totalProduk = intent.getExtras().getString("totalproduk");
        iduser = intent.getExtras().getString("iduser");
        inv = intent.getExtras().getString("invoice");

        btnKemas = (Button)findViewById(R.id.btn_dikemas);
        btnKirim = (Button)findViewById(R.id.btn_dikirim);
        btnCall = (Button)findViewById(R.id.calltoko);

        produk = new ArrayList<OrderUtil>();
        GetKemas kemas = new GetKemas();
        kemas.execute();

//        produk.add(new Produk_Util("1","SBK-01","Beras Premium Sania","Rp. 62.500","Rp. 58.000","Beras Premium Berat 5Kg","",R.drawable.sania));
//        produk.add(new Produk_Util("1","SBK-09","Gulaku","Rp. 62.500","Rp. 12.000","Gulaku","",R.drawable.gulaku));

        myrv = (RecyclerView) findViewById(R.id.recycler_cart);
        myAdapter = new Kemas_Adapter(this,produk);
        myrv.setLayoutManager(new LinearLayoutManager(this));
        myrv.setAdapter(myAdapter);

        btnKemas.setBackgroundColor(Color.WHITE);
        btnKemas.setOnClickListener(this);
        btnKirim.setOnClickListener(this);
        btnCall.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_dikemas:
                btnKemas.setBackgroundColor(Color.WHITE);
                btnKirim.setBackgroundColor(Color.parseColor("#D89000"));

                produk.clear();
                GetKemas kemas = new GetKemas();
                kemas.execute();

                myAdapter = new Kemas_Adapter(this,produk);
                myrv.setLayoutManager(new LinearLayoutManager(this));
                myrv.setAdapter(myAdapter);
                break;
            case R.id.btn_dikirim:
                btnKirim.setBackgroundColor(Color.WHITE);
                btnKemas.setBackgroundColor(Color.parseColor("#D89000"));

                produk.clear();
//                produk.add(new Produk_Util("1","SBK-01","Beras Premium Sania","Rp. 62.500","Rp. 58.000","Beras Premium Berat 5Kg","",R.drawable.sania));
//                produk.add(new Produk_Util("1","SBK-09","Gulaku","Rp. 62.500","Rp. 12.000","Gulaku","",R.drawable.gulaku));
//                produk.add(new Produk_Util("1","SBK-01","Beras Premium Sania","Rp. 62.500","Rp. 58.000","Beras Premium Berat 5Kg","",R.drawable.sania));
//                produk.add(new Produk_Util("1","SBK-09","Gulaku","Rp. 62.500","Rp. 12.000","Gulaku","",R.drawable.gulaku));

//                kirimAdapter = new Kirim_Adapter(this,produk);
//                myrv.setLayoutManager(new LinearLayoutManager(this));
//                myrv.setAdapter(kirimAdapter);
                break;
            case R.id.calltoko:
                String posted_by = "+6285720009138";
//
//                String uri = "tel:" + posted_by ;
//                Intent intent = new Intent(Intent.ACTION_CALL);
//                intent.setData(Uri.parse(uri));
//                startActivity(intent);
                if (ContextCompat.checkSelfPermission(StatusPesanan.this,
                        Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(StatusPesanan.this,
                            Manifest.permission.CALL_PHONE)) {

//                        String uri = "tel:" + posted_by ;
//                        Intent intent = new Intent(Intent.ACTION_CALL);
//                        intent.setData(Uri.parse(uri));
//                        startActivity(intent);
//                        intent.setData(Uri.parse(uri));
//                        startActivity(intent);
                    } else {
                        ActivityCompat.requestPermissions((Activity) StatusPesanan.this,
                                new String[]{Manifest.permission.CALL_PHONE},
                                MY_PERMISSIONS_REQUEST_SEND_SMS);
                    }
                }else{

                    String uri = "tel:" + posted_by ;
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse(uri));
                    startActivity(intent);
                    intent.setData(Uri.parse(uri));
                    startActivity(intent);
                }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    String posted_by = "+6285720009138";

                    String uri = "tel:" + posted_by ;
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse(uri));
                    startActivity(intent);

                } else {
                    Toast.makeText(StatusPesanan.this,
                            "Telepon gagal terhubung", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

    }

    private class GetKemas   extends AsyncTask<String, Void, String> {

        ProgressDialog pdLoading = new ProgressDialog(StatusPesanan.this);
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
                url = new URL(GlobalData.globalUrl + "pesanan.php?iduser=1&op=kemas&invoice="+inv);
                Log.e("URL KEMAS", url.toString());

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

                        OrderUtil data = new OrderUtil();

                        data.setId_order(dataKaryawan.getString("id_order"));
                        data.setInvoice(dataKaryawan.getString("no_invoice"));
                        data.setWaktu(dataKaryawan.getString("waktu_order"));
                        data.setIduser(dataKaryawan.getString("id_pelanggan"));
                        data.setJumlah(dataKaryawan.getString("jumlah_item"));
                        data.setJumlah_belanja(dataKaryawan.getString("jumlah_belanja"));
                        data.setKurir(dataKaryawan.getString("nama_kurir"));
                        data.setTelepon(dataKaryawan.getString("telepon_kurir"));
                        data.setThumb(dataKaryawan.getString("thumb"));
                        data.setStatus(dataKaryawan.getString("status"));

                        produk.add(data);
                    }

                }else{
                    Toast.makeText(StatusPesanan.this, "Data tidak dapat diload, silahkan refresh", Toast.LENGTH_LONG).show();

                }

            }catch (JSONException e){
                e.printStackTrace();
                Log.e("JSON ERROR", e.toString());
            }

//            for(int i=0; i<produk.size(); i++){
//                if(produk.get(i).isCek()) {
//                    totalbelanja += Integer.parseInt(produk.get(i).getHarga()) * Integer.parseInt(produk.get(i).getJumlah());
//                    totalproduk += Integer.parseInt(produk.get(i).getJumlah());
//                }
//            }
//            Locale localeID = new Locale("in", "ID");
//            NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
//            tvTotal.setText(formatRupiah.format((double)totalbelanja));

            RecyclerView myrv = (RecyclerView) findViewById(R.id.recycler_cart);
            myAdapter = new Kemas_Adapter(StatusPesanan.this, produk);
            myrv.setLayoutManager(new LinearLayoutManager(StatusPesanan.this));
            myrv.setAdapter(myAdapter);
        }
    }
}