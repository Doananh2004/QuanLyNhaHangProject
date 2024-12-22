package vn.trandoananh.quanlynhahang.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MySqlService {
  private static final String URL = "jdbc:mysql://127.0.0.1:3306/database_quanlynhahang";
  private static final String USER = "root";
  private static final String PASSWORD = "";
  private static Connection conn;

  /**
   * Phương thức khởi tạo để thiết lập kết nối cơ sở dữ liệu.
   */
  public MySqlService() {
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
      conn = DriverManager.getConnection(URL, USER, PASSWORD);
    } catch (Exception e) {
      e.printStackTrace();
      conn = null;
    }
  }

  /**
   * Phương thức lấy kết nối hiện tại.
   *
   * @return Kết nối cơ sở dữ liệu hoặc null nếu kết nối không khả dụng.
   */
  public static Connection getConnection() {
    return conn;
  }

  /**
   * Đóng tài nguyên ResultSet.
   *
   * @param resultSet ResultSet cần đóng.
   */
  public void closeResultSet(ResultSet resultSet) {
    try {
      if (resultSet != null && !resultSet.isClosed()) {
        resultSet.close();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Đóng tài nguyên PreparedStatement.
   *
   * @param preparedStatement PreparedStatement cần đóng.
   */
  public void closePreparedStatement(PreparedStatement preparedStatement) {
    try {
      if (preparedStatement != null && !preparedStatement.isClosed()) {
        preparedStatement.close();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Đóng kết nối cơ sở dữ liệu.
   */
  public void closeConnection() {
    try {
      if (conn != null && !conn.isClosed()) {
        conn.close();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
