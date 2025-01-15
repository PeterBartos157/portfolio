package windows;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class SuccessController {

    @FXML
    private Label info1;

    @FXML
    private Label info2;

    @FXML
    private AnchorPane success;

    @FXML
    void close0(ScrollEvent event) {
    	Stage successInter = (Stage) success.getScene().getWindow();
    	successInter.close();
    	return;
    }
    
    public void setLabel(String info) {
    	this.info1.setText(info);
    	this.info2.setText("");
    }
    
    public void setLabel(String info1, String info2) {
    	this.info1.setText(info1);
    	this.info2.setText(info2);
    }
    
    @FXML
    void close1(KeyEvent event) {
    	Stage successInter = (Stage) success.getScene().getWindow();
    	successInter.close();
    	return;
    }
    
    @FXML
    void close2(MouseEvent event) {
    	Stage successInter = (Stage) success.getScene().getWindow();
    	successInter.close();
    	return;
    }

}
