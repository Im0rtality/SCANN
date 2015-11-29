package Network

import java.io._
import java.nio.charset.StandardCharsets

import DataSet.{Sample, DataSet}
import breeze.linalg.DenseVector
import play.api.libs.json.{JsObject, Json}

import scala.io.Source

class DataSetCache {

    def has(cacheKey: String): Boolean = new File(cacheKey).exists()

    def get(cacheKey: String): DataSet = {
        val json = Json.parse(Source.fromFile(cacheKey).mkString)
        val inputs: List[Sample] = (json \ "input").as[List[JsObject]].map(o => {
            new Sample(DenseVector((o \ "input").as[Array[Double]]), DenseVector((o \ "target").as[Array[Double]]))
        })
        DataSet.loadCached(inputs, (json \ "min").as[Array[Double]], (json \ "max").as[Array[Double]])
    }

    def set(cacheObject: DataSet, cacheKey: String) = {
        val inputs = cacheObject.input.map(s => Json.obj("input" -> s.input.toArray, "target" -> s.target.toArray))
        val min = cacheObject.min.toArray
        val max = cacheObject.max.toArray
        val stream = new FileOutputStream(cacheKey, false)
        stream.write(Json.toJson(Json.obj("input" -> inputs, "min" -> min, "max" -> max)).toString().getBytes(StandardCharsets.UTF_8))
        stream.close()
    }
}

object DataSetCache {
    def apply(cacheKey: String, block: => DataSet): DataSet = {
        val cache = new DataSetCache
        if (cache.has(cacheKey + ".cached.json")) {
            cache.get(cacheKey + ".cached.json")
        } else {
            val result = block
            cache.set(result, cacheKey + ".cached.json")
            result
        }
    }
}