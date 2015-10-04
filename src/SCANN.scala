import breeze.linalg._

object SCANN {
    def main(args: Array[String]) {

        val network = Network(2, 1, 1)
        println(network)
        network.calculate(DenseVector(2.0, 4.0))
    }
}

//object Samples {
//    def sample1(): List[(DenseVector[Double], DenseVector[Double])] = {
//        //List(2, 1)
//        List(
//            (DenseVector(2.0, 4.0), DenseVector(0.0)),
//            (DenseVector(3.0, 7.0), DenseVector(0.0)),
//            (DenseVector(5.0, 8.0), DenseVector(0.0)),
//            (DenseVector(3.0, 1.0), DenseVector(1.0)),
//            (DenseVector(5.0, 1.0), DenseVector(1.0)),
//            (DenseVector(7.0, 4.0), DenseVector(1.0))
//        )
//    }
//
//    def xor(): List[(DenseVector[Double], DenseVector[Double])] = {
//        //        List(2, 3, 1)
//        List(
//            (DenseVector(0.0, 0.0), DenseVector(0.0)),
//            (DenseVector(1.0, 0.0), DenseVector(1.0)),
//            (DenseVector(0.0, 1.0), DenseVector(1.0)),
//            (DenseVector(1.0, 1.0), DenseVector(0.0))
//        )
//    }
//
//    def majority(): List[(DenseVector[Double], DenseVector[Double])] = {
//        //        List(4, 1)
//        List(
//            (DenseVector(0.0, 0.0, 0,0, 0,0), DenseVector(0.0)),
//            (DenseVector(1.0, 0.0), DenseVector(1.0)),
//            (DenseVector(0.0, 1.0), DenseVector(1.0)),
//            (DenseVector(1.0, 1.0), DenseVector(0.0))
//        )
//    }
//}