package vn.trandoananh.quanlynhahang.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import vn.trandoananh.quanlynhahang.AppQuanLyNhaHang;
import vn.trandoananh.quanlynhahang.Models.MonAn;
import vn.trandoananh.quanlynhahang.Utils.BanAnService;
import vn.trandoananh.quanlynhahang.Utils.GoiMonService;

import java.text.DecimalFormat;
import java.util.*;

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
  private Button btnThem;

  @FXML
  private Button btnXoa;

  @FXML
  private Button btnTinhTien;

  @FXML
  private Button btnXuatThongTin;

  @FXML
  private Button btnXacNhanThanhToan;

  @FXML
  private GridPane pnDsBanAn;

  private String tangDaChon = "1";
  private String banDaChon = "#";
  private final ObservableList<MonAn> thucDonData = FXCollections.observableArrayList();
  private final Map<String, HBox> tableStatusMap = new HashMap<>();

  @FXML
  public void initialize(){
    initializeTableColumns();
    comboTang.setItems(FXCollections.observableArrayList("1", "2", "3"));
    comboTang.setValue("1");
    populateTableGrid(12);
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
      DanhSachMenuController danhSachMenuController = new DanhSachMenuController();
      try {
        AppQuanLyNhaHang.showUI("/vn/trandoananh/quanlynhahang/gui/DanhSachMenu.fxml");
        danhSachMenuController.initialize();
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });

    menuHelpAbout.setOnAction(_ -> {
      AboutController aboutController = new AboutController();
      try {
        AppQuanLyNhaHang.showUI("/vn/trandoananh/quanlynhahang/gui/AboutGUI.fxml");
        aboutController.initialize();
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });

    btnThem.setOnAction(_ -> {
      try {
        handleAddMenuItem();
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
    btnXoa.setOnAction(_ -> handleRemoveMenuItem());
    btnTinhTien.setOnAction(_ -> handleCalculateTotal());
    btnXuatThongTin.setOnAction(_ -> {
      try {
        handleExportInfo();
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
    btnXacNhanThanhToan.setOnAction(_ -> handleConfirmPayment());

    comboTang.setOnAction(_ -> {
      tangDaChon = comboTang.getValue();
      selectedTableLabel.setText("Bàn " + banDaChon + " Tầng " + tangDaChon);
      updateTableData(tangDaChon,banDaChon);
      capNhatTrangThaiBanAn();
    });
  }

  private void handleAddMenuItem() throws Exception {
//    showAlert("Vui lòng chọn bàn ăn trước khi thêm món!");
    if(!banDaChon.equals("#")){
      GoiMonController goiMonController = new GoiMonController();
      AppQuanLyNhaHang.showUI("/vn/trandoananh/quanlynhahang/gui/GoiMonGUI.fxml");
      goiMonController.initialize();
      goiMonController.initData(tangDaChon,banDaChon);
    } else {
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setTitle("Cảnh báo");
      alert.setHeaderText(null);
      alert.setContentText("Vui lòng chọn bàn ăn trước khi thêm món!");
      alert.showAndWait();
    }
  }

  private void handleRemoveMenuItem() {
    // Get the selected item from the TableView
    MonAn selectedItem = menuTable.getSelectionModel().getSelectedItem();

    GoiMonService goiMonService = new GoiMonService();
    if (selectedItem != null) {
      // Call the service to remove the item from the menu
      goiMonService.xoaMonAn(tangDaChon, banDaChon, selectedItem.getMaMonAn());

      // Update the TableView by refreshing the menu
      updateTableData(tangDaChon,banDaChon);

      // Update the table or other related UI components
      capNhatTrangThaiBanAn();

      // Log the removal for debugging purposes
      System.out.println("Removed item: " + selectedItem.getTenMonAn());
    } else {
      // Show alert if no item is selected
      if (goiMonService.laySoLuongMonAn(tangDaChon, banDaChon) > 0) {
        showAlert("Vui lòng chọn món ăn cần xóa!");
      } else {
        showAlert("Không có gì để xóa!");
      }
    }
  }

  private void handleCalculateTotal() {
    if (!banDaChon.equals("#")) {
      // Get the list of menu items for the selected table and floor
      GoiMonService goiMonService = new GoiMonService();
      Vector<MonAn> dsMonAnTheoBan = goiMonService.layDsThucDonTheoBan(tangDaChon, banDaChon);

      if (!dsMonAnTheoBan.isEmpty()) {
        int tongTien = 0;
        DecimalFormat df = new DecimalFormat("#,###,##0.00");

        // Calculate the total by summing up the cost of each item
        for (MonAn monAn : dsMonAnTheoBan) {
          tongTien += monAn.getSoLuong() * monAn.getDonGia();
        }

        // Display the total amount in an alert
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText("Tổng số tiền là: " + df.format(tongTien) + " đồng.");
        alert.show();
      } else {
        // If the table is empty, show a warning
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText("Bàn ăn đang trống!");
        alert.show();
      }
    } else {
      // If no table is selected, show a warning
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setHeaderText(null);
      alert.setContentText("Vui lòng chọn bàn ăn cần tính tiền!");
      alert.show();
    }
  }

  private void handleExportInfo() throws Exception {
    if (!banDaChon.equals("#")) {
      // Get the list of menu items for the selected table and floor
      GoiMonService goiMonService = new GoiMonService();
      Vector<MonAn> dsThucDon = goiMonService.layDsThucDonTheoBan(tangDaChon, banDaChon);

      if (!dsThucDon.isEmpty()) {
        // Create and show the export information dialog
        XuatThongTinController xuatThongTinController = new XuatThongTinController();
        AppQuanLyNhaHang.showUI("/vn/trandoananh/quanlynhahang/gui/XuatThongTinGUI.fxml");
        xuatThongTinController.initialize(dsThucDon);

      } else {
        // If the table is empty, show a warning
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText("Bàn ăn đang trống!");
        alert.show();
      }
    } else {
      // If no table is selected, show a warning
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setHeaderText(null);
      alert.setContentText("Vui lòng chọn bàn ăn cần xuất thông tin!");
      alert.show();
    }
  }

  private void handleConfirmPayment() {
    if (!banDaChon.equals("#")) {
      // Check if there are items in the menu for the selected table
      if (!menuTable.getItems().isEmpty()) {
        // Display a confirmation dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm");
        alert.setHeaderText("Xác nhận đã thanh toán");
        alert.setContentText("Bạn có chắc chắn muốn thanh toán?");

        // Show the confirmation dialog and check the result
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
          // If confirmed, clear the menu items and update the table status
          GoiMonService goiMonService = new GoiMonService();
          goiMonService.xoaMonAn(tangDaChon, banDaChon);
          updateTableData(tangDaChon, banDaChon);
          capNhatTrangThaiBanAn();
        }
      } else {
        // If the table is empty, show a warning
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText("Bàn ăn đang trống!");
        alert.show();
      }
    } else {
      // If no table is selected, show a warning
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setHeaderText(null);
      alert.setContentText("Vui lòng chọn bàn cần thanh toán!");
      alert.show();
    }
  }

  private void updateTableData(String maTang, String maBan) {
    // Clear existing data from TableView
    menuTable.getItems().clear();

    // Get the list of dishes (MonAn) for the selected floor and table
    GoiMonService goiMonService = new GoiMonService();
    Vector<MonAn> dsThucDonTheoBan = goiMonService.layDsThucDonTheoBan(maTang,maBan);

    // Loop through the list of MonAn and add them to the TableView
    for (MonAn monAn : dsThucDonTheoBan) {
      // Add the MonAn object directly to the TableView
      menuTable.getItems().add(monAn);
    }

    System.out.println("Cập nhật thực đơn cho bàn " + banDaChon + " tầng " + tangDaChon);
  }

  private void showAlert(String message) {
    Alert alert = new Alert(Alert.AlertType.WARNING);
    alert.setTitle("Thông báo");
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }

  private void populateTableGrid(int numberOfTables) {
    pnDsBanAn.getChildren().clear(); // Clear existing nodes
    tableStatusMap.clear(); // Clear the map

    int columns = 3; // Number of columns in the grid
    int rowIndex = 0;
    int columnIndex = 0;

    for (int i = 1; i <= numberOfTables; i++) {
      VBox pnBanAn = new VBox();
      pnBanAn.setAlignment(Pos.CENTER);
      pnBanAn.setSpacing(5);
      pnBanAn.setPrefSize(90, 100);

      HBox pnStatusBanAn = new HBox();
      pnStatusBanAn.setAlignment(Pos.CENTER);
      pnStatusBanAn.setPrefSize(80, 20);
      pnStatusBanAn.setStyle("-fx-background-color: lightgreen;"); // Default to "active"

      pnBanAn.getChildren().add(pnStatusBanAn);

      ImageView tableIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/vn/trandoananh/quanlynhahang/images/table.png"))));
      tableIcon.setFitWidth(50);
      tableIcon.setFitHeight(50);
      tableIcon.setPreserveRatio(true);

      pnBanAn.getChildren().add(tableIcon);

      Label tableLabel = new Label("Table " + i);
      tableLabel.setFont(Font.font("System Bold", 12));
      pnBanAn.getChildren().add(tableLabel);

      String key = comboTang.getSelectionModel().getSelectedItem() + "_" + i;
      tableStatusMap.put(key, pnStatusBanAn); // Add the HBox to the map

      int finalI = i;
      pnBanAn.setOnMouseClicked(event -> handleTableSelection(finalI));

      pnDsBanAn.add(pnBanAn, columnIndex, rowIndex);

      columnIndex++;
      if (columnIndex == columns) {
        columnIndex = 0;
        rowIndex++;
      }
    }
  }

  private void capNhatTrangThaiBanAn() {
    for (int i = 1; i <= 3; i++) {
      for (int j = 1; j <= 12; j++) {
        String tang = String.valueOf(i);
        String ban = String.valueOf(j);

        // Check the number of dishes on the table
        GoiMonService goiMonService = new GoiMonService();
        int soLuongMonAn = goiMonService.laySoLuongMonAn(tang, ban);
        String trangThai = soLuongMonAn > 0 ? "busy" : "active";

        // Update the status in the backend
        BanAnService banAnService = new BanAnService();
        banAnService.setTrangThaiBanAn(tang, ban, trangThai);

        // Update the UI
        String key = tang + "_" + ban;
        if (tableStatusMap.containsKey(key)) {
          HBox pnStatus = tableStatusMap.get(key);
          if (trangThai.equals("active")) {
            pnStatus.setStyle("-fx-background-color: green;");
          } else if (trangThai.equals("busy")) {
            pnStatus.setStyle("-fx-background-color: red;");
          }
        }
      }
    }
  }

  private void handleTableSelection(int tableNumber) {
    System.out.println("Selected Table: " + tableNumber);
    // Add your handling logic here (e.g., update details, highlight selection)
    banDaChon = String.valueOf(tableNumber);
    selectedTableLabel.setText("Bàn " + banDaChon + " Tầng " + tangDaChon);
    updateTableData(tangDaChon, banDaChon);
  }
}