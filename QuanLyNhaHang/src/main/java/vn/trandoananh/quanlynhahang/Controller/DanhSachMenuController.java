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
    searchIcon.setImage(new Image(Objects.requireNonNull(getClass().getResource("/vn/trandoananh/images/search.png")).toString()));
    addIcon.setImage(new Image(Objects.requireNonNull(getClass().getResource("/vn/trandoananh/images/add.png")).toString()));
    eraseIcon.setImage(new Image(Objects.requireNonNull(getClass().getResource("/vn/trandoananh/images/erase.png")).toString()));
    editIcon.setImage(new Image(Objects.requireNonNull(getClass().getResource("/vn/trandoananh/images/edit.png")).toString()));
    closeIcon.setImage(new Image(Objects.requireNonNull(getClass().getResource("/vn/trandoananh/images/close.png")).toString()));

    // Cấu hình các cột
    maMonAnColumn.setCellValueFactory(data -> data.getValue().maMonAnProperty());
    tenMonAnColumn.setCellValueFactory(data -> data.getValue().tenMonAnProperty());
    donGiaColumn.setCellValueFactory(data -> data.getValue().donGiaProperty().asObject());

    // Hiển thị dữ liệu
    docDuLieu();

    // Thêm sự kiện cho ô tìm kiếm
    txtTimKiem.textProperty().addListener((observable, oldValue, newValue) -> locThongTin(newValue));

    // Thêm sự kiện cho các nút
    btnThem.setOnAction(event -> themMonAn());
    btnXoa.setOnAction(event -> xoaMonAn());
    btnChinhSua.setOnAction(event -> chinhSuaMonAn());
    btnThoat.setOnAction(event -> ((Stage) btnThoat.getScene().getWindow()).close());
  }

  private void docDuLieu() {
    List<MonAn> danhSach = menuService.layDanhSachMenu();
    dsMonAn = FXCollections.observableArrayList(danhSach);
    tblMenu.setItems(dsMonAn);
  }

  private void locThongTin(String keyword) {
    List<MonAn> dsLoc = dsMonAn.stream()
        .filter(monAn -> monAn.getMaMonAn().toLowerCase().contains(keyword.toLowerCase())
            || monAn.getTenMonAn().toLowerCase().contains(keyword.toLowerCase()))
        .collect(Collectors.toList());
    tblMenu.setItems(FXCollections.observableArrayList(dsLoc));
  }

  private void themMonAn() {
    // Xử lý thêm món ăn
    // Tham khảo phần logic trong code gốc
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
    MonAn monAn = tblMenu.getSelectionModel().getSelectedItem();
    if (monAn != null) {
      // Xử lý chỉnh sửa món ăn
      // Tham khảo phần logic trong code gốc
    } else {
      Alert alert = new Alert(Alert.AlertType.WARNING, "Vui lòng chọn món ăn muốn chỉnh sửa!");
      alert.showAndWait();
    }
  }
}
