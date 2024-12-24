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
import javafx.scene.text.TextAlignment;
import vn.trandoananh.quanlynhahang.Models.MonAn;
import vn.trandoananh.quanlynhahang.Utils.BanAnService;
import vn.trandoananh.quanlynhahang.Utils.GoiMonService;

import java.util.Objects;
import java.util.Vector;

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
  private VBox floorAndTablesPanel;

  @FXML
  private Label[] banAn;

  private String tangDaChon = "1";
  private final String banDaChon = "#";
  private final ObservableList<MonAn> thucDonData = FXCollections.observableArrayList();

  @FXML
  public void initialize() {
    initializeTableColumns();
    comboTang.setItems(FXCollections.observableArrayList("1", "2", "3"));
    comboTang.setValue("1");
    setupBanAnPane();
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
      capNhatTrangThaiBanAn();
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
    // Clear existing data from TableView
    menuTable.getItems().clear();

    // Get the list of dishes (MonAn) for the selected floor and table
    GoiMonService goiMonService = new GoiMonService();
    Vector<MonAn> dsThucDonTheoBan = goiMonService.layDsThucDonTheoBan(tangDaChon, banDaChon);

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

  private void setupBanAnPane() {
    GridPane pnDsBanAn = new GridPane();
    pnDsBanAn.setHgap(10); // Khoảng cách ngang giữa các phần tử
    pnDsBanAn.setVgap(10); // Khoảng cách dọc giữa các phần tử
    pnDsBanAn.setAlignment(Pos.CENTER); // Căn giữa toàn bộ bảng trong container

    VBox[] pnBanAn = new VBox[12];
    HBox[] pnStatusBanAn = new HBox[12]; // Cập nhật từ Pane[] sang VBox[]
    banAn = new Label[12];

    for (int i = 0; i < 12; i++) {
      banAn[i] = new Label("Bàn " + (i + 1));
      banAn[i].setGraphic(new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/vn/trandoananh/images/table.png")))));
      banAn[i].setStyle("-fx-background-color: transparent;");
      banAn[i].setContentDisplay(ContentDisplay.TOP);
      banAn[i].setTextAlignment(TextAlignment.CENTER);

      // Tạo HBox cho trạng thái bàn
      pnStatusBanAn[i] = new HBox();
      pnStatusBanAn[i].setPrefHeight(10); // Chiều cao cố định

      // Tạo VBox cho mỗi bàn ăn
      pnBanAn[i] = new VBox();
      pnBanAn[i].setSpacing(5); // Khoảng cách giữa các phần tử trong VBox
      pnBanAn[i].setAlignment(Pos.CENTER); // Căn giữa nội dung trong VBox
      pnBanAn[i].setStyle("-fx-border-color: lightgray; -fx-border-width: 2; -fx-border-style: solid;");
      pnBanAn[i].getChildren().addAll(pnStatusBanAn[i], banAn[i]);

      // Thêm VBox vào GridPane
      int row = i / 4; // 4 bàn mỗi hàng
      int col = i % 4;
      pnDsBanAn.add(pnBanAn[i], col, row);
    }

    // Gán GridPane vào một khu vực trên giao diện chính
    floorAndTablesPanel.getChildren().add(pnDsBanAn);

    // Cập nhật trạng thái bàn ăn
    capNhatTrangThaiBanAn();
  }

  private void capNhatTrangThaiBanAn() {
    // Duyệt qua các tầng
    for (int i = 1; i <= 3; i++) {
      for (int j = 1; j <= 12; j++) {
        String tang = String.valueOf(i);
        String ban = String.valueOf(j);

        // Kiểm tra số lượng món ăn trên bàn
        GoiMonService goiMonService = new GoiMonService();
        int soLuongMonAn = goiMonService.laySoLuongMonAn(tang, ban);
        String trangThai = soLuongMonAn > 0 ? "busy" : "active";

        // Cập nhật trạng thái bàn ăn
        BanAnService banAnService = new BanAnService();
        banAnService.setTrangThaiBanAn(tang, ban, trangThai);

        // Cập nhật giao diện
        if (comboTang.getSelectionModel().getSelectedItem().equals(tang)) {
          HBox[] pnStatusBanAn = new HBox[12];
          HBox pnStatus = pnStatusBanAn[j - 1];
          if (trangThai.equals("active")) {
            pnStatus.setStyle("-fx-background-color: green;");
          } else if (trangThai.equals("busy")) {
            pnStatus.setStyle("-fx-background-color: red;");
          }
        }
      }
    }
  }
}