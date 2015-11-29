import java.io.File

import DataSet.DataSet
import Network._
import Utils.Benchmark
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
                var (network, dataset, classification) = Builder.build(config.descriptor)
                val (training, validation) = dataset.split()
                network = Cache(network, {
                    network.train(dataset.input)
                })

                network.validate(dataset.input)

                if (classification.input.nonEmpty) {
                    Benchmark({
                        val output = classification.input.map(s => network.calculate(s.input))
                        output.foreach(s => println(s.toArray.map(d => "%.2f".format(d)).mkString(" ")))
                    }, "CLASSIFICATION")
                }

            case None =>
        }
    }
}