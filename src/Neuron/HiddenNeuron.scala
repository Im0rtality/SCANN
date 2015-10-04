package Neuron

import Activation.Sigmoid
import Layer._
import breeze.linalg._

class HiddenNeuron(sinapses: Int, layer: Layer) extends Neuron(sinapses, layer) {

    override def initialize() = {
        weights = DenseVector.ones[Double](sinapses) * 0.5
    }

    override def calculate(input: Any): Double = {
        output = Sigmoid.activate(sum(input.asInstanceOf[DenseVector[Double]] :* weights))
        output
    }

    override def updateError(target: Double) = {
        _error = sum(weights :* DenseVector(layer.next.neurons.map(n => {
            println(n)
            0.0
        }).toArray))
    }
}
