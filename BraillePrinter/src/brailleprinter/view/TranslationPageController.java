/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brailleprinter.view;

import brailleprinter.FileProcessor;
import brailleprinter.TextProcessor;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

/**
 * FXML Controller class
 *
 * @author Мария
 */
public class TranslationPageController {
   
    private String inputText;
    private File currentPath;
    private String newFileName = "newBraillePage";
    
    @FXML
    private Label path;

    @FXML
    private Button changePathBtn;
  
//    @FXML
//    private void changePath() {
//        final DirectoryChooser directoryChooser = new DirectoryChooser();
//        
//        File file = directoryChooser.showDialog(changePathBtn.getScene().getWindow());
//        if (file != null) {
//            this.currentPath = file;
//            this.path.setText(file.toString());
//        }
//    }

    @FXML
    private TextField fileName;
    
    @FXML
    private void handleFileNameChange() {
        this.newFileName = fileName.getText();
    }
    
    public static void addTextLimiter(final TextField tf, final int maxLength) {
        tf.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if (tf.getText().length() > maxLength) {
                    String s = tf.getText().substring(0, maxLength);
                    tf.setText(s);
                }
            }
        });
    }
    
    @FXML
    private TextField input;

    @FXML
    private void handleInputChange() {
        this.inputText = input.getText();
    }
    
    @FXML
    private void processText() throws IOException {
        
        FileProcessor fileProcessor;
        fileProcessor = new FileProcessor(this.currentPath);
        
        TextProcessor textProcessor;
        textProcessor = new TextProcessor(this.inputText);
        
        textProcessor.translateToSvg();
        textProcessor.translateToStl();
        
        String svg = textProcessor.getSvg();
        String stl = textProcessor.getStl();
        
        File svgFile = fileProcessor.createFile(this.newFileName, "svg");
        fileProcessor.writeToFile(svgFile, svg);
        
        File stlFile = fileProcessor.createFile(this.newFileName, "stl");
        fileProcessor.writeToFile(stlFile, stl);
        
        showSuccessMessage(stlFile.getName());
    }
    
    private void showSuccessMessage(String fileName) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Файл создан");
        alert.setHeaderText("Ура!");
        alert.setContentText("Файл " + fileName + " успешно создан!");
 
        alert.showAndWait();
    }
    
}
