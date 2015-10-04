package Neuron

import Layer.Layer

class InputNeuron(id: Int, layer: Layer) extends Neuron(id, 1, layer) {

    override def calculate(input: Any): Double = {
        output = input.asInstanceOf[Double]
        output
    }

    override def toString: String = Console.GREEN + s"N#${layer.id}$id" + Console.RESET + "()"
}

