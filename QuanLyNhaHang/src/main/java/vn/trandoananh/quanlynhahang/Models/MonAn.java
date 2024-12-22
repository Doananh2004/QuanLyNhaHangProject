package vn.trandoananh.quanlynhahang.Models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;

public class MonAn implements Serializable {
  // Các thuộc tính dạng Property
  private final StringProperty maMonAn;
  private final StringProperty tenMonAn;
  private final IntegerProperty donGia;

  // Phương thức khởi tạo mặc định
  public MonAn() {
    this.maMonAn = new SimpleStringProperty();
    this.tenMonAn = new SimpleStringProperty();
    this.donGia = new SimpleIntegerProperty();
  }

  // Phương thức khởi tạo với tham số
  public MonAn(String maMonAn, String tenMonAn, int donGia) {
    this.maMonAn = new SimpleStringProperty(maMonAn);
    this.tenMonAn = new SimpleStringProperty(tenMonAn);
    this.donGia = new SimpleIntegerProperty(donGia);
  }

  // Getter và Setter cho maMonAn
  public String getMaMonAn() {
    return maMonAn.get();
  }

  public void setMaMonAn(String maMonAn) {
    this.maMonAn.set(maMonAn);
  }

  public StringProperty maMonAnProperty() {
    return maMonAn;
  }

  // Getter và Setter cho tenMonAn
  public String getTenMonAn() {
    return tenMonAn.get();
  }

  public void setTenMonAn(String tenMonAn) {
    this.tenMonAn.set(tenMonAn);
  }

  public StringProperty tenMonAnProperty() {
    return tenMonAn;
  }

  // Getter và Setter cho donGia
  public int getDonGia() {
    return donGia.get();
  }

  public void setDonGia(int donGia) {
    this.donGia.set(donGia);
  }

  public IntegerProperty donGiaProperty() {
    return donGia;
  }
}