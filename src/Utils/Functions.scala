package Utils

import breeze.linalg._
import breeze.numerics._

object Functions {
    /**
     * Mean Squared Error
     */
    def meanSquaredError(target: DenseVector[Double], output: DenseVector[Double]): Double = {
        sum(pow(output - target, 2)) / output.activeSize
    }
}
