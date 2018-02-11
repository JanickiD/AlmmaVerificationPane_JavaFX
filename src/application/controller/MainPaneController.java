package application.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.database.DBConnector;
import application.model.Club;
import application.model.Division;
import application.model.Zawodnik;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;

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

	// obiekt do przechowywania listy dywizji
	ObservableList<Division> division = FXCollections.observableArrayList();
	@FXML
	private TableView<Division> tbl_showDivisions;

	@FXML
	private TableColumn<Division, String> col_division;

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

	// obiekt przechowuj�cy stage dla scen "dodaj zawodnika"
	private static Stage stage;

	public static Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		MainPaneController.stage = stage;
	}

	// akcja na przycisku "dodaj zawodnika"
	@FXML
	void addPlayer(MouseEvent event) throws IOException {
		Stage stage = new Stage();
		Parent parent = FXMLLoader.load(getClass().getResource("/application/view/AddPlayer1.fxml"));
		Scene scene = new Scene(parent);
		stage.setScene(scene);
		stage.setTitle("Dodaj zawodnika 1/2");
		stage.show();

	}

	// akcja na przycisku "wyczy��"
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

	// akcja na przycisku "zamknij program"
	@FXML
	void quit(MouseEvent event) {
		Runtime.getRuntime().exit(1);

	}

	// obiekt do przechowywania listy element�w typu Zawodnik o statusie
	// niezweryfikowany
	private ObservableList<Zawodnik> notVerifiedPlayers = FXCollections.observableArrayList();

	// obiekt po��czenia z baz�
	DBConnector db;

	// obiekt przechowuj�cy ID zawodnika zaznaczonego w
	// zweryfikowanu/niezweryfikowany
	private Integer id = null;

	// akcja na przycisku "Od�wie� okno"
	@FXML
	void refresh_main_pane(MouseEvent event) throws SQLException {
		show_notVerified_Players();
		showVerifiedPlayers();
	}

	private void show_notVerified_Players() {
		Connection conn = null;
		notVerifiedPlayers.clear();
		try {
			conn = db.connection();
			PreparedStatement ps = conn.prepareStatement(
					"select p.id_p, p.name_p, p.last_name_p, w.value_weight, c.name_club from player as p join weight_cat as w on p.id_weight = w.id_weight join club as c on p.id_club = c.id_club where p.verified = ?;");
			ps.setString(1, "0");
			ResultSet rs = ps.executeQuery();

			notVerifiedPlayers.clear();
			while (rs.next()) {
				notVerifiedPlayers.add(
						new Zawodnik(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
			}

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
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// obiekt do przechowywania listy element�w typu Zawodnik o statusie
	// zweryfikowany
	private ObservableList<Zawodnik> verifiedPlayers = FXCollections.observableArrayList();

	private ObservableList<String> weights;

	@FXML
	void verify(MouseEvent event) {
		Connection conn = null;

		try {
			conn = db.connection();
			PreparedStatement ps = conn.prepareStatement("UPDATE player SET verified=? WHERE id_p= ?;");
			ps.setString(1, "1");
			ps.setString(2, id.toString());
			ps.executeUpdate();

			refresh_main_pane(event);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void showVerifiedPlayers() {
		Connection connection = null;

		try {
			connection = db.connection();
			PreparedStatement ps2 = connection.prepareStatement(
					"select p.id_p, p.name_p, p.last_name_p, w.value_weight, c.name_club from player as p join weight_cat as w on p.id_weight = w.id_weight join club as c on p.id_club = c.id_club where p.verified = ?;");
			ps2.setString(1, "1");
			ResultSet rs = ps2.executeQuery();

			verifiedPlayers.clear();
			while (rs.next()) {
				verifiedPlayers.add(
						new Zawodnik(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
			}

			col_v_id.setCellValueFactory(new PropertyValueFactory<Zawodnik, Integer>("id"));
			col_v_name.setCellValueFactory(new PropertyValueFactory<Zawodnik, String>("name"));
			col_v_lastName.setCellValueFactory(new PropertyValueFactory<Zawodnik, String>("lastName"));
			col_v_weight.setCellValueFactory(new PropertyValueFactory<Zawodnik, String>("weight"));
			col_v_club.setCellValueFactory(new PropertyValueFactory<Zawodnik, String>("club"));

			tbl_verified.setItems(null);
			tbl_verified.setItems(verifiedPlayers);
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

	// obiekt do przechowywania listy dywizji w kt�rych walczy zaznaczony w tabeli
	// notVerified zawodnik
	private void showDivision() {
		Connection connection = null;
		division.clear();
		try {

			connection = db.connection();
			PreparedStatement ps = connection.prepareStatement(
					"SELECT c.name_cat FROM category as c join category_has_player as ch on c.id_cat = ch.category_id_cat where ch.player_id_p = ?");
			ps.setString(1, id.toString());
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				division.add(new Division(rs.getString(1)));
			}

			col_division.setCellValueFactory(new PropertyValueFactory<Division, String>("division"));

			tbl_showDivisions.setItems(null);
			tbl_showDivisions.setItems(division);

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

	@FXML
	void getSelectedPlayerID(MouseEvent event) {
		id = null;
		TableView<Zawodnik> source = (TableView<Zawodnik>) event.getSource();
		if ("tbl_notVerified".equals(source.getId())) {
			id = tbl_notVerified.getSelectionModel().getSelectedItem().getId();
		} else {
			id = tbl_verified.getSelectionModel().getSelectedItem().getId();
		}
		showDivision();
	}

	public void initialize() throws SQLException {
		db = new DBConnector();
		showVerifiedPlayers();
		show_notVerified_Players();
		tbl_notVerified.setEditable(true);
		editCalls();

	}

	private void editCalls() {
		// edycja kolumny name
		editNameCell();

		// edycja kolumny last name
		editLastNameCell();

		// edycja kolumny weight
		editWeightCell();

		// edycja kolumny club
		editClubCell();
	}

	private void editNameCell() {
		col_nv_name.setCellFactory(TextFieldTableCell.forTableColumn());
		col_nv_name.setOnEditCommit(new EventHandler<CellEditEvent<Zawodnik, String>>() {
			@Override
			public void handle(CellEditEvent<Zawodnik, String> t) {
				((Zawodnik) t.getTableView().getItems().get(t.getTablePosition().getRow())).setName(t.getNewValue());

				Zawodnik selectedItem = tbl_notVerified.getSelectionModel().getSelectedItem();
				// update wiersza po edycji pola
				updateCell(selectedItem);
			}
		});
	}

	private void editLastNameCell() {
		col_nv_lastName.setCellFactory(TextFieldTableCell.forTableColumn());
		col_nv_lastName.setOnEditCommit(new EventHandler<CellEditEvent<Zawodnik, String>>() {
			@Override
			public void handle(CellEditEvent<Zawodnik, String> event) {
				((Zawodnik) event.getTableView().getItems().get(event.getTablePosition().getRow()))
						.setLastName(event.getNewValue());

				Zawodnik selectedItem = tbl_notVerified.getSelectionModel().getSelectedItem();
				updateCell(selectedItem);
			}
		});
	}

	private ObservableList<String> weightList() {
		ObservableList<String> weights = FXCollections.observableArrayList();
		Connection connection = null;

		try {
			connection = db.connection();

			PreparedStatement ps = connection.prepareStatement("SELECT value_weight FROM weight_cat");
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				weights.add(rs.getString(1));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return weights;
	}

	private void editWeightCell() {
		weights = FXCollections.observableArrayList();
		weights = weightList();

		col_nv_weight.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Zawodnik, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<Zawodnik, String> param) {
						Zawodnik value = param.getValue();
						String weight = value.getWeight();
						return new SimpleObjectProperty<String>(weight);
					}
				});

		col_nv_weight.setCellFactory(ComboBoxTableCell.forTableColumn(weights));
		col_nv_weight.setOnEditCommit((CellEditEvent<Zawodnik, String> event) -> {
			TablePosition<Zawodnik, String> pos = event.getTablePosition();

			String newValue = event.getNewValue();
			int row = pos.getRow();
			Zawodnik zawodnik = event.getTableView().getItems().get(row);
			zawodnik.setWeight(newValue);

			updateCell(zawodnik);
		});
	}

	private ObservableList<String> getClubs() {
		ObservableList<String> clubs = FXCollections.observableArrayList();
		Connection connection = null;

		try {
			connection = db.connection();
			PreparedStatement ps = connection.prepareStatement("SELECT name_club from club order by name_club asc;");
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				clubs.add(rs.getString(1));
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
		return clubs;
	}

	private void editClubCell() {
		ObservableList<String> clubs = FXCollections.observableArrayList();
		clubs = getClubs();

		col_nv_club.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Zawodnik, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<Zawodnik, String> param) {
						Zawodnik value = param.getValue();

						String club = value.getClub();
						return new SimpleObjectProperty<String>(club);
					}
				});
		col_nv_club.setCellFactory(ComboBoxTableCell.forTableColumn(clubs));
		col_nv_club.setOnEditCommit((CellEditEvent<Zawodnik, String> event) -> {
			TablePosition<Zawodnik, String> tablePosition = event.getTablePosition();
			String newValue = event.getNewValue();
			int row = tablePosition.getRow();
			Zawodnik zawodnik = event.getTableView().getItems().get(row);
			zawodnik.setClub(newValue);

			updateCell(zawodnik);
		});

	}

	private void updateCell(Zawodnik selectedItem) {
		Connection connection = null;
		try {
			connection = db.connection();

			// pobranie id wagi
			PreparedStatement weightPS = connection
					.prepareStatement("SELECT id_weight from weight_cat WHERE value_weight = ?");
			weightPS.setString(1, selectedItem.getWeight());
			ResultSet weightRs = weightPS.executeQuery();
			String weight_id = null;
			if (weightRs.next()) {
				weight_id = weightRs.getString(1);
			}

			PreparedStatement clubPS = connection.prepareStatement("SELECT id_club FROM club WHERE name_club = ?");
			clubPS.setString(1, selectedItem.getClub());
			ResultSet club_id = clubPS.executeQuery();
			String id_club = null;
			if (club_id.next()) {
				id_club = club_id.getString(1);
			}

			PreparedStatement mainPS = connection.prepareStatement(
					"UPDATE player SET id_p = ?, name_p = ?, last_name_p = ?, id_weight = ?, id_club = ? WHERE id_p = ?");
			mainPS.setInt(1, selectedItem.getId());
			mainPS.setString(2, selectedItem.getName());
			mainPS.setString(3, selectedItem.getLastName());
			mainPS.setString(4, weight_id);
			mainPS.setString(5, id_club);
			mainPS.setInt(6, selectedItem.getId());

			mainPS.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
