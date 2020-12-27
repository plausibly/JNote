package Formats;

import javafx.scene.control.TextArea;

public class ColorCmd extends FormatCmd {

    /**
     * Constructing a ColorCmd object
     * 
     * @param txt  TextArea being formatted
     * @param family   String of the txt's font family
     * @param size  double value of the font size
     * @param isBold    boolean value on whether or not the txt is bold
     * @param isItalic  boolean value on whether or not the txt is in italics
     */
    public ColorCmd(TextArea txt, String family, double size, boolean isBold, boolean isItalic){
        super(txt, family, size, isBold, isItalic);
    }

    /**
     * Execute the ColorCmd command. Switches the font color
     * to a specified one.
     */
    public void execute() {
        // todo
        
    }
    
}
