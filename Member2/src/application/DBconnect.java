package application;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBconnect {
	
	public Connection conn;
	
	public Connection getConnection() {
		
		String driver="oracle.jdbc.driver.OracleDriver";
		String url ="jdbc:oracle:thin:@localhost:1521:xe";
		String id = "school";
		String pw="school";
		
		try {
			Class.forName(driver);
			conn=DriverManager.getConnection(url, id, pw);
			System.out.println("성공");
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("실패");
		}
		return conn;
		
		
	}

}
