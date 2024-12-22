package vn.trandoananh.quanlynhahang.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BanAnService {
  private final Connection conn;

  public BanAnService() {
    conn = MySqlService.getConnection();
  }

  /**
   * Cập nhật trạng thái của bàn ăn trong cơ sở dữ liệu.
   *
   * @param maTang   Tầng chứa bàn ăn {1, 2, 3}
   * @param maBan    Mã bàn ăn theo từng tầng {1, 2, 3, ..., 11, 12}
   * @param trangThai Trạng thái của bàn ăn ("active" hoặc "busy").
   * @return true nếu cập nhật thành công, false nếu thất bại.
   */
  public boolean capNhatTrangThaiBan(String maTang, String maBan, String trangThai) {
    try {
      String sql = "UPDATE status_banan SET TrangThai = ? WHERE MaTang = ? AND MaBan = ?";
      PreparedStatement preStatement = conn.prepareStatement(sql);
      preStatement.setString(1, trangThai);
      preStatement.setString(2, maTang);
      preStatement.setString(3, maBan);

      int rowsUpdated = preStatement.executeUpdate();
      return rowsUpdated > 0;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  /**
   * Lấy trạng thái của bàn ăn từ cơ sở dữ liệu.
   *
   * @param maTang Tầng chứa bàn ăn {1, 2, 3}
   * @param maBan  Mã bàn ăn theo từng tầng {1, 2, 3, ..., 11, 12}
   * @return Trạng thái của bàn ăn ("active", "busy") hoặc null nếu không tìm thấy.
   */
  public String layTrangThaiBan(String maTang, String maBan) {
    try {
      String sql = "SELECT TrangThai FROM status_banan WHERE MaTang = ? AND MaBan = ?";
      PreparedStatement preStatement = conn.prepareStatement(sql);
      preStatement.setString(1, maTang);
      preStatement.setString(2, maBan);

      ResultSet resultSet = preStatement.executeQuery();
      if (resultSet.next()) {
        return resultSet.getString("TrangThai");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Đặt lại trạng thái của tất cả các bàn ăn trong tầng về trạng thái mặc định.
   *
   * @param maTang   Tầng cần đặt lại trạng thái {1, 2, 3}.
   * @param trangThai Trạng thái mặc định để đặt lại ("active" hoặc "busy").
   * @return true nếu đặt lại thành công, false nếu thất bại.
   */
  public boolean datLaiTrangThaiTatCaBanTrongTang(String maTang, String trangThai) {
    try {
      String sql = "UPDATE status_banan SET TrangThai = ? WHERE MaTang = ?";
      PreparedStatement preStatement = conn.prepareStatement(sql);
      preStatement.setString(1, trangThai);
      preStatement.setString(2, maTang);

      int rowsUpdated = preStatement.executeUpdate();
      return rowsUpdated > 0;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  /**
   * Kiểm tra xem bàn ăn có tồn tại trong cơ sở dữ liệu không.
   *
   * @param maTang Tầng chứa bàn ăn {1, 2, 3}
   * @param maBan  Mã bàn ăn theo từng tầng {1, 2, 3, ..., 11, 12}
   * @return true nếu bàn ăn tồn tại, false nếu không.
   */
  public boolean kiemTraBanAnTonTai(String maTang, String maBan) {
    try {
      String sql = "SELECT 1 FROM status_banan WHERE MaTang = ? AND MaBan = ?";
      PreparedStatement preStatement = conn.prepareStatement(sql);
      preStatement.setString(1, maTang);
      preStatement.setString(2, maBan);

      ResultSet resultSet = preStatement.executeQuery();
      return resultSet.next();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }
}
