package cl.jacevedo.choppercontroller.droidController

import android.view.InputEvent
import android.view.KeyEvent
import android.view.MotionEvent

class DpadMotion {
    private var directionPressed: Int = -1 // initialized to -1
    private val TAG = "DpadMotion"
    fun getDirectionPressed(event: InputEvent?): Int {
        // If the input event is a MotionEvent, check its hat axis values.

        if (event is MotionEvent) {
            // Use the hat axis value to find the D-pad direction

            val motionEvent = event
            val xaxis = motionEvent.getAxisValue(MotionEvent.AXIS_HAT_X)
            val yaxis = motionEvent.getAxisValue(MotionEvent.AXIS_HAT_Y)

            // Check if the AXIS_HAT_X value is -1 or 1, and set the D-pad
            // LEFT and RIGHT direction accordingly.
            if (xaxis.compareTo(-1.0f) == 0) {
                directionPressed = LEFT
            } else if (xaxis.compareTo(1.0f) == 0) {
                directionPressed = RIGHT
            } else if (yaxis.compareTo(-1.0f) == 0) {
                directionPressed = UP
            } else if (yaxis.compareTo(1.0f) == 0) {
                directionPressed = DOWN
            } else if ((xaxis.compareTo(0.0f) == 0)
                && (yaxis.compareTo(0.0f) == 0)
            ) {
                directionPressed = CENTER
            }
        }
        return directionPressed
    }

    companion object {
        const val CENTER: Int = KeyEvent.KEYCODE_DPAD_CENTER
        const val LEFT: Int = KeyEvent.KEYCODE_DPAD_LEFT
        const val UP: Int = KeyEvent.KEYCODE_DPAD_UP
        const val RIGHT: Int = KeyEvent.KEYCODE_DPAD_RIGHT
        const val DOWN: Int = KeyEvent.KEYCODE_DPAD_DOWN
    }
}