package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ModifyController implements Initializable{


	@FXML
	Button modifyButton, cancelButton, closeButton;
	@FXML
	TextField usernameTextField, useridTextField, hakTextField, banTextField, bunTextField;
	@FXML
	PasswordField pw1PasswordField, pw2PasswordField;
	
	@FXML
	private void modifyButtonAction(ActionEvent evnet) {
		//만약 검증(빈칸, 비번일치, 학번)을 모두 통과하면
			//디비접속==>정보수정
		//그 외에는
			//만약 빈칸 오류이면 ==>경고메세지
			//만약 비번 오류이면 ==>경고메세지
			//만약 학번 오류이면 ==>경고메세지
		
		Boolean checkEmpty1 = ischeckEmpty();
		Boolean checkNum = ischeckNum();
		Boolean checkPw = ischeckPw();
		
		if(checkEmpty1 && checkNum  && checkPw) {
			//메시지 띄우기(정말 수정하시겠습니까? ==> 대답이 2가지)
				//대답이 확인이면 ==> 디비 접속, sql 작성, 실행
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setContentText(useridTextField.getText() + "님의 정보를 수정하시겠습니까?");
			Optional<ButtonType> AR = alert.showAndWait();
			
			if(AR.get() == ButtonType.OK) {
				DBconnect conn = new DBconnect();
				Connection conn2 = conn.getConnection();
				
				String sql = "update user_table set user_name = ?, user_pw = ?, hak = ?, ban = ?, bun = ?"
						+ " where user_id = ?";
				
				try {
					PreparedStatement ps = conn2.prepareStatement(sql);
					ps.setString(1 ,usernameTextField.getText());
					ps.setString(2, pw1PasswordField.getText());
					ps.setString(3, hakTextField.getText());
					ps.setString(4, banTextField.getText());
					ps.setString(5, bunTextField.getText());
					ps.setString(6, Main.Global_userid);
					
					ResultSet rs = ps.executeQuery();
					if(rs.next()) {
						Alert alert2 = new Alert(AlertType.INFORMATION);
						alert2.setContentText(Main.Global_userid + "님의 정보 수정을 완료하였습니다");
						alert2.show();
						closeButtonAction(evnet);
}
					
					//수정이 성공하면 ==> 메시지 창 띄우기 (수정을 완료함)
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			}
			
			//디비접속, sql작성, 실행, 정보 수정
//			DBconnect conn = new DBconnect();
//			Connection conn2 = conn.getConnection();
//			
//			String sql = "update user_table set user_name = ?, user_pw = ?, hak = ?, ban = ?, bun = ?"
//					+ " where user_id = ?";
//			
//			try {
//				PreparedStatement ps = conn2.prepareStatement(sql);
//				ps.setString(1, usernameTextField.getText());
//				ps.setString(2, pw1PasswordField.getText());
//				ps.setString(3, hakTextField.getText());
//				ps.setString(4, banTextField.getText());
//				ps.setString(5, bunTextField.getText());
//				ps.setString(6, Main.Global_userid);
//				
//				ResultSet rs = ps.executeQuery();
//				if(rs.next()) {
//					
//				}
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
			
			
			
		}else {
			if(checkEmpty1==false) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setContentText("모든 칸을 입력하세요");
				alert.show();
			}else if(checkPw==false) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setContentText("비번이 틀렸습니다");
				alert.show();
			}else if(checkNum==false) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setContentText("학번이 잘못되었습니다");
				alert.show();
			}
			
		}
		
	}

	private Boolean ischeckNum() {
		Boolean result = false;
		int hak=0;
		int ban=0;
		int bun=0;
		
		
		try {
			hak=Integer.parseInt(hakTextField.getText());
			ban=Integer.parseInt(banTextField.getText());
			bun=Integer.parseInt(bunTextField.getText());
			//만약 학번이 통과되면
			//result ==> true
			
			
			if((hak >=1 && hak<=3)&&(ban>=1 && ban<=12) && (bun>=1 && bun<=30)) {
				result = true;
				
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private Boolean ischeckPw() {
		Boolean result = false;
		//만약 첫번째 비번과 두번째 비번이 같으면
			//result ==> ture
		if(pw1PasswordField.getText().equals(pw2PasswordField.getText())) {
			result = true;
			
		}
		
		
		
		
		return result;
	}
	private Boolean ischeckEmpty() {
		Boolean result = false;
		
	if(usernameTextField.getText().isEmpty()==false&&
			pw1PasswordField.getText().isEmpty()==false&&
			pw2PasswordField.getText().isEmpty()==false&&
			hakTextField.getText().isEmpty()==false&&
			banTextField.getText().isEmpty()==false&&
			bunTextField.getText().isEmpty()==false) {
		result = true;
		
	}
		
	return result;
		
	}
	@FXML
	private void cancelButtonAction(ActionEvent evnet) {
		usernameTextField.setText("");
		pw1PasswordField.setText("");
		pw2PasswordField.setText("");
		hakTextField.setText("");
		banTextField.setText("");
		bunTextField.setText("");
		
	}
	@FXML
	private void closeButtonAction(ActionEvent evnet) {
		Stage stage = (Stage)closeButton.getScene().getWindow();
		stage.close();
		
	}
	
	//회원정보수정 화면 초기화 작업
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//디비 접속
		//sql 작성(현재 로그인된 아이디에 해당하는 자료 검색)
		//sql 실행 ==> 화면에 표시
		DBconnect conn = new DBconnect();
		Connection conn2 = conn.getConnection();
		
		String sql = "select IDX, USER_NAME,USER_ID, USER_PW, HAK, BAN, BUN"
				+ " from USER_TABLE"
				+ " where USER_ID = ?";
		
		try {
			PreparedStatement ps = conn2.prepareStatement(sql);
			ps.setString(1, Main.Global_userid);
			
			ResultSet rs = ps.executeQuery();

			//만약 결과값이 있다면
				//화면의 각 콘트롤에 값표시
			if(rs.next()) {
				usernameTextField.setText(rs.getString(2));
				useridTextField.setText(rs.getString(3));
				pw1PasswordField.setText(rs.getString(4));
				pw2PasswordField.setText(rs.getString(4));
				hakTextField.setText(rs.getString(5));
				banTextField.setText(rs.getString(6));
				bunTextField.setText(rs.getString(7));
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}
	
}
