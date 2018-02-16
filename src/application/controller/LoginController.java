package application.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.Main;
import application.database.DBConnector;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField tf_login;

    @FXML
    private PasswordField pf_pass;

    @FXML
    private Button btn_login;

    @FXML
    private Button btn_close;

    @FXML
    void close(MouseEvent event) {
    	Runtime.getRuntime().exit(1);
    }
    
   
    @FXML
    void doLogin(KeyEvent event) throws SQLException, IOException {

    	if(event.getCode().equals(KeyCode.ENTER)) {
    		doLogin();
    	}
    }

    
    public static String user_group;
    public static String login;
    DBConnector db;
    
    @FXML
    void login(MouseEvent event) throws SQLException, IOException {
    	doLogin();


    }


	private void doLogin() throws SQLException, IOException {
		Connection con = db.connection();
    	String sql = "SELECT user_group FROM user where login=? and pass=?;";
    	PreparedStatement ps = con.prepareStatement(sql);
    	ps.setString(1, tf_login.getText());
    	ps.setString(2, pf_pass.getText());
    	ResultSet rs = ps.executeQuery();
    	
    	if(rs.next()){
    		user_group = rs.getString("user_group");
    		login = tf_login.getText();
    		
    		Stage stage = Main.getPrimaryStage();
    		Parent parent = FXMLLoader.load(getClass().getResource("/application/view/MainPane.fxml"));
    		Scene scene = new Scene(parent);
    		stage.setScene(scene);
    		stage.setTitle("Weryfikacja zawodników");
    		stage.show();
    		Main.getPrimaryStage().setScene(scene);
    		
    	if (con != null) {
    		con.close();
    	}
    		
    		
    	} else {
    		Alert error = new Alert(AlertType.ERROR);
    		error.setHeaderText("B³¹d!");
    		error.setContentText("Nieprawid³owy login lub has³o");
    		error.setTitle("Nale¿y podaæ poprawne dane logowania");
    		error.showAndWait();
    	}
	}


    public void initialize() {
    	db = new DBConnector();
    }

}
