package com.bangkitgiat.koperasibum;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HistoryPesanan extends AppCompatActivity {

    List<Produk_Util> produk;
    History_Adapter myAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);

        produk = new ArrayList<Produk_Util>();
//        produk.add(new Produk_Util("1","SBK-01","Beras Premium Sania","Rp. 62.500","Rp. 58.000","Beras Premium Berat 5Kg","",R.drawable.sania));
//        produk.add(new Produk_Util("1","SBK-09","Gulaku","Rp. 62.500","Rp. 12.000","Gulaku","",R.drawable.gulaku));

        RecyclerView myrv = (RecyclerView) findViewById(R.id.recycler_cart);
        myAdapter = new History_Adapter(this,produk);
//        mLayoutManager = new LinearLayoutManager(getActivity());
        myrv.setLayoutManager(new LinearLayoutManager(this));
        myrv.setAdapter(myAdapter);
        myrv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iBayar = new Intent(HistoryPesanan.this, Rincian_Pesanan.class);
                startActivity(iBayar);
            }
        });
    }
}
