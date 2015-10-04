package Neuron

import Layer._
import breeze.linalg._

class OutputNeuron(id: Int, sinapses: Int, layer: Layer) extends HiddenNeuron(id, sinapses, layer) {

    override def updateError(target: Double) = {
        _error = output * (1 - output) * (target - output)
    }

    override def updateWeights(learningRate: Double, learningMomentum: Double) = {
        val outputs = DenseVector(layer.prev.neurons.map(_.output).toArray)
        weights = weights :+ learningRate * _error * outputs
    }
}
