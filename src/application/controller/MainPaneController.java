package application.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.database.DBConnector;
import application.model.Zawodnik;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class MainPaneController {

    @FXML
    private TableView<Zawodnik> tbl_notVerified;

    @FXML
    private TableColumn<Zawodnik, Integer> col_nv_id;

    @FXML
    private TableColumn<Zawodnik, String> col_nv_name;

    @FXML
    private TableColumn<Zawodnik, String> col_nv_lastName;

    @FXML
    private TableColumn<Zawodnik, String> col_nv_weight;

    @FXML
    private TableColumn<Zawodnik, String> col_nv_club;

    @FXML
    private TableView<Zawodnik> tbl_verified;

    @FXML
    private TableColumn<Zawodnik, Integer> col_v_id;

    @FXML
    private TableColumn<Zawodnik, String> col_v_name;

    @FXML
    private TableColumn<Zawodnik, String> col_v_lastName;

    @FXML
    private TableColumn<Zawodnik, String> col_v_weight;

    @FXML
    private TableColumn<Zawodnik, String> col_v_club;

    @FXML
    private TextField tf_find;

    @FXML
    private Button bnt_find;

    @FXML
    private Button btn_clean;

    @FXML
    private TableView<?> tbl_showDivisions;

    @FXML
    private TableColumn<?, ?> col_division;

    @FXML
    private Button btn_editDivisions;

    @FXML
    private Button btn_verified;

    @FXML
    private Button btn_addPlayer;

    

    @FXML
    private Button btn_refresh;

    @FXML
    private ProgressBar progress_bar;

    @FXML
    private Button btn_quit;

    @FXML
    void addPlayer(MouseEvent event) throws IOException {
    	Stage stage = new Stage();
    	Parent parent = FXMLLoader.load(getClass().getResource("/application/view/AddPlayer1.fxml"));
    	Scene scene = new Scene(parent);
    	stage.setScene(scene);
    	stage.setTitle("Dodaj zawodnika 1/2");
    	stage.show();

    }

    @FXML
    void clear_tf_Find(MouseEvent event) {
    	tf_find.setText(null);

    }

    @FXML
    void editDivisions(MouseEvent event) {

    }

    @FXML
    void findPlayer(MouseEvent event) {

    }

    @FXML
    void quit(MouseEvent event) {
    	Runtime.getRuntime().exit(1);

    }

    
    // obiekt do przechowywania listy elementów typu Zawodnik o statusie niezweryfikowany
    private ObservableList<Zawodnik> notVerifiedPlayers = FXCollections.observableArrayList();
    
    // obiekt po³¹czenia z baz¹
    DBConnector db;
    
    @FXML
    void refresh_main_pane(MouseEvent event) {
    	Connection conn = null;
    	
    	try {
			conn = db.connection();
			PreparedStatement ps = conn.prepareStatement("select p.id_p, p.name_p, p.last_name_p, w.value_weight, c.name_club from player as p join weight_cat as w on p.id_weight = w.id_weight join club as c on p.id_club = c.id_club where p.verified = ?;");
			ps.setString(1, "0");
			ResultSet rs = ps.executeQuery();
			
			notVerifiedPlayers.clear();
			while(rs.next()) {
				notVerifiedPlayers.add(new Zawodnik(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
			}
			notVerifiedPlayers.forEach(System.out::println);
			
			col_nv_id.setCellValueFactory(new PropertyValueFactory<Zawodnik, Integer>("id"));
			col_nv_name.setCellValueFactory(new PropertyValueFactory<Zawodnik, String>("name"));
			col_nv_lastName.setCellValueFactory(new PropertyValueFactory<Zawodnik, String>("lastName"));
			col_nv_weight.setCellValueFactory(new PropertyValueFactory<Zawodnik, String>("weight"));
			col_nv_club.setCellValueFactory(new PropertyValueFactory<Zawodnik, String>("club"));
			
			tbl_notVerified.setItems(null);
			tbl_notVerified.setItems(notVerifiedPlayers);
					
			
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
    }

    
    // obiekt do przechowywania listy elementów typu Zawodnik o statusie zweryfikowany
    private ObservableList<Zawodnik> verifiedPlayers = FXCollections.observableArrayList();
    @FXML
    void verify(MouseEvent event) {
    	Integer id = tbl_notVerified.getSelectionModel().getSelectedItem().getId();
    	Connection conn = null;
    			
    	try {
			conn = db.connection();
			PreparedStatement ps = conn.prepareStatement("UPDATE player SET verified=? WHERE id_p= ?;");
			ps.setString(1, "1");
			ps.setString(2, id.toString());
			ps.executeUpdate();
			
			refresh_main_pane(event);
			
			showVerifiedPlayers(conn);
			
			
			
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
    	

    }

	private void showVerifiedPlayers(Connection conn) throws SQLException {
		PreparedStatement ps2 = conn.prepareStatement("select p.id_p, p.name_p, p.last_name_p, w.value_weight, c.name_club from player as p join weight_cat as w on p.id_weight = w.id_weight join club as c on p.id_club = c.id_club where p.verified = ?;");
		ps2.setString(1, "1");
		ResultSet rs = ps2.executeQuery();
		
		verifiedPlayers.clear();
		while(rs.next()) {
			verifiedPlayers.add(new Zawodnik(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
		}
		
		col_v_id.setCellValueFactory(new PropertyValueFactory<Zawodnik, Integer>("id"));
		col_v_name.setCellValueFactory(new PropertyValueFactory<Zawodnik, String>("name"));
		col_v_lastName.setCellValueFactory(new PropertyValueFactory<Zawodnik, String>("lastName"));
		col_v_weight.setCellValueFactory(new PropertyValueFactory<Zawodnik, String>("weight"));
		col_v_club.setCellValueFactory(new PropertyValueFactory<Zawodnik, String>("club"));
		
		tbl_verified.setItems(null);
		tbl_verified.setItems(verifiedPlayers);
	}
	
    public void initialize() throws SQLException {
    	db = new DBConnector();
 
    }

}
