package Formats;

import javafx.scene.control.TextArea;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class BoldCmd extends FormatCmd {

    public BoldCmd(TextArea txt, String family, double size, boolean isBold, boolean isItalic){
        super(txt, family, size, isBold, isItalic);
    }

    @Override
    public void execute() {
        FontPosture post = FontPosture.REGULAR;
        FontWeight wght = FontWeight.BOLD;
        
        if (isBold){
            wght = FontWeight.NORMAL;
        }
        if (isItalic){
            post = FontPosture.ITALIC;
        }
        Font fnt = Font.font(this.family, wght, post, this.size);
        this.txt.setFont(fnt);
    }
    
}
