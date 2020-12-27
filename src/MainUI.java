
/**
 * The main program to run the text editor. The GUI is implemented here
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Optional;

import javafx.stage.FileChooser.ExtensionFilter;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import Formats.*;

public class MainUI extends Application {
    private TextArea txtInput;
    private Stage pStage;
    private boolean newChanges = false;
    private boolean isBold = false;
    private boolean isItalic = false;
    private String currFamily = Font.getDefault().getFamily();
    private List<String> familyList = Font.getFamilies();
    private double fsize = 15;
    private int cCount = 0;
    private Label lblChar;

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
            String formats = "";
            File fSave = choose.showSaveDialog(stage);
            if (fSave != null) { // need to fix so it maintains formatting
                System.out.println("Writing...");
                try {
                    BufferedWriter writeFile = new BufferedWriter(new FileWriter(fSave));
                    writeFile.write(txt.getText()); // need to maintain FORMATTING!!
                    if (isBold) {
                        formats = formats + "[b]";
                    }
                    if (isItalic) {
                        formats = formats + "[i]";
                    }
                    writeFile.write(formats);
                    writeFile.close();
                    this.newChanges = false;

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
                        for (int cr; (cr = reader.read(buff)) != -1;) {
                            sbuff.append(buff, 0, cr);
                        }
                        int bDex = sbuff.lastIndexOf("[b]");
                        int iDex = sbuff.lastIndexOf("[i]");

                        this.isBold = bDex != -1 ? true : false;
                        this.isItalic = iDex != -1 ? true : false;

                        this.txtInput
                                .setFont(Font.font(this.currFamily, this.isBold ? FontWeight.BOLD : FontWeight.NORMAL,
                                        this.isItalic ? FontPosture.ITALIC : FontPosture.REGULAR, this.fsize));
                        txt.setText(sbuff.toString());
                        reader.close();
                        this.newChanges = false;
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
     * Confirmation alert asking to save unchanged work
     * 
     * @return ButtonType Returns the button choice: Yes, no, cancelled.
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
     * Formats the text area with selected formatting
     * 
     * @param text TextArea being formatted
     * @param fnt  Font of the textarea
     * @param type String containing the type of formatting
     */
    public void TextFormat(TextArea txt, Stage stage, String type) {
        FormatRequest fReqs = new FormatRequest();

        switch (type.toLowerCase()) {
            case "bold":
                fReqs.addFormat(new BoldCmd(txt, this.currFamily, this.fsize, this.isBold, this.isItalic));
                this.isBold = !this.isBold;
                break;
            case "italics":
                fReqs.addFormat(new ItalicsCmd(txt, this.currFamily, this.fsize, this.isBold, this.isItalic));
                this.isItalic = !this.isItalic;
                break;

            case "fonts":
                System.out.println("Fonts Requested");
                ChoiceDialog<String> fchoices = new ChoiceDialog<>(this.currFamily, this.familyList);
                fchoices.setGraphic(null);
                Optional<String> result = fchoices.showAndWait();
                if (result.isPresent())
                    this.currFamily = result.get();
                fReqs.addFormat(new FontCmd(txt, this.currFamily, this.fsize, this.isBold, this.isItalic));
                break;
            case "size":
                System.out.println("Size requested");
                TextInputDialog inp = new TextInputDialog(Double.toString(this.fsize));
                inp.setTitle("Enter a new font size");
                inp.setContentText("Font Size");
                inp.setHeaderText(null);
                inp.setGraphic(null);
                Optional<String> res = inp.showAndWait();
                this.txtInput.deselect();
                if (res.isPresent() && res.get().matches("^\\d*.\\d*$")) {
                    this.fsize = Double.parseDouble(res.get());
                }
                fReqs.addFormat(new FontCmd(txt, this.currFamily, this.fsize, this.isBold, this.isItalic));
                break;
        }
        fReqs.applyFormats();

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
        this.txtInput = new TextArea();
        this.txtInput.setWrapText(true);
        this.txtInput.setFont(Font.font(this.currFamily, this.isBold ? FontWeight.BOLD : FontWeight.NORMAL,
                this.isItalic ? FontPosture.ITALIC : FontPosture.REGULAR, this.fsize));
        cCount = 0;

        // todo: add right clicks

        // ** Menu Objects **
        MenuBar mBar = new MenuBar();
        Menu mFile = new Menu("File");
        Menu mEdit = new Menu("Edit");
        Menu mPref = new Menu("Preferences");
        Menu mFind = new Menu("Find");
        MenuItem mNew = new MenuItem("New");
        MenuItem mSave = new MenuItem("Save");
        MenuItem mOpen = new MenuItem("Open");
        MenuItem mBold = new MenuItem("Bold");
        MenuItem mItalic = new MenuItem("Italics");
        MenuItem mFont = new MenuItem("Fonts");
        MenuItem mColor = new MenuItem("Color");
        MenuItem mSize = new MenuItem("Size");

        mFile.getItems().addAll(mNew, mSave, mOpen);
        mEdit.getItems().addAll(mFont, mColor, mBold, mItalic, mSize);
        mBar.getMenus().addAll(mFile, mEdit, mPref, mFind);

        // ** Other Objects**

        lblChar = new Label("Characters: " + cCount);
        lblChar.setPadding(new Insets(5));

        // ** Initialize Actions **
        mNew.setOnAction(e -> {
            if (this.newChanges) {
                ButtonType conf = confirmation();
                if (conf.equals(ButtonType.YES))
                    IORequest(this.txtInput, this.pStage, true);
                if (!conf.equals(ButtonType.CANCEL))
                    this.txtInput.setText("");
            }
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
            TextFormat(this.txtInput, this.pStage, ((MenuItem) e.getSource()).getText());
        });
        mItalic.setOnAction(e -> {
            TextFormat(this.txtInput, this.pStage, ((MenuItem) e.getSource()).getText());
        });
        mFont.setOnAction(e -> {
            TextFormat(this.txtInput, this.pStage, ((MenuItem) e.getSource()).getText());
        });

        mSize.setOnAction(e -> {
            TextFormat(this.txtInput, this.pStage, ((MenuItem) e.getSource()).getText());
        });
        this.txtInput.textProperty().addListener(e -> {
            this.newChanges = true;
            this.cCount = this.txtInput.getText().length();
            this.lblChar.setText("Characters: " + cCount);

        });

        mNew.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
        mSave.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        mOpen.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
        mBold.setAccelerator(new KeyCodeCombination(KeyCode.B, KeyCombination.CONTROL_DOWN));
        mItalic.setAccelerator(new KeyCodeCombination(KeyCode.I, KeyCombination.CONTROL_DOWN));
        mFind.setAccelerator(new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN));

        bPane.setTop(mBar);
        bPane.setCenter(this.txtInput);
        bPane.setBottom(lblChar);

        // ** Initialization **
        Scene myScene = new Scene(bPane, 1030, 600);
        this.pStage.setScene(myScene);
        this.pStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
