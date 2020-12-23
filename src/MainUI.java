package texteditor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.lang.model.util.ElementScanner6;
import javax.swing.Action;
import javafx.stage.FileChooser.ExtensionFilter;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class MainUI extends Application {
    private TextArea txtInput;
    private Stage pStage;
    private boolean newChanges = false;

    public void IORequest(TextArea txt, Stage stage, boolean type) {
        FileChooser choose = new FileChooser();
        choose.getExtensionFilters().addAll(new ExtensionFilter("Text File", "*.txt"),
                new ExtensionFilter("All Files", "*.*"));

        if (type) {
            System.out.println("Save requested...");
            choose.setTitle("Save File");
            File fSave = choose.showSaveDialog(stage);
            if (fSave != null) {
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
            System.out.println("Open iniat");
            choose.setTitle("Open File");
            File fOpen = choose.showOpenDialog(stage);

            if (fOpen != null) {
                try {
                    txt.setText("");
                    BufferedReader rFile = new BufferedReader(new FileReader(fOpen));
                    String curr = "";

                    rFile.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            } else {
                System.out.println("Cancelled");
            }

        }

    }

    // add javadocs
    @Override
    public void start(Stage primaryStage) {
        this.pStage = new Stage();
        this.pStage.setTitle("Text Editor");

        BorderPane bPane = new BorderPane();

        Font txtFont = Font.font("Arial", FontWeight.NORMAL, FontPosture.REGULAR, 15);

        this.txtInput = new TextArea();
        this.txtInput.setFont(txtFont);
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
        this.pStage.setOnCloseRequest(e -> {
            if (this.newChanges) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to save changes before closing?",
                        ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
                
                alert.setTitle("Unsaved Changes");
                ButtonType res = alert.showAndWait().orElse(ButtonType.CANCEL);
                if (res.equals(ButtonType.YES)) {
                    System.out.println("Save requeted");
                    IORequest(this.txtInput, this.pStage, true);
                } else if (res.equals(ButtonType.NO))
                    System.out.println("No save. Close ");
                else
                    e.consume();

            }
        });

        mBold.setOnAction(new FontAction(this.pStage, txtFont, this.txtInput));
        mItalic.setOnAction(new FontAction(this.pStage, txtFont, this.txtInput));
        mFont.setOnAction(new FontAction(this.pStage, txtFont, this.txtInput));

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

class FontAction implements EventHandler<ActionEvent> {
    private Font fnt;
    private Stage sg;
    private TextArea txt;

    public FontAction(Stage sg, Font fnt, TextArea txt) {
        this.sg = sg;
        this.fnt = fnt;
        this.txt = txt;
    }

    @Override
    public void handle(ActionEvent e) {
        String checker = ((MenuItem) e.getSource()).getText();
        switch (checker) {
            case "Bold":
                System.out.println("Bold requested...");
                if (!fnt.getStyle().equals("Bold"))
                    fnt = Font.font(fnt.getFamily(), FontWeight.BOLD, fnt.getSize());
                else
                    fnt = Font.font(fnt.getFamily(), FontWeight.NORMAL, fnt.getSize());

                txt.setFont(fnt);
                break;

            case "Italics":
                System.out.println("Italics requested...");

                break;

        }

    }

}
