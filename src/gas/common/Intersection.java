/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gas.common;

import java.util.List;

import units.UnitsTools;
import units.qual.*;

/**
 *
 * @author Martin Groß
 * @param <C> the type of connection;
 */
public interface Intersection<C extends Connection> {

    List<C> getConnections();

    @m double getHeight();

    String getId();

    @bar double getPressureMax();

    @bar double getPressureMin();

    double getX();

    double getY();

}
