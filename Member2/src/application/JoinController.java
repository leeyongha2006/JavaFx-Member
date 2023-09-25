package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class JoinController {

	@FXML
	Button joinButton, cancelButton, closeButton;

	@FXML
	TextField nameTextField, idTextField, hakTextField, banTextField, bunTextField;

	@FXML
	PasswordField pw1PasswordField, pw2PasswordField;

	@FXML
	private void joinButtonActionEvent(ActionEvent event) {
		// 빈칸 여부 확인
		// 아이디 중복 확인
		// 비번 2개가 일치
		// 학번 확인
		// => 위의 4가지를 확인한 결과를 저장할 변수 선언(불링형)

		Boolean checkEmpty = false;
		Boolean checkId = false;
		Boolean checkPw = false;
		Boolean checkNum = false;

		checkEmpty = ischeckEmpty();
		checkId = ischeckId();
		checkPw = ischeckPw();
		checkNum = ischeckNum();

		
		// 만약에 위의 4가지 확인 결과가 모두 true이면
		// 디비에 저장해
		// 빈칸이 있으면 ==> 경고메시지
		// 아이디 중복이면 => 경고메시지
		// 비번이 불일치하면 => 경고메시지
		// 학번이 잘못됬으면 => 경고메시지

		if (checkEmpty == true && checkId == true && checkPw == true && checkNum == true) {
			// 디비에 저장
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
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			
			
			
		} else {
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
	}

	private Boolean ischeckNum() {

		Boolean result = false;

		// 학년은 1~3사이, 반은 1~12사이, 번호는 1~30사이이면
		// result 값을 true
		// 문자와 숫자는 비교할 수 없음
		// 정수형 변수를 3개 선언(텍스트필드에서 가져온 값을 저장)

		int hak;
		int ban;
		int bun;

		try {

			hak = Integer.parseInt(hakTextField.getText());
			ban = Integer.parseInt(banTextField.getText());
			bun = Integer.parseInt(bunTextField.getText());

			if ((hak >= 1 && hak <= 3) && (ban >= 1 && ban <= 12) && (bun >= 1 && bun <= 30)) {

				result = true;
			}
		} catch (Exception e) {

		}
		return result;
	}

	private Boolean ischeckPw() {

		Boolean result = false;

		// 만약 첫번째 비번과 두번째 비번이 같으면
		// result값을 true로
		if (pw1PasswordField.getText().equals(pw2PasswordField.getText())) {
			result = true;
		}
		return result;
	}

	private Boolean ischeckId() {

		Boolean result = false;

		// 아이디 중복 체크
		// 디비 접속
		// sql(현재 id칸에 입력된 값과 동일한 자료 검색)
		// 만약 실행 결과값이 있다면 result false
		// 그렇지 않으면 result true
			DBconnect conn = new DBconnect();
			Connection conn2 = conn.getConnection();
			
			String sql = "select USER_ID" 
					+ " from USER_TABLE" 
					+ " where USER_ID = ?";
			
			try {
				PreparedStatement ps = conn2.prepareStatement(sql);
				
				ps.setString(1, idTextField.getText());
				
				ResultSet rs = ps.executeQuery();
				
				if(rs.next()) {
					result = false;
				}else {
					result = true;
				}
					
			} catch (SQLException e) {
				e.printStackTrace();
			}
		

		return result;
	}

	// 반환값 불린형(참 또는 거짓)
	private Boolean ischeckEmpty() {

		// 반환값을 저장할 변수 선언
		Boolean result = false;

		// 만약에 이름, 아이디, 비번, 비번2, 학년, 반, 번호가 비어있찌않으면
		// 변수 result 값을 true로
		if (nameTextField.getText().isEmpty() == false && idTextField.getText().isEmpty() == false
				&& pw1PasswordField.getText().isEmpty() == false && pw2PasswordField.getText().isEmpty() == false
				&& hakTextField.getText().isEmpty() == false && bunTextField.getText().isEmpty() == false
				&& banTextField.getText().isEmpty() == false) {
			result = true;
		}

		return result;
	}

	@FXML
	private void cancelButtonActionEvent(ActionEvent event) {
		// 초기화
		idTextField.setText("");
		nameTextField.setText("");
		bunTextField.setText("");
		hakTextField.setText("");
		pw1PasswordField.setText("");
		pw2PasswordField.setText("");
		banTextField.setText("");

	}

	@FXML
	private void closeButtonActionEvent(ActionEvent event) {
		// 창닫기
		Stage stage = (Stage) closeButton.getScene().getWindow();
		stage.close();

	}
}