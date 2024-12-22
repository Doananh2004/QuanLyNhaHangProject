package vn.trandoananh.quanlynhahang.Models;

public class Member {
  private String hoVaTen;
  private String mssv;
  private String lop;
  private String email;

  public Member(String hoVaTen, String mssv, String lop, String email) {
    this.hoVaTen = hoVaTen;
    this.mssv = mssv;
    this.lop = lop;
    this.email = email;
  }

  public String getHoVaTen() {
    return hoVaTen;
  }

  public void setHoVaTen(String hoVaTen) {
    this.hoVaTen = hoVaTen;
  }

  public String getMssv() {
    return mssv;
  }

  public void setMssv(String mssv) {
    this.mssv = mssv;
  }

  public String getLop() {
    return lop;
  }

  public void setLop(String lop) {
    this.lop = lop;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
