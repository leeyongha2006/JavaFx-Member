# JavaFx-Member
JavaFX를 사용하여 회원가입 화면 구현하기
# 회원가입 화면
![image](https://github.com/leeyongha2006/JavaFx-Member/assets/126844590/5d42878c-40c7-499c-98ca-0735c06a6396)
# DBConnect
``` java
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
```
로그인 및 회원가입을 위해 데이터베이스와 연동한다
# joinController
``` java
		Boolean checkEmpty = false;
		Boolean checkId = false;
		Boolean checkPw = false;
		Boolean checkNum = false;
```
 빈칸 여부, 아이디 중복 확인, 비번 2개 일치, 학번을 확인한 결과를 저장할 변수 선언(불링형)


