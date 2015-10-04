package Layer

import Neuron.Neuron
import breeze.linalg._

abstract class Layer(val size: Int) {
    var prev: Layer = _
    var next: Layer = _
    var neurons: List[Neuron] = _

    var output: DenseVector[Double] = DenseVector()

    def calculate(inputs: DenseVector[Double]): DenseVector[Double]

    def updateError(target: DenseVector[Double])

    def initialize() = {
        neurons.foreach(n => n.initialize())
    }
}