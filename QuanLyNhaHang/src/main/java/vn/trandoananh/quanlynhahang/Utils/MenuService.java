package vn.trandoananh.quanlynhahang.Utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import vn.trandoananh.quanlynhahang.Models.MonAn;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MenuService extends MySqlService{
  public MenuService() {
    super();
  }

  /**
   * Lấy danh sách món ăn từ cơ sở dữ liệu và trả về dưới dạng ObservableList để sử dụng trong JavaFX.
   *
   * @return ObservableList<MonAn> danh sách món ăn.
   */
  public ObservableList<MonAn> layDanhSachMenu() {
    ObservableList<MonAn> dsMonAn = FXCollections.observableArrayList();
    try {
      String sql = "SELECT * FROM menu_nhahang";
      PreparedStatement preStatement = conn.prepareStatement(sql);
      ResultSet result = preStatement.executeQuery();
      while (result.next()) {
        MonAn monAn = new MonAn();
        monAn.setMaMonAn(result.getString("MaMonAn"));
        monAn.setTenMonAn(result.getString("TenMonAn"));
        monAn.setDonGia(result.getInt("DonGia"));
        dsMonAn.add(monAn);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return dsMonAn;
  }

  /**
   * Xóa món ăn theo mã món ăn từ cơ sở dữ liệu.
   *
   * @param maMonAn Mã món ăn cần xóa.
   * @return true nếu xóa thành công, false nếu thất bại.
   */
  public boolean xoaMonAn(String maMonAn) {
    try {
      String sql = "DELETE FROM menu_nhahang WHERE MaMonAn = ?";
      PreparedStatement preStatement = conn.prepareStatement(sql);
      preStatement.setString(1, maMonAn);
      int result = preStatement.executeUpdate();
      return result > 0;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  /**
   * Thêm món ăn mới vào cơ sở dữ liệu.
   *
   * @param monAn Đối tượng món ăn cần thêm.
   * @return true nếu thêm thành công, false nếu thất bại.
   */
  public boolean themMonAn(MonAn monAn) {
    try {
      String sql = "INSERT INTO menu_nhahang (MaMonAn, TenMonAn, DonGia) VALUES (?, ?, ?)";
      PreparedStatement preStatement = conn.prepareStatement(sql);
      preStatement.setString(1, monAn.getMaMonAn());
      preStatement.setString(2, monAn.getTenMonAn());
      preStatement.setInt(3, monAn.getDonGia());
      int result = preStatement.executeUpdate();
      return result > 0;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  /**
   * Cập nhật thông tin món ăn trong cơ sở dữ liệu.
   *
   * @param monAn Đối tượng món ăn cần cập nhật thông tin.
   * @return true nếu cập nhật thành công, false nếu thất bại.
   */
  public boolean chinhSuaThongTinMonAn(MonAn monAn) {
    try {
      String sql = "UPDATE menu_nhahang SET TenMonAn = ?, DonGia = ? WHERE MaMonAn = ?";
      PreparedStatement preStatement = conn.prepareStatement(sql);
      preStatement.setString(1, monAn.getTenMonAn());
      preStatement.setInt(2, monAn.getDonGia());
      preStatement.setString(3, monAn.getMaMonAn());
      int result = preStatement.executeUpdate();
      return result > 0;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }
}
