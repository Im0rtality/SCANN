package Neuron

import Layer.Layer
import breeze.linalg.DenseVector

abstract class Neuron(val sinapses: Int, val layer: Layer) {
    var weights: DenseVector[Double] = _
    var output: Double = _
    var error: Double = _

    def initialize()

    def updateError(target: Double)

    def calculate(inputs: Any): Double

    override def toString: String = {
        weights.data.map(w => "%.3f".format(w)).mkString("N(", ", ", ")")
    }
}
