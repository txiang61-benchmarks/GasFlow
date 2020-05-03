/**
 * AnacondaValve.java
 *
 */

package gas.io.anaconda;

import gas.io.XMLProperty;

import units.UnitsTools;
import units.qual.*;

/**
 *
 * @author Martin Groß
 */
public class AnacondaValve extends AnacondaConnection {

    private String compressorId;
    private @m3PERhr double flowInit;
    private @bar double pressureDifferentialMax;
    private @bar double pressureDifferentialMin;

    public @m3PERhr double getFlowInit() {
        return flowInit;
    }    
    
    public @bar double getPressureDifferentialMax() {
        return pressureDifferentialMax;
    }

    public @bar double getPressureDifferentialMin() {
        return pressureDifferentialMin;
    }

    @Override
    protected boolean checkNodeName(String name) {
        return name.equals("valve");
    }

    @Override
    protected boolean parseAttribute(String name, String value) {
        if (!super.parseAttribute(name, value)) {
            switch (name) {
                case "compressor":
                    compressorId = value;
                    return true;
                default:
                    return false;
            }
        } else {
            return true;
        }
    }

    @Override
    protected void parseProperties() {
        super.parseProperties();
        XMLProperty fMax = getProperties().get("flowInit");
        if (fMax.getUnit().equals("1000m_cube_per_hour")) {
            flowInit = Double.parseDouble(fMax.getValue()) * 1000 * UnitsTools.m3/UnitsTools.hr;
        } else {
            throw new AssertionError("Volumetric flow rate unit unknown: " + fMax.getUnit());
        }        
        pressureDifferentialMax = (@bar double) getProperties().get("pressureDifferentialMax").getAmount();
        if (getProperties().containsKey("pressureDifferentialMin")) {
            pressureDifferentialMin = (@bar double) getProperties().get("pressureDifferentialMin").getAmount();
        }
    }
}
