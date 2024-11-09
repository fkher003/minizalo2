package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	//bien toan cuc
	public static volatile XuLy xuLy = new XuLy();
	
	public static ThreadNhapXuat threadNhapXuat;
	//bien cuc bo
	private ServerSocket serverSocket = null;
	
	public Server(int port) throws IOException {
		try {
			serverSocket = new ServerSocket(port);
		}catch(IOException e) {
			System.out.println("Lỗi rồi (1): " + e.getMessage());
		}
	}
	
	public void Run() {
		int userID = 1;
		Socket incomingSocket;
		try {
			while(true) {
				System.out.println("Máy chủ: Đang chờ máy trạm đăng nhập");
				
				incomingSocket = serverSocket.accept();
				System.out.println("Máy chủ: Đã có máy trạm" + incomingSocket.getRemoteSocketAddress() + "kết nối vào máy chủ " + incomingSocket.getLocalSocketAddress());
				
				threadNhapXuat = new ThreadNhapXuat(incomingSocket, userID++);
				threadNhapXuat.start();
				
				xuLy.themVao(threadNhapXuat);
				System.out.println("Máy chủ: Số thread đang chạy là: " + xuLy.getKichThuoc());
			}
		}catch(IOException e) {
			System.out.println("Lỗi rồi (2): " + e.getMessage());
		}
		finally {
			try {
				serverSocket.close();
			}catch(IOException e) {
				System.out.println("Lỗi rồi (3): " + e.getMessage());
			}
		}
	}
}
