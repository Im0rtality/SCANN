package Utils

import breeze.linalg._
import breeze.numerics._

object epsilonEquals {
    def apply(a: DenseVector[Long], b: DenseVector[Double]): Boolean = {
        sum(abs(convert(a, Double) - b)) < 0.1
    }
}
