package Layer

import Neuron._

class OutputLayer(size: Int) extends HiddenLayer(size) {

    override def initialize() = {
        neurons = List.fill(size) {
            new OutputNeuron(prev.neurons.size, this)
        }

        super.initialize()
    }

    override def toString = neurons.mkString("Output\t\t[", ", ", "]")
}
