package cl.jacevedo.choppercontroller.view

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.jacevedo.choppercontroller.R
import cl.jacevedo.choppercontroller.TAG
import cl.jacevedo.choppercontroller.data.BluetoothDroidObject
import cl.jacevedo.choppercontroller.data.ButtonDroidEntity
import cl.jacevedo.choppercontroller.data.DeviceType
import cl.jacevedo.choppercontroller.ui.theme.ChopperControllerTheme
import cl.jacevedo.choppercontroller.viewmodel.DroidActivityViewModel

@Preview(
    device = "spec:width=800dp,height=360dp,dpi=420,orientation=landscape",
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun DroidViewPreview() {
    val mainDroidViewModel = DroidActivityViewModel()
    val listObject = mutableListOf(
        ButtonDroidEntity(1,1, 1, "test", "1"),
        ButtonDroidEntity(1,1, 2, "test", "1"),
        ButtonDroidEntity(1,1, 2, "test", "1"),
        ButtonDroidEntity(1,1, 2, "test", "1"),
        ButtonDroidEntity(1,1, 2, "asdasdasdasd", "1"),
        ButtonDroidEntity(1,1, 2, "test", "1"),
        ButtonDroidEntity(1,1, 2, "test", "1"),
        ButtonDroidEntity(1,1, 2, "test", "1"),
        )

    mainDroidViewModel.audioButtonsList.value = listObject
    mainDroidViewModel.macroButtonsList.value = listObject
    ChopperControllerTheme {
        DroidActivityView(mainDroidViewModel, buttonClick = {}, sliderUpdate = {})
    }
}

@Composable
fun DroidActivityView(droidActivityViewModel: DroidActivityViewModel = viewModel(),
                      bluetoothDroidObject: BluetoothDroidObject? = BluetoothDroidObject("","",0, DeviceType.CHOPPER),
                      buttonClick : (ButtonDroidEntity) -> Unit,
                      sliderUpdate : (Float) -> Unit) {
    val macroButtonList = droidActivityViewModel.macroButtonsList.observeAsState()
    val audioButtonList = droidActivityViewModel.audioButtonsList.observeAsState()
    val uploadVolume = droidActivityViewModel.droidVolume.observeAsState(1f)
    Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
        Row(
            modifier = Modifier
                .padding(
                    top = padding.calculateTopPadding(),
                    bottom = padding.calculateBottomPadding()
                )
                .background(Color.Black)
                .fillMaxSize()

        ) {
            Column(
                Modifier
                    .width((LocalConfiguration.current.screenWidthDp.dp / 3.dp).dp)
                    .padding(
                        top = 16.dp, bottom = 16.dp, start = padding.calculateStartPadding(
                            LayoutDirection.Ltr
                        ) + 20.dp
                    )) {
                Image(
                    painter = painterResource(id = getButtonImage(bluetoothDroidObject)),
                    modifier = Modifier
                        .fillMaxHeight(1f)
                        .padding(end = 32.dp, start = 16.dp),
                    contentDescription = "chopper"
                )
                Slider(modifier = Modifier.focusProperties { canFocus=false }, value = uploadVolume.value, onValueChangeFinished = {
                    sliderUpdate(uploadVolume.value ?: 0f)
                }, onValueChange = {
                    droidActivityViewModel.droidVolume.value = it
                }, steps = 0, valueRange = 1f..255f)
            }
            Column(modifier = Modifier
                .padding(
                    top = 10.dp,
                    bottom = 10.dp,
                    end = padding.calculateEndPadding(LayoutDirection.Ltr) + 20.dp
                )
                .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween){
               Row (modifier = Modifier
                   .height((LocalConfiguration.current.screenHeightDp.dp))
                   .focusProperties { canFocus = false }){
                    LazyVerticalGrid( modifier = Modifier
                        .weight(2f)
                        .focusProperties { canFocus = false }, columns = GridCells.Fixed(2),  verticalArrangement = Arrangement.Top) {
                        audioButtonList.value?.forEach{ button ->
                            item {
                                componentButton(label = button.label ?: "", onClick = {buttonClick(button)})
                            }
                        }
                    }
                    LazyVerticalGrid( modifier = Modifier.weight(1f), columns = GridCells.Fixed(1),  verticalArrangement = Arrangement.Top) {
                        macroButtonList.value?.forEach{ button ->
                            item {
                                componentButton(label = button.label ?: "", onClick = {buttonClick(button)})
                            }
                        }

                    }
                }

            }
        }
    }

}

@Composable
fun componentButton(label: String, onClick: () -> Unit) {
    Button(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(8.dp),
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent, // Gold color for Star Wars feel
            contentColor = Color.Black
        )) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.button),
                contentDescription = "Chopper",
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize(),
            ) // Maintain aspect ratio and fit within bounds
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 24.dp),
                contentAlignment = Alignment.Center

            ) {
                DroidTextView(
                    color = Color.Gray,
                    overflow = TextOverflow.Ellipsis,
                    maxlines = 1,
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                    text = label,
                )
            }
        }
    }
}