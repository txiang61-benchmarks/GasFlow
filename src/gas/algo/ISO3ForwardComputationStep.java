/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gas.algo;

import algorithm.Algorithm;
import ds.graph.GasEdge;
import ds.graph.GasNode;
import ds.graph.Graph;
import ds.graph.IdentifiableAmountMapping;
import gas.ds.GasFlow;
import gas.ds.ForwardComputationStep;
import gas.problem.SourceSinkForwardComputationProblem;
import gas.quantity.EdgeConstant;
import javolution.lang.MathLib;
import units.UnitsTools;
import units.qual.*;

/**
 *
 * @author Martin
 */
public class ISO3ForwardComputationStep extends Algorithm<SourceSinkForwardComputationProblem, ForwardComputationStep> {

    static final double DOUBLE_RELATIVE_ERROR = MathLib.pow(2, -53);

    static final double DECREMENT = (1.0 - DOUBLE_RELATIVE_ERROR);

    static final double INCREMENT = (1.0 + DOUBLE_RELATIVE_ERROR);
    
    

    protected @kgPERs double sgnSqrt(@kg2PERs2 double amount) {
        return MathLib.sqrt(amount);
    }
    
    @Override
    protected ForwardComputationStep runAlgorithm(SourceSinkForwardComputationProblem problem) {
        Graph<GasNode, GasEdge> network = problem.getNetwork();
        ForwardComputationStep flow = new ForwardComputationStep(problem.getNetwork());
        GasFlow currentFlow = flow.getNextGasFlow();    

        IdentifiableAmountMapping<GasNode, @bar Double> pressures = problem.getInitialPressures();
        IdentifiableAmountMapping<GasEdge, @m2s2 Double> edgeConstants = problem.getEdgeConstants();
        IdentifiableAmountMapping<GasEdge, @kgPERs Double> massFlowRates = currentFlow.getMassFlowRates();
        IdentifiableAmountMapping<GasNode, @kgPERs Double> massFlowRateDeltas = new IdentifiableAmountMapping<>(network.nodes());
        IdentifiableAmountMapping<GasNode, @bar Double> newPressures = currentFlow.getPressures();

        for (GasEdge edge : network.edges()) {
            double edgeConstant = (@m2s2 double) edgeConstants.get(edge);
            double pressureStart = UnitsTools.bar_to_pa((@bar double) pressures.get(edge.start()));
            double pressureEnd = UnitsTools.bar_to_pa((@bar double)pressures.get(edge.end()));

            double pressureStartSq = pressureStart*pressureStart;

            double pressureEndSq = pressureEnd*pressureEnd;
            double massFlowRateSq = (pressureStartSq - pressureEndSq) * edgeConstant;
            double massFlowRate = sgnSqrt(massFlowRateSq);
            massFlowRates.set(edge, massFlowRate);
        }

        for (GasNode node : network.nodes()) {
            //System.out.println("Step: Processing node " + node.id());
            double massFlowRateDelta = 0 * UnitsTools.kg/UnitsTools.s;
            for (GasEdge edge : network.incomingEdges(node)) {
                massFlowRateDelta = massFlowRateDelta + (@kgPERs double) massFlowRates.get(edge);
            }
            for (GasEdge edge : network.outgoingEdges(node)) {
                massFlowRateDelta = massFlowRateDelta - (@kgPERs double) massFlowRates.get(edge);
            }
            massFlowRateDeltas.set(node, massFlowRateDelta);
        }

        double timeStep = problem.getTimeStep();

        for (GasNode node : network.nodes()) {
            //System.out.println(problem.getSpeedOfSound().getRelativeError());
            @kg double gasMass = UnitsTools.g8_to_kg((@bar double) pressures.get(node)/(problem.getSpeedOfSound()*problem.getSpeedOfSound())*node.getVolume());
            //System.out.println("m O: " + gasMass.getRelativeError());
            double massFlowRateDelta = (@kgPERs double) massFlowRateDeltas.get(node);
            double newDensity = (gasMass + (massFlowRateDelta*timeStep))/node.getVolume();

            @bar double newPressure = UnitsTools.pa_to_bar(newDensity*problem.getSpeedOfSound()*problem.getSpeedOfSound());
            //System.out.println("p^2 N: " + newPressure.getRelativeError());
            newPressures.set(node, newPressure);
        }

        return flow;
    }
}
