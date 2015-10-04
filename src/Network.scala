import Layer._
import breeze.linalg._

class Network(inputs: Int, hiddenLayers: Int, outputs: Int, hiddenSizes: Option[List[Int]] = None) {
    var hiddenLayerSizes: List[Int] = hiddenSizes.getOrElse(List.fill(hiddenLayers){Math.ceil((inputs + outputs) * 2 / 3).toInt})

    var layers: List[Layer] = _

    def initialize() = {
        layers = List(new InputLayer(inputs))
        layers = layers ++ List.tabulate(hiddenLayers) {
            n => new HiddenLayer(hiddenLayerSizes(n))
        }
        layers = layers :+ new OutputLayer(outputs)

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

    //    def train(samples: List[(DenseVector[Double], DenseVector[Double])]) = {
    //        var correct = 9999
    //        var index = 0
    //        println("Dataset size:\t%d\n".format(samples.size))
    //        try {
    //            //            while (true) {
    //            samples.foreach(sample => {
    //                println("Iteration #%d".format(index))
    //                println("Classifying:\t%s".format(sample._1))
    //
    //                val actual = calculate(sample._1)
    //                println("Expected:\t\t%s\nActual:  \t\t%s".format(sample._2, actual))
    //                println("SqError:\t\t%f".format(error(sample._2, actual)))
    //                //                    backpropagate((sample._2 - actual).toArray.toList)
    //
    //                index += 1
    //                if (samples.size <= index) {
    //                    throw new IllegalStateException("breaking loop")
    //                }
    //            })
    //            //            }
    //        } catch {
    //            case e: IllegalStateException =>
    //        }
    //    }

    def calculateError(target: DenseVector[Double]) = {
        layers.reverse.foreach(_.updateError(target))
        layers.last.neurons.map(_.error)
    }

    override def toString: String = {
        layers.mkString("\n")
    }
}

object Network {
    def apply(inputs: Int, hidden: Int, outputs: Int, hiddenSize: Option[List[Int]] = None): Network = {
        val network = new Network(inputs, hidden, outputs, hiddenSize)
        network.initialize()
        network
    }
}