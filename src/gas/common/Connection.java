/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gas.common;

import gas.io.ConnectionType;

import units.UnitsTools;
import units.qual.*;

/**
 *
 * @author gross
 */
public interface Connection<I extends Intersection> {

    @m3PERhr double getFlowMax();

    @m3PERhr double getFlowMin();

    I getFrom();

    I getTo();

    ConnectionType getType();
}
