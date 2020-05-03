/*
 * AnacondaPipe.java
 * 
 * 
 */
package gas.io.anaconda;

import gas.io.Pipe;
import gas.io.XMLProperty;
import units.UnitsTools;
import units.qual.*;

/**
 *
 * @author Martin Groß
 */
public class AnacondaPipe extends AnacondaConnection implements Pipe {

    private @mm double diameter;
    private @m3PERhr double flowInInit;
    private @m3PERhr double flowOutInit;    
    private @mm double length;
    private @mm double roughness;

    public @Dimensionless double computeSlope() {
        return (getTo().getHeight() - getFrom().getHeight())/UnitsTools.mm_to_m(getLength());
    }

    public @mm double getDiameter() {
        return diameter;
    }

    public @m3PERhr double getFlowInInit() {
        return flowInInit;
    }

    public @m3PERhr double getFlowOutInit() {
        return flowOutInit;
    }

    public @mm double getLength() {
        return length;
    }

    public @mm double getRoughness() {
        return roughness;
    }
    
    @Override
    protected boolean checkNodeName(String name) {
        return name.equals("pipe");
    }

    @Override
    protected void parseProperties() {
        super.parseProperties();
        diameter = (@mm double) getProperties().get("diameter").getAmount();
        XMLProperty fMax = getProperties().get("flowInInit");
        if (fMax.getUnit().equals("1000m_cube_per_hour")) {
            flowInInit = Double.parseDouble(fMax.getValue()) * 1000 * UnitsTools.m3/UnitsTools.hr;
        } else {
            throw new AssertionError("Volumetric flow rate unit unknown: " + fMax.getUnit());
        }
        XMLProperty fMin = getProperties().get("flowOutInit");
        if (fMin.getUnit().equals("1000m_cube_per_hour")) {
            flowOutInit = Double.parseDouble(fMax.getValue()) * 1000 * UnitsTools.m3/UnitsTools.hr;
        } else {
            throw new AssertionError("Volumetric flow rate unit unknown: " + fMin.getUnit());
        }
        length = (@mm double) getProperties().get("length").getAmount();
        roughness = (@mm double) getProperties().get("roughness").getAmount();
    }
}
