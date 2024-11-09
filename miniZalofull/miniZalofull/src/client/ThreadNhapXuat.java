package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ThreadNhapXuat extends Thread{
	private String serverName;
	private int port;
	
	Label labelBan;
	TextArea textAreaTrucTuyen;
	TextArea textAreaNoiDung;
	TextField textFieldSoanThao;
	ComboBox<String> comboBoxChonNguoiNhan;
	
	private Socket socket;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	private int userID;
	
	public ThreadNhapXuat(String serverName, int port, Label labelBan, TextArea textAreaTrucTuyen, TextArea textAreaNoiDung, TextField textFieldSoanThao, ComboBox<String> comboBoxChonNguoiNhan) {
		this.serverName = serverName;
		this.port = port;
		this.labelBan = labelBan;
		this.textAreaTrucTuyen = textAreaTrucTuyen;
		this.textAreaNoiDung = textAreaNoiDung;
		this.textFieldSoanThao = textFieldSoanThao;
		this.comboBoxChonNguoiNhan = comboBoxChonNguoiNhan;
	}
	
	@Override
	public void run() {
		try {
			
			socket = new Socket(serverName, port);
			System.out.println("Kết nối thành công");
			
			//mở kênh nhập dữ liệu
			InputStream in = socket.getInputStream();
			InputStreamReader reader = new InputStreamReader(in);
			bufferedReader = new BufferedReader(reader);
			
			//Mở kênh xuất dữ liệu
			OutputStream out = socket.getOutputStream();
			OutputStreamWriter writer = new OutputStreamWriter(out);
			bufferedWriter = new BufferedWriter(writer);
			
			System.out.println("Máy trạm: Mở kênh nhập xuất dữ liệu với máy chủ thành công!!");
			//Liên tục nhập dữ liệu từ kênh sau đó phân tích cú pháp của thông điệp để hiển thị lên client
			nhan();
			
			socket.close();
		}catch(IOException e) {
			System.out.println("Lỗi rồi: " + e.getMessage());
			return;
		}
	}
	
	public void nhan() {
		String thongDiepNhan;
		while(true) {
			try {
				thongDiepNhan = bufferedReader.readLine();
				
				if(thongDiepNhan != null) {
					String[] phanTachThongDiep = thongDiepNhan.split("#~");
					
					
					if(phanTachThongDiep[0].equals("userID")) {
						userID = Integer.parseInt(phanTachThongDiep[2]);
						labelBan.setText("Bạn: username " + userID);
					} else if(phanTachThongDiep[0].equals("capNhatDSOnline")) {
						capNhatDSOnline(phanTachThongDiep[2]);
					} else if(phanTachThongDiep[0].equals("capNhatDangNhapDangXuat")) {
						textAreaNoiDung.setText(textAreaNoiDung.getText() + phanTachThongDiep[2] + "\n");
					} else if(phanTachThongDiep[0].equals("guiMotNguoi")) {
						textAreaNoiDung.setText(textAreaNoiDung.getText() + "username" + phanTachThongDiep[1] + ": " + phanTachThongDiep[2] + "\n");
					} else if(phanTachThongDiep[0].equals("guiMoiNguoi")) {
						textAreaNoiDung.setText(textAreaNoiDung.getText() + "username " + phanTachThongDiep[1] + " (gửi mọi người): " + phanTachThongDiep[2] + "\n");
					}
				}
			}catch(IOException e) {
				System.out.println("Lỗi rồi: " + e.getMessage());
				return;
			}
		}
	}
	
	public void capNhatDSOnline(String danhSachOnline) {
		String[] tachDanhSachOnline = danhSachOnline.split("-");
		String usernameOnline = "";
		List<String> dsOnline = new ArrayList<>();
		
		for(String i : tachDanhSachOnline) {
			dsOnline.add(i);
			usernameOnline += "username " + i + "\n";
		}
		
		textAreaTrucTuyen.setText(usernameOnline);
		
		capNhatComboBoxChonNguoiNhan(dsOnline);
	}
	
	public void capNhatComboBoxChonNguoiNhan(List<String> onlineList) {
		comboBoxChonNguoiNhan.getItems().clear();
		comboBoxChonNguoiNhan.setPromptText("Chọn người nhận");
		comboBoxChonNguoiNhan.getItems().addAll("Mọi người");
		for(String i : onlineList) {
			if(!i.equals("" + userID)) {
				comboBoxChonNguoiNhan.getItems().addAll("username " + i);
			}
		}
	}
	
	public void gui() {
		String thongDiep = textFieldSoanThao.getText();
		String diaChiDich = comboBoxChonNguoiNhan.getValue();
		
		if(thongDiep.isBlank() || diaChiDich == null) {
			thongBao("Bạn chưa nhập thông điệp hoặc chưa chọn người nhận");
		} else {
			if(diaChiDich.equals("Mọi người")) {
				xuat("guiMoiNguoi" + "#~" + userID + "#~" + thongDiep);
				
				textAreaNoiDung.setText(textAreaNoiDung.getText() + "Bạn (gửi mọi người): " + thongDiep + "\n");
			} else {
				String[] diaChiDichSplit = diaChiDich.split(" ");
				xuat("guiMotNguoi" + "#~" + userID + "#~" + thongDiep + "#~" + diaChiDichSplit[1]);
				
				textAreaNoiDung.setText(textAreaNoiDung.getText() + "Bạn (gửi username "+ diaChiDichSplit[1] + "): ");
			}
			textFieldSoanThao.setText("");
		}
	}
	public void xuat(String thongDiep) {
		try {
			bufferedWriter.write(thongDiep);
			bufferedWriter.newLine();
			bufferedWriter.flush();
		}catch(IOException e) {
			System.out.println("Lỗi rồi: " + e.getMessage());
			return;
		}
	}
	public void thongBao(String tb) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Thông báo");
		alert.setHeaderText(null);
		alert.setContentText(tb);
		alert.showAndWait();
	}
}
