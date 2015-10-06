package DataSet

import breeze.linalg.DenseVector

import scala.io.Source

class CsvLoader(val fileName: String) {
    val reader = Source.fromFile(fileName).bufferedReader()
    val stream = Stream.continually(reader.readLine()).takeWhile(_ != null)

    def load: DataSet = {
        val classifier = new Classifier()
        var data: List[ClassSample] = List()
        stream.dropWhile(_.startsWith("#")).foreach(line => {
            val _class :: inputs = line.split(",").reverse.toList
            classifier.register(_class)
            data = data :+ new ClassSample(formatInput(inputs.reverse), _class)
        })

        new DataSet(data.map(_.toSample(classifier)))
    }

    def formatInput(value: List[String]): DenseVector[Double] = {
        DenseVector(value.map(_.trim().toDouble).toArray)
    }
}



