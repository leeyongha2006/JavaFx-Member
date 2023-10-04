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
 ``` java
		checkEmpty = ischeckEmpty();
		checkId = ischeckId();
		checkPw = ischeckPw();
		checkNum = ischeckNum();	
```
메소드를 선언한다
``` java
	if (checkEmpty == true && checkId == true && checkPw == true && checkNum == true) {
			DBconnect conn = new DBconnect();
			Connection conn2 = conn.getConnection();
			
			String sql = "insert into user_table(idx,USER_NAME,USER_ID,USER_PW,hak,BAN,bun)values(user_idx_pk.nextval, ? , ? , ? , ? , ? , ?)";
			
			try {
				PreparedStatement ps = conn2.prepareStatement(sql);
				ps.setString(1, nameTextField.getText());
				ps.setString(2, idTextField.getText());
				ps.setString(3, pw1PasswordField.getText());
				ps.setString(4, hakTextField.getText());
				ps.setString(5, banTextField.getText());
				ps.setString(6, bunTextField.getText());
				
				ResultSet rs = ps.executeQuery();
```
위의 4가지 확인 결과가 모두 true이면 DB에 저장한 후 값을 넣어주는 쿼리문을 작성한다
``` java
if(rs.next()) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setContentText("회원가입이 완료되었습니다");
					alert.show();
					
					Stage stage = (Stage)closeButton.getScene().getWindow();
					stage.close();
				}
				rs.close();
				ps.close();
				conn2.close();
```
회원가입이 완료되었다는 알림창을 띄우고 현재 띄워져있는 창을 닫는다 
``` javascript
else {
			if (checkEmpty == false) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setContentText("모든 항목을 입력하세요");
				alert.show();
			} else if (checkId == false) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setContentText("이미 사용중인 아이디입니다");
				alert.show();
			} else if (checkPw == false) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setContentText("암호를 확인하세요");
				alert.show();
			} else if (checkNum == false) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setContentText("학번이 잘못되었습니다");
				alert.show();
			}
		}
```
빈칸이 있거나 아이디 중복,비번 불일치, 학번이 잘못된 경우에는 경고메시지를 띄운다





