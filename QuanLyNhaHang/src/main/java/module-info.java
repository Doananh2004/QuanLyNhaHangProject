module vn.trandoananh.quanlynhahang {
  requires javafx.controls;
  requires javafx.fxml;
  requires java.sql;


  opens vn.trandoananh.quanlynhahang to javafx.fxml;
  exports vn.trandoananh.quanlynhahang;
}