import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import appli.schumanconnect.utils.ScenePage;
import appli.schumanconnect.utils.UserConnectedSingleton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ManagementFournitureController {

    @FXML
    private TableView<Fourniture> fournitureTable;

    @FXML
    private TableColumn<Fourniture, String> fournisseurColumn;

    @FXML
    private TableColumn<Fourniture, String> outilColumn;

    @FXML
    private TableColumn<Fourniture, Integer> quantiteColumn;

    @FXML
    public void initialize() {
        fournisseurColumn.setCellValueFactory(cellData -> cellData.getValue().fournisseurProperty());
        outilColumn.setCellValueFactory(cellData -> cellData.getValue().outilProperty());
        quantiteColumn.setCellValueFactory(cellData -> cellData.getValue().quantiteProperty().asObject());

        loadFournitures();
    }

    private void loadFournitures() {
        ObservableList<Fourniture> fournitureList = FXCollections.observableArrayList();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/schumanconnect", "root", "");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT fournisseur, outil, quantite FROM demandes_fournitures")) {

            while (resultSet.next()) {
                Fourniture fourniture = new Fourniture(
                        resultSet.getString("fournisseur"),
                        resultSet.getString("outil"),
                        resultSet.getInt("quantite")
                );
                fournitureList.add(fourniture);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        fournitureTable.setItems(fournitureList);
    }
    @FXML
    public void changePageSceneViewFournitures(ActionEvent event) throws IOException {
        ScenePage.switchView("/appli/schumanconnect/furnitureView/viewFournitures.fxml", event);
    }
}