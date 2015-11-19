package Network

import play.api.libs.json.Reads
import play.api.libs.json._
import play.api.libs.functional.syntax._

object Parameters {
    def fresh(): Parameters = {
        new Parameters(0.3, 0, 0.001, 1000)
    }

    implicit val parametersReader: Reads[Parameters] = (
        (__ \ "rate").read[Double] and
            (__ \ "momentum").read[Double] and
            (__ \ "error").read[Double] and
            (__ \ "epochs").read[Int]
        ) (Parameters.apply _)
}

case class Parameters(rate: Double, momentum: Double, error: Double, epochs: Int) {

    override def toString = s"Params:\t\trate=$rate, momentum=$momentum, error=$error, epochs=$epochs"
}