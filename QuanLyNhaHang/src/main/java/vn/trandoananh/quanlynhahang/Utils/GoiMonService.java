package vn.trandoananh.quanlynhahang.Utils;

import vn.trandoananh.quanlynhahang.Models.MonAn;
import vn.trandoananh.quanlynhahang.Models.data;

import java.sql.*;
import java.util.Vector;

public class GoiMonService {
  private static final String URL = "jdbc:mysql://127.0.0.1:3306/database_quanlynhahang";
  private static final String USER = "root";
  private static final String PASSWORD = "";

  /**
   * Phương thức themMonAn được dùng để thêm dữ liệu món ăn vào danh sách thực đơn của bàn ăn ở
   * vị trí (maTang, maBan) vào bảng goimon trong cơ sở dữ liệu.
   *
   * @param maTang  Tầng chứa bàn ăn {1, 2, 3}
   * @param maBan   Mã bàn ăn theo từng tầng {1, 2, 3,..., 11, 12}
   * @param monAn   Món ăn cần thêm
   * @param soLuong Số lượng món ăn cần thêm
   */
  public void themMonAn(String maTang, String maBan, MonAn monAn, int soLuong) {
    int soLuongBanDau = kiemTraMonAn(maTang, maBan, monAn.getMaMonAn());
    if (soLuongBanDau == 0) {
      String sql = "INSERT INTO goimon (MaTang, MaBan, MaMonAn, TenMonAn, DonGia, SoLuong) VALUES (?, ?, ?, ?, ?, ?)";
      try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
           PreparedStatement preStatement = connection.prepareStatement(sql)) {

        preStatement.setString(1, data.maTang);
        preStatement.setString(2, data.maBan);
        preStatement.setString(3, monAn.getMaMonAn());
        preStatement.setString(4, monAn.getTenMonAn());
        preStatement.setInt(5, monAn.getDonGia());
        preStatement.setInt(6, soLuong);
        preStatement.executeUpdate();

      } catch (SQLException e) {
        e.printStackTrace();
      }
    } else {
      thayDoiSoLuongMonAn(maTang, maBan, monAn.getMaMonAn(), soLuong + soLuongBanDau);
    }
  }

  /**
   * Phương thức xoaMonAn được dùng để xóa dữ liệu món ăn từ danh sách thực đơn của bàn ăn ở
   * vị trí (maTang, maBan) ở bảng goimon trong cơ sở dữ liệu.
   *
   * @param maTang  Tầng chứa bàn ăn {1, 2, 3}
   * @param maBan   Mã bàn ăn theo từng tầng {1, 2, 3,..., 11, 12}
   * @param maMonAn Món ăn cần xóa
   */
  public void xoaMonAn(String maTang, String maBan, String maMonAn) {
    String sql = "DELETE FROM goimon WHERE MaTang=? AND MaBan=? AND MaMonAn=?";
    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement preStatement = connection.prepareStatement(sql)) {

      preStatement.setString(1, maTang);
      preStatement.setString(2, maBan);
      preStatement.setString(3, maMonAn);
      preStatement.executeUpdate();

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Phương thức xoaMonAn được dùng để xóa tất cả món ăn từ danh sách thực đơn của bàn ăn ở
   * vị trí (maTang, maBan) ở bảng goimon trong cơ sở dữ liệu.
   *
   * @param maTang Tầng chứa bàn ăn {1, 2, 3}
   * @param maBan  Mã bàn ăn theo từng tầng {1, 2, 3,..., 11, 12}
   */
  public void xoaMonAn(String maTang, String maBan) {
    String sql = "DELETE FROM goimon WHERE MaTang=? AND MaBan=?";
    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement preStatement = connection.prepareStatement(sql)) {

      preStatement.setString(1, maTang);
      preStatement.setString(2, maBan);
      preStatement.executeUpdate();

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Phương thức kiemTraMonAn được dùng để kiểm tra xem maMonAn có tồn tại trong danh sách thực đơn
   * của bàn ăn ở vị trí (maTang, maBan) ở bảng goimon trong cơ sở dũ liệu.
   *
   * @param maTang  Tầng chứa bàn ăn {1, 2, 3}
   * @param maBan   Mã bàn ăn theo từng tầng {1, 2, 3,..., 11, 12}
   * @param maMonAn Mã món ăn cần kiểm tra
   * @return Số lượng của maMonAn xuất hiện trong danh sách thực đơn của bàn
   */
  public int kiemTraMonAn(String maTang, String maBan, String maMonAn) {
    int soLuongBanDau = 0;
    String sql = "SELECT SoLuong FROM goimon WHERE MaTang=? AND MaBan=? AND MaMonAn=?";
    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement preStatement = connection.prepareStatement(sql)) {

      preStatement.setString(1, maTang);
      preStatement.setString(2, maBan);
      preStatement.setString(3, maMonAn);
      try (ResultSet result = preStatement.executeQuery()) {
        if (result.next()) {
          soLuongBanDau = result.getInt("SoLuong");
        }
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return soLuongBanDau;
  }

  public int laySoLuongMonAn(String maTang, String maBan) {
    int soMonAn = 0;
    String sql = "SELECT * FROM goimon WHERE MaTang = ? AND MaBan = ?";
    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement preStatement = connection.prepareStatement(sql)) {
      preStatement.setString(1, maTang);
      preStatement.setString(2, maBan);
      try (ResultSet result = preStatement.executeQuery()) {
        while (result.next()) {
          soMonAn++;
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return soMonAn;
  }

  /**
   * Phương thức thayDoiSoLuongMonAn được dùng để thay đổi số lượng món ăn theo maMonAn ở vị trí (maTang, maBan)
   * trong bảng goimon trong cơ sở dữ liệu.
   *
   * @param maTang  Tầng chứa bàn ăn {1, 2, 3}
   * @param maBan   Mã bàn ăn theo từng tầng {1, 2, 3,..., 11, 12}
   * @param maMonAn Mã món ăn muốn thay đổi số lượng
   * @param soLuong Số lượng muốn sau khi thay đổi
   */
  public void thayDoiSoLuongMonAn(String maTang, String maBan, String maMonAn, int soLuong) {
    String sql = "UPDATE goimon SET SoLuong=? WHERE MaTang=? AND MaBan=? AND MaMonAn=?";
    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement preStatement = connection.prepareStatement(sql)) {

      preStatement.setInt(1, soLuong);
      preStatement.setString(2, maTang);
      preStatement.setString(3, maBan);
      preStatement.setString(4, maMonAn);
      preStatement.executeUpdate();

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Phương thức layDsThucDonTheoBan được dùng để lấy danh sách các món ăn trong thực đơn của bàn ăn ở
   * vị trí (maTang, maBan) trong bảng goimon trong cơ sở dữ liệu.
   *
   * @param maTang Tầng chứa bàn ăn {1, 2, 3}
   * @param maBan  Mã bàn ăn theo từng tầng {1, 2, 3,..., 11, 12}
   * @return danh sách thực đơn theo bàn ăn
   */
  public Vector<MonAn> layDsThucDonTheoBan(String maTang, String maBan) {
    Vector<MonAn> dsThucDonTheoBan = new Vector<>();
    String sql = "SELECT * FROM goimon WHERE MaTang=? AND MaBan=?";
    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement preStatement = connection.prepareStatement(sql)) {

      preStatement.setString(1, maTang);
      preStatement.setString(2, maBan);
      try (ResultSet result = preStatement.executeQuery()) {
        while (result.next()) {
          MonAn monAn = new MonAn();
          monAn.setMaMonAn(result.getString("MaMonAn"));
          monAn.setTenMonAn(result.getString("TenMonAn"));
          monAn.setDonGia(result.getInt("DonGia"));
          monAn.setSoLuong(result.getInt("SoLuong"));
          dsThucDonTheoBan.add(monAn);
        }
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return dsThucDonTheoBan;
  }
}
