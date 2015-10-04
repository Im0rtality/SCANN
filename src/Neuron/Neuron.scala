package Neuron

import Layer.Layer
import Config.Env
import breeze.linalg.DenseVector

abstract class Neuron(val id: Int, val sinapses: Int, val layer: Layer) {
    var weights: DenseVector[Double] = _
    var output: Double = _
    var _error: Double = _

    def error(): Double = _error

    def initialize() = {}

    def updateError(target: Double) = {}

    def calculate(inputs: Any): Double

    override def toString: String = {
        weights.data.map(w => Env.PRECISION.format(w)).mkString(Console.GREEN + s"N#${layer.id}$id" + Console.RESET + "(", ", ", ")")
    }
}
