package Neuron

import Activation.Sigmoid
import Layer.Layer
import breeze.linalg._

class GenericNeuron(sinapses: Int, layer: Layer) extends Neuron(sinapses, layer) {

    override def initialize() = {
        weights = DenseVector.ones[Double](sinapses) * 0.5
    }

    override def calculate(input: Any): Double = {
        output = Sigmoid.activate(sum(input.asInstanceOf[DenseVector[Double]] :* weights))
        output
    }

    override def updateError(target: Double) = {
        if (layer.next.isInstanceOf[Layer]) {
            error = sum(weights :* DenseVector(layer.next.neurons.map(_.error).toArray))
        } else {
            error = output - target
        }
    }
}
