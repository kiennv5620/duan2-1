/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package da.dao;

import da.helper.JdbcHelper;
import da.model.HocSinh;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Administrator
 */
public class HocSinhDAO {

    JdbcHelper Jdbc = new JdbcHelper();

    private HocSinh readFromResultSet(ResultSet rs) throws SQLException {
        HocSinh model = new HocSinh();
        model.setiD(UUID.fromString(rs.getString("id")));
        model.setMaHS(rs.getString("mahocsinh"));
        model.setHoTen(rs.getString("hoten"));
        model.setGioiTinh(rs.getBoolean("gioitinh"));
        model.setNgaySinh(rs.getDate("ngaysinh"));
        model.setDiaChi(rs.getString("diachi"));
        model.setDienThoai(rs.getString("dienthoai"));
        model.setDanToc(rs.getString("dantoc"));
        model.setTonGiao(rs.getString("tongiao"));
        model.setNgayVD(rs.getDate("ngayvaodoan"));
        model.setNoiSinh(rs.getString("noisinh"));
        model.setCmND(rs.getString("cmnd"));
        model.setLop(rs.getString("lop_id"));
        model.setHotenBo(rs.getString("hoten_bo"));
        model.setHotenMe(rs.getString("hoten_me"));
        model.setDienThoaiBo(rs.getString("dienthoai_bo"));
        model.setDienThoaiMe(rs.getString("dienthoai_me"));
        model.setDvctBo(rs.getString("dv_cong_tac_bo"));
        model.setDvctMe(rs.getString("dv_cong_tac_me"));
        model.setNguoiDamHo(rs.getString("nguoidamho"));
        model.setTrangThai(rs.getBoolean("trangthai"));
        model.setAnh(rs.getString("anh"));

        return model;

    }

