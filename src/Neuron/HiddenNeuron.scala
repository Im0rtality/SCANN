package Neuron

import Activation.Sigmoid
import Layer._
import breeze.linalg._

class HiddenNeuron(id: Int, sinapses: Int, layer: Layer) extends Neuron(id, sinapses, layer) {

    override def initialize() = {
        weights = DenseVector.rand(sinapses)
    }

    override def calculate(input: Any): Double = {
        output = Sigmoid.activate(sum(input.asInstanceOf[DenseVector[Double]] :* weights))
        output
    }

    override def updateWeights(learningRate: Double, learningMomentum: Double) = {
        val prevOutputs = DenseVector(layer.prev.neurons.map(_.output).toArray)
        weights = weights :+ (learningRate * _error * prevOutputs)
    }

    override def updateError(target: Double) = {
        val nextWeights = DenseVector(layer.next.neurons.map(n => n.weights.toArray.toList(id)).toArray)
        val nextErrors = DenseVector(layer.next.neurons.map(n => n.error()).toArray)
        _error = delta() * sum(nextWeights :* nextErrors)
    }
}
