package vn.trandoananh.quanlynhahang;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import vn.trandoananh.quanlynhahang.Utils.MySqlService;

import java.sql.Connection;

public class AppQuanLyNhaHang extends Application {
  public static Stage primaryStage;
  private Connection conn;

  @Override
  public void start(Stage stage) {
    primaryStage.setTitle("App Quản Lý Nhà Hàng");
    conn = MySqlService.getConnection();
    if(conn != null){
      try {
        showUI("/vn/trandoananh/quanlynhahang/QuanLyNhaHangGUI.fxml");
      }catch (Exception e){
        e.printStackTrace();
      }
    } else {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Error");
      alert.setHeaderText(null);
      alert.setContentText("Kết nối cơ sở dữ liệu không thành công!");
      alert.showAndWait();
      System.exit(0);
    }
    primaryStage.show();
  }

  // Generic method to switch between UIs
  public static void showUI(String fxmlFile) throws Exception {
    FXMLLoader loader = new FXMLLoader(AppQuanLyNhaHang.class.getResource(fxmlFile));
    Scene scene = new Scene(loader.load());
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}