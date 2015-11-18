package Network

import DataSet.Sample
import Layer._
import Utils._
import breeze.linalg._
import breeze.numerics._
import scala.pickling.Defaults._, scala.pickling.json._
import scala.io.Source
import scalax.io.Resource

class Network(inputs: Int, hiddenLayers: Int, outputs: Int, hiddenSizes: Option[List[Int]] = None) {
    var hiddenLayerSizes: List[Int] = hiddenSizes.getOrElse(List.fill(hiddenLayers) {
        Math.ceil((inputs + outputs) / 2.0).toInt
    })

    var params: Parameters = Parameters.fresh()
    var layers: List[Layer] = _

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

    def store(file: String) = {
        Resource.fromFile(file).truncate(0)
        Resource.fromFile(file).write(this.pickle.value)
    }

    def calculate(input: DenseVector[Double]): DenseVector[Double] = {
        var output: DenseVector[Double] = input
        layers.foreach(layer => {
            output = layer.calculate(output)
        })
        output
    }

    def train(samples: List[Sample]) = {
        Benchmark {
            var iteration = 0
            var correct = 0
            var error: Double = 0
            do {
                error = samples.map(sample => {
                    val output = calculate(sample.input)
                    calculateError(sample.target)
                    calculateWeight()

                    Functions.meanSquaredError(sample.target, output)
                }).sum / samples.size


                iteration += 1
                println("#%9d   => %.10f".format(iteration, error))
            } while (params.minimumError < error && iteration < params.maxEpochs)

            println(Console.YELLOW + "TRAINING FINISHED" + Console.RESET)
            println(s"Iterations: \t$iteration")
            println("Error^2: \t\t%.5f".format(error))
        }
    }

    def validate(samples: List[Sample]): Double = {
        println("\n\nVALIDATING\n")
        val outputs = Benchmark {
            samples.map(sample => {
                val output = calculate(sample.input)
                (output, sample.target, epsilonEquals(round(output), sample.target))
            })
        }

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

    def loadFrom(file: String): Network = {
        val network: Network = JSONPickle(Source.fromFile(file).mkString).unpickle[Network]
        network
    }
}