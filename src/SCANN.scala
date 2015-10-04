import Network.{Parameters, Network}
import breeze.linalg._

object SCANN {
    def main(args: Array[String]) {

        val sample = Samples.simple1D()
        val network = sample._1
        val dataset = sample._2
        network.params = new Parameters(0.03, 1, 0.001)
        println(network)
        println()

        network.train(dataset)

        val accuracy = network.validate(dataset)
        println("Accuracy: %.2f%%".format(accuracy * 100.0))
    }
}

object Samples {
    def xor(): (Network, List[(DenseVector[Double], DenseVector[Double])]) = {
        (
            Network(2, 2, 1, Some(List(3, 2))),
            List(
                (DenseVector(0.0, 0.0), DenseVector(0.0)),
                (DenseVector(1.0, 0.0), DenseVector(1.0)),
                (DenseVector(0.0, 1.0), DenseVector(1.0)),
                (DenseVector(1.0, 1.0), DenseVector(0.0))
            )
            )
    }

    def simple1D(): (Network, List[(DenseVector[Double], DenseVector[Double])]) = {
        (
            // class = input >= 0.5 ? 1 : 0
            Network(2, 0, 1),
            List(
                (DenseVector(0.0, 1d), DenseVector(0.0)),
                (DenseVector(0.1, 1d), DenseVector(0.0)),
                (DenseVector(0.2, 1d), DenseVector(0.0)),
                (DenseVector(0.3, 1d), DenseVector(0.0)),
                (DenseVector(0.4, 1d), DenseVector(0.0)),
                (DenseVector(0.5, 1d), DenseVector(1.0)),
                (DenseVector(0.6, 1d), DenseVector(1.0)),
                (DenseVector(0.7, 1d), DenseVector(1.0)),
                (DenseVector(0.8, 1d), DenseVector(1.0)),
                (DenseVector(0.9, 1d), DenseVector(1.0))
            )
            )
    }
}