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

public class Bayar extends AppCompatActivity {
    List<CartUtil> produk;
    Bayar_Adapter myAdapter;
    Button bayar;

    private String totalProduk, totalBelanja, iduser, inv;
    private TextView tvBayar, tvSaldo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bayar);

        Intent intent = getIntent();
        totalBelanja = intent.getExtras().getString("totalbelanja");
        totalProduk = intent.getExtras().getString("totalproduk");
        iduser = intent.getExtras().getString("iduser");
        inv = intent.getExtras().getString("invoice");


        tvBayar= (TextView)findViewById(R.id.totalbayar);
        tvSaldo = (TextView)findViewById(R.id.sisasaldo);

        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        tvBayar.setText(formatRupiah.format((double)Integer.parseInt(totalBelanja)));
        tvSaldo.setText(formatRupiah.format((double)230000-Integer.parseInt(totalBelanja)));



        bayar = (Button)findViewById(R.id.btn_placeorder);
        bayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iPesan = new Intent(Bayar.this, StatusPesanan.class);
                iPesan.putExtra("totalbelanja", ""+totalBelanja);
                iPesan.putExtra("totalproduk" , ""+totalProduk );
                iPesan.putExtra("iduser" , ""+iduser );
                iPesan.putExtra("invoice" , ""+inv );
                startActivity(iPesan);
            }
        });

        produk = new ArrayList<CartUtil>();
//        produk.add(new Produk_Util("1","SBK-01","Beras Premium Sania","Rp. 62.500","Rp. 58.000","Beras Premium Berat 5Kg","",R.drawable.sania));
//        produk.add(new Produk_Util("1","SBK-09","Gulaku","Rp. 62.500","Rp. 12.000","Gulaku","",R.drawable.gulaku));

        RecyclerView myrv = (RecyclerView) findViewById(R.id.recycler_bayar);
        myAdapter = new Bayar_Adapter(this,produk);
//        mLayoutManager = new LinearLayoutManager(getActivity());
        myrv.setLayoutManager(new LinearLayoutManager(this));
        myrv.setAdapter(myAdapter);
    }


}
