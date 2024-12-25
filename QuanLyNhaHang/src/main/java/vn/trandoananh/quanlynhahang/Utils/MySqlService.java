package vn.trandoananh.quanlynhahang.Utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class MySqlService {
  public Connection conn;
  /**
   * Phương thức khởi tạo để thiết lập kết nối cơ sở dữ liệu.
   */
  public MySqlService() {
    String URL = "jdbc:mysql://127.0.0.1:3306/database_quanlynhahang";
    String USER = "root";
    String PASSWORD = "";
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
      conn = DriverManager.getConnection(URL, USER, PASSWORD);
    } catch (Exception e) {
      e.printStackTrace();
      conn = null;
    }
  }
}
