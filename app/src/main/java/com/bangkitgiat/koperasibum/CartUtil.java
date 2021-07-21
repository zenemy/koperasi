package com.bangkitgiat.koperasibum;

public class CartUtil {

    private String id, id_user, id_produk, jumlah, tgl, jam, status;
    private String nama, harga, foto;
    private boolean cek;

    public CartUtil(){

    }

    public CartUtil(String id,  String id_user, String id_produk, String jumlah, String tgl, String jam, String status, String nama, String harga, String foto, boolean cek){
        this.id = id;
        this.id_user = id_user;
        this.id_produk = id_produk;
        this.jumlah = jumlah;
        this.tgl = tgl;
        this.jam = jam;
        this.status= status;
        this.nama = nama;
        this.harga = harga;
        this.foto = foto;
        this.cek = cek;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getId_produk() {
        return id_produk;
    }

    public void setId_produk(String id_produk) {
        this.id_produk = id_produk;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }

    public String getTgl() {
        return tgl;
    }

    public void setTgl(String tgl) {
        this.tgl = tgl;
    }

    public String getJam() {
        return jam;
    }

    public void setJam(String jam) {
        this.jam = jam;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public boolean isCek() {
        return cek;
    }

    public void setCek(boolean cek) {
        this.cek = cek;
    }
}
