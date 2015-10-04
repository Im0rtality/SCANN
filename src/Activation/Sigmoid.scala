package Activation

object Sigmoid extends ActivationFunction {
    override def activate(t: Double): Double = {
        1 / (1 + Math.exp(-t))
    }

    override def derivative(t: Double): Double = {
        activate(1 - activate(t))
    }
}
