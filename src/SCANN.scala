import Network._

object SCANN {
    def main(args: Array[String]) {
        var (network, dataset) = Builder.build("./data/iris.json")

        network = Cache(network, {
            network.train(dataset.input)
        })

        val accuracy = network.validate(dataset.input)
        println("Accuracy:\t\t%.2f%%".format(accuracy * 100.0))
    }
}