package Layer

import Neuron.HiddenNeuron

class HiddenLayer(id: Int, size: Int) extends Layer(id, size) {

    override def initialize() = {
        neurons = List.tabulate(size) {
            n => new HiddenNeuron(n, prev.neurons.size, this)
        }

        super.initialize()
    }

    override def toString = neurons.mkString(s"#$id HiddenLayer\t[", ", ", "]")
}
