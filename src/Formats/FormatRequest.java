/**
 * Executes all the formatting requests.
 */
package Formats;

import java.util.ArrayList;

public class FormatRequest {
    ArrayList<FormatCmd> fQueue;

    /**
     * Constructor for all the formatting requests. Creates an arraylist to queue up
     * all the requests.
     */
    public FormatRequest() {
        this.fQueue = new ArrayList<FormatCmd>();
    }

    /**
     * Add a formatting request to the queue.
     * 
     * @param c the FormatCmd being added
     */
    public void addFormat(FormatCmd c) {
        this.fQueue.add(c);
    }

    /**
     * Apply all the formattings in the queue.
     */
    public void applyFormats() {
        for (FormatCmd x : this.fQueue)
            x.execute();
        this.fQueue.clear();
    }

}
