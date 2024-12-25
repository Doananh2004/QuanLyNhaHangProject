package vn.trandoananh.quanlynhahang;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import vn.trandoananh.quanlynhahang.Utils.MySqlService;

public class AppQuanLyNhaHang extends Application {
  public static Stage primaryStage;
  MySqlService database = new MySqlService();

  @Override
  public void start(Stage stage) {
    primaryStage = stage;
    primaryStage.setTitle("App Quản Lý Nhà Hàng");
    if(database.conn != null){
      try {
        showUI("/vn/trandoananh/quanlynhahang/gui/QuanLyNhaHangGUI.fxml");
      } catch (Exception e) {
        throw new RuntimeException(e);
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

  public static Stage getPrimaryStage() {
    return primaryStage;
  }

  // Generic method to switch between UIs
  public static void showUI(String fxmlFile) throws Exception {
    FXMLLoader loader = new FXMLLoader(AppQuanLyNhaHang.class.getResource(fxmlFile));
    Parent root = loader.load();
    Scene scene = new Scene(root);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}