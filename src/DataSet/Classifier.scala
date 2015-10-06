package DataSet

import breeze.linalg.DenseVector

class Classifier {
    var classes: Set[String] = Set()

    def register(_class: String) = {
        classes = classes + _class
    }

    def classToVector(_class: String): DenseVector[Double] = {
        val vector = DenseVector.zeros[Double](classes.size)
        vector(classes.toList.indexOf(_class)) = 1
        vector
    }
}
