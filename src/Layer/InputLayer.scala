package Layer

import Neuron.InputNeuron
import breeze.linalg.DenseVector

class InputLayer(size: Int) extends Layer(0, size) {

    override def calculate(inputs: DenseVector[Double]): DenseVector[Double] = inputs

    override def updateError(target: DenseVector[Double]): Unit = {}

    override def initialize() = {
        neurons = List.tabulate(size) {
            n => new InputNeuron(n, this)
        }

        super.initialize()
    }

    override def toString = neurons.mkString(s"#$id Input\t\t\t[", ", ", "]")
}
