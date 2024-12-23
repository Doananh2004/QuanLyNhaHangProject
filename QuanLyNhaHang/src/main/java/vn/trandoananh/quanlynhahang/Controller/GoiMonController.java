package vn.trandoananh.quanlynhahang.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import vn.trandoananh.quanlynhahang.Models.MonAn;
import vn.trandoananh.quanlynhahang.Utils.BanAnService;
import vn.trandoananh.quanlynhahang.Utils.GoiMonService;
import vn.trandoananh.quanlynhahang.Utils.MenuService;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;

public class GoiMonController {
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
  private ImageView imgSearch;

  private final MenuService menuService = new MenuService();
  private final GoiMonService goiMonService = new GoiMonService();
  private final BanAnService banAnService = new BanAnService();

  private String maTang;
  private String maBan;

  private final ObservableList<MonAn> dsMonAn = FXCollections.observableArrayList();

  public void initialize() {
    imgSearch.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/vn/trandoananh/images/search.png"))));

    colMaMonAn.setCellValueFactory(cellData -> cellData.getValue().maMonAnProperty());
    colTenMonAn.setCellValueFactory(cellData -> cellData.getValue().tenMonAnProperty());
    colDonGia.setCellValueFactory(cellData -> cellData.getValue().donGiaProperty().asObject());

    tblDsMenuNhaHang.setItems(dsMonAn);

    txtTimKiem.textProperty().addListener((_, _, newValue) -> locThongTin(newValue));

    btnThem.setOnAction(_ -> themMonAn());
  }

  public void initData(String maTang, String maBan) {
    this.maTang = maTang;
    this.maBan = maBan;
    hienThiMenuNhaHang();
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
    MonAn selectedMonAn = tblDsMenuNhaHang.getSelectionModel().getSelectedItem();
    if (selectedMonAn != null) {
      try {
        int soLuong = Integer.parseInt(txtSoLuong.getText());
        if (soLuong < 1) throw new NumberFormatException();

        goiMonService.themMonAn(maTang, maBan, selectedMonAn, soLuong);

        DecimalFormat df = new DecimalFormat("#,###");
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Thêm món thành công!", ButtonType.OK);
        alert.setHeaderText("Tổng giá: " + df.format((long) selectedMonAn.getDonGia() * soLuong));
        alert.show();
      } catch (NumberFormatException e) {
        Alert alert = new Alert(Alert.AlertType.ERROR, "Số lượng phải là số nguyên dương!", ButtonType.OK);
        alert.show();
      }
    } else {
      Alert alert = new Alert(Alert.AlertType.WARNING, "Vui lòng chọn món ăn cần thêm!", ButtonType.OK);
      alert.show();
    }
  }

}
