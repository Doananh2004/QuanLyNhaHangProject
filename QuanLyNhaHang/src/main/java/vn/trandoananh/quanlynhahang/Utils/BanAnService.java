package vn.trandoananh.quanlynhahang.Utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BanAnService extends MySqlService{
  public BanAnService(){
    super();
  }

  /**
   * Phương thức setTrangThaiBanAn được dùng để cài đặt trạng thái bàn ăn ở vị trí (maTang, maBan)
   * vào bảng status_banan trong cơ sở dữ liệu
   *
   * @param maTang Tầng chứa bàn ăn {1,2,3}
   * @param maBan Mã bàn theo từng tầng {1, 2, 3,..., 11, 12}
   * @param trangThai Trạng thái cần lưu cho bàn ăn {active/busy}
   */
  public void setTrangThaiBanAn(String maTang, String maBan, String trangThai) {
    try {
      String sql = "update status_banan set TrangThai=? where MaTang=? and MaBan=?";
      PreparedStatement preStatement = conn.prepareStatement(sql);
      preStatement.setString(1, trangThai);
      preStatement.setString(2, maTang);
      preStatement.setString(3, maBan);
      preStatement.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Phương thức getTrangThaiBanAn được dùng để lấy trạng thái của bàn ăn ở vị trí (maTang, maBan) từ
   * bảng status_banan trong cơ sở dữ liệu
   *
   * @param maTang Tầng chứa bàn ăn {1,2,3}
   * @param maBan Mã bàn theo từng tầng {1, 2, 3,..., 11, 12}
   * @return Trạng thái của bàn ăn ở vị trí (maTang, maBan) là active/busy
   */
  public String getTrangThaiBanAn(String maTang, String maBan) {
    String status = "";
    try {
      String sql = "select * from status_banan where MaTang=? and MaBan=?";
      PreparedStatement preStatement = conn.prepareStatement(sql);
      preStatement.setString(1, maTang);
      preStatement.setString(2, maBan);
      ResultSet result = preStatement.executeQuery();
      while (result.next()) {
        status = result.getString(3);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return status;
  }
}
