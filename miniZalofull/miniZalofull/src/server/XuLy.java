package server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XuLy {
	private List<ThreadNhapXuat> danhSachThreadNhapXuat;
	
	public XuLy() {
        danhSachThreadNhapXuat = new ArrayList<>();
    }
	
	public int getKichThuoc() {
		return danhSachThreadNhapXuat.size();
	}
	
	public void themVao(ThreadNhapXuat threadNhapXuat) {
		danhSachThreadNhapXuat.add(threadNhapXuat);
	}
	
	public void loaiRa(int userID) {
		for(ThreadNhapXuat threadNhapXuat : danhSachThreadNhapXuat) {
			if(threadNhapXuat.getUserID() == userID) {
				danhSachThreadNhapXuat.remove(threadNhapXuat);
				break;
			}
		}
	}
	
	/**
	 * Gửi cho client với nội dung là số userID để client biết mã ID của chính mình
	 * @param userID Định danh của chính mình
	 */
	public void guiUserIDchoClient(int userID) {
		guiMotNguoi("userID" + "#~" + "server" + "#~" + userID + "#~" +userID, userID);
	}
	
	public void guiDanhSachUserDangOnline() {
		String st = "";
		for(ThreadNhapXuat threadNhapXuat : danhSachThreadNhapXuat) {
			st += threadNhapXuat.getUserID();
		}
		guiMoiNguoi("capNhatDSOnline" + "#~" + "server" + "#~" + st);
	}
	
	/**
	 * Gửi cho tất cả, bao gồm chính mình
	 * 
	 * @param thongDiep Thông điệp cần gửi
	 * @param id 		Định danh chính mình
	 */
	public void guiMoiNguoi(String thongDiep) {
		for(ThreadNhapXuat threadNhapXuat : danhSachThreadNhapXuat) {
				try {
					threadNhapXuat.xuat(thongDiep);
				}catch(IOException e) {
					System.out.println("Lỗi rồi: " + e.getMessage());
				}
		}
	}
	
	
	/**
	 * Gửi cho tất cả, trừ userID chính mình
	 * 
	 * @param thongDiep Thông điệp cần gửi (message)
	 * @param userID	Định danh chính mình(người gửi)
	 */
	public void guiMoiNguoi(String thongDiep, int userID) {
		for(ThreadNhapXuat threadNhapXuat : danhSachThreadNhapXuat) {
			if(threadNhapXuat.getUserID() != userID) {
				try {
					threadNhapXuat.xuat(thongDiep);
				}catch(IOException e) {
					System.out.println("Lỗi rồi: " + e.getMessage());
				}
			}
		}
	}
	public void guiMotNguoi(String thongDiep, int userID) {
		for(ThreadNhapXuat threadNhapXuat : danhSachThreadNhapXuat) {
			if(threadNhapXuat.getUserID() == userID) {
				try {
					threadNhapXuat.xuat(thongDiep);
					break;
				}catch(IOException e) {
					System.out.println("Lỗi rồi: " + e.getMessage());
				}
			}
		}
	}
	
	
	/**
	 * Chuyển tiếp thông điệp từ 1 client khác nhau đến nhiều client
	 * @param thongDiep Thông tin cần gửi
	 * @param userID Định danh chính mình(người gửi)
	 */
	public void chuyenTiepThongDiep(String thongDiep, int userID) {
		String[] tachThongDiep = thongDiep.split("#~");
		if(tachThongDiep[0].equals("guiMoiNguoi")) {
			guiMoiNguoi("guiMoiNguoi" + "#~" + tachThongDiep[1] + "#~" + tachThongDiep[2], userID);
		}
		if(tachThongDiep[0].equals("guiMotNguoi")) {
			guiMotNguoi("guiMotNguoi" + "#~" + tachThongDiep[1] + "#~" + tachThongDiep[2] + "#~" + tachThongDiep[3], Integer.parseInt(tachThongDiep[3]));
		}
	}
}
