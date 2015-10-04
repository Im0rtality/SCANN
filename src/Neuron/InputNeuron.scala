package Neuron

import Layer.Layer
import breeze.linalg._

class InputNeuron(layer: Layer) extends Neuron(1, layer) {

    override def initialize() = {
        weights = DenseVector(1.0)
    }

    override def updateError(target: Double) = {}

    override def calculate(input: Any): Double = {
        input.asInstanceOf[Double]
    }

    override def toString: String = "N(1.000)"
}

