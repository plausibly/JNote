/**
 * Abstract class of a formatting command. This is used to implement the different
 * formatting types for a TextArea.
 */

package Formats;

import javafx.scene.control.TextArea;

public abstract class FormatCmd {
    protected TextArea txt;
    protected boolean isBold, isItalic;
    protected double size;
    protected String family;

    /**
     * Constructing a FormatCmd object
     * 
     * @param txt      TextArea being formatted
     * @param family   String of the txt's font family
     * @param size     double value of the font size
     * @param isBold   boolean value on whether or not the txt is bold
     * @param isItalic boolean value on whether or not the txt is in italics
     */
    public FormatCmd(TextArea txt, String family, double size, boolean isBold, boolean isItalic) {
        this.family = family;
        this.size = size;
        this.txt = txt;
        this.isBold = isBold;
        this.isItalic = isItalic;
    }

    /**
     * Abstracted execute command. This runs the format "command".
     */
    public abstract void execute();

}
