module vn.trandoananh.quanlynhahang {
  requires javafx.controls;
  requires javafx.fxml;
  requires java.sql;
  requires mysql.connector.j;

  opens vn.trandoananh.quanlynhahang to javafx.fxml;
  exports vn.trandoananh.quanlynhahang;
  // Export the package containing your controller
  exports vn.trandoananh.quanlynhahang.Controller to javafx.fxml;
  opens vn.trandoananh.quanlynhahang.Controller to javafx.fxml; // Reflection access
  exports vn.trandoananh.quanlynhahang.Models;
}