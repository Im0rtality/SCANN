package DataSet

import breeze.linalg.DenseVector
import breeze.numerics.round

class Sample(val input: DenseVector[Double], val target: DenseVector[Double]) {

    override def toString = "[%s]\t[%s]".format(input.toArray.mkString(", "), round(target).toArray.mkString(","))
}

class ClassSample(val input: DenseVector[Double], val _class: String) {
    def toSample(classifier: Classifier): Sample = {
        new Sample(input, classifier.classToVector(_class))
    }
}