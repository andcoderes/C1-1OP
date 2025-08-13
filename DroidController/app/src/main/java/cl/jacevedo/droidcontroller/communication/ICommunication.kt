package cl.jacevedo.droidcontroller.communication

enum class ConnectionStatus(val value: Int) {
    ARDUINO(-1),
    BUTTONS(0),
    MOVEMENTS(1),
    SETTINGS(2),
    AUDIO(3),
    PING(4)
}

interface ICommunication