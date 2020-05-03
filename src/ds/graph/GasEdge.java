/**
 * GasEdge.java
 *
 */

package ds.graph;

import static java.lang.Math.PI;
import units.UnitsTools;
import units.qual.*;

/**
 *
 * @author Martin Groß
 */
public class GasEdge extends AbstractEdge<GasNode> implements Identifiable {

    private @m double diameter;
    private @m3PERhr double flowMax;
    private @m3PERhr double flowMin;
    private int id;
    private @m double length;
    private @m double roughness;

    public GasEdge(GasNode start, GasNode end) {
        this(start, end, -1);
    }

    public GasEdge(GasNode start, GasNode end, int id) {
        super(start, end);
        this.id = id;        
        this.diameter = 0 * UnitsTools.m;
        this.length = 0 * UnitsTools.m;
        this.roughness = 0 * UnitsTools.m;
    }

    public GasEdge(GasNode start, GasNode end, int id, @m double diameter, @m double length, @m double roughness) {
        this(start, end, id);
        this.diameter = diameter;
        this.length = length;
        this.roughness = roughness;
    }

    public static GasEdge createEdge(GasNode start, GasNode end) {
        return new GasEdge(start, end);
    }

    @Override
    public int id() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public @m double getDiameter() {
        return diameter;
    }

    public void setDiameter(@m double diameter) {
        this.diameter = diameter;
    }

    public @m3PERhr double getFlowMax() {
        return flowMax;
    }

    public void setFlowMax(@m3PERhr double flowMax) {
        this.flowMax = flowMax;
    }

    public @m3PERhr double getFlowMin() {
        return flowMin;
    }

    public void setFlowMin(@m3PERhr double flowMin) {
        this.flowMin = flowMin;
    }

    public @m double getLength() {
        return length;
    }

    public void setLength(@m double length) {
        this.length = length;
    }

    public @Dimensionless double getSlope() {
        return (end().getHeight() - start().getHeight())/length;
    }
    
    public @m double getRoughness() {
        return roughness;
    }

    public void setRoughness(@m double roughness) {
        this.roughness = roughness;
    }
    
    public @m3 double getHalfVolume() {
        return diameter*diameter*(PI / 8.0)*length;
    }

    @Override
    public String toString() {
        return String.format("(%1$s,%2$s)", start().id(), end().id(), id());
    }
    
  
}
