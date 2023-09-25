package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.jar.Attributes.Name;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ManageController implements Initializable{
	

	@FXML
	private Button updateButton, deleteButton, readButton, closeButton;

	@FXML
	private TextField usernameTextField, useridTextField, hakTextField, banTextField, bunTextField;

	@FXML
	private PasswordField pwPasswordField, pw2PasswordField;

	@FXML
	TableView<Member> memberTableView;

	@FXML
	TableColumn<Member, String> usernameTableColumn, useridTableColumn, userpwTableColumn, hakTableColumn, banTableColumn, bunTableColumn;

	@FXML
	private void updateButtonAction(ActionEvent e) {
		//체크 (빈칸, 암호일치, 비번, 학번) ==> 이상이 없으면 디비 접속, 수정
		//이상이 있으면 => 경고메세지
		if(ischeckEmpty()==true && ischeckPw()==true && ischeckNum()==true) {
			
			DBconnect conn = new DBconnect();
			Connection conn2 = conn.getConnection();
			
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setContentText(useridTextField.getText() + "님의 정보를 수정하시겠습니까?");
			Optional<ButtonType> AR = alert.showAndWait();
			if(AR.get() == ButtonType.OK) {
			
				String sql = "update user_table set USER_NAME = ?, USER_PW = ?, HAK = ?, BAN = ?, BUN = ?"
						+ " where USER_ID = ?";
				
				try {
					PreparedStatement ps = conn2.prepareStatement(sql);
					
					ps.setString(1, usernameTextField.getText());
					ps.setString(2, pwPasswordField.getText());
					ps.setString(2, pw2PasswordField.getText());
					ps.setString(3, hakTextField.getText());
					ps.setString(4, banTextField.getText());
					ps.setString(5, bunTextField.getText());
					ps.setString(6, useridTextField.getText());
					
					ResultSet rs = ps.executeQuery();
					Alert alert1 = new Alert(AlertType.INFORMATION);
					alert1.setContentText("수정하였습니다.");
					
					alert1.show();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}else {
				Alert alert1 = new Alert(AlertType.INFORMATION);
				alert1.setContentText("수정을 취소하였습니다.");
				alert1.show();
			}
			
		}else {
			if(ischeckEmpty()==false) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setContentText("빈칸이 있습니다");
				alert.show();
			}else if(ischeckPw()==false) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setContentText("비번을 확인하세요");
				alert.show();
			}else if(ischeckNum()==false) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setContentText("학번을 확인하세요");
				alert.show();
			}
			System.out.println("빈칸이있습니다");
		}
	}

	private boolean ischeckNum() {
		Boolean result = false;
		try {
			int hak = Integer.parseInt(hakTextField.getText());
			int ban = Integer.parseInt(banTextField.getText());
			int bun = Integer.parseInt(bunTextField.getText());

			if((hak >=1  && hak <=3) && (ban >=1 && ban <=12) && (bun>=1 && bun <=30)) {
				result= true;
				System.out.println("통과");
			}
		}catch (Exception e) {
		e.printStackTrace();
		System.out.println("틀림");
		}


		return result;
	}

	private boolean ischeckPw() {
		Boolean result = false;
		//첫번째 숫자와 두번째 숫자가 같다
		if(pwPasswordField.getText().equals(pw2PasswordField.getText())) {
			System.out.println("암호 일치");
			return true;
		}else {
			System.out.println("암호불일치");

		}


		return result;
	}

	private boolean ischeckEmpty() {
		Boolean result = false;
		//이름이 비어있지 않고 아이디가 암호가 비어있지 않고, 암호2도 비어있지 않고
		if(usernameTextField.getText().isEmpty()==false && pwPasswordField.getText().isEmpty()==false
				&& pw2PasswordField.getText().isEmpty()==false && hakTextField.getText().isEmpty()==false
				&& banTextField.getText().isEmpty()==false && bunTextField.getText().isEmpty()==false) {

			return true;
		}

		return result;
	}

	@FXML
    private void deleteButtonAction(ActionEvent event) {
        Alert alert1 = new Alert(AlertType.CONFIRMATION);
        alert1.setContentText(useridTextField.getText() + "님의 정보를 삭제하시겠습니까?");
        Optional<ButtonType> AR = alert1.showAndWait();
        if(AR.get() == ButtonType.OK) {
            DBconnect conn = new DBconnect();
            Connection conn2 = conn.getConnection();
            
            String sql = "DELETE FROM USER_TABLE"
                    + " where user_id=?";
            try {
                PreparedStatement ps = conn2.prepareStatement(sql);
                ps.setString(1, useridTextField.getText());
                ResultSet rs = ps.executeQuery();
            } catch (SQLException e) {
                
                e.printStackTrace();
            }
        }else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setContentText("삭제 취소하셨습니다");
            alert.show();
        }
    }
    

	@FXML
	private void memberTableViewAction(MouseEvent event) {
		//만약 테이블뷰에서 선택한 값이 비어 있지 않으면
		//테이블뷰에서 클릭한 부분의 값을 위의 콘트롤에 표시해줘

		if(memberTableView.getSelectionModel().getSelectedItem() != null) {
			usernameTextField.setText(memberTableView.getSelectionModel().getSelectedItem().getName());
			useridTextField.setText(memberTableView.getSelectionModel().getSelectedItem().getId());
			pwPasswordField.setText(memberTableView.getSelectionModel().getSelectedItem().getPw());
			pw2PasswordField.setText(memberTableView.getSelectionModel().getSelectedItem().getPw());
			hakTextField.setText(memberTableView.getSelectionModel().getSelectedItem().getHak());
			banTextField.setText(memberTableView.getSelectionModel().getSelectedItem().getBan());
			bunTextField.setText(memberTableView.getSelectionModel().getSelectedItem().getBun());
		}

	}


	@FXML
	private void readButtonAction(ActionEvent e) {
		//DB 접속
		//사용자 테이블에 있는 자료 다 가져와
		DBconnect conn = new DBconnect();
		Connection conn2 = conn.getConnection();

		String sql = "select *  from user_table order by idx";

		try {
			PreparedStatement ps = conn2.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			ObservableList<Member> memberlist = FXCollections.observableArrayList();
			while(rs.next()) {
				//어레이리스트에 값을 넣어줘
				memberlist.add(
						new Member(
								rs.getString(2),
								rs.getString(3),
								rs.getString(4),
								rs.getString(5),
								rs.getString(6),
								rs.getString(7)
								)
						);

			}
			//어레이리스트에 있는 값을 테이블 뷰에 세팅하기
			memberTableView.setItems(memberlist);



		} catch (SQLException e1) {
			e1.printStackTrace();
		}



	}
	@FXML
	private void closeButtonAction(ActionEvent e) {
		Stage stage = (Stage)closeButton.getScene().getWindow();
		stage.close();

	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		usernameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		useridTableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
		userpwTableColumn.setCellValueFactory(new PropertyValueFactory<>("pw"));
		hakTableColumn.setCellValueFactory(new PropertyValueFactory<>("hak"));
		banTableColumn.setCellValueFactory(new PropertyValueFactory<>("ban"));
		bunTableColumn.setCellValueFactory(new PropertyValueFactory<>("bun"));


	}

}
