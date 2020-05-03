/**
 * GasLibValve.java
 *
 */

package gas.io.gaslib;

import units.UnitsTools;
import units.qual.*;

/**
 *
 * @author Martin Groß
 */
public class GasLibValve extends GasLibConnection {

    private @bar double pressureDifferentialMax;

    public @bar double getPressureDifferentialMax() {
        return pressureDifferentialMax;
    }

    @Override
    protected boolean checkNodeName(String name) {
        return name.equals("valve");
    }

    @Override
    protected void parseProperties() {
        super.parseProperties();
        pressureDifferentialMax = (@bar double) getProperties().get("pressureDifferentialMax").getAmount();
    }
}
