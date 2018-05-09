/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import exception.ConnClosedException;
import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import model.Book;
import model.User;

/**
 *
 * @author Aron
 */
public class DatabaseConnection {
    
    private boolean isOpen = false;
    
    private Connection connection = null;
    private Statement st;
    private File dbPath;
    private File database;

    /**
        @build a connection and holds it until gets closed.
        if the connection cannot be prepared then instantly close it.
    */
    public DatabaseConnection(String database) throws ClassNotFoundException{
        if(database == null)
            database = "BookShopDB.db";
        try{
            dbPath = new File(getClass().getClassLoader().getResource(database).getFile());
            this.database = dbPath;

        }catch(Exception e){
            System.err.println(e.getMessage());
            return;
        }
        
        Class.forName("org.sqlite.JDBC");
        
        try{
            
            connection = DriverManager.getConnection("jdbc:sqlite:"+dbPath);
            st = connection.createStatement();
            
            if(!initDb())
                this.destroy();
            else
                this.openConnection();
            
        }catch(SQLException e){
            
            System.err.println(e.getMessage());
            
        }
    }
    
    public boolean isOpen() throws ConnClosedException {
        if(isOpen == false)
            throw new ConnClosedException("The database connection is closed.");
        
        return this.isOpen;
    }
    
    public void openConnection(){this.isOpen = true;}
    
    /**
        If SQL schema updated or never imported this will do the job.
    */
    private boolean initDb(){
        boolean rvSucc = false;
        if(dbPath == null || connection == null)
            return false;
        try{
            st.addBatch(SQLSchema.SESSION.toString());
            st.addBatch(SQLSchema.FELHASZNALO.toString());
            st.addBatch(SQLSchema.KOLCSONZES.toString());
            st.addBatch(SQLSchema.KONYV.toString());
            st.addBatch(SQLSchema.INSERTBOOKS.toString());

            st.executeBatch();
            rvSucc = true;
 
        }catch(SQLException e){
            System.err.println(e.getMessage());
        }
        
        return rvSucc;
    }
    
    /**
        @return specific user if user_id > 0 else all users if database contains at least 1 user.
    */
    public List<User> getUserSingleOrMultiple(int user_id) {
        List<User> container = new ArrayList<User>();
        ResultSet rs;
        
        try {
          if(isOpen()){
               String sqlGetUser = "SELECT * FROM Felhasznalo WHERE id = "+ user_id+";";
               if(user_id < 0) // try multiple
               {
                  sqlGetUser = "SELECT * FROM Felhasznalo";
                  rs = st.executeQuery(sqlGetUser);
                  
                  while(rs.next()){
                      User usr = new User(rs.getString("nev"),rs.getString("felhasznalonev"),rs.getString("jelszo"),rs.getString("email"));
                      
                      usr.setId(rs.getInt("id"));
                      
                      container.add(usr);
                  }
                  
                  return container;
               }
               
               rs = st.executeQuery(sqlGetUser);
                  
               while(rs.next()){
                    User usr = new User(rs.getString("nev"),rs.getString("felhasznalonev"),rs.getString("jelszo"),rs.getString("email"));
                    
                    usr.setId(rs.getInt("id"));
                    
                    container.add(usr);
               }
              
          } 
            
        } catch (ConnClosedException | SQLException ex) {
            System.err.println(ex.getMessage());
        }

        return container;
    }
    /**
        @return specific book if book_id > 0 else all books if database contains at least 1 book.
    */
    public List<Book> getBookSingleOrMultiple(int book_id){
        List<Book> container = new ArrayList();
        ResultSet rs;
               
        try {
          if(isOpen()){
              
               String sqlGetBook = "SELECT * FROM Konyv WHERE isbn = "+ book_id+";";
               if(book_id < 0) // try multiple
               {
                  sqlGetBook = "SELECT * FROM Konyv";
                  rs = st.executeQuery(sqlGetBook);
                  
                  while(rs.next()){
                      
                      Book book = new Book(
                    		  rs.getInt("isbn"),
                    		  rs.getString("cim"),
                              rs.getString("kategoria"),
                              rs.getInt("darab"),
                              rs.getString("leiras"),
                              rs.getInt("kolcsonzesek_szama"),
                              rs.getString("feltoltes_datuma"));
                      
                      container.add(book);
                  }
                  
                  return container;
               }
               
               rs = st.executeQuery(sqlGetBook);
                  
               while(rs.next()){
                   
                     Book book = new Book(
                    		 rs.getInt("isbn"),
                    		 rs.getString("cim"),
                              rs.getString("kategoria"),
                              rs.getInt("darab"),
                              rs.getString("leiras"),
                              rs.getInt("kolcsonzesek_szama"),
                              rs.getString("feltoltes_datuma"));
                     
                     
                    container.add(book);
               }
              
          } 
            
        } catch (ConnClosedException | SQLException ex) {
            System.err.println(ex.getMessage());
        }
        
        return container;
    }
    /**
     * 
     * @param book
     * @return true if the book is imported
     */
    public boolean importBook(Book book) throws SQLException{
        boolean rvSucc = false;
        int i = 1;
        String sql = "INSERT INTO Konyv("
                    + "isbn,"
                    + "cim,"
                    + "kategoria,"
                    + "darab,"
                    + "leiras,"
                    + "kolcsonzesek_szama,"
                    + "feltoltes_datuma) VALUES(?,?,?,?,?,?,?)";
        
        try {
           if(isOpen()){
               PreparedStatement pstmt = connection.prepareStatement(sql);
            
               pstmt.setInt(i++, book.getIsbn());
               pstmt.setString(i++,book.getCim());
               pstmt.setString(i++, book.getKategoria());
               pstmt.setInt(i++, book.getDarab());
               pstmt.setString(i++, book.getLeiras());
               pstmt.setInt(i++, book.getKolcsonzesek_szama());
               pstmt.setString(i++, book.getFeltoltes_datuma());

               if(pstmt.executeUpdate() == 1)
                   rvSucc = true;
           } 

        } catch (ConnClosedException | SQLException ex) {
            System.err.println(ex.getMessage());
        }
        
        return rvSucc;
    }
    
