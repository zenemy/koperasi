 package com.bangkitgiat.koperasibum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

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

 public class MainActivity extends AppCompatActivity {

     List<Produk_Util> produk;
     Produk_Adapter myAdapter;
     RecyclerView myrv;
     TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);


        produk = new ArrayList<Produk_Util>();


        GetProducts p = new GetProducts();
        p.execute();


    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bar, menu);
        MenuItem menuItem = menu.findItem(R.id.miCompose);
        SearchView searchView = (SearchView)menuItem.getActionView();
        searchView.setQueryHint("Cari Produk");

        MenuItem item = menu.findItem(R.id.miCart);
        item.setActionView(R.layout.badgenotif);
        RelativeLayout notifCount = (RelativeLayout) item.getActionView();

        tv = (TextView) notifCount.findViewById(R.id.actionbar_notifcation_textview);
        tv.setText("0");

        GetCart c = new GetCart();
        c.execute();

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iCart = new Intent(MainActivity.this, ShopingCart.class);
                iCart.putExtra("id", "1");
                startActivity(iCart);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                myAdapter.getFilter().filter(newText);

                return false;
            }
        });
        return true;
    }

     @Override
     public boolean onOptionsItemSelected(MenuItem item) {
         // Handle presses on the action bar items
         switch (item.getItemId()) {
             case R.id.miCompose:
//                 Toast.makeText(MainActivity.this, "Clicked", Toast.LENGTH_LONG).show();
                 return true;
             case R.id.miCart:
                 Intent iCart = new Intent(MainActivity.this, ShopingCart.class);
                 startActivity(iCart);
                 return true;
             case R.id.action_profil:
                 Intent iProfil = new Intent(MainActivity.this, Profil.class);
                 startActivity(iProfil);
                 return true;
             case R.id.action_history:
                 Intent iHistory = new Intent(MainActivity.this, HistoryPesanan.class);
                 startActivity(iHistory);
                 return true;
             case R.id.action_order:
                 Intent iOrder = new Intent(MainActivity.this, StatusPesanan.class);
                 startActivity(iOrder);
                 return true;
             default:
                 return super.onOptionsItemSelected(item);
         }
     }

     private class GetProducts extends AsyncTask<String, Void, String> {

         ProgressDialog pdLoading = new ProgressDialog(MainActivity.this);
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
                 url = new URL(GlobalData.globalUrl + "produk.php?kategori=all");
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
                         data.setKategori(dataKaryawan.getString("id_kategori"));
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
                     Toast.makeText(MainActivity.this, "Data tidak dapat diload, silahkan refresh", Toast.LENGTH_LONG).show();

                 }

             }catch (JSONException e){
                 e.printStackTrace();
                 Log.e("JSON ERROR", e.toString());
             }
// Initialize contacts
//                        dataPos = Contact.createContactsList(20);
             // Create adapter passing in the sample user data
             Log.e("URL FOTO MAIN","data-->"+produk.toString());
//             Produk_Adapter adapter = new Produk_Adapter(MainActivity.this, produk);
             // Attach the adapter to the recyclerview to populate items
//             myrv.setLayoutManager(new GridLayoutManager(MainActivity.this,2));
//             myrv.setAdapter(adapter);
             // Set layout manager to position the items
//             myrv.setLayoutManager(new LinearLayoutManager(MainActivity.this));
             // That's all!
             //cek data masuk apa gak


             myrv = (RecyclerView) findViewById(R.id.recyclerview_id);
             myAdapter = new Produk_Adapter(MainActivity.this,produk);
             myrv.setLayoutManager(new GridLayoutManager(MainActivity.this,2));
             myrv.setAdapter(myAdapter);
         }
     }

     private class GetCart extends AsyncTask<String, Void, String> {

         ProgressDialog pdLoading = new ProgressDialog(MainActivity.this);
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
                 url = new URL(GlobalData.globalUrl + "cart.php?iduser=1&op=view");
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
                 kode = json_obj.getString("jumlah");

                 Log.e("JSON registrasi", kode);

             } catch (JSONException e) {
                 e.printStackTrace();
             }
tv.setText(kode);
         }
     }

}