package DataSet

import breeze.numerics._

import scala.util.Random

class DataSet(val input: List[Sample]) {
    val inputLayerSize: Int = input.head.input.activeSize
    val outputLayerSize: Int = input.head.target.activeSize

    def split(): (DataSet, DataSet) = {
        val all = Random.shuffle(input)
        val training = all.slice(0, round(input.length * 0.9).toInt)
        val validation = all.drop(training.length)

        (new DataSet(training), new DataSet(validation))
    }

    override def toString = input.mkString("DataSet(\n\t", "\n\t", "\n)")
}
