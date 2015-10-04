package Neuron

import Layer._

class OutputNeuron(sinapses: Int, layer: Layer) extends HiddenNeuron(sinapses, layer) {

    override def updateError(target: Double) = {
        _error = output - target
    }
}
