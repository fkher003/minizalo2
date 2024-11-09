
module miniZalofull {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.sql
	
	opens client to javafx.graphics, javafx.fxml;
}
