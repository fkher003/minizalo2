package client;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class DieuKhienClient implements Initializable{
	
	@FXML
	private Label labelBan;
	@FXML
	private TextArea textAreaTrucTuyen;
	@FXML
	private TextArea textAreaNoiDung;
	@FXML
	private TextField textFieldSoanThao;
	@FXML
	private ComboBox<String> comboBoxChonNguoiNhan;
	
	//khai bao cac bien cuc bo cua lop 
	private String serverName = "localhost";
	private int port = 3333;
	
	private ThreadNhapXuat t;
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle res) {
		ketNoiMayChu();
	}
	
	//Phuong thuc ket noi den server
	public void ketNoiMayChu() {
		try {
			t = new ThreadNhapXuat(serverName, port, labelBan, textAreaTrucTuyen, textAreaNoiDung, textFieldSoanThao, comboBoxChonNguoiNhan);
			t.start();
			
		} catch(Exception e) {
			System.out.println("Lỗi: " + e.getMessage());
			return;
		}
	}
	
	public void hanhDongGui(ActionEvent event) {
		t.gui();
	}
}
