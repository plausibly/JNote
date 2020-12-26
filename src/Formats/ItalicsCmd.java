package Formats;

import javafx.scene.control.TextArea;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class ItalicsCmd extends FormatCmd {

    public ItalicsCmd(TextArea txt, String family, double size, boolean isBold, boolean isItalic){
        super(txt, family, size, isBold, isItalic);
    }

    @Override
    public void execute() {
        FontPosture post = FontPosture.ITALIC;
        FontWeight wght = FontWeight.NORMAL;

        if (isBold)
            wght = FontWeight.BOLD;
        if (isItalic)
            post = FontPosture.REGULAR;
        
        Font fnt = Font.font(this.family, wght, post, this.size);
        this.txt.setFont(fnt);
        
    }
    
}
