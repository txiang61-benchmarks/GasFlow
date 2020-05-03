/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gas.io;

import units.UnitsTools;
import units.qual.*;

/**
 *
 * @author Martin
 */
public interface Pipe {
    
    public @mm double getDiameter();
    public @mm double getLength();
    public @mm double getRoughness();
    
}
