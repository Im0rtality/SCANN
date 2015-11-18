package Network

object Parameters {
    def fresh(): Parameters = {
        new Parameters(0.3, 0, 0.001, 1E9)
    }
}

class Parameters(var learningRate: Double, var learningMomentum: Double, val minimumError: Double, _maxEpochs: Double) {
    val maxEpochs: Long = _maxEpochs.round

    override def toString = s"Params:\t\trate=$learningRate, momentum=$learningMomentum, minErr=$minimumError, maxEpochs=$maxEpochs"
}