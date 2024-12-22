package vn.trandoananh.quanlynhahang.Utils;

import vn.trandoananh.quanlynhahang.Models.MonAn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

public class GoiMonService {
  private final Connection conn;

  public GoiMonService(){
    conn = MySqlService.getConnection();
  }

  /**
   * Thêm món ăn vào danh sách thực đơn của bàn.
   *
   * @param maTang  Tầng chứa bàn ăn {1, 2, 3}.
   * @param maBan   Mã bàn ăn theo từng tầng {1, 2, 3,..., 11, 12}.
   * @param monAn   Món ăn cần thêm.
   * @param soLuong Số lượng món ăn cần thêm.
   */
  public void themMonAn(String maTang, String maBan, MonAn monAn, int soLuong) {
    int soLuongBanDau = kiemTraMonAn(maTang, maBan, monAn.getMaMonAn());
    if (soLuongBanDau == 0) {
      try {
        String sql = "INSERT INTO goimon VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement preStatement = conn.prepareStatement(sql);
        preStatement.setString(1, maTang);
        preStatement.setString(2, maBan);
        preStatement.setString(3, monAn.getMaMonAn());
        preStatement.setString(4, monAn.getTenMonAn());
        preStatement.setInt(5, monAn.getDonGia());
        preStatement.setInt(6, soLuong);
        preStatement.executeUpdate();
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else {
      thayDoiSoLuongMonAn(maTang, maBan, monAn.getMaMonAn(), soLuong + soLuongBanDau);
    }
  }

  /**
   * Xóa một món ăn khỏi danh sách thực đơn của bàn.
   *
   * @param maTang  Tầng chứa bàn ăn {1, 2, 3}.
   * @param maBan   Mã bàn ăn theo từng tầng {1, 2, 3,..., 11, 12}.
   * @param maMonAn Mã món ăn cần xóa.
   */
  public void xoaMonAn(String maTang, String maBan, String maMonAn) {
    try {
      String sql = "DELETE FROM goimon WHERE MaTang=? AND MaBan=? AND MaMonAn=?";
      PreparedStatement preStatement = conn.prepareStatement(sql);
      preStatement.setString(1, maTang);
      preStatement.setString(2, maBan);
      preStatement.setString(3, maMonAn);
      preStatement.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Xóa tất cả món ăn khỏi danh sách thực đơn của bàn.
   *
   * @param maTang Tầng chứa bàn ăn {1, 2, 3}.
   * @param maBan  Mã bàn ăn theo từng tầng {1, 2, 3,..., 11, 12}.
   */
  public void xoaTatCaMonAn(String maTang, String maBan) {
    try {
      String sql = "DELETE FROM goimon WHERE MaTang=? AND MaBan=?";
      PreparedStatement preStatement = conn.prepareStatement(sql);
      preStatement.setString(1, maTang);
      preStatement.setString(2, maBan);
      preStatement.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Kiểm tra xem một món ăn có tồn tại trong danh sách thực đơn không.
   *
   * @param maTang  Tầng chứa bàn ăn {1, 2, 3}.
   * @param maBan   Mã bàn ăn theo từng tầng {1, 2, 3,..., 11, 12}.
   * @param maMonAn Mã món ăn cần kiểm tra.
   * @return Số lượng món ăn nếu tồn tại, 0 nếu không tồn tại.
   */
  public int kiemTraMonAn(String maTang, String maBan, String maMonAn) {
    try {
      String sql = "SELECT SoLuong FROM goimon WHERE MaTang=? AND MaBan=? AND MaMonAn=?";
      PreparedStatement preStatement = conn.prepareStatement(sql);
      preStatement.setString(1, maTang);
      preStatement.setString(2, maBan);
      preStatement.setString(3, maMonAn);
      ResultSet result = preStatement.executeQuery();
      if (result.next()) {
        return result.getInt("SoLuong");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return 0;
  }

  /**
   * Lấy danh sách thực đơn theo bàn.
   *
   * @param maTang Tầng chứa bàn ăn {1, 2, 3}.
   * @param maBan  Mã bàn ăn theo từng tầng {1, 2, 3,..., 11, 12}.
   * @return Danh sách món ăn trong thực đơn của bàn.
   */
  public Vector<MonAn> layDsThucDonTheoBan(String maTang, String maBan) {
    Vector<MonAn> danhSachMonAn = new Vector<>();
    try {
      String sql = "SELECT * FROM goimon WHERE MaTang=? AND MaBan=?";
      PreparedStatement preStatement = conn.prepareStatement(sql);
      preStatement.setString(1, maTang);
      preStatement.setString(2, maBan);
      ResultSet result = preStatement.executeQuery();
      while (result.next()) {
        MonAn monAn = new MonAn();
        monAn.setMaMonAn(result.getString("MaMonAn"));
        monAn.setTenMonAn(result.getString("TenMonAn"));
        monAn.setDonGia(result.getInt("DonGia"));
        monAn.setSoLuong(result.getInt("SoLuong"));
        danhSachMonAn.add(monAn);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return danhSachMonAn;
  }

  /**
   * Thay đổi số lượng món ăn theo bàn.
   *
   * @param maTang  Tầng chứa bàn ăn {1, 2, 3}.
   * @param maBan   Mã bàn ăn theo từng tầng {1, 2, 3,..., 11, 12}.
   * @param maMonAn Mã món ăn cần thay đổi.
   * @param soLuong Số lượng mới.
   */
  public void thayDoiSoLuongMonAn(String maTang, String maBan, String maMonAn, int soLuong) {
    try {
      String sql = "UPDATE goimon SET SoLuong=? WHERE MaTang=? AND MaBan=? AND MaMonAn=?";
      PreparedStatement preStatement = conn.prepareStatement(sql);
      preStatement.setInt(1, soLuong);
      preStatement.setString(2, maTang);
      preStatement.setString(3, maBan);
      preStatement.setString(4, maMonAn);
      preStatement.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Lấy tổng số lượng món ăn trong danh sách thực đơn của bàn.
   *
   * @param maTang Tầng chứa bàn ăn {1, 2, 3}.
   * @param maBan  Mã bàn ăn theo từng tầng {1, 2, 3,..., 11, 12}.
   * @return Tổng số lượng món ăn.
   */
  public int laySoLuongMonAn(String maTang, String maBan) {
    int soLuongTong = 0;
    try {
      String sql = "SELECT SUM(SoLuong) AS TongSoLuong FROM goimon WHERE MaTang=? AND MaBan=?";
      PreparedStatement preStatement = conn.prepareStatement(sql);
      preStatement.setString(1, maTang);
      preStatement.setString(2, maBan);
      ResultSet result = preStatement.executeQuery();
      if (result.next()) {
        soLuongTong = result.getInt("TongSoLuong");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return soLuongTong;
  }
}
