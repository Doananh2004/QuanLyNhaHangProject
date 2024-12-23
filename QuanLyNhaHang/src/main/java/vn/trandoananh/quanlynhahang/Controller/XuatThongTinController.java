package vn.trandoananh.quanlynhahang.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import vn.trandoananh.quanlynhahang.Models.MonAn;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

public class XuatThongTinController {

  @FXML
  private Label lblThoiGian;

  @FXML
  private TableView<MonAn> tblDsHoaDon;

  @FXML
  private TableColumn<MonAn, String> colMaMonAn;

  @FXML
  private TableColumn<MonAn, String> colTenMonAn;

  @FXML
  private TableColumn<MonAn, String> colDonGia;

  @FXML
  private TableColumn<MonAn, Integer> colSoLuong;

  @FXML
  private TableColumn<MonAn, String> colThanhTien;

  private Vector<MonAn> dsThucDon;

  public void initialize(Vector<MonAn> dsThucDon) {
    this.dsThucDon = dsThucDon;
    setupTable();
    displayTime();
  }

  private void setupTable() {
    colMaMonAn.setCellValueFactory(new PropertyValueFactory<>("maMonAn"));
    colTenMonAn.setCellValueFactory(new PropertyValueFactory<>("tenMonAn"));
    colDonGia.setCellValueFactory(cellData -> {
      DecimalFormat df = new DecimalFormat("#,###");
      return new javafx.beans.property.SimpleStringProperty(df.format(cellData.getValue().getDonGia()));
    });
    colSoLuong.setCellValueFactory(new PropertyValueFactory<>("soLuong"));
    colThanhTien.setCellValueFactory(cellData -> {
      int thanhTien = cellData.getValue().getDonGia() * cellData.getValue().getSoLuong();
      DecimalFormat df = new DecimalFormat("#,###");
      return new javafx.beans.property.SimpleStringProperty(df.format(thanhTien));
    });

    tblDsHoaDon.getItems().addAll(dsThucDon);
  }

  private void displayTime() {
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    lblThoiGian.setText("Thời gian xuất: " + sdf.format(calendar.getTime()));
  }
}
