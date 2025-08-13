package cl.jacevedo.droidcontroller.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cl.jacevedo.droidcontroller.data.ButtonDroidEntity

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