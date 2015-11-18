import java.io.IOException

import DataSet._
import Network._

object SCANN {
    def main(args: Array[String]) {
        val (network, dataset, file) = prepareIris()
        //         val (network, dataset) = prepareXor()
        //         val (network, dataset) = prepare1D()
        println(network)
        println()
        network.train(dataset.input)
        network.store(file)

        val accuracy = network.validate(dataset.input)
        println("Accuracy:\t\t%.2f%%".format(accuracy * 100.0))
    }

    private def prepareIris(): (Network, DataSet, String) = {
        val dataset = Loader.fromCsv("./data/iris.csv")
        var network: Network = null
        try {
            network = Network.loadFrom("./data/iris.ann")
        } catch {
            case e: IOException =>
                network = Network(dataset.inputLayerSize, 1, dataset.outputLayerSize, Some(List(4)))
        }
        network.params = new Parameters(0.3, 0.9, 0.005, 1E3) // => gives ~96% accuracy
        (network, dataset, "./data/iris.ann")
    }

    private def prepareXor(): (Network, DataSet, String) = {
        val dataset = Loader.fromCsv("./data/xor.csv")
        val network = Network(dataset.inputLayerSize, 1, dataset.outputLayerSize, Some(List(2)))
        (network, dataset, "./data/xor.ann")
    }

    private def prepare1D(): (Network, DataSet, String) = {
        val dataset = Loader.fromCsv("./data/1D-sample.csv")
        val network = Network(dataset.inputLayerSize, 0, dataset.outputLayerSize)
        network.params = new Parameters(0.5, 0, 0.001, 1E3)
        (network, dataset, "./data/1D-sample.ann")
    }
}