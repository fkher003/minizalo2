package client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class KiemThuClient extends Application{
	@Override
	public void start(Stage sanKhau) {
		try {
			Parent giaoDien = FXMLLoader.load(getClass().getResource("viewClient.fxml"));
			
			Scene canhVat = new Scene(giaoDien);
			sanKhau.setTitle("Ứng dụng - MiniZalo");
			sanKhau.setScene(canhVat);
			sanKhau.show();
			sanKhau.setResizable(false);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
