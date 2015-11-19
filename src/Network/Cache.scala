package Network

import java.io.File

import play.api.libs.json._
import scala.io.Source
import scalax.io.Resource

class Cache(file: String) {
    var weights: List[List[List[Double]]] = _

    def has(): Boolean = new File(file).exists()

    def get(network: Network): Network = {
        val jsonString = Source.fromFile(file).mkString
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

        val output = Resource.fromFile(file)
        output.truncate(0)
        output.write(Json.toJson(weights).toString())
        network
    }
}

object Cache {
    def apply(file: String, network: Network, block: => Network): Network = {
        val cache = new Cache(file)
        if (cache.has()) {
            println("Cached weights found. Loading...")
            cache.get(network)
        } else {
            val result = block
            cache.set(result)
            result
        }
    }
}




