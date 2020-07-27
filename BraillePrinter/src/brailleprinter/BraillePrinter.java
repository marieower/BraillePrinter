/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brailleprinter;

import static java.awt.Color.red;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/**
 *
 * @author Мария
 */
public class BraillePrinter extends Application {
    
    private final Button btnChangePath = new Button("Изменить");
    private final Button btnCreateSvg = new Button("Создать SVG");
    private final Button btnCreateStl = new Button("Создать STL");
    
    private File path = new File("C:\\");
    private String fileName = "newBraille";
    private String text = "";
    
    private final TextField tfPath = new TextField(path.toString());
    private final TextField tfFileName = new TextField(fileName);
    private final TextArea taText = new TextArea();
       
    private int MAX_CHARS_IN_LINE = 30;
    private int MAX_TEXT_LENGTH = 25*30;
    private int MAX_FILENAME_LENGTH = 20;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Application.launch(BraillePrinter.class, args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        
        GridPane pane = (GridPane)translationScreen();
        
        
        btnCreateSvg.setDisable(true);
        btnCreateStl.setDisable(true);
        
        btnCreateSvg.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                try {
                    createSvg();
                } catch (IOException ex) {
                    showFailMessage("Не удалось записать файл в " + path.toString() 
                            + ".\nВыберите другой путь!");
                }
            }
        });
        
        btnCreateStl.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                try {
                    createStl();
                } catch (IOException ex) {
                    showFailMessage("Не удалось записать файл в " + path.toString() 
                            + ".\nВыберите другой путь!");
                }
            }
        });
        
        btnChangePath.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                changePath();
            }
        });
        
        tfFileName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (isValidFileName(newValue) || newValue.isEmpty()) {
                this.fileName = newValue;
            } else {
                tfFileName.setText(oldValue);
            }
            
            if (newValue.isEmpty()) {
                btnCreateSvg.setDisable(true);
                btnCreateStl.setDisable(true);
            } else {
                btnCreateSvg.setDisable(false);
                btnCreateStl.setDisable(false);
            }
        });
        
        taText.textProperty().addListener((observable, oldValue, newValue) -> {
            if (isValidText(newValue) || newValue.isEmpty()) {
                this.text = newValue;
            } else {
                taText.setText(oldValue);
            }
            
            if (newValue.isEmpty()) {
                btnCreateSvg.setDisable(true);
                btnCreateStl.setDisable(true);
            } else {
                btnCreateSvg.setDisable(false);
                btnCreateStl.setDisable(false);
            }
        });
        
        Image logo = new Image(BraillePrinter.class.getResourceAsStream("/icon.png"));
        
        Scene scene = new Scene(pane, 580, 260); // Manage scene size
        primaryStage.setTitle("Braille Printer");
        primaryStage.getIcons().add(logo);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        
    }
       
    private Pane translationScreen() {
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);  // Override default
        grid.setPadding(new Insets(20, 0, 20, 20));
        grid.setHgap(10);
        grid.setVgap(12);
        
        // labels / hints / inputs / buttons colums size
        grid.getColumnConstraints().add(new ColumnConstraints(80)); 
        grid.getColumnConstraints().add(new ColumnConstraints(20)); 
        grid.getColumnConstraints().add(new ColumnConstraints(300));
        grid.getColumnConstraints().add(new ColumnConstraints(120));
        
        grid.getRowConstraints().add(new RowConstraints(30));
        grid.getRowConstraints().add(new RowConstraints(30));
        grid.getRowConstraints().add(new RowConstraints(30));
        grid.getRowConstraints().add(new RowConstraints(30));
        grid.getRowConstraints().add(new RowConstraints(30));
        
        ImageView hintImage1 = new ImageView(new Image(getClass().getResourceAsStream("/question.png")));
        ImageView hintImage2 = new ImageView(new Image(getClass().getResourceAsStream("/question.png")));
        
        Tooltip ttFileName = new Tooltip();
        ttFileName.setText("Имя файла не должно содержать следующих знаков:\n" + 
                "\\ / * ? \" < > |");

        Tooltip ttText = new Tooltip();
        ttText.setText("Допустимые символы:\n" +
                        "- буквы русского алфавита\n" +
                        "- буквы английского алфавита\n" +
                        "- цифры\n" +
                        "- символ пробела\n" +
                        "- символы . , : ; - ? !");
        
        Label lblPath = new Label("Путь");
        Label lblFileName = new Label("Имя файла");
        
        Label lblText = new Label("Текст");
        
        Label hintFileName = new Label("", hintImage1);
        hintFileName.setTooltip(ttFileName);
        Label hintText = new Label("", hintImage2);
        hintText.setTooltip(ttText);
        
        tfPath.setEditable(false);
        
        grid.add(lblPath, 0, 0);
        grid.add(lblFileName, 0, 1);
        grid.add(lblText, 0, 2);
        
        grid.add(hintFileName, 1, 1);
        grid.add(hintText, 1, 2);
        
        grid.add(tfPath, 2, 0);
        grid.add(tfFileName, 2, 1);
        grid.add(taText, 2, 2, 1, 3);
        
        grid.add(btnChangePath, 3, 0);
        grid.add(btnCreateSvg, 3, 3);
        grid.add(btnCreateStl, 3, 4);
        
        return grid;
    }

    private void changePath() {
        final DirectoryChooser directoryChooser = new DirectoryChooser();
        
        File file = directoryChooser.showDialog(btnChangePath.getScene().getWindow());
        if (file != null) {
            this.path = file;
            this.tfPath.setText(file.toString());
        }
    }
    
    private void setFieldsLimits() {
        taText.setTextFormatter(new TextFormatter<String>(change -> 
            change.getControlNewText().length() <= MAX_CHARS_IN_LINE ? change : null));
        

    }
    
    private boolean isValidFileName(String name) {
        if (name.length() > MAX_FILENAME_LENGTH || name.length() < 1) 
            return false;
        
        String forbiddenSymbols = "\\/*?\"<>|";
        for (int i = 0; i < forbiddenSymbols.length(); i++) {
            if (name.contains(Character.toString(forbiddenSymbols.charAt(i))))
                return false;
        }
        
        return true;
    }
    
    private boolean isValidText(String input) {
        TextProcessor tp = new TextProcessor(input);
        if (tp.preparedText.length() > MAX_TEXT_LENGTH || input.length() < 1) 
            return false;
        
        String permittedSymbols = ".,:;-?! ";
        
        for (int i = 0; i < input.length(); i++) {
            // not a letter, not a digit, not in permittedSymbols list
            if (!Character.isLetterOrDigit(input.charAt(i)) &&
                    !permittedSymbols.contains(Character.toString(input.charAt(i))))
                return false;
        }
        
        return true;
    }
    
    private static void addTextLimiter(final TextField tf, final int maxLength) {
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
    
    private void createSvg() throws IOException {
        FileProcessor fileProcessor;
        fileProcessor = new FileProcessor(this.path);
        File svgFile = fileProcessor.createFile(this.fileName, "svg");
        
        TextProcessor textProcessor;
        textProcessor = new TextProcessor(this.text);
        
        textProcessor.translateToSvg();
        textProcessor.translateToStl();
        
        String svg = textProcessor.getSvg();
        
        fileProcessor.writeToFile(svgFile, svg);
        
        showSuccessMessage(svgFile.getName());
    }
    
    private void createStl() throws IOException {
        FileProcessor fileProcessor;
        fileProcessor = new FileProcessor(this.path);
        File stlFile = fileProcessor.createFile(this.fileName, "stl");
        
        TextProcessor textProcessor;
        textProcessor = new TextProcessor(this.text);
        
        textProcessor.translateToSvg();
        textProcessor.translateToStl();
        
        String svg = textProcessor.getSvg();
        String stl = textProcessor.getStl();
        
        fileProcessor.writeToFile(stlFile, stl);
        
        showSuccessMessage(stlFile.getName());
    }
    
    private void showSuccessMessage(String fileName) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Файл создан");
        alert.setHeaderText("Ура!");
        alert.setContentText("Файл " + fileName + " успешно создан!");
 
        alert.showAndWait();
    }
    
    private void showFailMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Произошла ошибка");
        alert.setHeaderText("Упс!");
        alert.setContentText(message);
 
        alert.showAndWait();
    }
    
    
}