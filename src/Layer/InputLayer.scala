package Layer

import Neuron.InputNeuron
import breeze.linalg.DenseVector

class InputLayer(size: Int) extends Layer(size) {

    def calculate(inputs: DenseVector[Double]): DenseVector[Double] = {
        inputs
    }

    def updateError(target: DenseVector[Double]): Unit = {
    }

    override def initialize() = {
        neurons = List.fill(size) {
            new InputNeuron(this)
        }

        super.initialize()
    }

    override def toString = neurons.mkString("Input\t\t[", ", ", "]")
}