    /**
     * @param user
     * @return
     * @throws SQLException 
     */
    public boolean importUser(User user) throws SQLException{
        boolean rvSucc = false;
        int i = 1;
        String sql = "INSERT INTO Felhasznalo ("
                + "nev,"
                + "felhasznalonev,"
                + "jelszo,"
                + "email"
                + ") VALUES(?,?,?,?)";
        //System.out.println(sql);
        try{
            if(isOpen()){
                PreparedStatement pstmt = connection.prepareStatement(sql);

                pstmt.setString(i++,user.getNev());
                pstmt.setString(i++, user.getFelhasznalonev());
                pstmt.setString(i++,user.getJelszo());
                pstmt.setString(i++, user.getEmail());

                if(pstmt.executeUpdate() == 1)
                    rvSucc = true;

                pstmt.close();
            }
        }catch(ConnClosedException | SQLException e){
            System.err.println(e.getMessage());
        }
        return rvSucc;
    }
    
    /**
        @update book
    */
    public boolean updateBook(Book book){
       boolean rvSucc = false;
       int i = 1;
       String upBook = "UPDATE SET isbn = ?, cim = ?, kategoria = ? , darab = ? , leiras = ? , kolcsonzesek_szama = ? , feltoltes_datuma = ? WHERE isbn = ?;";
       
       try{
           if(isOpen()){
                PreparedStatement pstmt = connection.prepareStatement(upBook);

                pstmt.setInt(i++, book.getIsbn());
                pstmt.setString(i++,book.getCim());
                pstmt.setString(i++, book.getKategoria());
                pstmt.setInt(i++, book.getDarab());
                pstmt.setString(i++, book.getLeiras());
                pstmt.setInt(i++, book.getKolcsonzesek_szama());
                pstmt.setString(i++, book.getFeltoltes_datuma());
                pstmt.setInt(i++, book.getIsbn());

                if(pstmt.executeUpdate() == 1)
                    rvSucc = true;
           }
           
       }catch(ConnClosedException | SQLException e){
            System.err.println(e.getMessage());
       }
       
       return rvSucc;
    }
    
