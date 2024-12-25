package vn.trandoananh.quanlynhahang.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import vn.trandoananh.quanlynhahang.Models.MonAn;
import vn.trandoananh.quanlynhahang.Utils.MenuService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class DanhSachMenuController {
  @FXML
  private TextField txtTimKiem;
  @FXML
  private TableView<MonAn> tblMenu;
  @FXML
  private TableColumn<MonAn, String> maMonAnColumn;
  @FXML
  private TableColumn<MonAn, String> tenMonAnColumn;
  @FXML
  private TableColumn<MonAn, Integer> donGiaColumn;
  @FXML
  private Button btnThem, btnXoa, btnChinhSua, btnThoat;
  @FXML
  private ImageView searchIcon, addIcon, eraseIcon, editIcon, closeIcon;

  private final MenuService menuService = new MenuService();
  private ObservableList<MonAn> dsMonAn;

  @FXML
  public void initialize() {
    // Gán icon cho các nút
    searchIcon.setImage(new Image(Objects.requireNonNull(getClass().getResource("/vn/trandoananh/quanlynhahang/images/search.png")).toString()));
    addIcon.setImage(new Image(Objects.requireNonNull(getClass().getResource("/vn/trandoananh/quanlynhahang/images/add.png")).toString()));
    eraseIcon.setImage(new Image(Objects.requireNonNull(getClass().getResource("/vn/trandoananh/quanlynhahang/images/erase.png")).toString()));
    editIcon.setImage(new Image(Objects.requireNonNull(getClass().getResource("/vn/trandoananh/quanlynhahang/images/edit.png")).toString()));
    closeIcon.setImage(new Image(Objects.requireNonNull(getClass().getResource("/vn/trandoananh/quanlynhahang/images/close.png")).toString()));

    // Cấu hình các cột
    maMonAnColumn.setCellValueFactory(data -> data.getValue().maMonAnProperty());
    tenMonAnColumn.setCellValueFactory(data -> data.getValue().tenMonAnProperty());
    donGiaColumn.setCellValueFactory(data -> data.getValue().donGiaProperty().asObject());

    // Hiển thị dữ liệu
    docDuLieu();

    // Thêm sự kiện cho ô tìm kiếm
    txtTimKiem.textProperty().addListener((_, _, _) -> locThongTin());

    // Thêm sự kiện cho các nút
    btnThem.setOnAction(_ -> themMonAn());
    btnXoa.setOnAction(_ -> xoaMonAn());
    btnChinhSua.setOnAction(_ -> chinhSuaMonAn());
    btnThoat.setOnAction(_ -> ((Stage) btnThoat.getScene().getWindow()).close());
  }

  private void docDuLieu() {
    List<MonAn> danhSach = menuService.layDanhSachMenu();
    dsMonAn = FXCollections.observableArrayList(danhSach);
    tblMenu.setItems(dsMonAn);
  }

  private void locThongTin() {
    String keyword = txtTimKiem.getText();
    List<MonAn> dsLoc = dsMonAn.stream()
        .filter(monAn -> monAn.getMaMonAn().toLowerCase().contains(keyword.toLowerCase())
            || monAn.getTenMonAn().toLowerCase().contains(keyword.toLowerCase()))
        .collect(Collectors.toList());
    tblMenu.setItems(FXCollections.observableArrayList(dsLoc));
  }

  private void themMonAn() {
    // Xử lý thêm món ăn
    TextInputDialog maMonAnDialog = new TextInputDialog();
    maMonAnDialog.setTitle("Thêm Món Ăn");
    maMonAnDialog.setHeaderText("Nhập mã món ăn:");
    maMonAnDialog.setContentText("Mã món ăn:");

    Optional<String> maMonAnResult = maMonAnDialog.showAndWait();
    if (maMonAnResult.isPresent() && !maMonAnResult.get().trim().isEmpty()) {
      String maMonAn = maMonAnResult.get().trim();
      if (!kiemTraTonTaiMaMonAn(maMonAn)) {
        TextInputDialog tenMonAnDialog = new TextInputDialog();
        tenMonAnDialog.setTitle("Thêm Món Ăn");
        tenMonAnDialog.setHeaderText("Nhập tên món ăn:");
        tenMonAnDialog.setContentText("Tên món ăn:");

        Optional<String> tenMonAnResult = tenMonAnDialog.showAndWait();
        if (tenMonAnResult.isPresent() && !tenMonAnResult.get().trim().isEmpty()) {
          String tenMonAn = tenMonAnResult.get().trim();

          TextInputDialog donGiaDialog = new TextInputDialog();
          donGiaDialog.setTitle("Thêm Món Ăn");
          donGiaDialog.setHeaderText("Nhập đơn giá:");
          donGiaDialog.setContentText("Đơn giá:");

          Optional<String> donGiaResult = donGiaDialog.showAndWait();
          if (donGiaResult.isPresent() && !donGiaResult.get().trim().isEmpty() && donGiaResult.get().matches("^\\d+$")) {
            int donGia = Integer.parseInt(donGiaResult.get());
            MonAn monAn = new MonAn(maMonAn, tenMonAn, donGia);
            boolean status = menuService.themMonAn(monAn);
            if (status) {
              locThongTin();
              showAlert(Alert.AlertType.INFORMATION, "Thành Công", "Thêm món ăn thành công!");
            } else {
              showAlert(Alert.AlertType.ERROR, "Lỗi", "Đã xảy ra lỗi khi thêm món ăn.");
            }
          } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Đơn giá không hợp lệ hoặc để trống!");
          }
        } else {
          showAlert(Alert.AlertType.WARNING, "Cảnh Báo", "Tên món ăn không được để trống!");
        }
      } else {
        showAlert(Alert.AlertType.WARNING, "Cảnh Báo", "Mã món ăn đã tồn tại!");
      }
    } else {
      showAlert(Alert.AlertType.WARNING, "Cảnh Báo", "Mã món ăn không được để trống!");
    }
  }

  private void xoaMonAn() {
    MonAn monAn = tblMenu.getSelectionModel().getSelectedItem();
    if (monAn != null) {
      boolean status = menuService.xoaMonAn(monAn.getMaMonAn());
      if (status) {
        dsMonAn.remove(monAn);
        tblMenu.refresh();
      }
    } else {
      Alert alert = new Alert(Alert.AlertType.WARNING, "Vui lòng chọn món ăn muốn xóa!");
      alert.showAndWait();
    }
  }

  private void chinhSuaMonAn() {
    MonAn selectedMonAn = tblMenu.getSelectionModel().getSelectedItem();
    if (selectedMonAn != null) {
      TableColumn focusedColumn = tblMenu.getFocusModel().getFocusedCell().getTableColumn();

      if (focusedColumn == maMonAnColumn) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Chỉnh sửa không hợp lệ");
        alert.setHeaderText(null);
        alert.setContentText("Không thể sửa đổi mã món ăn!");
        alert.showAndWait();
      } else if (focusedColumn == tenMonAnColumn) {
        TextInputDialog dialog = new TextInputDialog(selectedMonAn.getTenMonAn());
        dialog.setTitle("Chỉnh sửa tên món ăn");
        dialog.setHeaderText(null);
        dialog.setContentText("Tên món ăn:");

        dialog.showAndWait().ifPresent(tenMonAn -> {
          if (!tenMonAn.trim().isEmpty()) {
            selectedMonAn.setTenMonAn(tenMonAn);
            boolean status = menuService.chinhSuaThongTinMonAn(selectedMonAn);
            if (status) {
              locThongTin();
              Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
              successAlert.setTitle("Thành công");
              successAlert.setHeaderText(null);
              successAlert.setContentText("Sửa tên món ăn thành công!");
              successAlert.showAndWait();
            }
          } else {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Lỗi");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Tên món ăn không được để trống!");
            errorAlert.showAndWait();
          }
        });
      } else if (focusedColumn == donGiaColumn) {
        TextInputDialog dialog = new TextInputDialog(String.valueOf(selectedMonAn.getDonGia()));
        dialog.setTitle("Chỉnh sửa đơn giá");
        dialog.setHeaderText(null);
        dialog.setContentText("Đơn giá:");

        dialog.showAndWait().ifPresent(strDonGia -> {
          if (!strDonGia.trim().isEmpty() && strDonGia.matches("^\\d+$")) {
            int donGia = Integer.parseInt(strDonGia);
            selectedMonAn.setDonGia(donGia);
            boolean status = menuService.chinhSuaThongTinMonAn(selectedMonAn);
            if (status) {
              locThongTin();
              Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
              successAlert.setTitle("Thành công");
              successAlert.setHeaderText(null);
              successAlert.setContentText("Sửa đơn giá món ăn thành công!");
              successAlert.showAndWait();
            }
          } else if (strDonGia.trim().isEmpty()) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Lỗi");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Đơn giá không được để trống!");
            errorAlert.showAndWait();
          } else {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Lỗi");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Đơn giá nhập không đúng định dạng!");
            errorAlert.showAndWait();
          }
        });
      }
    } else {
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setTitle("Cảnh báo");
      alert.setHeaderText(null);
      alert.setContentText("Vui lòng chọn tên món ăn hoặc đơn giá cần sửa!");
      alert.showAndWait();
    }
  }

  private void showAlert(Alert.AlertType alertType, String title, String content) {
    Alert alert = new Alert(alertType);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(content);
    alert.showAndWait();
  }

  private boolean kiemTraTonTaiMaMonAn(String maMonAn) {
    for (MonAn monAn : dsMonAn) {
      if (monAn.getMaMonAn().equalsIgnoreCase(maMonAn)) {
        return true;
      }
    }
    return false;
  }
}
