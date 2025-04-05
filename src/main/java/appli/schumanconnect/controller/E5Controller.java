package appli.schumanconnect.controller;

import appli.schumanconnect.model.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

@FXML
private TableView<User> varTable;

@FXML
private TableColumn<User, String> col1;

@FXML
private TableColumn<User, String> col2;

private ObservableList<User> user_list;

public class E5Controller implements Initializable {


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.user_list = UserRepository.getAllUser();
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
        this.col1.setCellValueFactory(new PropertyValueFactory<>("id_user"));
        this.col2.setCellValueFactory(new PropertyValueFactory<>("nom"));



}
    }
}
