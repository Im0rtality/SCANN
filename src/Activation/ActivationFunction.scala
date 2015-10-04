package Activation

abstract class ActivationFunction {
    def activate(t: Double): Double

    def derivative(t: Double): Double
}
