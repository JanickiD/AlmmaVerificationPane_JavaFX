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
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
		Connection connection = null;
		String name = tf_name.getText();
		String lastName = tf_lastName.getText();
		Club club = combo_club.getValue();
		Weight weigh = combo_weight.getValue();

		try {
			connection = db.connection();

			// get id_weight
			String value_veight = weigh.toString();
			String id_weight = MainPaneController.getID_weight(connection, value_veight);

			// get id_club
			String club_name = club.toString();
			String id_club = getId_club(connection, club_name);

			// input
			if (!"".equals(name) && !"".equals(lastName) && club != null && weigh != null) {
				PreparedStatement ps = connection.prepareStatement(
						"INSERT INTO `almma`.`player` (`name_p`, `last_name_p`, `id_weight`, `id_club`, `verified`) VALUES (?, ?, ?, ?, ?);");
				ps.setString(1, name);
				ps.setString(2, lastName);
				ps.setString(3, id_weight);
				ps.setString(4, id_club);
				ps.setString(5, "0");

				ps.executeUpdate();

				Alert info = new Alert(AlertType.INFORMATION);
				info.setHeaderText("Zawodnik dodany poprawnie!");
				info.setContentText("Zdefiniuj dywizje w których walczy dodany zawodnik");
				info.setTitle("Powodzenie");
				info.showAndWait();

				((Node) event.getSource()).getScene().getWindow().hide();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private String getId_club(Connection connection, String club_name) throws SQLException {
		String id_club = null;
		PreparedStatement ps = connection.prepareStatement("SELECT id_club FROM club WHERE name_club = ?");
		ps.setString(1, club_name);
		ResultSet rs = ps.executeQuery();

		if (rs.next()) {
			id_club = rs.getString(1);
		}

		return id_club;
	}

	private void getWeights() {
		Connection connection = null;

		try {
			connection = db.connection();
			PreparedStatement ps = connection.prepareStatement("SELECT value_weight FROM weight_cat");
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				weights.add(new Weight(rs.getString(1)));
			}

			combo_weight.setItems(weights);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		}
	}

	private void getClubs() {
		Connection connection = null;

		try {
			connection = db.connection();
			PreparedStatement ps = connection.prepareStatement("SELECT name_club from club order by name_club asc;");
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				club.add(new Club(rs.getString(1)));
			}

			combo_club.setItems(club);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
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
