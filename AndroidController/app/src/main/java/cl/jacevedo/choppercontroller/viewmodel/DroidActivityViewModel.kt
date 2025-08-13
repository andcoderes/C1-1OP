package cl.jacevedo.choppercontroller.viewmodel

import androidx.compose.runtime.MutableState
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cl.jacevedo.choppercontroller.data.BluetoothDroidObject
import cl.jacevedo.choppercontroller.data.ButtonDroidEntity

class DroidActivityViewModel: ViewModel() {
    val droidVolume : MutableLiveData<Float> by lazy{
        MutableLiveData<Float>()
    }
    val macroButtonsList : MutableLiveData<MutableList<ButtonDroidEntity>> by lazy {
        MutableLiveData<MutableList<ButtonDroidEntity>>()
    }
    val audioButtonsList : MutableLiveData<MutableList<ButtonDroidEntity>> by lazy {
        MutableLiveData<MutableList<ButtonDroidEntity>>()
    }
}