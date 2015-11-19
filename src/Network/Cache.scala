package Network

import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file._
import play.api.libs.json._

import scala.io.Source

object Cache {
    def apply(network: Network, block: => Network): Network = {
        if (has(network)) {
            println("Cached weights found. Loading...")
            get(network)
        } else {
            val result = block
            set(result)
            result
        }
    }

    var weights: List[List[List[Double]]] = _

    def has(network: Network): Boolean = new File(network.cacheFile).exists()

    def get(network: Network): Network = {
        val jsonString = Source.fromFile(network.cacheFile).mkString
        val jsonObj = Json.parse(jsonString)
        val weights = jsonObj.as[List[List[List[Double]]]]
        network.layers.drop(1).zip(weights).foreach({ case (l, w) => l.loadWeights(w) })

        network
    }

    def set(network: Network): Network = {
        val weights: List[List[List[Double]]] = network.layers.drop(1).map(
            _.neurons.map(
                _.weights.toArray.toList
            )
        )

        Files.write(
            Paths.get(network.cacheFile),
            Json.toJson(weights).toString().getBytes(StandardCharsets.UTF_8),
            StandardOpenOption.TRUNCATE_EXISTING
        )

        network
    }
}




