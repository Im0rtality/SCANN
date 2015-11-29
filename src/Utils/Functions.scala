package Utils

import breeze.linalg._
import breeze.numerics._

object Functions {
    /**
     * Mean Absolute Error
     */
    def error(target: DenseVector[Double], output: DenseVector[Double]): Double = {
        sum(abs(output - target)) / output.activeSize.toDouble
    }

    def isPassing(target: Double, output: Double): Boolean = isDiffPassing(abs(target - output))

    def isDiffPassing(delta: Double, epsilon: Double = 0.1): Boolean = delta < epsilon
}
