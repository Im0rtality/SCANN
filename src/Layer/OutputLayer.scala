package Layer

import Neuron._

class OutputLayer(id: Int, size: Int) extends Layer(id, size) {

    override def initialize() = {
        neurons = List.tabulate(size) {
            n => new OutputNeuron(n, prev.neurons.size, this)
        }

        super.initialize()
    }

    override def toString = neurons.mkString(s"#$id Output\t\t[", ", ", "]")
}
