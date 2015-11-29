import java.io.File

import Network._
import scopt.OptionParser

object SCANN {
    def main(args: Array[String]) {
        val parser = new OptionParser[Config]("SCANN") {
            head("SCANN")
            opt[File]('n', "network") required() valueName "<file>" action { (x, c) =>
                c.copy(descriptor = x)
            } text "network is a required path to JSON file containing network configuration"
        }
        parser.parse(args, Config()) match {
            case Some(config) =>
                var (network, dataset) = Builder.build(config.descriptor)

                val (training, validation) = dataset.split()

                network = Cache(network, {
                    network.train(training.input)
                })

                val accuracy = network.validate(validation.input)
                println("Accuracy:\t\t%.2f%%".format(accuracy * 100.0))
            case None =>
        }
    }
}