    /**
        @update user
    */
    public boolean updateUser(User user){
       boolean rvSucc = false;
       int i = 1;
       String upUser = "UPDATE SET nev = ?, felhasznalonev = ?, jelszo = ? , email = ? WHERE felhasznalonev = ?;";
       
       try{
           if(isOpen()){
                PreparedStatement pstmt = connection.prepareStatement(upUser);

                pstmt.setString(i++,user.getNev());
                pstmt.setString(i++, user.getFelhasznalonev());
                pstmt.setString(i++,user.getJelszo());
                pstmt.setString(i++, user.getEmail());
                pstmt.setString(i++, user.getFelhasznalonev());

                if(pstmt.executeUpdate() == 1)
                    rvSucc = true;
           }
       }catch(ConnClosedException | SQLException e){
           System.err.println(e.getMessage());
       } 
       return rvSucc;
    }
    
    /**
        @delete book
    */
    public boolean deleteBook(int book_id){
       boolean rvSucc = false;
       String delBook = "DELETE FROM KONYV WHERE ISBN = ?;";
       
       try{
           if(isOpen()){
                PreparedStatement pstmt = connection.prepareStatement(delBook);

                pstmt.setInt(1, book_id);

                if(pstmt.executeUpdate() == 1)
                    rvSucc = true;
           }
       }catch(ConnClosedException | SQLException e){
           System.err.println(e.getMessage());
       }
       return rvSucc;
    }
    
    /**
        @delete user
    */
    public boolean deleteUser(int user_id){
        boolean rvSucc = false;
        String delUsr = "DELETE FROM FELHASZNALO WHERE ID = ?;";
        try{
            if(isOpen()){
                PreparedStatement pstmt = connection.prepareStatement(delUsr);

                pstmt.setInt(1, user_id);

                if(pstmt.executeUpdate() == 1)
                    rvSucc = true;
            }
        }catch(ConnClosedException | SQLException e){
           System.err.println(e.getMessage());
        }
        return rvSucc;
    }
    
    /**
     * Prepares user for login and stores the session.
     * If user logs in from another platform, destroys the previously connected session.
     *  @login user
    */
    public boolean logIn(String felhasznalo_nev, String jelszo){
        boolean rvSucc = false;
        
        if(felhasznalo_nev == null || jelszo == null)
            return false;
        
        String sqlSelect = "SELECT * FROM Felhasznalo WHERE felhasznalonev = '"+felhasznalo_nev+"' AND jelszo = '"+jelszo+"';";
        String sqlInsert = "INSERT INTO Session (ip_address,mac_address,user_id) VALUES(?,?,?);";

        try{
            if(isOpen()){
                ResultSet rs = st.executeQuery(sqlSelect);
                int id = 0;

                if(rs.next()){
                    id = rs.getInt("id");
                }

                User u = new User(rs.getString("nev"),rs.getString("felhasznalonev"),rs.getString("jelszo"),rs.getString("email"));
                
                if(id > 0){
                    int i = 1;
                    rvSucc = true;
                    
                    PreparedStatement pstmt = connection.prepareStatement(sqlInsert);
                    
                    pstmt.setString(i++, u.getIp_address());
                    pstmt.setString(i++, u.getMac_address());
                    pstmt.setInt(i++, id);
                    pstmt.executeUpdate();
                    
                    String sqlSession = "SELECT id FROM Session WHERE user_id = "+id+";";
                    rs = st.executeQuery(sqlSession);
                    
                    id = 0;
                    if (rs.next()){
                        id = rs.getInt("id");
                    }
                    
                    if(id > 0){
                        String sqlDestroySession = "DELETE FROM Session WHERE id = ?";
                        
                        pstmt = connection.prepareStatement(sqlDestroySession);
                        pstmt.setInt(1, id);             
                        pstmt.executeUpdate();
                        
                    }
                    
                }
                
            }
        }catch(ConnClosedException | SQLException e){
           System.err.println(e.getMessage());
        }
        return rvSucc;
    }
    
    /**
     * Prepare user for log out and destroy the session if ip and mac pair correct.
        @logout user
    */
    public boolean logOut(User user){
        boolean rvSucc = false;
        String sql = "DELETE FROM Session WHERE user_id = ? AND ip_address = ? AND mac_address = ?;";
        
        
         try{
            if(isOpen()){
                int i = 1;
                
               PreparedStatement pstmt = connection.prepareStatement(sql);
               
               pstmt.setInt(i++, user.getId());
               pstmt.setString(i++, user.getIp_address());
               pstmt.setString(i++, user.getMac_address());
               
               if(pstmt.executeUpdate() == 1)
                   rvSucc = true;
            }
        }catch(ConnClosedException | SQLException e){
           System.err.println(e.getMessage());
        }
        
        return rvSucc;
    }
    
