package windows;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

@SuppressWarnings("unused")
public class NotificationController implements Initializable {

    @FXML
    private Button close;
    @FXML
    private ListView<String> list;
    
    private String username;
    private List<String> notifications;
    
    @Override
	public void initialize(URL url, ResourceBundle rb) {
    	
    	list.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				
				//ObservableList<String> selection = (ObservableList<String>) list.getSelectionModel().getSelectedItems();
			}
		});
	}

    public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<String> getNotifications() {
		return notifications;
	}
	
	public void setNotifications(List<String> notifications) {
    	this.notifications = notifications;
    	if (notifications == null)
    		return;
    	Iterator<String> iterNotification = notifications.iterator();
    	while (iterNotification.hasNext() != false) {
    		String notification = iterNotification.next();
    		list.getItems().add(notification + "");
    	}
    }
	
	 @FXML
	 void delete(ActionEvent event) {
		if (this.getUsername().compareTo("Admin") == 0)
			MainRun.getAuction().getAdmin().setNotifications(new ArrayList<>());
		else
			MainRun.getAuction().getUsersHT().get(this.getUsername()).setNotifications(new ArrayList<>());
		list.getItems().removeAll(notifications);
		notifications = null;
	 }
    
    @FXML
    void close(ActionEvent event) {
    	Stage Inter = (Stage) close.getScene().getWindow();
    	Inter.close();
    }

}
