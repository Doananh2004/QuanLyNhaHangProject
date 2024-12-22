package vn.trandoananh.quanlynhahang.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import vn.trandoananh.quanlynhahang.Models.MonAn;
import vn.trandoananh.quanlynhahang.Utils.BanAnService;
import vn.trandoananh.quanlynhahang.Utils.GoiMonService;
import vn.trandoananh.quanlynhahang.Utils.MenuService;

import java.util.Vector;

public class GoiMonController {
  @FXML
  private TextField txtTimKiem;
  @FXML
  private TableView<MonAn> tblDsMenuNhaHang;
  @FXML
  private TextField txtSoLuong;
  @FXML
  private Button btnThem;

  private final MenuService menuService = new MenuService();
  private final GoiMonService goiMonService = new GoiMonService();
  private final BanAnService banAnService = new BanAnService();

  private ObservableList<MonAn> dsMonAnTrongMenu;

  @FXML
  public void initialize() {
    loadMenuItems();
    addEventHandlers();
  }

  private void loadMenuItems() {
    dsMonAnTrongMenu = menuService.layDanhSachMenu();
    ObservableList<MonAn> observableList = FXCollections.observableArrayList(dsMonAnTrongMenu);
    tblDsMenuNhaHang.setItems(observableList);
  }

  private void addEventHandlers() {
    txtTimKiem.textProperty().addListener((_, _, newValue) -> filterMenu(newValue));

    btnThem.setOnAction(_ -> {
      MonAn selectedMonAn = tblDsMenuNhaHang.getSelectionModel().getSelectedItem();
      if (selectedMonAn != null) {
        String strSoLuong = txtSoLuong.getText();
        if (strSoLuong.matches("^\\d+$")) {
          int soLuong = Integer.parseInt(strSoLuong);
          if (soLuong >= 1) {
            goiMonService.themMonAn("1", "1", selectedMonAn, soLuong); // Example maTang and maBan
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Món ăn đã được thêm!");
            alert.show();
          } else {
            showError("Số lượng ít nhất phải là 1");
          }
        } else {
          showError("Số lượng không hợp lệ!");
        }
      } else {
        showError("Vui lòng chọn một món ăn!");
      }
    });
  }

  private void filterMenu(String filter) {
    Vector<MonAn> filteredList = new Vector<>();
    for (MonAn monAn : dsMonAnTrongMenu) {
      if (monAn.getMaMonAn().contains(filter) || monAn.getTenMonAn().contains(filter)) {
        filteredList.add(monAn);
      }
    }
    ObservableList<MonAn> observableList = FXCollections.observableArrayList(filteredList);
    tblDsMenuNhaHang.setItems(observableList);
  }

  private void showError(String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR, message);
    alert.show();
  }
}
