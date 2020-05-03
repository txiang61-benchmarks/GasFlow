/**
 * GasComputationProblem.java
 *
 */

package gas.problem;

import units.UnitsTools;
import units.qual.*;

/**
 *
 * @author Martin Groß
 */
public class GasComputationProblem {

    /**
     * Molar mass of the gas mixture.
     */
    public static final @gPERmol double M = 0.0183 * UnitsTools.g/UnitsTools.mol;
    /**
     * Constant Temperature (T = 288.15K).
     */
    public static final @K double T = 288.15 * UnitsTools.K;
    /**
     * Constant Compressibility Factor (z = 0.9).
     */
    public static final @Dimensionless double z = 0.9;

    /**
     * Specific gas constant.
     */
    public static final @m2PERs2K double Rs = UnitsTools.R/UnitsTools.gPERmol_to_kgPERmol(M);
    /**
     * Speed of sound.
     */
    public static final @mPERs double c = Math.sqrt(z*Rs*T);
    /**
     * Speed of sound squared.
     */
    public static final @m2PERs2 double c2 = z*Rs*T;

    public static @Dimensionless double computeFrictionFactor(@m double roughness, @m double diameter) {
        double d = roughness/diameter;
        return 1/((1.14 - 2*Math.log10(d))*(1.14 - 2*Math.log10(d)));
    }
}
