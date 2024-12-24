package vn.trandoananh.quanlynhahang.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import vn.trandoananh.quanlynhahang.Models.MonAn;

public class QuanLyNhaHangController {
  @FXML
  private MenuItem menuFileExit;

  @FXML
  private MenuItem menuAdvancedMenu;

  @FXML
  private MenuItem menuHelpAbout;

  @FXML
  private ComboBox<String> comboTang;

  @FXML
  private TableView<MonAn> menuTable;

  @FXML
  private TableColumn<MonAn, String> colMaMonAn;

  @FXML
  private TableColumn<MonAn, String> colTenMonAn;

  @FXML
  private TableColumn<MonAn, Integer> colDonGia;

  @FXML
  private TableColumn<MonAn, Integer> colSoLuong;

  @FXML
  private TableColumn<MonAn, Integer> colThanhTien;

  @FXML
  private Label selectedTableLabel;

  @FXML
  private HBox pnStatusBanAn;

  @FXML
  private VBox pnBanAn;

  @FXML
  private Button btnThem;

  @FXML
  private Button btnXoa;

  @FXML
  private Button btnTinhTien;

  @FXML
  private Button btnXuatThongTin;

  @FXML
  private Button btnXacNhanThanhToan;

  private String tangDaChon = "1";
  private final String banDaChon = "#";
  private final ObservableList<MonAn> thucDonData = FXCollections.observableArrayList();

  @FXML
  public void initialize() {
    initializeTableColumns();
    comboTang.setItems(FXCollections.observableArrayList("1", "2", "3"));
    comboTang.setValue("1");
    addEvents();
  }

  private void initializeTableColumns() {
    colMaMonAn.setCellValueFactory(cellData -> cellData.getValue().maMonAnProperty());
    colTenMonAn.setCellValueFactory(cellData -> cellData.getValue().tenMonAnProperty());
    colDonGia.setCellValueFactory(cellData -> cellData.getValue().donGiaProperty().asObject());
    colSoLuong.setCellValueFactory(cellData -> cellData.getValue().soLuongProperty().asObject());
    colThanhTien.setCellValueFactory(cellData -> cellData.getValue().thanhTienProperty().asObject());
    menuTable.setItems(thucDonData);
  }

  private void addEvents() {
    menuFileExit.setOnAction(_ -> System.exit(0));

    menuAdvancedMenu.setOnAction(_ -> {
      // Implement menu advanced event
      System.out.println("Advanced menu clicked");
    });

    menuHelpAbout.setOnAction(_ -> {
      // Implement help about event
      System.out.println("Help about clicked");
    });

    btnThem.setOnAction(_ -> handleAddMenuItem());
    btnXoa.setOnAction(_ -> handleRemoveMenuItem());
    btnTinhTien.setOnAction(_ -> handleCalculateTotal());
    btnXuatThongTin.setOnAction(_ -> handleExportInfo());
    btnXacNhanThanhToan.setOnAction(_ -> handleConfirmPayment());

    comboTang.setOnAction(_ -> {
      tangDaChon = comboTang.getValue();
      selectedTableLabel.setText("Bàn " + banDaChon + " Tầng " + tangDaChon);
      updateTableData();
    });
  }

  private void handleAddMenuItem() {
    showAlert("Vui lòng chọn bàn ăn trước khi thêm món!");
  }

  private void handleRemoveMenuItem() {
    MonAn selectedItem = menuTable.getSelectionModel().getSelectedItem();
    if (selectedItem != null) {
      thucDonData.remove(selectedItem);
      System.out.println("Removed item: " + selectedItem.getTenMonAn());
    } else {
      showAlert("Vui lòng chọn món ăn cần xóa!");
    }
  }

  private void handleCalculateTotal() {
    showAlert("Vui lòng chọn bàn ăn cần tính tiền!");
  }

  private void handleExportInfo() {
    showAlert("Vui lòng chọn bàn ăn cần xuất thông tin!");
  }

  private void handleConfirmPayment() {
    showAlert("Vui lòng chọn bàn cần thanh toán!");
  }

  private void updateTableData() {
    thucDonData.clear();
    // Update data based on tangDaChon and banDaChon
    System.out.println("Updating data for table " + banDaChon + " on floor " + tangDaChon);
  }

  private void showAlert(String message) {
    Alert alert = new Alert(Alert.AlertType.WARNING);
    alert.setTitle("Thông báo");
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }
}