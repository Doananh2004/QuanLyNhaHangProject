package vn.trandoananh.quanlynhahang.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import vn.trandoananh.quanlynhahang.Models.Member;

import java.util.Objects;

public class AboutController {
  @FXML
  private TableView<Member> tblInfoMember;

  @FXML
  private TableColumn<Member, String> hoVaTenColumn;

  @FXML
  private TableColumn<Member, String> mssvColumn;

  @FXML
  private TableColumn<Member, String> lopColumn;

  @FXML
  private TableColumn<Member, String> emailColumn;

  @FXML
  private ImageView logoImage;

  public void initialize() {
    // Liên kết các cột với thuộc tính của lớp Member
    hoVaTenColumn.setCellValueFactory(new PropertyValueFactory<>("hoVaTen"));
    mssvColumn.setCellValueFactory(new PropertyValueFactory<>("mssv"));
    lopColumn.setCellValueFactory(new PropertyValueFactory<>("lop"));
    emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

    // Tạo danh sách dữ liệu
    ObservableList<Member> members = FXCollections.observableArrayList(
        new Member("Trần Doãn Anh", "64130078", "64.CNTT-CLC2", "anh.td.64cntt@ntu.edu.vn")
    );

    // Thêm dữ liệu vào bảng
    tblInfoMember.setItems(members);

    // Hiển thị logo
    logoImage.setImage(new Image(Objects.requireNonNull(getClass().getResource("/vn/trandoananh/images/restaurant.png")).toString()));
  }
}
