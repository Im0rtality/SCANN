package Neuron

import Activation.Sigmoid
import Layer._
import breeze.linalg._

class HiddenNeuron(id:Int, sinapses: Int, layer: Layer) extends Neuron(id, sinapses, layer) {

    override def initialize() = {
        weights = DenseVector.ones[Double](sinapses) * 0.5
    }

    override def calculate(input: Any): Double = {
        output = Sigmoid.activate(sum(input.asInstanceOf[DenseVector[Double]] :* weights))
        output
    }

    override def updateError(target: Double) = {
        val weights = DenseVector(layer.next.neurons.map(n => n.weights.toArray.toList(id)).toArray)
        val errors = DenseVector(layer.next.neurons.map(n => n.error()).toArray)
        _error = sum(weights :* errors)
    }
}
