package DataSet

import breeze.linalg.DenseVector
import breeze.numerics.round

import scala.util.Random

class DataSet(val input: List[Sample], val min: DenseVector[Double], val max: DenseVector[Double]) {
    def inputLayerSize: Int = input.head.input.activeSize
    def outputLayerSize: Int = input.head.target.activeSize

    override def toString = input.mkString("DataSet(\n\t", "\n\t", "\n)")

    def split(): (DataSet, DataSet) = {
        val all = Random.shuffle(input)
        val training = all.slice(0, round(input.length * 0.9).toInt)
        val validation = all.drop(training.length)

        (new DataSet(training, min, max), new DataSet(validation, min, max))
    }
}

object DataSet {
    def loadCached(input: List[Sample], min: Array[Double], max: Array[Double]): DataSet = {
        new DataSet(input, DenseVector(min), DenseVector(max))
    }

    def load(input: List[Sample], min: Array[Double], max: Array[Double]): DataSet = {
        new DataSet(input.map(s => normalize(s, DenseVector(min), DenseVector(max))), DenseVector(min), DenseVector(max))
    }

    def normalize(sample: Sample, min: DenseVector[Double], max: DenseVector[Double]): Sample = {
        new Sample((sample.input - min) / (max - min), sample.target)
    }
}