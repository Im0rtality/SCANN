package Network

object Parameters {
    def fresh(): Parameters = {
        new Parameters(0.3, 0.9, 0.01)
    }
}

class Parameters(var learningRate: Double, var learningMomentum: Double, val minimumError: Double) {

    override def toString = s"Params: rate=$learningRate, momentum=$learningMomentum, minErr=$minimumError"
}