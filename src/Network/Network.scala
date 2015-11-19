package Network

import DataSet.Sample
import Layer._
import Utils._
import breeze.linalg._
import breeze.numerics._

class Network(inputs: Int, hiddenLayers: Int, outputs: Int, hiddenSizes: Option[List[Int]] = None) {
    var hiddenLayerSizes: List[Int] = hiddenSizes.getOrElse(List.fill(hiddenLayers) {
        ceil((inputs + outputs) / 2.0).toInt
    })

    var params: Parameters = Parameters.fresh()
    var layers: List[Layer] = _

    var cacheFile: String = _

    def initialize() = {
        layers = List(new InputLayer(inputs))
        layers = layers ++ List.tabulate(hiddenLayers) {
            n => new HiddenLayer(n, hiddenLayerSizes(n))
        }
        layers = layers :+ new OutputLayer(hiddenLayers, outputs)

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

    def train(samples: List[Sample]) = {
        Benchmark({
            var iteration = 0
            var error: Double = 0
            do {
                error = samples.map(sample => {
                    val output = calculate(sample.input)
                    calculateError(sample.target)
                    calculateWeight()

                    Functions.meanSquaredError(sample.target, output)
                }).max


                iteration += 1
                println(s"#$iteration\t\tmax MSE: \t$error")
            } while (params.error < error && iteration < params.epochs)

            println(s"Iterations: \t$iteration")
            println("Error^2: \t\t%.5f".format(error))
        }, "TRAINING")

        this
    }

    def validate(samples: List[Sample]): Double = {
        val outputs = Benchmark({
            samples.map(sample => {
                val output = calculate(sample.input)
                (output, sample.target, epsilonEquals(round(output), sample.target))
            })
        }, "VALIDATING")

        (0.0 + outputs.count({ case (_, _, pass) => pass })) / samples.size
    }

    def calculateError(target: DenseVector[Double]): List[Double] = {
        layers.reverse.foreach(_.updateError(target))
        layers.last.neurons.map(_.error())
    }

    def calculateWeight() = {
        layers.reverse.foreach(_.updateWeights(params.rate, params.momentum))
    }

    override def toString: String = {
        params.toString + "\nArchitecture:\t" + layers.map(_.neurons.length).mkString("-")
    }
}

object Network {
    def apply(inputs: Int, hidden: Int, outputs: Int, hiddenSize: Option[List[Int]] = None): Network = {
        val network = new Network(inputs, hidden, outputs, hiddenSize)
        network.initialize()
        network
    }
}