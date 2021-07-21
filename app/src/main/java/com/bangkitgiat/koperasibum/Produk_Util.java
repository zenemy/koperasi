package com.bangkitgiat.koperasibum;

public class Produk_Util {

    private String id, kategori, stok, nama, aktif_produk, keterangan, foto_produk, harga_jual, diskon, berat;

    public Produk_Util(String id, String kategori, String stok, String nama, String aktif_produk, String keterangan, String foto_produk, String harga_jual, String diskon, String berat){

        this.id = id;
      this.kategori = kategori;
      this.stok=stok;
      this.nama=nama;
      this.aktif_produk = aktif_produk;
      this.keterangan = keterangan;
      this.foto_produk = foto_produk;
      this.harga_jual = harga_jual;
      this.diskon = diskon;
      this.berat = berat;

    }

    public Produk_Util(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getStok() {
        return stok;
    }

    public void setStok(String stok) {
        this.stok = stok;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAktif_produk() {
        return aktif_produk;
    }

    public void setAktif_produk(String aktif_produk) {
        this.aktif_produk = aktif_produk;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getFoto_produk() {
        return foto_produk;
    }

    public void setFoto_produk(String foto_produk) {
        this.foto_produk = foto_produk;
    }

    public String getHarga_jual() {
        return harga_jual;
    }

    public void setHarga_jual(String harga_jual) {
        this.harga_jual = harga_jual;
    }

    public String getDiskon() {
        return diskon;
    }

    public void setDiskon(String diskon) {
        this.diskon = diskon;
    }

    public String getBerat() {
        return berat;
    }

    public void setBerat(String berat) {
        this.berat = berat;
    }
}
