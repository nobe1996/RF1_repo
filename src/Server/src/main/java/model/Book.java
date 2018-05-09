/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;


/**
 *
 * @author Aron
 */
public class Book {
    private int isbn;
    private String cim;
    private String kategoria;
    private int darab;
    private String leiras;
    private int kolcsonzesek_szama;
    private String feltoltes_datuma;
    
    public Book(
    		int isbn,
    		String cim,
            String kategoria,
            int darab,
            String leiras,
            int kolcsonzesek_szama,
            String feltoltes_datuma
            ){
    	this.isbn = isbn;
        this.cim = cim;
        this.kategoria = kategoria;
        this.darab = darab;
        this.leiras = leiras;
        this.kolcsonzesek_szama = kolcsonzesek_szama;
        this.feltoltes_datuma = feltoltes_datuma;
    }

    public int getIsbn() {
        return isbn;
    }

    public String getCim() {
        return cim;
    }

    public String getKategoria() {
        return kategoria;
    }

    public int getDarab() {
        return darab;
    }

    public String getLeiras() {
        return leiras;
    }

    public int getKolcsonzesek_szama() {
        return kolcsonzesek_szama;
    }

    public String getFeltoltes_datuma() {
        return feltoltes_datuma;
    }

    public void setIsbn(int isbn) {
        this.isbn = isbn;
    }

    public void setCim(String cim) {
        this.cim = cim;
    }

    public void setKategoria(String kategoria) {
        this.kategoria = kategoria;
    }

    public void setDarab(int darab) {
        this.darab = darab;
    }

    public void setLeiras(String leiras) {
        this.leiras = leiras;
    }

    public void setKolcsonzesek_szama(int kolcsonzesek_szama) {
        this.kolcsonzesek_szama = kolcsonzesek_szama;
    }

    public void setFeltoltes_datuma(String feltoltes_datuma) {
        this.feltoltes_datuma = feltoltes_datuma;
    }
    
    
    
    
    
}
