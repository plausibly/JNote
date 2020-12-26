/**
* Bold formatting command. This object runs a command to 
* set text weight to Bold. 
*/
package Formats;

import javafx.scene.control.TextArea;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class BoldCmd extends FormatCmd {
    
    /**
     * Constructing a BoldCmd object
     * 
     * @param txt  TextArea being formatted
     * @param family   String of the txt's font family
     * @param size  double value of the font size
     * @param isBold    boolean value on whether or not the txt is bold
     * @param isItalic  boolean value on whether or not the txt is in italics
     */
    public BoldCmd(TextArea txt, String family, double size, boolean isBold, boolean isItalic){
        super(txt, family, size, isBold, isItalic);
    }

    /**
     * Execute the bold command. Switches the font weight
     * between normal and bold.
     */
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
