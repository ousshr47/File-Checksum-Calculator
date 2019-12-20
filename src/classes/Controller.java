package classes;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.xml.bind.DatatypeConverter;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;

import eu.hansolo.enzo.notification.Notification.Notifier;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import utils.Hash;

public class Controller implements Initializable
{
    @FXML
    private JFXButton join;

    @FXML
    private Label path;
    
    @FXML
    private JFXTextArea hash;

    @FXML
    private JFXButton encrypt;

    @FXML
    private JFXButton copy;
    
    @FXML
    private JFXComboBox<String> algo;
    
    final FileChooser fileChooser = new FileChooser();
    
    private File file = new File("");

	public static Stage stage;
	
	String abspath = "";
	
	File file2 = new File("");
	
	public String nomFichier;
	
	public String filehash = "";

    private void setExtFilters(FileChooser chooser) 
	{
		chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All files", "*.*"));
	}
    
    private static String toHex(byte[] bytes) 
	{
		return DatatypeConverter.printHexBinary(bytes);
	}
    
    @FXML
    void Calculate(ActionEvent event) 
    {
    	if (nomFichier == null || nomFichier.length() == 0) 
    	{
			Notifier.setOffsetX(825);
			Notifier.setOffsetY(400);
			Notifier.INSTANCE.notifyError("Error", "No file selected !!");
		}
    	if (algo.getValue()== null || algo.getValue().length() == 0) 
        {
    		Notifier.setOffsetX(825);
			Notifier.setOffsetY(400);
			Notifier.INSTANCE.notifyError("Error", "No algorithm selected !!");
        } 
    	else
    	{
    		String path2 = file.getAbsolutePath();
			File inputFile = new File(path2);
			String algotype = algo.getValue();
			
			if (algotype.equals("MD5"))
			{
				filehash = toHex(Hash.MD5.checksum(inputFile));
				hash.setVisible(true);
				hash.setText(filehash);
			}
			else if (algotype.equals("SHA-1"))
			{
				filehash = toHex(Hash.SHA1.checksum(inputFile));
				hash.setVisible(true);
				hash.setText(filehash);
			}
			else if (algotype.equals("SHA-256"))
			{
				filehash = toHex(Hash.SHA256.checksum(inputFile));
				hash.setVisible(true);
				hash.setText(filehash);
			}
			else if (algotype.equals("SHA-512"))
			{
				filehash = toHex(Hash.SHA512.checksum(inputFile));
				hash.setVisible(true);
				hash.setText(filehash);
			}
    	}
    }

    @FXML
    void Copy(ActionEvent event) 
    {
    	copyToClipboardText(filehash);
    	Notifier.setOffsetX(825);
		Notifier.setOffsetY(400);
		Notifier.INSTANCE.notifySuccess("Success", "Hash copied to clipboard !!");
    }

    @FXML
    void Upload(ActionEvent event) throws IOException, InterruptedException 
    {
    	setExtFilters(fileChooser);
		file = fileChooser.showOpenDialog(stage);
		fileChooser.setTitle("Browse");
		if (file != null) 
		{
			abspath = file.getAbsolutePath();
			file2 = file;
			nomFichier = file.getName();
			System.out.println(nomFichier);
			path.setVisible(true);
			path.setText(file.getName());
		}
    }
    
    @Override
	public void initialize(URL location, ResourceBundle resources) 
	{
		path.setVisible(false);
		hash.setVisible(false);
		algo.getItems().addAll("MD5","SHA-1","SHA-256","SHA-512");
	}
    
    public static void copyToClipboardText(String s) 
    {
		final Clipboard clipboard = Clipboard.getSystemClipboard();
		final ClipboardContent content = new ClipboardContent();
		content.putString(s);
		clipboard.setContent(content);
	}

}
