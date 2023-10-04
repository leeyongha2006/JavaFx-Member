# JavaFx-Member
JavaFX를 사용하여 회원가입 화면 구현하기
# 회원가입 화면
![image](https://github.com/leeyongha2006/JavaFx-Member/assets/126844590/5d42878c-40c7-499c-98ca-0735c06a6396)
# joinController
![image](https://github.com/leeyongha2006/JavaFx-Member/assets/126844590/2580c745-fccc-4909-b0c2-49770f0d15ec)

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
``` java
private Boolean ischeckNum() {

		Boolean result = false;

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
```
hak, ban, bun을 선언하여 정수형으로 만들고  학년은 1에서3사이, 반은 1에서12사이, 번호는 1에서30사이라면 result값을 true로 된다
``` java
private Boolean ischeckPw() {

		Boolean result = false;

		if (pw1PasswordField.getText().equals(pw2PasswordField.getText())) {
			result = true;
		}
		return result;
	}
```
첫번째 비번과 두번째 비번이 같으면 result값을 true로 된다
``` java
private Boolean ischeckId() {

		Boolean result = false;

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
```
아이디 중복 체크=> 디비 접속sql(현재 id칸에 입력된 값과 동일한 자료 검색)<br>
=>만약 실행 결과값이 있다면 result false<br>
=>그렇지 않으면 result true<br>
``` java
private Boolean ischeckEmpty() {
		Boolean result = false;

		if (nameTextField.getText().isEmpty() == false && idTextField.getText().isEmpty() == false
				&& pw1PasswordField.getText().isEmpty() == false && pw2PasswordField.getText().isEmpty() == false
				&& hakTextField.getText().isEmpty() == false && bunTextField.getText().isEmpty() == false
				&& banTextField.getText().isEmpty() == false) {
			result = true;
		}
		return result;
	}
```
만약에 이름, 아이디, 비번, 비번2, 학년, 반, 번호가 비어있지 않으면변수 result 값을 true로 된다

# LoginController
관리자에 체크하면 관리자 로그인, 체크하지 않으면 사용자 로그인되는 방식이다
![image](https://github.com/leeyongha2006/JavaFx-Member/assets/126844590/409f34ab-99e9-461e-96cc-cea3ae3c6551)
``` java
if(adminCheckBox.isSelected() == true) { // 관리자 체크박스에 체크 되어있고
				if(isAdminLogin() == true) { // 메소드를 만들어서 관리자 DB에 저장되어 있는 데이터와 입력받은 데이터를 비교하여 값이 true이라면
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setContentText("관리자 로그인 성공!");
					alert.show();
					loginButton.setText("로그아웃"); // 로그인 버튼을 로그아웃 버튼으로 전환
					joinButton.setDisable(false); // 회원가입 버튼 활성화
					joinButton.setText("회원관리메뉴"); // 회원가입 버튼을 회원관리메뉴로 전환
					Adminlogin = isAdminLogin();
				}
```
![image](https://github.com/leeyongha2006/JavaFx-Member/assets/126844590/9e5eb623-f5fb-4989-a5ee-526efb6d2410)

``` java
else { // 관리자 체크박스에 체크 X
				if(isUserLogin() == true) { // 메소드를 만들어서 사용자DB에 저장되어 있는 데이터와 입력받은 데이터를 비교하여 값이 true이라면
					Main.global_id = idTextField.getText();
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setContentText("사용자 로그인 성공!");
					alert.show();
					loginButton.setText("로그아웃"); // 로그인 버튼을 로그아웃 버튼으로 전환
					joinButton.setText("회원정보수정"); // 회원가입 버튼을 회원정보수정으로 전환
					Userlogin = isUserLogin(); 
				}
```














