package Network

import java.io.File
import play.api.libs.json._
import scala.io.Source
import scalax.io.Resource

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

        val output = Resource.fromFile(network.cacheFile)
        output.truncate(0)
        output.write(Json.toJson(weights).toString())
        network
    }
}