    /**
        @register user
    
    public boolean signUp(User user){
        boolean rvSucc = false;
        
        int i=1;
        String insUsr = "INSERT INTO FELHASZNALO VALUES(?,?,?,?);";
       
        try{
            if(isOpen()){
                PreparedStatement pstmt = connection.prepareStatement(insUsr);

                pstmt.setString(i++,user.getNev());
                pstmt.setString(i++, user.getFelhasznalonev());
                pstmt.setString(i++,user.getJelszo());
                pstmt.setString(i++, user.getEmail());
                pstmt.setString(i++, user.getIp_address());
                pstmt.setString(i++, user.getMac_address());

                if(pstmt.executeUpdate() == 1)
                    rvSucc = true;
            }
        }catch(ConnClosedException | SQLException e){
           System.err.println(e.getMessage());
        }
        
        return rvSucc;
    }
    */
    
    
    /**
        @destroy database connection
    */
    public void destroy(){
        /*try{
            if(st != null)
                st.close();
            if(connection != null)
                connection.close();
            
            this.isOpen = false;
        }catch(SQLException e){
            System.err.println(e.getMessage());
        }*/
    }
    
    /**
     * 
     * @return the top 10 most viewed books
     */
    public List<Book> getTopViewedBooks(){
        List<Book> list = new ArrayList<Book>();
		ResultSet rs;
        
        try {
          if(isOpen()){
               String sqlGetTopViewedBooks = "SELECT * FROM KONYV ORDER BY kolcsonzesek_szama DESC LIMIT 10;";
          
               rs = st.executeQuery(sqlGetTopViewedBooks);
                  
               while(rs.next()){
                     Book book = new Book(
                    		 rs.getInt("isbn"), 
                    		 rs.getString("cim"),
                              rs.getString("kategoria"),
                              rs.getInt("darab"),
                              rs.getString("leiras"),
                              rs.getInt("kolcsonzesek_szama"),
                              rs.getString("feltoltes_datuma"));
                     
                     
                    list.add(book);
               }
              
          } 
            
        } catch (ConnClosedException | SQLException ex) {
            System.err.println(ex.getMessage());
        }

        return list;
    }
    
    /**
     * 
     * @return the top 10 new books in the database
     */
    public List<Book> getTopNewBooks(){
        List<Book> list = new ArrayList<Book>();
        
		ResultSet rs;
        
        try {
          if(isOpen()){
               String sqlGetTopNewBooks = "SELECT * FROM KONYV ORDER BY feltoltes_datuma DESC LIMIT 10;";
          
               rs = st.executeQuery(sqlGetTopNewBooks);
                  
               while(rs.next()){
                     Book book = new Book(
                    		  rs.getInt("isbn"),
                    		  rs.getString("cim"),
                              rs.getString("kategoria"),
                              rs.getInt("darab"),
                              rs.getString("leiras"),
                              rs.getInt("kolcsonzesek_szama"),
                              rs.getString("feltoltes_datuma"));
                     
                     
                    list.add(book);
               }
              
          } 
            
        } catch (ConnClosedException | SQLException ex) {
            System.err.println(ex.getMessage());
        }
		
        return list;
    }
    
    public int isUserLogged(){
        int user_id = -1;
        String ip = "", mac = "";
         try {
            InetAddress IP=InetAddress.getLocalHost();
            ip = IP.getHostAddress();
            
            NetworkInterface network = NetworkInterface.getByInetAddress(IP);
            
            byte[] mac_byte = network.getHardwareAddress();
             
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac_byte.length; i++) {
                sb.append(String.format("%02X%s", mac_byte[i], (i < mac_byte.length - 1) ? "-" : ""));
            }
            
            mac = sb.toString();
            
        } catch (UnknownHostException | SocketException ex) {
            System.err.println(ex.getMessage());
        }

        String sql = "SELECT user_id FROM Session WHERE ip_address = '"+ip+"' AND mac_address='"+mac+"';";
        try{
            ResultSet rs = st.executeQuery(sql);
            
            if(rs.next())
                user_id = rs.getInt("user_id");
            
        }catch(Exception e){
            System.err.println(e.getMessage());
        }
        
        return user_id;
    }
    
    
}
