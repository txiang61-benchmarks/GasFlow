/**
 * GasLibControlValve.java
 *
 */

package gas.io.gaslib;

import org.w3c.dom.Element;

import units.UnitsTools;
import units.qual.*;

/**
 *
 * @author Martin Groß
 */
public class GasLibControlValve extends GasLibConnection {

    private boolean gasPreheaterExisting;
    private boolean internalBypassRequired;
    private @bar double pressureDifferentialMax;
    private @bar double pressureDifferentialMin;
    private @bar double pressureInMin;
    private @bar double pressureLossIn;
    private @bar double pressureLossOut;
    private @bar double pressureOutMax;

    public boolean isGasPreheaterExisting() {
        return gasPreheaterExisting;
    }

    public boolean isInternalBypassRequired() {
        return internalBypassRequired;
    }

    public @bar double getPressureDifferentialMax() {
        return pressureDifferentialMax;
    }

    public @bar double getPressureDifferentialMin() {
        return pressureDifferentialMin;
    }

    public @bar double getPressureInMin() {
        return pressureInMin;
    }

    public @bar double getPressureLossIn() {
        return pressureLossIn;
    }

    public @bar double getPressureLossOut() {
        return pressureLossOut;
    }

    public @bar double getPressureOutMax() {
        return pressureOutMax;
    }

    @Override
    protected boolean checkNodeName(String name) {
        return name.equals("controlValve");
    }

    @Override
    protected boolean parseAttribute(String name, String value) {
        if (!super.parseAttribute(name, value)) {
            switch (name) {
                case "gasPreheaterExisting":
                    gasPreheaterExisting = parseBoolean(value);
                    return true;
                case "internalBypassRequired":
                    internalBypassRequired = parseBoolean(value);
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
        if (getProperties().containsKey("pressureDifferentialMax")) {
            pressureDifferentialMax = (@bar double) getProperties().get("pressureDifferentialMax").getAmount();
        }
        if (getProperties().containsKey("pressureDifferentialMin")) {
            pressureDifferentialMin = (@bar double) getProperties().get("pressureDifferentialMin").getAmount();
        }
        pressureInMin = (@bar double) getProperties().get("pressureInMin").getAmount();
        pressureOutMax = (@bar double) getProperties().get("pressureOutMax").getAmount();
        if (getProperties().containsKey("pressureLossIn")) {
            pressureLossIn = (@bar double) getProperties().get("pressureLossIn").getAmount();
        }
        if (getProperties().containsKey("pressureLossOut")) {
            pressureLossOut = (@bar double) getProperties().get("pressureLossOut").getAmount();
        }
    }
    
    @Override
    protected void writeAttributes(Element element) {
        super.writeAttributes(element);
        element.setAttribute("gasPreheaterExisting", writeBoolean(gasPreheaterExisting));
        element.setAttribute("internalBypassRequired", writeBoolean(internalBypassRequired));
    }        
}
