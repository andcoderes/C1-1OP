package cl.jacevedo.droidcontroller.communication


data class MovementObject( val lxAxis: Int, val lyAxis: Int, val rxAxis: Int, val ryAxis: Int, val buttonR2:Int, val buttonL2: Int ) {
    fun isAllZero(): Boolean {
        return lxAxis == 0 && lyAxis == 0 && rxAxis == 0 && ryAxis == 0 && buttonR2 == 0  && buttonL2 == 0
    }
    fun movementObject() = Movement(ConnectionStatus.MOVEMENTS.value, arrayOf(lxAxis,lyAxis), arrayOf(rxAxis,lxAxis), arrayOf(buttonL2, buttonR2))
}