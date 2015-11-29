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
            samples.map(s => calculate(s.input))
            do {
                val errors = samples.map(sample => {
                    calculateError(sample.target)
                    calculateWeight()

                    val output = calculate(sample.input)
                    Functions.error(sample.target, output)
                })

                error = errors.sum / errors.length
                val correct = errors.count(e => Functions.isDiffPassing(e))
                printIteration(iteration, error, correct * 100.0 / samples.length)
                Cache.set(this, "%05d".format(iteration))
                iteration += 1
            } while (params.error < error && iteration < params.epochs)

            val errors = samples.map(sample => {
                Functions.error(sample.target, calculate(sample.input))
            })

            printIteration(iteration, errors.sum / errors.length, errors.count(e => Functions.isDiffPassing(e)) * 100.0 / samples.length)

            println(s"Iterations: \t$iteration")
            println("Error: \t\t%.5f".format(error))
            this
        }, "TRAINING")
    }

    def validate(samples: List[Sample]) = {
        val errors = Benchmark({
            samples.map(sample => {
                val output = calculate(sample.input)
                Functions.error(sample.target, output)
            })
        }, "VALIDATING")

        val passing = errors.count(e => Functions.isDiffPassing(e, 0.1))
        println("Error:\t\t\t%.5f".format(errors.sum / samples.length))
        println("Accuracy:\t\t%.2f%% (%d/%d passing)".format(passing.toDouble / samples.size * 100.0, passing, samples.size))
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

    def printIteration(iteration: Int, error: Double, correct: Double) = {
        println("#%05d error: %.5f (%.2f%% correct)".format(iteration, error, correct))
    }
}

object Network {
    def apply(inputs: Int, hidden: Int, outputs: Int, hiddenSize: Option[List[Int]] = None): Network = {
        val network = new Network(inputs, hidden, outputs, hiddenSize)
        network.initialize()
        network
    }
}