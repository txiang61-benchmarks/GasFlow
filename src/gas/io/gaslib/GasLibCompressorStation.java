/**
 * GasLibCompressorStation.java
 *
 */

package gas.io.gaslib;

import gas.io.XMLIntersection;
import java.util.Map;
import org.w3c.dom.Element;

import units.UnitsTools;
import units.qual.*;

/**
 *
 * @author Martin Groß
 */
public class GasLibCompressorStation extends GasLibConnection {
    
    private @m double diameterIn;
    private @m double diameterOut;
    private @Dimensionless double dragFactorIn;
    private @Dimensionless double dragFactorOut;
    private XMLIntersection fuelGasVertex;
    private String fuelGasVertexId;
    private boolean gasCoolerExisting;
    private boolean internalBypassRequired;
    private @bar double pressureInMin;
    private @bar double pressureOutMax;

    public @m double getDiameterIn() {
        return diameterIn;
    }

    public @m double getDiameterOut() {
        return diameterOut;
    }

    public @Dimensionless double getDragFactorIn() {
        return dragFactorIn;
    }

    public @Dimensionless double getDragFactorOut() {
        return dragFactorOut;
    }

    public XMLIntersection getFuelGasVertex() {
        return fuelGasVertex;
    }

    public boolean isGasCoolerExisting() {
        return gasCoolerExisting;
    }

    public boolean isInternalBypassRequired() {
        return internalBypassRequired;
    }

    public @bar double getPressureInMin() {
        return pressureInMin;
    }

    public @bar double getPressureOutMax() {
        return pressureOutMax;
    }

    @Override
    public void connectToIntersections(Map<String, XMLIntersection> intersections) {
        super.connectToIntersections(intersections);
        fuelGasVertex = intersections.get(fuelGasVertexId);
    }

    @Override
    protected boolean checkNodeName(String name) {
        return name.equals("compressorStation");
    }

    @Override
    protected boolean parseAttribute(String name, String value) {
        if (!super.parseAttribute(name, value)) {
            switch (name) {
                case "fuelGasVertex":
                    fuelGasVertexId = value;
                    return true;
                case "gasCoolerExisting":
                    gasCoolerExisting = parseBoolean(value);
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
    protected void writeAttributes(Element element) {
        super.writeAttributes(element);
        element.setAttribute("fuelGasVertex", fuelGasVertexId);
        element.setAttribute("gasCoolerExisting", writeBoolean(gasCoolerExisting));
        element.setAttribute("internalBypassRequired", writeBoolean(internalBypassRequired));
    }    

    @Override
    protected void parseProperties() {
        super.parseProperties();
        if (getProperties().containsKey("diameterIn")) {
            diameterIn = (@m double) getProperties().get("diameterIn").getAmount();
        }
        if (getProperties().containsKey("diameterOut")) {
            diameterOut = (@m double) getProperties().get("diameterOut").getAmount();
        }
        if (getProperties().containsKey("dragFactorIn")) {
            dragFactorIn = (@Dimensionless double) getProperties().get("dragFactorIn").getAmount();
        }
        if (getProperties().containsKey("dragFactorOut")) {
            dragFactorOut = (@Dimensionless double) getProperties().get("dragFactorOut").getAmount();
        }
        pressureInMin = (@bar double) getProperties().get("pressureInMin").getAmount();
        pressureOutMax = (@bar double) getProperties().get("pressureOutMax").getAmount();
    }
}
