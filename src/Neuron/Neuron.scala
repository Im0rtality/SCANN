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

    def updateWeights(learningRate: Double, learningMomentum: Double) = {}

    def calculate(inputs: Any): Double

    def delta(): Double = output * (1 - output)

    def store(): Seq[Double] = {
        if (weights != null) {
            weights.toArray.toList
        } else {
            List()
        }
    }

    def load(data: List[Double]) = {
        weights = DenseVector(data.toArray)
    }

    override def toString: String = {
        Console.GREEN + s"N#${layer.id}$id" + Console.RESET + weights.toArray.map(Env.PRECISION.format(_)).mkString("[", ",", "]")
    }
}
