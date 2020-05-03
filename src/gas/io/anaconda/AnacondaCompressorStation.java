/**
 * AnacondaCompressorStation.java
 *
 */

package gas.io.anaconda;

import units.qual.*;

/**
 *
 * @author Martin Groß
 */
public class AnacondaCompressorStation extends AnacondaConnection {
    
    private String bypassValveId;
    private @Dimensionless double efficiency;
    private @m3PERhr double flowInit;
    private @s double minDownTime;
    private @s double minRunTime;
    private @W double powerMax;
    private @W double powerMin;
    private double scalingOfControl;
    private @m3 double shutdownCosts;
    private @m3 double startupCosts;
    private @Dimensionless double specificFuelConsumption;
    private String typeOfControl;

    public String getBypassValveId() {
        return bypassValveId;
    }

    public @Dimensionless double getEfficiency() {
        return efficiency;
    }

    public @m3PERhr double getFlowInit() {
        return flowInit;
    }

    public @s double getMinDownTime() {
        return minDownTime;
    }

    public @s double getMinRunTime() {
        return minRunTime;
    }

    public @W double getPowerMax() {
        return powerMax;
    }

    public @W double getPowerMin() {
        return powerMin;
    }

    public double getScalingOfControl() {
        return scalingOfControl;
    }

    public @m3 double getShutdownCosts() {
        return shutdownCosts;
    }

    public @m3 double getStartupCosts() {
        return startupCosts;
    }

    public @Dimensionless double getSpecificFuelConsumption() {
        return specificFuelConsumption;
    }

    public String getTypeOfControl() {
        return typeOfControl;
    }

    @Override
    protected boolean checkNodeName(String name) {
        return name.equals("compressor");
    }

    @Override
    protected boolean parseAttribute(String name, String value) {
        if (!super.parseAttribute(name, value)) {
            switch (name) {
                case "bypassValve":
                    bypassValveId = value;
                    return true;
                case "scalingOfControl":
                    scalingOfControl = Double.parseDouble(value);
                    return true;
                case "typeOfControl":
                    // "pressure" or "power"
                    typeOfControl = value; 
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

    }
}
