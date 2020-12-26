package Formats;

import javafx.scene.control.TextArea;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class ItalicsCmd extends FormatCmd {

    /**
     * Constructing a ItalicsCmd object
     * 
     * @param txt  TextArea being formatted
     * @param family   String of the txt's font family
     * @param size  double value of the font size
     * @param isBold    boolean value on whether or not the txt is bold
     * @param isItalic  boolean value on whether or not the txt is in italics
     */
    public ItalicsCmd(TextArea txt, String family, double size, boolean isBold, boolean isItalic){
        super(txt, family, size, isBold, isItalic);
    }

    /**
     * Execute the italics command. Switches the font posture
     * between regular and italics.
     */
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
