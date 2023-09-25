package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
	
	
	
	// 로그인 성공 여부를 저장할 변수
	Boolean AdminLogin = false; 
	Boolean UserLogin = false; 
	
	@FXML private Button loginButton, joinButton, cancelButton, closeButton;
	@FXML private CheckBox adminCheckBox;
	@FXML private TextField idTextField;
	@FXML private PasswordField pwPasswordField;
	
	
	@FXML
	private void loginButtonAction(ActionEvent event) {
	// 만약에 loginButton에 로그인이라고 되어 있으면
		// 만약에 아이디 또는 비번이 빈칸이면{
			// 경고메세지 }
		// 그 외에는 {
			// 관리자에 체크가 되어 있으면
					// 관리자 로그인하는 메소드 호출
					//isAdminLogin()
				// 그 외에는
					// 사용자 로그인하는 메소드 호출 }
					// isUserLogin()
	// 그 외에는 로그아웃해줘
		if(loginButton.getText().equals("로그인")) {
			if(idTextField.getText().isBlank() || pwPasswordField.getText().isBlank()) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("경고메세지");
				alert.setContentText("아이디, 비번 다시 입력하세요.");
				alert.show();
				
			} else {
				if(adminCheckBox.isSelected() == true) {
					// 관리자 로그인 메소드 호출 ==> 반환값 받기
					AdminLogin = isAdminLogin();
					
				} else {
					UserLogin = isUserLogin();
				}
			}
		
	} else {
		// 로그아웃 메소드 호출 Logout()
			// 로그인으로 글자 변경
			// 아이디, 비번 초기화
			// 체크박스 풀기
			// adminCheckBox ==> 회원가입으로
			// 메세지 창(로그아웃되었습니다.)
		Logout();
		}
	}
	
	public void Logout() {
		AdminLogin = false;
		UserLogin = false;
		loginButton.setText("로그인");
		idTextField.setText("");
		pwPasswordField.setText("");
		adminCheckBox.setSelected(false);
		joinButton.setText("회원가입");
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("경고메세지");
		alert.setContentText("로그아웃 되었습니다.");
		alert.show();
		Main.Global_userid = "";
		
	}
	
	public Boolean isUserLogin() {
		Boolean result = false;
		
		DBconnect conn = new DBconnect();
		Connection conn2 = conn.getConnection();
		
		String sql = "select user_id, user_pw"
				+ " from user_table"
				+ " where user_id = ? and user_pw = ?";
		
		try {
			PreparedStatement ps = conn2.prepareStatement(sql);
			
			ps.setString(1, idTextField.getText());
			ps.setString(2, pwPasswordField.getText());
			
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				result = true;
				loginButton.setText("로그아웃");
				joinButton.setText("회원정보수정");
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("메세지");
				alert.setContentText("사용자 로그인 성공.");
				alert.show();
				Main.Global_userid = idTextField.getText();
				
			} else {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("메세지");
				alert.setContentText("사용자 로그인 실패.");
				alert.show();
			}
			
			rs.close();
			ps.close();
			conn2.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	
		return result;
		
	}

	public Boolean isAdminLogin() {
		// result는 로그인 성공여부를 저장하는 변수
		// true 로그인 성공
		// false 로그인 안됨
		Boolean result = false;
		// DB 접속, sql문, 쿼리 실행
		
		DBconnect conn = new DBconnect();
		Connection conn2 = conn.getConnection();
		
		String sql = "select admin_id, admin_pw"
				+ " from admin_table"
				+ " where admin_id = ? and admin_pw = ?";
		
		try {
			PreparedStatement ps = conn2.prepareStatement(sql);
			
			ps.setString(1, idTextField.getText());
			ps.setString(2, pwPasswordField.getText());
			
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				result = true;
				// AdminLogin = true;
				loginButton.setText("로그아웃");
				joinButton.setDisable(false);
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("메세지");
				alert.setContentText("관리자 로그인 성공.");
				alert.show();
				
			} else {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("메세지");
				alert.setContentText("관리자 로그인 실패.");
				alert.show();
			}
			
			rs.close();
			ps.close();
			conn2.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	
		return result; // 관리자 로그인 성공여부를 반환

	}
	
	@FXML
	private void joinButtonAction(ActionEvent event) {
		// if 관리자에 체크가 되어 있으면
			// 로그인 상태이면(AdminLogin == True) ==> 회원관리 창 띄우기
			// 로그아웃 상태이면 ==> 아무것도 안 띄우기
		// else 그 외에는(사용자 상태)
			// 로그인 상태이면(UserLogin == True) ==> 회원정보 띄우기
			// 로그아웃 상태이면 ==> 회원가입 띄우기
		
		if(adminCheckBox.isSelected() == true) {
			if(AdminLogin == true) {
				try {
					Parent root = FXMLLoader.load(getClass().getResource("manage.fxml"));
					Scene scene = new Scene(root);
					Stage stage = new Stage();
					stage.setScene(scene);
					stage.show();
					stage.setTitle("회원가입 화면");
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
			
		} else {
			if(UserLogin == true) {
				try {
					Parent root = FXMLLoader.load(getClass().getResource("modify.fxml"));
					Scene scene = new Scene(root);
					Stage stage = new Stage();
					stage.setScene(scene);
					stage.show();
					stage.setTitle("회원정보 화면");
				} catch(Exception e) {
					e.printStackTrace();
				}
				
			} else {
				try {
					Parent root = FXMLLoader.load(getClass().getResource("join.fxml"));
					Scene scene = new Scene(root);
					Stage stage = new Stage();
					stage.setScene(scene);
					stage.show();
					stage.setTitle("회원가입");
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
			
			
		}
		
	}
	
	@FXML
	private void cancelButtonAction(ActionEvent event) {
		idTextField.setText("");
		pwPasswordField.setText("");
	}
	
	@FXML
	private void closeButtonAction(ActionEvent event) {
		Stage stage = (Stage)closeButton.getScene().getWindow();
		stage.close();
		
		
	}
	
	@FXML
	private void adminCheckBoxAction(ActionEvent event) {
		// 만약 관리자에 check가 되어 있으면
			// joinButton ==> 관리자 회원관리 세팅
			// joinButton을 비활성화
		// 그 외에는 
			// joinButton ==> 회원가입 세팅
			// joinButton 활성화
		
		if(adminCheckBox.isSelected() == true) {
			joinButton.setText("회원관리메뉴");
			joinButton.setDisable(true);
			
		} else {
			joinButton.setText("회원가입");
			joinButton.setDisable(false);
		}
		
	}
}