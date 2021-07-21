package com.bangkitgiat.koperasibum;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

public class Checkout extends AppCompatActivity {
    private Button bayar;
    private String totalProduk, totalBelanja;
    private TextView tvProduk, tvBelanja, tvBayar, tvSubBelanja;
    String inv;
    String idUser = "1";
    int d=0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout);

        // Recieve data
        Intent intent = getIntent();
        totalBelanja = intent.getExtras().getString("totalbelanja");
        totalProduk = intent.getExtras().getString("totalproduk");

        Log.e("Checkout", "Total Belanja = "+totalBelanja);
        Log.e("Checkout", "Total Produk = "+totalProduk);

        tvProduk = (TextView)findViewById(R.id.totalproduk);
        tvBelanja = (TextView)findViewById(R.id.totalbelanja);
        tvBayar = (TextView) findViewById(R.id.totalbayar);
        tvSubBelanja = (TextView)findViewById(R.id.totalbelanja_sub);

        tvProduk.setText("Total Pesanan ("+totalProduk+" Produk)");
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        tvBelanja.setText(formatRupiah.format((double)Integer.parseInt(totalBelanja)));
        tvSubBelanja.setText(formatRupiah.format((double)Integer.parseInt(totalBelanja)));
        tvBayar.setText(formatRupiah.format((double)Integer.parseInt(totalBelanja)+30000));
        bayar = (Button)findViewById(R.id.btn_placeorder);
        bayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Integer.parseInt(totalBelanja)>230000){
                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(Checkout.this);
                    //Uncomment the below code to Set the message and title from the strings.xml file
                    builder.setMessage("Informasi").setTitle("Saldo tidak mencukupi");

                    //Setting message manually and performing action on button click
                    builder.setMessage("Mohon maaf saldo anda tidak mencukupi. Silahkan kurangi isi keranjang belanja anda")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent iCart = new Intent(Checkout.this, ShopingCart.class);
                                    startActivity(iCart);
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
                }else{
                    Random r = new Random();
                    int x = r.nextInt(9999)+1111;
                    SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
                    dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                    Date today = Calendar.getInstance().getTime();
                    String tanggal = dateFormat.format(today);
                    inv = ""+x+""+tanggal;
                    Log.e("INVOICE", inv);

                    idUser = "1";

                    AddOrder a = new AddOrder();
                    a.execute();
                    d = Integer.parseInt(totalBelanja)+30000;
                    Intent iBayar = new Intent(Checkout.this, Bayar.class);
                    iBayar.putExtra("totalbelanja", ""+d);
                    iBayar.putExtra("totalproduk" , ""+totalProduk );
                    iBayar.putExtra("iduser" , ""+idUser );
                    iBayar.putExtra("invoice" , ""+inv );
                    startActivity(iBayar);
                }

            }
        });
    }
    private class AddOrder extends AsyncTask<String, Void, String> {

        ProgressDialog pdLoading = new ProgressDialog(Checkout.this);
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
                url = new URL(GlobalData.globalUrl + "pesanan.php?iduser=1&op=add&invoice="+inv+"&jumlah="+totalProduk+"&belanja="+d);

                Log.e("URL ADD ORDER", url.toString());

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
                Toast.makeText(Checkout.this,"Pesanan telah ditambahkan kedalam sistem", Toast.LENGTH_LONG).show();


            }else{
                Toast.makeText(Checkout.this, "Pesanan tidak tersimpan. Harap ulangi dalam beberapa waktu", Toast.LENGTH_LONG).show();

            }

        }
    }
}
