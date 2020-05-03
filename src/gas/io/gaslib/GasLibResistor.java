/**
 * GasLibResistor.java
 *
 */

package gas.io.gaslib;

import units.UnitsTools;
import units.qual.*;

/**
 *
 * @author Martin Groß
 */
public class GasLibResistor extends GasLibConnection {

    private @mm double diameter;
    private @Dimensionless double dragFactor;

    public @mm double getDiameter() {
        return diameter;
    }

    public @Dimensionless double getDragFactor() {
        return dragFactor;
    }
    
    @Override
    protected boolean checkNodeName(String name) {
        return name.equals("resistor");
    }

    @Override
    protected void parseProperties() {
        //System.out.println(super.getClass());
        super.parseProperties();
        if (getProperties().containsKey("diameter")) {
            diameter = (@mm double) getProperties().get("diameter").getAmount();
        }        
        if (getProperties().containsKey("dragFactor")) {
            dragFactor = (@Dimensionless double) getProperties().get("dragFactor").getAmount();
        }        
    }
}
