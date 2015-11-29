package DataSet

import Network._
import breeze.linalg._
import breeze.numerics.abs

import scala.io.Source

class CsvLoader(val fileName: String) {
    val reader = Source.fromFile(fileName).bufferedReader()
    val stream = Stream.continually(reader.readLine()).takeWhile(_ != null)

    def load: DataSet = {
        DataSetCache(fileName, {
            val classifier = new Classifier()
            var data: List[ClassSample] = List()
            var minInputs: Array[Double] = null
            var maxInputs: Array[Double] = null
            stream.dropWhile(_.startsWith("#")).foreach(line => {
                val _class :: inputs = line.split(",").reverse.toList
                classifier.register(_class)
                val inputParams = formatInput(inputs.reverse)
                if (minInputs == null) {
                    minInputs = inputParams.toArray
                } else {
                    minInputs = minInputs.zip(inputParams).map({ case (m, i) => min(abs(m), abs(i)) })
                }
                if (maxInputs == null) {
                    maxInputs = inputParams.toArray
                } else {
                    maxInputs = maxInputs.zip(inputParams).map({ case (m, i) => max(abs(m), abs(i)) })
                }
                data = data :+ new ClassSample(DenseVector(inputParams.toArray), _class)
            })

            DataSet.load(data.map(_.toSample(classifier)), minInputs, maxInputs)
        })
    }

    def loadClassification(dataSet: DataSet): DataSet = {
        val classifier = new Classifier()
        var data: List[ClassSample] = List()
        stream.dropWhile(_.startsWith("#")).foreach(line => {
            val _class :: inputs = line.split(",").reverse.toList
            classifier.register(_class)
            val inputParams = formatInput(inputs.reverse)
            data = data :+ new ClassSample(DenseVector(inputParams.toArray), _class)
        })

        DataSet.load(data.map(_.toSample(classifier)), dataSet.min.toArray, dataSet.max.toArray)
    }

    def formatInput(value: List[String]): List[Double] = {
        value.map(_.trim().toDouble)
    }
}

object CsvLoader {
    def apply(fileName: String): DataSet = {
        new CsvLoader(fileName).load
    }

    def loadClassification(fileName: String, normalization: DataSet): DataSet = {
        if (fileName == "") {
            println("Loading cached dataset")
            DataSet.loadCached(List(), normalization.min.toArray, normalization.max.toArray)
        } else {
            new CsvLoader(fileName).loadClassification(normalization)
        }
    }
}
