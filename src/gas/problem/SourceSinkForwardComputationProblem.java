/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gas.problem;

import ds.graph.GasEdge;
import ds.graph.GasNode;
import ds.graph.Graph;
import ds.graph.IdentifiableAmountMapping;
import gas.quantity.EdgeConstant;
import static java.lang.Math.PI;
import units.UnitsTools;
import units.qual.*;

/**
 *
 * @author Martin
 */
public class SourceSinkForwardComputationProblem {

    /**
     * Compressibility Factor (z)
     */
    private @Dimensionless double compressibilityFactor;
    /**
     * Dynamic Viscosity (eta in Pa s)
     */
    private @Pas double dynamicViscosity;
    
    private IdentifiableAmountMapping<GasEdge, @m2s2 Double> edgeConstants;
    
       
    private IdentifiableAmountMapping<GasNode, @bar Double> initialPressures;
    /**
     * Molar mass of the gas mixture. (M)
     */
    private @gPERmol double molarMass;
    /**
     * The network
     */
    private Graph<GasNode, GasEdge> network;
    
    private GasNode sink;
    private GasNode source;
    
    private @bar double sourcePressure;
    private @bar double sinkPressure;    
    /**
     * Specific gas constant (R_s)
     */
    private volatile @m2PERs2K double specificGasConstant;
    /**
     * Speed of sound (c)
     */
    private @mPERs double speedOfSound;
    /**
     * Temperature (T)
     */
    private @K double temperature;
    /**
     *
     */
    private @s double timeStep;

    public SourceSinkForwardComputationProblem() {
        sourcePressure = 10 * UnitsTools.bar;
        sinkPressure = 1 * UnitsTools.bar;        
        compressibilityFactor = 0.9;
        dynamicViscosity = 0.000011 * UnitsTools.Pa*UnitsTools.s;
        molarMass = 0.0183 * UnitsTools.g/UnitsTools.mol;
        temperature = 288.15 * UnitsTools.K;
        specificGasConstant = UnitsTools.R/UnitsTools.gPERmol_to_kgPERmol(molarMass);
        speedOfSound = Math.sqrt(compressibilityFactor*specificGasConstant*temperature);
        /****/
        timeStep = 1 * UnitsTools.s;
    }

    protected @Dimensionless double computeFrictionFactor(@m double roughness, @m double diameter) {
        double d = roughness/diameter;
        return 1/((1.14 - 2*Math.log10(d))*(1.14 - 2*Math.log10(d)));
    }    

    protected @m2s2 double computeEdgeCoefficient(GasEdge edge) {
        double diameter = edge.getDiameter();
        double length = edge.getLength();
        double slope = edge.getSlope();
        double roughness = edge.getRoughness();
        double frictionFactor = computeFrictionFactor(roughness, diameter);
        double term1 = UnitsTools.gravity*slope*diameter/(getSpeedOfSound()*getSpeedOfSound()*getSpeedOfSound()*getSpeedOfSound())/frictionFactor;
        double term2 = diameter/(getSpeedOfSound()*getSpeedOfSound())/(frictionFactor)/(length);
        double term3 = term1 + term2;
        double term4 = term3*diameter*diameter*diameter*diameter*(PI*PI/16.0);        
        return term4;
    };
    
    protected @m2s2 double computeExactEdgeCoefficient(GasEdge edge, @m2s2 double precision) {
        double diameter = edge.getDiameter();
        double length = edge.getLength();
        double slope = edge.getSlope();
        double roughness = edge.getRoughness();
        double frictionFactor = computeFrictionFactor(roughness, diameter);
        double term1 = UnitsTools.gravity*slope*diameter/(getSpeedOfSound()*getSpeedOfSound()*getSpeedOfSound()*getSpeedOfSound())/frictionFactor;
        double term2 = diameter/(getSpeedOfSound()*getSpeedOfSound())/(frictionFactor)/(length);
        double term3 = term1 + term2;
        double term4 = term3*diameter*diameter*diameter*diameter*(PI*PI/16.0);
        return term4 + precision;
    };    
    
    protected void updateConstants() {
        specificGasConstant = UnitsTools.R/UnitsTools.gPERmol_to_kgPERmol(molarMass);
        speedOfSound = Math.sqrt(compressibilityFactor*specificGasConstant*temperature);
    }

    /****/
    public void assumeExactPressures() {
        for (GasNode node : network.nodes()) {
            initialPressures.set(node, initialPressures.get(node));
        }
    }
    
    public @Dimensionless double getCompressibilityFactor() {
        return compressibilityFactor;
    }

    public void setCompressibilityFactor(double compressibilityFactor) {
        this.compressibilityFactor = compressibilityFactor;
        updateConstants();
    }

    public @Pas double getDynamicViscosity() {
        return dynamicViscosity;
    }

    @Deprecated
    public void setDynamicViscosity(@Pas double dynamicViscosity) {
        this.dynamicViscosity = dynamicViscosity;
    }    
    
    public IdentifiableAmountMapping<GasEdge, @m2s2 Double> getEdgeConstants() {
        return edgeConstants;
    }    
    
    public IdentifiableAmountMapping<GasNode, @bar Double> getInitialPressures() {
        return initialPressures;
    }

    public void setInitialPressures(IdentifiableAmountMapping<GasNode, @bar Double> initialPressures) {
        this.initialPressures = initialPressures;
    }    
    
    public @gPERmol double getMolarMass() {
        return molarMass;
    }

    public void setMolarMass(@gPERmol double molarMass) {
        this.molarMass = molarMass;
        updateConstants();
    }

    public Graph<GasNode, GasEdge> getNetwork() {
        return network;
    }
    
    public void setNetwork(Graph<GasNode, GasEdge> network) {
        this.network = network;
        edgeConstants = new IdentifiableAmountMapping<>(network.edges());
        initialPressures = new IdentifiableAmountMapping<>(network.nodes());
        for (GasNode node : network.nodes()) {
            initialPressures.set(node, 1 * UnitsTools.bar);
            double volume = 0 * UnitsTools.m3;
            for (GasEdge edge : network.incidentEdges(node)) {
                volume = volume + edge.getHalfVolume();
            }
            node.setVolume(volume);
        }
        for (GasEdge edge : network.edges()) {
            edgeConstants.set(edge, computeExactEdgeCoefficient(edge, (@m2s2 double) 0.0));            
        }
    }    
    
    @Deprecated
    public @Dimensionless double getReynoldsNumber(@m double diameter, @mPERs double velocity) {
        return diameter*velocity/dynamicViscosity;
    }

    public @m2PERs2K double getSpecificGasConstant() {
        return specificGasConstant;
    }

    public @mPERs double getSpeedOfSound() {
        return speedOfSound;
    }    
    
    public @K double getTemperature() {
        return temperature;
    }

    public void setTemperature(@K double temperature) {
        this.temperature = temperature;
        updateConstants();
    }

    public @s double getTimeStep() {
        return timeStep;
    }

    public void setTimeStep(@s double timeStep) {
        this.timeStep = timeStep;
    }

    public GasNode getSink() {
        return sink;
    }

    public void setSink(GasNode sink) {
        this.sink = sink;
    }

    public GasNode getSource() {
        return source;
    }

    public void setSource(GasNode source) {
        this.source = source;
    }

    public @bar double getSourcePressure() {
        return sourcePressure;
    }

    public void setSourcePressure(@bar double sourcePressure) {
        this.sourcePressure = sourcePressure;
    }

    public @bar double getSinkPressure() {
        return sinkPressure;
    }

    public void setSinkPressure(@bar double sinkPressure) {
        this.sinkPressure = sinkPressure;
    }
    
    
}
