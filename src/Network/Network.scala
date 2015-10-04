package Network

import Layer._
import Utils.epsilonEquals
import breeze.linalg._
import breeze.numerics.round

class Network(inputs: Int, hiddenLayers: Int, outputs: Int, hiddenSizes: Option[List[Int]] = None) {
    var hiddenLayerSizes: List[Int] = hiddenSizes.getOrElse(List.fill(hiddenLayers) {
        Math.ceil((inputs + outputs) * 2 / 3).toInt
    })

    var params: Parameters = Parameters.fresh()
    var layers: List[Layer] = _

    def initialize() = {
        layers = List(new InputLayer(inputs))
        layers = layers ++ List.tabulate(hiddenLayers) {
            n => new HiddenLayer(n + 1, hiddenLayerSizes(n))
        }
        layers = layers :+ new OutputLayer(hiddenLayers + 1, outputs)

        layers.sliding(3).foreach(group => {
            group(1).prev = group.head
            group(1).next = group.last
        })

        layers.head.next = layers.tail.head
        layers.last.prev = layers.reverse.tail.head

        layers.foreach(l => l.initialize())

        layers
    }

    def calculate(input: DenseVector[Double]): DenseVector[Double] = {
        var output: DenseVector[Double] = input
        layers.foreach(layer => {
            output = layer.calculate(output)
        })
        output
    }

    def train(samples: List[(DenseVector[Double], DenseVector[Double])]) = {
        var iteration = 0
        var error: DenseVector[Double] = DenseVector()
        do {
            error = samples.map({ case (input, target) =>
                calculate(input)
                val error = calculateError(target)
                calculateWeight()
                DenseVector(error.toArray)
            }).last

            iteration += 1
        } while (Math.abs(max(error)) > params.minimumError && iteration < 100000)

        println(s"Iterations: $iteration")
        println(this)
    }

    def validate(samples: List[(DenseVector[Double], DenseVector[Double])]): Double = {
        val outputs = samples.map({ case (input, target) =>
            val output = calculate(input)
            (output, target, epsilonEquals(round(output), target))
        })

        //println(outputs.mkString("\n"))

        (0.0 + outputs.count({ case (_, _, pass) => pass })) / samples.size
    }

    def calculateError(target: DenseVector[Double]): List[Double] = {
        layers.reverse.foreach(_.updateError(target))
        layers.last.neurons.map(_.error())
    }

    def calculateWeight() = {
        layers.reverse.foreach(_.updateWeights(params.learningRate, params.learningMomentum))
    }

    override def toString: String = {
        params.toString + "\n" + layers.mkString("\n")
    }
}

object Network {
    def apply(inputs: Int, hidden: Int, outputs: Int, hiddenSize: Option[List[Int]] = None): Network = {
        val network = new Network(inputs, hidden, outputs, hiddenSize)
        network.initialize()
        network
    }
}