package com.bangkitgiat.koperasibum;

public class OrderUtil {
    private String id_order, invoice, waktu, iduser, jumlah, kurir, telepon, thumb, status, jumlah_belanja;

    public OrderUtil(){

    }

    public OrderUtil (String id_order, String invoice, String waktu, String iduser, String jumlah, String kurir, String telepon, String thumb, String status, String jumlah_belanja){
        this.id_order = id_order;
        this.invoice = invoice;
        this.waktu = waktu;
        this.iduser = iduser;
        this.jumlah = jumlah;
        this.kurir = kurir;
        this.telepon= telepon;
        this.thumb = thumb;
        this.status = status;
        this.jumlah_belanja = jumlah_belanja;
    }

    public String getId_order() {
        return id_order;
    }

    public void setId_order(String id_order) {
        this.id_order = id_order;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public String getIduser() {
        return iduser;
    }

    public void setIduser(String iduser) {
        this.iduser = iduser;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }

    public String getKurir() {
        return kurir;
    }

    public void setKurir(String kurir) {
        this.kurir = kurir;
    }

    public String getTelepon() {
        return telepon;
    }

    public void setTelepon(String telepon) {
        this.telepon = telepon;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getJumlah_belanja() {
        return jumlah_belanja;
    }

    public void setJumlah_belanja(String jumlah_belanja) {
        this.jumlah_belanja = jumlah_belanja;
    }
}
