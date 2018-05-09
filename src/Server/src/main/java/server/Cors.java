package server;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.path;
import static spark.Spark.before;

import java.sql.SQLException;
import java.util.List;

import com.google.gson.Gson;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Book;

import model.User;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Aron
 */
public class Cors {

	static DatabaseConnection connection;

        public Cors(DatabaseConnection conn){
            connection = conn;
        }
        
	public static void setupDatabase(DatabaseConnection conn) {
		connection = conn;
	}

	public static void setupRoutes() {
		before("/*", (q, a) -> System.out.println("API call received"));
		before((request, response) -> {
			response.header("Access-Control-Request-Method", "POST, GET, OPTIONS, DELETE, PUT");
			response.header("Access-Control-Allow-Origin", "null");
		});

		path("/user", () -> {
			get("/get/:id", (request, response) -> userData(request.params(":id")));

			get("/login/:uname/:passwd", (req, res) -> connection.logIn(req.params(":uname"), req.params(":passwd")));

			get("/logout/:uid", (req, res) -> {
				connection.logOut(connection.getUserSingleOrMultiple(Integer.parseInt(req.params(":uid"))).get(0));
				res.redirect("/");
				return "";
			});
                        
			get("/import/:name/:uname/:pw/:email", (request, response) -> insertUser(request.params(":name"),
					request.params(":uname"), request.params(":pw"), request.params(":email")));

			get("/get/random", (req, res) -> "");
                        
                        get("/isLogged", (req,res) -> connection.isUserLogged());
			// get("/cookie/set", (request, response) ->
			// logIn(request.params()))
		});
                
                path("/book", () ->{
                        post("/import/:isbn/:title/:category/:count/:desc/:order_count/:upload_date",(req, res) -> insertBook(req.params(":isbn"),
                        		req.params(":title"),
                                req.params(":category"),
                                req.params(":count"),
                                req.params(":desc"),
                                req.params(":order_count"),
                                req.params(":upload_date")));
                        
                        
                        post("/import",
					(req, res) -> insertBook(req.queryParams("isbn"),req.queryParams("title"), req.queryParams("category"),
							req.queryParams("count"), req.queryParams("desc"), req.queryParams("order_count"),
							req.queryParams("upload_date")));
                        
                        get("/topViewed", (req,res) -> topBookData());
                
                        get("/topNew", (req,res) -> newBookData());
                });


		get("/hello", (req, res) -> "Hello from our Java Spark server");

                post("/goodbye", (req, res) -> {
			System.out.println("post call: " + req.queryParams("param"));
			return "";
		});
	}

	private static String userData(String id) {
		String ret = "";
		List<User> users = connection.getUserSingleOrMultiple(Integer.parseInt(id));
		Gson gson = new Gson();
		for (User u : users) {
			ret += gson.toJson(u);
		}
		return ret;
	}
        
        private static String topBookData(){
            String ret = "";
            List<Book> books = connection.getTopViewedBooks();
            Gson gson = new Gson();
            for(Book b : books){
                ret += gson.toJson(b);
            }
            
            return ret;
        }
        
        private static String newBookData(){
            String ret = "";
            List<Book> books = connection.getTopViewedBooks();
            Gson gson = new Gson();
            for(Book b : books){
                ret += gson.toJson(b);
            }
            
            return ret;
        }

	private static boolean insertBook(String isbn, String title, String category, String count, String desc, String order_count,
		String upload_date) {
		try {
                        
			return connection.importBook(new Book(Integer.parseUnsignedInt(isbn), title, category, Integer.parseInt(count), desc,
					Integer.parseInt(order_count), upload_date));
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return false;
	}

	private static boolean insertUser(String name, String username, String password, String email) {
		User user = new User(name, username, password, email);
		try {
                        System.out.println(name+" "+username+" "+password+" "+email);
			connection.importUser(user);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

}
