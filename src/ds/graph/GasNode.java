/**
 * GasNode.java
 *
 */

package ds.graph;

import units.UnitsTools;
import units.qual.*;

/**
 *
 * @author Martin Groß
 */
public class GasNode implements Identifiable {

    private int id;
    private @m double height;
    private @m3 double volume;

    public GasNode() {
        
    }

    public GasNode(int i) {
        id = i;
    }

    public GasNode(int id, @m double height) {
        this.id = id;
        this.height = height;
    }    
    
    public GasNode(int id, @m double height, @m3 double volume) {
        this.id = id;
        this.height = height;
        this.volume = volume;
    }

    public @m double getHeight() {
        return height;
    }

    public void setHeight(@m double height) {
        this.height = height;
    }

    public @m3 double getVolume() {
        return volume;
    }

    public void setVolume(@m3 double volume) {
        this.volume = volume;
    }

    @Override
    public int id() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "GasNode{" + "id=" + id + ", height=" + height + ", volume=" + volume + '}';
    }

}
