package Formats;

import javafx.scene.control.TextArea;

public abstract class FormatCmd {
    protected TextArea txt;
    protected boolean isBold, isItalic;
    protected double size;
    protected String family;

    public FormatCmd(TextArea txt, String family, double size, boolean isBold, boolean isItalic){
        this.family = family;
        this.size = size;
        this.txt = txt;
        this.isBold = isBold;
        this.isItalic = isItalic;
    }
    
    public abstract void execute();
    
}
