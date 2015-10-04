package Layer

import Neuron.Neuron
import breeze.linalg._

abstract class Layer(val id: Int, val size: Int) {
    var prev: Layer = _
    var next: Layer = _
    var neurons: List[Neuron] = _

    var output: DenseVector[Double] = DenseVector()

    def calculate(inputs: DenseVector[Double]): DenseVector[Double] = {
        output = DenseVector(neurons.map(_.calculate(inputs)).toArray)
        output
    }

    def updateError(target: DenseVector[Double]) = {
        neurons.zip(target.toArray).foreach(t => {
            t._1.updateError(t._2)
        })
    }

    def initialize() = {
        neurons.foreach(n => n.initialize())
    }
}