    private List<HocSinh> select(String sql, Object... args) {
        List<HocSinh> list = new ArrayList<>();
        try {
            ResultSet rs = null;

            rs = JdbcHelper.executeQuery(sql, args);
            while (rs.next()) {
                HocSinh model = readFromResultSet(rs);
                list.add(model);

            }
            rs.getStatement().getConnection().close();

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return list;

    }

    public ResultSet select() {
        String sql = "select max(id)  from hocsinh";
        try {
            PreparedStatement ps = Jdbc.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (Exception ex) {
            throw new RuntimeException(ex);

        }
    }

    public ResultSet select2() {
        String sql = "select mahocsinh from hocsinh";
        try {
            PreparedStatement ps = Jdbc.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (Exception ex) {
            throw new RuntimeException(ex);

        }
    }
    
    public HocSinh select3(String mahocsinh) {
        String sql = "select * from hocsinh where mahocsinh=?";
        List<HocSinh> list = select(sql, mahocsinh);
        return list.size() >0 ? list.get(0) : null;
    }

    public ResultSet loadWith2(String tenlop, boolean ki, String nienhoc) {
        String sql = "select hs.mahocsinh,hs.hoten,hs.gioitinh,hs.ngaysinh from hocsinh as hs  join lophoc as lh on hs.lop=lh.malop  join phancong as pc on lh.manamhoc=pc.manamhoc  join namhoc as nh on pc.manamhoc=nh.manamhoc   and lh.tenlop=? and pc.hocki=? and nh.nienhoc=? group by hs.mahocsinh,hs.hoten,hs.gioitinh,hs.ngaysinh ";
        try {
            PreparedStatement ps = Jdbc.prepareStatement(sql);
            ps.setString(1, tenlop);
            ps.setBoolean(2, ki);
            ps.setString(3, nienhoc);

            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (Exception ex) {
            throw new RuntimeException(ex);

        }
    }

    public ResultSet selectWithMaHS(String mahocsinh) {
        String sql = "select hs.mahocsinh,hs.hoten,hs.gioitinh,hs.ngaysinh,hs.diachi,hs.dienthoai,hs.dantoc,hs.tongiao,hs.ngayvaodoan,hs.noisinh,hs.cmnd,hs.hotenBo,hs.hotenMe,hs.dienthoaiBo,hs.dienthoaiMe,hs.dvCongTacBo,hs.dvCongTacMe,hs.nguoidamho,hs.trangthai,hs.anh,lh.tenlop from hocsinh as hs join lophoc as lh  on hs.lop=lh.malop and hs.mahocsinh=?";
        try {
            PreparedStatement ps = Jdbc.prepareStatement(sql);
            ps.setString(1, mahocsinh);

            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (Exception ex) {
            throw new RuntimeException(ex);

        }
    }

    public ResultSet selectWithMaGV(String magiaovien) {
        String sql = "select gv.hoten,lh.tenlop from giaovien as gv  join phancong as pc on gv.id=pc.giaovien_id join lophoc as lh on pc.lop_id=lh.id and gv.magiaovien=?";
        try {
            PreparedStatement ps = Jdbc.prepareStatement(sql);
            ps.setString(1, magiaovien);

            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (Exception ex) {
            throw new RuntimeException(ex);

        }
    }

    public HocSinh findByName(String tl) {
        String sql = " select * from hocsinh  join lophoc on hocsinh.lop_id=lophoc.malop and lophoc.tenlop= ?";
        List<HocSinh> list = select(sql, tl);
        return list.size() > 0 ? list.get(0) : null;
    }

    public void insert(HocSinh model) {
        SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd");
        String sql = "insert into hocsinh(mahocsinh,hoten,gioitinh,ngaysinh,diachi,dienthoai,dantoc,tongiao,ngayvaodoan,noisinh,cmnd,lop,hotenBo,hotenMe,dienthoaiBo,dienthoaiMe,dvCongTacBo,dvCongTacMe,nguoidamho,trangthai,anh) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        JdbcHelper.executeUpdate(sql, model.getMaHS(), model.getHoTen(), model.getGioiTinh(), sfd.format(model.getNgaySinh()), model.getDiaChi(), model.getDienThoai(), model.getDanToc(), model.getTonGiao(), sfd.format(model.getNgayVD()), model.getNoiSinh(), model.getCmND(), model.getLop(), model.getHotenBo(), model.getHotenMe(), model.getDienThoaiBo(), model.getDienThoaiMe(), model.getDvctBo(), model.getDvctMe(), model.getNguoiDamHo(), model.isTrangThai(), model.getAnh());
    }

    public void update(HocSinh model) {
        SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd");
        String sql = "update hocsinh set hoten=?,gioitinh=?,ngaysinh=?,diachi=?,dienthoai=?,dantoc=?,tongiao=?,ngayvaodoan=?,noisinh=?,cmnd=?,lop=?,hotenBo=?,hotenMe=?,dienthoaiBo=?,dienthoaiMe=?,dvCongTacBo=?,dvCongTacMe=?,nguoidamho=?,trangthai=?,anh=? where mahocsinh=?";
        JdbcHelper.executeUpdate(sql, model.getHoTen(), model.getGioiTinh(), sfd.format(model.getNgaySinh()), model.getDiaChi(), model.getDienThoai(), model.getDanToc(), model.getTonGiao(), sfd.format(model.getNgayVD()), model.getNoiSinh(), model.getCmND(), model.getLop(), model.getHotenBo(), model.getHotenMe(), model.getDienThoaiBo(), model.getDienThoaiMe(), model.getDvctBo(), model.getDvctMe(), model.getNguoiDamHo(), model.isTrangThai(), model.getAnh(), model.getMaHS());
    }

    public ResultSet selectSiSoTong(String tenLop, boolean ki, String nienhoc) {
        String sql = "select count(*) as ss from hocsinh join lophoc as lh on hocsinh.lop_id=lh.id join phancong as pc on lh.id=pc.lop_id join namhoc as nh on pc.namhoc_manamhoc=nh.manamhoc and lh.tenlop=? and hocsinh.trangthai=true and pc.hocki=? and nh.nienhoc=?";
        try {
            PreparedStatement ps = Jdbc.prepareStatement(sql);
            ps.setString(1, tenLop);
            ps.setBoolean(2, ki);
            ps.setString(3, nienhoc);
            ResultSet rs = ps.executeQuery();

            return rs;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public ResultSet selectSiSoNam(String tenLop, boolean ki, String nienhoc) {
        String sql = "select count(*) as ss from hocsinh join lophoc as lh on hocsinh.lop_id=lh.id join phancong as pc on lh.id=pc.lop_id join namhoc as nh on pc.namhoc_manamhoc=nh.manamhoc and lh.tenlop=? and hocsinh.trangthai=true and pc.hocki=? and nh.nienhoc=? and hocsinh.gioitinh=true";
        try {
            PreparedStatement ps = Jdbc.prepareStatement(sql);

            ps.setString(1, tenLop);
            ps.setBoolean(2, ki);
            ps.setString(3, nienhoc);
            ResultSet rs = ps.executeQuery();

            return rs;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

}
