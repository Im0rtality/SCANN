package Neuron

import Layer._

class OutputNeuron(id: Int, sinapses: Int, layer: Layer) extends HiddenNeuron(id, sinapses, layer) {

    override def updateError(target: Double) = {
        _error = output - target
    }
}
