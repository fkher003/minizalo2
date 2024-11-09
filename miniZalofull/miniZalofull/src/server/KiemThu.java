package server;

import java.io.IOException;

public class KiemThu {
	public static void main(String[] args) {
		int port = 3333;
		try {
			Server server = new Server(port);
			server.Run();
		} catch(IOException e) {
			System.out.println("Lỗi rồi: " +e.getMessage());
		}
		
	}
}
