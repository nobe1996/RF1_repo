/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 *
 * @author Aron
 */
public class User {
    
    private int id;
    private String nev;
    private String felhasznalonev;
    private String jelszo;
    private String email;

    public User(String nev, String felhasznalonev, String jelszo, String email){
        this.nev = nev;
        this.felhasznalonev = felhasznalonev;
        this.jelszo = jelszo;
        this.email = email;
    }

    public String getIp_address() {
        try {
            InetAddress IP=InetAddress.getLocalHost();
            return IP.getHostAddress();
        } catch (UnknownHostException ex) {
            System.err.println(ex.getMessage());
        }
        
        return null;
    }

    public String getMac_address() {
        try {
            InetAddress IP=InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(IP);
            
            byte[] mac = network.getHardwareAddress();

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            }
                
            return sb.toString();
            
        } catch (SocketException | UnknownHostException ex) {
            System.err.println(ex.getMessage());
        }
        return null;
    }

    
    
    public int getId() {
        return id;
    }

    public String getNev() {
        return nev;
    }

    public String getFelhasznalonev() {
        return felhasznalonev;
    }

    public String getJelszo() {
        return jelszo;
    }

    public String getEmail() {
        return email;
    }

    public void setNev(String nev) {
        this.nev = nev;
    }

    public void setFelhasznalonev(String felhasznalonev) {
        this.felhasznalonev = felhasznalonev;
    }

    public void setJelszo(String jelszo) {
        this.jelszo = jelszo;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(int id) {
        this.id = id;
    }
    
}
