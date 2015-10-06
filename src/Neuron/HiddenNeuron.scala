package Neuron

import Activation.Sigmoid
import Layer._
import breeze.linalg._

class HiddenNeuron(id: Int, sinapses: Int, layer: Layer) extends Neuron(id, sinapses, layer) {

    var lastWeightDelta: DenseVector[Double] = DenseVector.zeros(sinapses + 1)

    override def initialize() = {
        weights = DenseVector.fill[Double](sinapses + 1)(Math.random() * 2 - 1)
    }

    override def calculate(input: Any): Double = {
        output = Sigmoid.activate(sum(input.asInstanceOf[DenseVector[Double]] :* weights))
        output
    }

    override def updateWeights(learningRate: Double, learningMomentum: Double) = {
        val prevOutputs = DenseVector(layer.prev.neurons.map(_.output).toArray :+ 1.0)
        val weightsDelta = learningRate * _error * prevOutputs
        weights = weights :+ weightsDelta :+ (learningMomentum * lastWeightDelta)
        lastWeightDelta = weightsDelta
    }

    override def updateError(target: Double) = {
        val nextWeights = DenseVector(layer.next.neurons.map(n => n.weights.toArray.toList(id)).toArray)
        val nextErrors = DenseVector(layer.next.neurons.map(n => n.error()).toArray)
        _error = delta() * sum(nextWeights :* nextErrors)
    }
}
