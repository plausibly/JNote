package Formats;

import javafx.scene.control.TextArea;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class FontCmd extends FormatCmd {

    /**
     * Constructing a FamilyCmd object
     * 
     * @param txt  TextArea being formatted
     * @param family   String of the txt's font family
     * @param size  double value of the font size
     * @param isBold    boolean value on whether or not the txt is bold
     * @param isItalic  boolean value on whether or not the txt is in italics
     */
    public FontCmd(TextArea txt, String family, double size, boolean isBold, boolean isItalic){
        super(txt, family, size, isBold, isItalic);
    }

    /**
     * Execute the FontCmd command. Creates a font based on parameters given in the
     * constructor. Mainly used to switch font families or the font size.
     */
    public void execute() {
        FontPosture post = isItalic ? FontPosture.ITALIC: FontPosture.REGULAR;
        FontWeight wght = isBold ? FontWeight.BOLD: FontWeight.NORMAL;
            
        Font fnt = Font.font(this.family, wght, post, this.size);
        this.txt.setFont(fnt);
        
    }
    
}
