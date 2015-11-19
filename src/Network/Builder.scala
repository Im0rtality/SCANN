package Network

import java.io.{File, FileInputStream}

import DataSet.{CsvLoader, DataSet}
import play.api.libs.json.Json

object Builder {
    def build(descriptor: File): (Network, DataSet) = {
        val json = Json.parse(new FileInputStream(descriptor))

        val dataSet = CsvLoader((json \ "dataset").as[String])

        val hidden = (json \ "architecture" \ "hidden").as[List[Int]]

        val network = new Network(dataSet.inputLayerSize, hidden.length, dataSet.outputLayerSize, Some(hidden))
        network.params = (json \ "parameters").as[Parameters]
        network.cacheFile = (json \ "cache").as[String]
        network.initialize()

        (network, dataSet)
    }
}
