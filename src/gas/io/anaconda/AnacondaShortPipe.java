/**
 * ShortPipe.java
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
public class AnacondaShortPipe extends AnacondaConnection {

    private @m3PERhr double flowInit;

    public @m3PERhr double getFlowInit() {
        return flowInit;
    }
    
    @Override
    protected boolean checkNodeName(String name) {
        return name.equals("shortPipe");
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
    }        
}
