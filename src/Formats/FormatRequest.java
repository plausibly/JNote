package Formats;

import java.util.ArrayList;

public class FormatRequest {
    ArrayList<FormatCmd> formats;

    public FormatRequest() {
        this.formats = new ArrayList<FormatCmd>();
    }

    public void addFormat(FormatCmd c){
        this.formats.add(c);
    }

    public void applyFormats(){
        for (FormatCmd x : this.formats)
            x.execute();
        this.formats.clear();
    }
    
}
