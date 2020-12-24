package GUI;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import javafx.stage.FileChooser.ExtensionFilter;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;

public class MainUI extends Application {
    private TextArea txtInput;
    private Stage pStage;
    private boolean newChanges = false;
    private Font txtFont;

    /**
     * Request to save/load to a file.
     * 
     * @param txt   The TextArea object containing the input being saved
     * @param stage The Stage object of the UI
     * @param type  Boolean, true means save the file and false is load a file
     */
    public void IORequest(TextArea txt, Stage stage, boolean type) {
        FileChooser choose = new FileChooser();
        choose.getExtensionFilters().addAll(new ExtensionFilter("Text File", "*.txt"),
                new ExtensionFilter("All Files", "*.*"));

        if (type) {
            System.out.println("Save requested...");
            choose.setTitle("Save File");
            File fSave = choose.showSaveDialog(stage);
            if (fSave != null) { // need to fix so it maintains formatting
                System.out.println("Writing...");
                try {
                    BufferedWriter writeFile = new BufferedWriter(new FileWriter(fSave));
                    writeFile.write(txt.getText()); // need to maintain FORMATTING!!
                    writeFile.close();

                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            } else {
                System.out.println("cancelled");
            }

        } else {
            System.out.println("Open iniated");
            ButtonType conf = ButtonType.NO;
            if (this.newChanges)
                conf = confirmation();
            if (conf.equals(ButtonType.YES)) {
                IORequest(txt, stage, true); // save the file before opening new one
            }

            if (!conf.equals(ButtonType.CANCEL)) {
                choose.setTitle("Open File");
                File fOpen = choose.showOpenDialog(stage);

                if (fOpen != null) {
                    try {
                        Reader reader = new InputStreamReader(new FileInputStream(fOpen));
                        // come back to this later.
                        StringBuilder sbuff = new StringBuilder();
                        char[] buff = new char[500];
                        for (int cr; (cr = reader.read(buff))!= -1;)
                            sbuff.append(buff, 0, cr);
                        
                        txt.setText(sbuff.toString());


                        reader.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                } else {
                    System.out.println("Cancelled");
                }

            }
        }

    }

    /**
     * Confirmation alert asking to save text file
     * 
     * @return ButtonType  Returns the button choice: Yes, no, cancelled.
     */
    public ButtonType confirmation() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "There are unsaved changes. Would you like to save before closing the current file?", ButtonType.YES,
                ButtonType.NO, ButtonType.CANCEL);

        alert.setTitle("Unsaved Changes");
        ButtonType res = alert.showAndWait().orElse(ButtonType.CANCEL);

        return res;
    }

    /**
     * Formats the text area with the selected type
     * 
     * @param text  TextArea being formatted
     * @param fnt   Font of the textarea
     * @param type
     */
    public void TextFormat(TextArea txt, Font fnt, Stage stage, String type) {

    }

    @Override
    /**
     * Starting the GUI
     * 
     * @param primaryStage The Stage object of the UI
     */
    public void start(Stage primaryStage) {
        this.pStage = new Stage();
        this.pStage.setTitle("JNote - Java-powered Notebook");
        BorderPane bPane = new BorderPane();

        this.txtFont = Font.font("Arial", FontWeight.NORMAL, FontPosture.REGULAR, 15);

        this.txtInput = new TextArea();
        this.txtInput.setFont(this.txtFont);
        this.txtInput.setWrapText(true);

        // ** Menu Objects **
        MenuBar mBar = new MenuBar();

        Menu mFile = new Menu("File");
        Menu mEdit = new Menu("Edit");

        MenuItem mNew = new MenuItem("New");
        MenuItem mSave = new MenuItem("Save");
        MenuItem mOpen = new MenuItem("Open");
        MenuItem mBold = new MenuItem("Bold");
        MenuItem mItalic = new MenuItem("Italics");
        MenuItem mFont = new MenuItem("Fonts");
        MenuItem mFind = new MenuItem("Find");

        mNew.setOnAction(e -> {
            System.out.println("New");
        });
        mSave.setOnAction(e -> {
            IORequest(this.txtInput, this.pStage, true);
        });
        mOpen.setOnAction(e -> {
            IORequest(this.txtInput, this.pStage, false);
        });
        this.pStage.setOnCloseRequest(e -> { // "x" button
            if (this.newChanges) {
                ButtonType res = confirmation();
                if (res.equals(ButtonType.YES)) {
                    System.out.println("Save requeted");
                    IORequest(this.txtInput, this.pStage, true);
                } else if (res.equals(ButtonType.NO))
                    System.out.println("No save. Close ");
                else
                    e.consume();
            }
        });

        mBold.setOnAction(e -> {
            TextFormat(this.txtInput, this.txtFont, this.pStage, ((MenuItem) e.getSource()).getText());
        });
        mItalic.setOnAction(e -> {
            TextFormat(this.txtInput, this.txtFont, this.pStage, ((MenuItem) e.getSource()).getText());
        });
        mFont.setOnAction(e -> {
            TextFormat(this.txtInput, this.txtFont, this.pStage, ((MenuItem) e.getSource()).getText());
        });

        mNew.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
        mSave.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        mOpen.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
        mBold.setAccelerator(new KeyCodeCombination(KeyCode.B, KeyCombination.CONTROL_DOWN));
        mItalic.setAccelerator(new KeyCodeCombination(KeyCode.I, KeyCombination.CONTROL_DOWN));
        mFind.setAccelerator(new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN));

        // ** Add objects **
        mFile.getItems().addAll(mNew, mSave, mOpen);
        mEdit.getItems().addAll(mFont, mBold, mItalic);
        mBar.getMenus().addAll(mFile, mEdit);

        bPane.setTop(mBar);
        bPane.setCenter(this.txtInput);

        // ** Some Initializations **
        Scene myScene = new Scene(bPane, 1030, 600);
        this.pStage.setScene(myScene);
        this.pStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
