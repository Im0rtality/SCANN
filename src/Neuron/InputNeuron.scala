package Neuron

import Layer.Layer

class InputNeuron(layer: Layer) extends Neuron(1, layer) {

    override def calculate(input: Any): Double = {
        input.asInstanceOf[Double]
    }

    override def toString: String = Console.GREEN + "N" + Console.RESET
}

