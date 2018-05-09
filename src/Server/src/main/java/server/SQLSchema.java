/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

/**
 *
 * @author Aron
 */
public enum SQLSchema {
    FELHASZNALO("CREATE TABLE IF NOT EXISTS Felhasznalo (\n" +
"    id             INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
"    nev            VARCHAR (50)  NOT NULL,\n" +
"    felhasznalonev VARCHAR (20)  NOT NULL UNIQUE,\n" +
"    jelszo         VARCHAR (20)  NOT NULL,\n" +
"    email          VARCHAR (100) NOT NULL,\n" +
"    ip_address          VARCHAR (100) NOT NULL,\n" +
"    mac_address          VARCHAR (100) NOT NULL\n" +
");"),
    
    KOLCSONZES("CREATE TABLE IF NOT EXISTS Kolcsonzes (\n" +
"    kolcs_id      INT (10) PRIMARY KEY\n" +
"                           NOT NULL,\n" +
"    felh_id       INTEGER REFERENCES Felhasznalo (id) ON DELETE SET NULL\n" +
"                                                       ON UPDATE CASCADE\n" +
"                           NOT NULL,\n" +
"    konyv_isbn    INTEGER REFERENCES Konyv (isbn) ON DELETE SET NULL\n" +
"                                                   ON UPDATE CASCADE\n" +
"                           NOT NULL,\n" +
"    kolcs_datum   DATE,\n" +
"    kolcs_lejarat DATE\n" +
");"),
    
    KONYV("CREATE TABLE IF NOT EXISTS Konyv (\n" +
"    isbn               INTEGER      PRIMARY KEY\n" +
"                                     NOT NULL,\n" +
"    cim                VARCHAR (150) NOT NULL,\n" +
"    kategoria          VARCHAR (100),\n" +
"    darab              INT (5),\n" +
"    leiras             VARCHAR (255),\n" +
"    kolcsonzesek_szama INT (5),\n" +
"    feltoltes_datuma   DATE          NOT NULL\n" +
");"),
    
     SESSION("CREATE TABLE IF NOT EXISTS Session (\n" +
"    id               INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
"    ip_address                VARCHAR (150),\n" +
"    mac_address          VARCHAR (100),\n" +
"    user_id              INT (13)\n" +
");"),INSERTBOOKS("INSERT OR IGNORE INTO 'Konyv' VALUES (453750, 'Brent Weeks - Az arnyak utjan', 'fantasy', 15, 'durzo', 666, '2017.11.29.');");
    
    private final String name;
    
    private SQLSchema(String s){
        name = s;
    }
   
    @Override
    public String toString(){
        return this.name;
    }

}
