package Layer

import Neuron.GenericNeuron
import breeze.linalg.DenseVector

class HiddenLayer(size: Int) extends Layer(size) {
    def calculate(inputs: DenseVector[Double]): DenseVector[Double] = {
        output = DenseVector(neurons.map(_.calculate(inputs)).toArray)
        output
    }

    def updateError(target: DenseVector[Double]) = {
        neurons.zip(target.toArray).foreach(t => {
            t._1.updateError(t._2)
        })
    }

    override def initialize() = {
        neurons = List.fill(size) {
            new GenericNeuron(prev.neurons.size, this)
        }

        super.initialize()
    }

    override def toString = neurons.mkString("HiddenLayer\t[", ", ", "]")
}
