/*
 * GasLibPipe.java
 * 
 * 
 */
package gas.io.gaslib;

import gas.io.Pipe;
import gas.io.XMLProperty;
import gas.quantity.HeatTransferCoefficient;
import units.UnitsTools;
import units.qual.*;

/**
 *
 * @author Martin Groß
 */
public class GasLibPipe extends GasLibConnection implements Pipe {

    private @mm double diameter;
    private @WPERm2K double heatTransferCoefficient;
    private @mm double length;
    private @bar double pressureMax;
    private @mm double roughness;

    public GasLibPipe() {
        super();
    }

    public GasLibPipe(String id, GasLibIntersection start, GasLibIntersection end, String startId, String endId,
            @mm double length) {
        super();
        this.id = id;
        from = start;
        fromId = startId;
        to = end;
        toId = endId;
        this.diameter = 500 * UnitsTools.mm;
        this.roughness = 0.05 * UnitsTools.mm;
        this.length = length;
        heatTransferCoefficient = 2 * UnitsTools.W/UnitsTools.m/UnitsTools.m/UnitsTools.K;
        pressureMax = 200 * UnitsTools.bar;
    }

    public @m2 double computeCrossSection() {
        return UnitsTools.mm_to_m(diameter) * UnitsTools.mm_to_m(diameter) * (Math.PI / 4);
    }

    public @Dimensionless double computeSiamCoefficient(GasLibNetworkFile networkFile) {
        double lambda = UnitsTools.mm_to_m(getLength())*-1.0*computeNikuradseFrictionFactor()*networkFile.getMeanSpecificGasConstant()
                *computePapayCompressibilityFactor(networkFile)*networkFile.getMeanTemperature()
                /computeCrossSection()/computeCrossSection()/UnitsTools.mm_to_m(getDiameter());
        return lambda;
    }

    public @Dimensionless double computeNikuradseFrictionFactor() {
        double diameterM = diameter;
        double roughnessM = roughness;
        double relativeRoughness = roughnessM / diameterM;
        double frictionFactor = Math.pow(1.138 - 2 * Math.log10(relativeRoughness), -2);
        return frictionFactor;
    }

    public @bar double getMeanPressure() {
        double minOfMax = Math.min(getTo().getPressureMax(), getFrom().getPressureMax());
        double maxOfMin = Math.max(getFrom().getPressureMin(), getTo().getPressureMin());
        return 0.5 * (minOfMax + maxOfMin);
    }

    public @Dimensionless double computePapayCompressibilityFactor(GasLibNetworkFile constants) {
        double tr = constants.getMeanReducedTemperature();
        double pr = getMeanPressure()/constants.getMeanPseudocriticalPressure();
        double z = 1 - 3.52 * pr * Math.exp(-2.26 * tr) + 0.247 * pr * pr * Math.exp(-1.878 * tr);
        return z;
    }

    public @Dimensionless double computeSlope() {
        return (getTo().getHeight() - getFrom().getHeight())/UnitsTools.mm_to_m(getLength());
    }

    public @s2PERm2 double computeTimelessCoefficient(@mPERs double c) {
        double crossSection = computeCrossSection();
        // lambda
        double friction = computeNikuradseFrictionFactor();
        // c^2
        double c2 = c*c;
        // c^4
        double c4 = c2*c2;
        // D / c^2 * length * friction factor
        double Dc2ll = UnitsTools.mm_to_m(diameter)/(c2*friction*UnitsTools.mm_to_m(getLength()));
        // gh'D / (c^4 * friction)
        double ghDc4l = computeSlope()*UnitsTools.gravity*UnitsTools.mm_to_m(diameter)/c4/friction;
        double result = crossSection/crossSection*Dc2ll + ghDc4l;
        return result;
    }

    public @bar double computeStartpointPressure(@mPERs double c, @bar double start, @bars3PERm double flow) {
        double beta = computeTimelessCoefficient(c);
        beta = beta * (3600 * UnitsTools.s) * (3600 * UnitsTools.s);
        double pi = start*start;
        return Math.sqrt(flow*flow/beta + pi);
    }

    public @bar double computeEndpointPressure(@mPERs double c, @bar double start, @bars3PERm double flow) {
        double beta = computeTimelessCoefficient(c);
        beta = beta * (3600 * UnitsTools.s) * (3600 * UnitsTools.s);
        double pi = start*start;
        return Math.sqrt(flow*flow/beta - pi);
    }

    public double computeMassFlow(@bar double start, @bar double end, @s double duration) {
        return 0;
    }

    public double computePiDifference(double massFlow) {
        return 0;
    }

    public @mm double getDiameter() {
        return diameter;
    }

    public @WPERm2K double getHeatTransferCoefficient() {
        return heatTransferCoefficient;
    }

    public @mm double getLength() {
        return length;
    }

    public void setLength(@mm double length) {
        this.length = length;
    }

    public @bar double getPressureMax() {
        return pressureMax;
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
        XMLProperty pHeatTransferCoefficient = getProperties().get("heatTransferCoefficient");
        if (pHeatTransferCoefficient.getUnit().equals("W_per_m_square_per_K")
                || pHeatTransferCoefficient.getUnit().equals("m/m")) {
            pHeatTransferCoefficient.setUnit("W/m^2/K");
        } else {
            throw new AssertionError("Heat transfer coefficient unit unknown: " + pHeatTransferCoefficient.getUnit());
        }
        heatTransferCoefficient = (@WPERm2K double) pHeatTransferCoefficient.getAmount();
        length = (@mm double) getProperties().get("pressure").getAmount();
        if (getProperties().containsKey("pressureMax")) {
            pressureMax = (@bar double) getProperties().get("pressureMax").getAmount();
        } else {
            pressureMax = (@bar int) 0;
        }
        roughness = (@mm double) getProperties().get("roughness").getAmount();
    }

    public void createProperties() {
        if (!getProperties().containsKey("flowMin")) {
            XMLProperty property = new XMLProperty("flowMin", "1000m_cube_per_hour", "-10000");
            properties.put(property.getName(), property);
        }
        if (!getProperties().containsKey("flowMax")) {
            XMLProperty property = new XMLProperty("flowMax", "1000m_cube_per_hour", "10000");
            properties.put(property.getName(), property);
        }
        if (!getProperties().containsKey("length")) {
            XMLProperty property = new XMLProperty("length", "meter", length + "");
            properties.put(property.getName(), property);
        }
        if (!getProperties().containsKey("diameter")) {
            XMLProperty property = new XMLProperty("diameter", "meter", diameter + "");
            properties.put(property.getName(), property);
        }
        if (!getProperties().containsKey("roughness")) {
            XMLProperty property = new XMLProperty("roughness", "meter", roughness + "");
            properties.put(property.getName(), property);
        }
        if (!getProperties().containsKey("pressureMax")) {
            XMLProperty property = new XMLProperty("pressureMax", "bar", pressureMax + "");
            properties.put(property.getName(), property);
        }
        if (!getProperties().containsKey("heatTransferCoefficient")) {
            XMLProperty property = new XMLProperty("heatTransferCoefficient", "W_per_m_square_per_K", heatTransferCoefficient + "");
            properties.put(property.getName(), property);
        }
    }
}
