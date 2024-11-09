package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ThreadNhapXuat extends Thread {
	private Socket socket;
	private int userID;
	
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	private boolean isClosed;
	
	public ThreadNhapXuat(Socket socket, int userID) {
		this.socket = socket;
		this.userID = userID;
		System.out.println("Máy chủ: Máy chạm thứ " + userID + "Đã kết nối");
		isClosed = false;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}
	public void run() {
		try {
			InputStream in = socket.getInputStream();
			InputStreamReader reader = new InputStreamReader(in);
			bufferedReader = new BufferedReader(reader);
			
			OutputStream out = socket.getOutputStream();
			OutputStreamWriter writer = new OutputStreamWriter(out);
			bufferedWriter = new BufferedWriter(writer);
			
			System.out.println("Máy chủ: Mở kênh Nhập - Xuất dữ liệu với máy trạm " +userID + "thành công");
			
			Server.xuLy.guiUserIDchoClient(userID);
			
			Server.xuLy.guiDanhSachUserDangOnline();
			
			Server.xuLy.guiMoiNguoi("capNhapDangNhapDangXuat" + "#~" + "server" + "#~" + " ***username " + userID + "đã đăng nhập");
			
			String thongDiep;
			while(true) {
				thongDiep = nhap();
				if(thongDiep != null)
					Server.xuLy.chuyenTiepThongDiep(thongDiep, getUserID());
			}
		} catch(IOException e) {
			Server.xuLy.loaiRa(userID);
			System.out.println("Máy chủ: Máy trạm " + userID + " đã thoát; Số thread đang chạy là: " + Server.xuLy.getKichThuoc());
			
			Server.xuLy.guiDanhSachUserDangOnline();
			
			Server.xuLy.guiMoiNguoi("capNhatDangNhapDangXuat" + "#~" + "server" + "#~" + "  ***username " + userID + " đã thoát*** ");
			
		}
	}
	public String nhap() throws IOException {
		return bufferedReader.readLine();
	}
	
	public void xuat(String thongDiep) throws IOException {
		bufferedWriter.write(thongDiep);
		bufferedWriter.newLine();
		bufferedWriter.flush();
	}
}
