package vn.trandoananh.quanlynhahang.Utils;

import java.sql.*;

public class BanAnService {
  private static final String URL = "jdbc:mysql://127.0.0.1:3306/database_quanlynhahang";
  private static final String USER = "root";
  private static final String PASSWORD = "";

  /**
   * Phương thức setTrangThaiBanAn được dùng để cài đặt trạng thái bàn ăn ở vị trí (maTang, maBan)
   * vào bảng status_banan trong cơ sở dữ liệu.
   *
   * @param maTang    Tầng chứa bàn ăn {1,2,3}
   * @param maBan     Mã bàn theo từng tầng {1, 2, 3,..., 11, 12}
   * @param trangThai Trạng thái cần lưu cho bàn ăn {active/busy}
   */
  public void setTrangThaiBanAn(String maTang, String maBan, String trangThai) {
    String sql = "UPDATE status_banan SET TrangThai=? WHERE MaTang=? AND MaBan=?";
    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement preStatement = connection.prepareStatement(sql)) {

      preStatement.setString(1, trangThai);
      preStatement.setString(2, maTang);
      preStatement.setString(3, maBan);
      preStatement.executeUpdate();

      System.out.println("Cập nhật trạng thái bàn ăn thành công!");
    } catch (SQLException e) {
      System.err.println("Lỗi khi cập nhật trạng thái bàn ăn!");
      e.printStackTrace();
    }
  }

  /**
   * Phương thức getTrangThaiBanAn được dùng để lấy trạng thái của bàn ăn ở vị trí (maTang, maBan)
   * từ bảng status_banan trong cơ sở dữ liệu.
   *
   * @param maTang Tầng chứa bàn ăn {1,2,3}
   * @param maBan  Mã bàn theo từng tầng {1, 2, 3,..., 11, 12}
   * @return Trạng thái của bàn ăn ở vị trí (maTang, maBan) là active/busy
   */
  public String getTrangThaiBanAn(String maTang, String maBan) {
    String status = "";
    String sql = "SELECT TrangThai FROM status_banan WHERE MaTang=? AND MaBan=?";
    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement preStatement = connection.prepareStatement(sql)) {

      preStatement.setString(1, maTang);
      preStatement.setString(2, maBan);
      try (ResultSet resultSet = preStatement.executeQuery()) {
        if (resultSet.next()) {
          status = resultSet.getString("TrangThai");
        }
      }

      System.out.println("Lấy trạng thái bàn ăn thành công!");
    } catch (SQLException e) {
      System.err.println("Lỗi khi lấy trạng thái bàn ăn!");
      e.printStackTrace();
    }
    return status;
  }
}
