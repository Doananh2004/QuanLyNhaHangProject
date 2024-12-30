package vn.trandoananh.quanlynhahang.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import vn.trandoananh.quanlynhahang.AppQuanLyNhaHang;
import vn.trandoananh.quanlynhahang.Models.MonAn;
import vn.trandoananh.quanlynhahang.Models.data;
import vn.trandoananh.quanlynhahang.Utils.BanAnService;
import vn.trandoananh.quanlynhahang.Utils.GoiMonService;
import vn.trandoananh.quanlynhahang.Utils.MenuService;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Vector;

public class GoiMonController implements Initializable {
  @FXML
  private TextField txtTimKiem;

  @FXML
  private TextField txtSoLuong;

  @FXML
  private Button btnThem;

  @FXML
  private TableView<MonAn> tblDsMenuNhaHang;

  @FXML
  private TableColumn<MonAn, String> colMaMonAn;

  @FXML
  private TableColumn<MonAn, String> colTenMonAn;

  @FXML
  private TableColumn<MonAn, Integer> colDonGia;

  @FXML
  private Button btnVeTrangChu;


  private final MenuService menuService = new MenuService();
  private final GoiMonService goiMonService = new GoiMonService();
  private final BanAnService banAnService = new BanAnService();

  private String maTang;
  private String maBan;

  private final ObservableList<MonAn> dsMonAn = FXCollections.observableArrayList();

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    initData(data.maTang, data.maBan);
    hienThiMenuNhaHang();
    colMaMonAn.setCellValueFactory(new PropertyValueFactory<>("maMonAn"));
    colTenMonAn.setCellValueFactory(cellData -> cellData.getValue().tenMonAnProperty());
    colDonGia.setCellValueFactory(cellData -> cellData.getValue().donGiaProperty().asObject());

    tblDsMenuNhaHang.setItems(dsMonAn);

    txtTimKiem.textProperty().addListener((_, _, newValue) -> locThongTin(newValue));

    btnThem.setOnAction(_ -> themMonAn());

    btnVeTrangChu.setOnAction(_ -> {
      try {
        veTrangChu();
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
  }

  public void initData(String maTang, String maBan) {
    System.out.println("Gọi món bàn " + maBan + " tầng " +maTang);
    this.maBan = maBan;
    hienThiMenuNhaHang();
  }

  private void veTrangChu() throws Exception {
    AppQuanLyNhaHang.showUI("/vn/trandoananh/quanlynhahang/gui/QuanLyNhaHangGUI.fxml");
  }

  private void hienThiMenuNhaHang() {
    List<MonAn> menu = menuService.layDanhSachMenu();
    dsMonAn.setAll(menu);
  }

  private void locThongTin(String duLieuLoc) {
    List<MonAn> filteredMenu = menuService.layDanhSachMenu().stream()
        .filter(monAn -> monAn.getMaMonAn().toUpperCase().contains(duLieuLoc.toUpperCase())
            || monAn.getTenMonAn().toUpperCase().contains(duLieuLoc.toUpperCase()))
        .toList();
    dsMonAn.setAll(filteredMenu);
  }

  private void themMonAn() {
    // Get the selected row from the TableView
    int rowSelected = tblDsMenuNhaHang.getSelectionModel().getSelectedIndex();

    if (rowSelected != -1) {
      // Retrieve the selected MonAn item from the TableView
      MonAn selectedMonAn = tblDsMenuNhaHang.getItems().get(rowSelected);

      // Get the quantity from the text field
      String strSoLuong = txtSoLuong.getText();

      // Validate the quantity input
      if (strSoLuong != null && !strSoLuong.trim().isEmpty()) {
        if (strSoLuong.matches("^\\d+$")) { // Check if it's a valid positive integer
          int soLuong = Integer.parseInt(strSoLuong);

          if (soLuong >= 1) {
            System.out.println(maBan);
            // Call the service to add the MonAn to the order
            goiMonService.themMonAn(maTang, maBan, selectedMonAn, soLuong);

            // Display total price in an alert
            DecimalFormat df = new DecimalFormat("#,###");
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Thêm món thành công!", ButtonType.OK);

            // Refresh the menu list
            hienThiThucDonBanAn(maTang, maBan);

            // Update table status based on the number of items
            if (goiMonService.laySoLuongMonAn(maTang, maBan) > 0) {
              banAnService.setTrangThaiBanAn(maTang, maBan, "busy");
            } else {
              banAnService.setTrangThaiBanAn(maTang, maBan, "active");
            }

            // Update the table status
            capNhatTrangThaiBanAn(maTang);

          } else {
            // Show an error alert if quantity is less than 1
            Alert alert = new Alert(Alert.AlertType.WARNING, "Số lượng ít nhất phải là 1", ButtonType.OK);
            alert.show();
          }
        } else {
          // Show an error alert if the quantity format is invalid
          Alert alert = new Alert(Alert.AlertType.ERROR, "Số lượng nhập không đúng định dạng!", ButtonType.OK);
          alert.show();
        }
      } else {
        // Show an error alert if quantity is empty
        Alert alert = new Alert(Alert.AlertType.WARNING, "Số lượng không được để trống!", ButtonType.OK);
        alert.show();
      }
    } else {
      // Show an error alert if no item is selected
      Alert alert = new Alert(Alert.AlertType.WARNING, "Vui lòng chọn món ăn cần thêm!", ButtonType.OK);
      alert.show();
    }
  }

  private void hienThiThucDonBanAn(String maTang, String maBan) {
    // Clear existing data from TableView
    tblDsMenuNhaHang.getItems().clear();

    // Get the list of dishes (MonAn) for the selected floor and table
    GoiMonService goiMonService = new GoiMonService();
    Vector<MonAn> dsThucDonTheoBan = goiMonService.layDsThucDonTheoBan(maTang, maBan);

    // Loop through the list of MonAn and add them to the TableView
    for (MonAn monAn : dsThucDonTheoBan) {
      // Add the MonAn object directly to the TableView
      tblDsMenuNhaHang.getItems().add(monAn);
    }
  }

  private void capNhatTrangThaiBanAn(String maTang) {
    QuanLyNhaHangController controller = new QuanLyNhaHangController();

    for (int j = 1; j <= 12; j++) {
      String ban = String.valueOf(j);
      // Kiểm tra số lượng món ăn trên bàn
      GoiMonService goiMonService = new GoiMonService();
      int soLuongMonAn = goiMonService.laySoLuongMonAn(maTang, ban);
      String trangThai = soLuongMonAn > 0 ? "busy" : "active";

      // Cập nhật trạng thái bàn trong QuanLyNhaHangController
      controller.capNhatTrangThaiBanAn();
    }
  }
}