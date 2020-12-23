package Commands;

import javafx.scene.control.TextArea;
import javafx.scene.text.Font;

public class BoldCmd implements formatCommand {
    private TextArea txt;
    private Font fnt;

    public BoldCmd(TextArea txt, Font fnt){
        this.fnt = fnt;
        this.txt = txt;
    }

    @Override
    public void execute() {
        // bold and unbold text
        String currStyle = fnt.getStyle();
        
    }
    
}
