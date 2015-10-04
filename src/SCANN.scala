import breeze.linalg._

object SCANN {
    def main(args: Array[String]) {

        val sample = Samples.xor()
        val network = sample._1
        val dataset = sample._2
        println(network)
        println()
        dataset.foreach({ case (input, target) =>
            val output = network.calculate(input)
            val error = network.calculateError(target)
            println(
                "Target: %s\tOutput: %s, \tError: %s".format(
                    target.toArray.mkString("[", " ", "]"),
                    output.toArray.map(f => f - (f % 0.001)).mkString("[", " ", "]"),
                    error.mkString("[", " ", "]")
                )
            )
            println(network)
        })
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
}