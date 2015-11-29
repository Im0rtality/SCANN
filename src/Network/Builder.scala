package Network

import java.io.{File, FileInputStream}

import DataSet.{CsvLoader, DataSet}
import Utils.Benchmark
import play.api.libs.json.Json

object Builder {
    def build(descriptor: File): (Network, DataSet, DataSet) = {
        val json = Json.parse(new FileInputStream(descriptor))

        val dataSet = Benchmark({
            CsvLoader((json \ "dataset").as[String])
        }, "LOAD DATASET")

        val classificationSet = Benchmark({
            CsvLoader.loadClassification((json \ "classification").as[String], dataSet)
        }, "LOAD CLASSIFICATION")

        println("DataSet size: %d items (%d inputs, %d outputs)"
            .format(dataSet.input.length, dataSet.inputLayerSize, dataSet.outputLayerSize))

        val hidden = (json \ "architecture" \ "hidden").as[List[Int]]

        val network = new Network(dataSet.inputLayerSize, hidden.length, dataSet.outputLayerSize, Some(hidden))
        network.params = (json \ "parameters").as[Parameters]
        network.cacheFile = (json \ "cache").as[String]
        network.initialize()

        println("Network: %d-%s-%d".format(
            dataSet.inputLayerSize,
            hidden.map(_.toString).mkString("[", "-", "]"),
            dataSet.outputLayerSize)
        )

        (network, dataSet, classificationSet)
    }
}
