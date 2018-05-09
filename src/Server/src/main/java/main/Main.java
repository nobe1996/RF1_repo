package main;

import static spark.Spark.port;

import server.Cors;
import server.DatabaseConnection;
import server.SQLSchema;

public class Main {

	static int PORT;

	public static void main(String[] args) throws ClassNotFoundException {
		
		if(args.length == 0){
			PORT = 8082;
		}else{
			PORT = Integer.parseInt(args[0]);
		}
		
		System.out.println(SQLSchema.FELHASZNALO);
		System.out.println(SQLSchema.SESSION);
		System.out.println(SQLSchema.INSERTBOOKS);
		
		port(PORT);
		DatabaseConnection dc = new DatabaseConnection("BookShopDB.db");
                
		dc.openConnection();
		Cors.setupDatabase(dc);
		Cors.setupRoutes();

		System.out.println("Now listening on localhost:" + PORT);
		
		//dc.destroy();
	}

}
