package application.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.database.DBConnector;
import application.model.Club;
import application.model.Weight;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class AddPlayer1Controller {
	DBConnector db;

    @FXML
    private TextField tf_name;

    @FXML
    private TextField tf_lastName;

    ObservableList<Weight> weights = FXCollections.observableArrayList();
    
    
    @FXML
    private ComboBox<Weight> combo_weight;

    
    ObservableList<Club> club = FXCollections.observableArrayList();
    @FXML
    private ComboBox<Club> combo_club;

    @FXML
    private Button btn_nextStep;

    @FXML
    private Button btn_cancel;
    
    @FXML
    void closePane(MouseEvent event) {
    	
    }

    @FXML
    void nextStep(MouseEvent event) throws IOException {
    	Parent parent= FXMLLoader.load(getClass().getResource("/application/view/AddPlayer2.fxml"));
    	Scene scene = new Scene(parent);
    	MainPaneController.getStage().setScene(scene);
    	
    	((Node)event.getSource()).getScene().getWindow().hide();
    }
    
    private void getWeights() {
    	Connection connection = null;
    			
    			try {
					connection=db.connection();
					 
					PreparedStatement ps = connection.prepareStatement("SELECT value_weight FROM weight_cat");
					ResultSet rs = ps.executeQuery();
					
					while (rs.next()) {
						weights.add(new Weight(rs.getString(1)));
					}
					
					combo_weight.setItems(weights);
					
				} catch (SQLException e) {
					e.printStackTrace();
				}
    }
    
    private void getClubs() {
    	Connection connection = null;
    		
    	try {
    		connection = db.connection();
    		PreparedStatement ps = connection.prepareStatement("SELECT name_club from club order by name_club asc;");
    		ResultSet rs = ps.executeQuery();
    		
    		while(rs.next()) {
    			club.add(new Club(rs.getString(1)));
    		}
    		
    		combo_club.setItems(club);
    		
    	}catch (SQLException e) {
    		e.printStackTrace();
    	}finally {
    		if(connection!= null) {
    			try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
    		}
    	}
    }
    
    public void initialize() {
    	db = new DBConnector();
    	getWeights();
    	getClubs();
    }

}



