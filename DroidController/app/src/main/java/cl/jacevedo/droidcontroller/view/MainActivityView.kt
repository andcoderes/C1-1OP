package cl.jacevedo.droidcontroller.view

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.jacevedo.droidcontroller.R
import cl.jacevedo.droidcontroller.data.BluetoothDroidObject
import cl.jacevedo.droidcontroller.data.DeviceType
import cl.jacevedo.droidcontroller.ui.theme.ChopperControllerTheme
import cl.jacevedo.droidcontroller.viewmodel.MainActivityViewModel


@Preview(
    device = "spec:width=800dp,height=360dp,dpi=420,orientation=landscape",
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun StarWarsButtonViewPreview() {
    val mainActivityViewModel = MainActivityViewModel()
    val listObject = mutableListOf(BluetoothDroidObject("chopper","", 1, DeviceType.CHOPPER), BluetoothDroidObject("chopper","", 2, DeviceType.MOUSE))
    mainActivityViewModel.bluetoothDroidList.value = listObject
    ChopperControllerTheme {
        MainActivityView(
            mainActivityViewModel = mainActivityViewModel,
            onAddDeviceClick = { /* Handle click */ },
            onDeviceClick = { /* Handle click */ },
        )
    }
}

@Composable
fun MainActivityView(
    mainActivityViewModel : MainActivityViewModel = viewModel(),
    onAddDeviceClick: () -> Unit,
    onDeviceClick: (device : BluetoothDroidObject) -> Unit
) {
    val bluetoothDroidList by mainActivityViewModel.bluetoothDroidList.observeAsState()
    Scaffold(modifier = Modifier.fillMaxSize() ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = padding.calculateStartPadding(
                        LayoutDirection.Ltr
                    ),
                    bottom = padding.calculateBottomPadding(),
                    end = padding.calculateEndPadding(LayoutDirection.Ltr)
                )
                .background(Color.Black)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(
                        start = 16.dp, end = 16.dp,
                        top = padding.calculateTopPadding()
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,

                ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    LazyVerticalGrid( modifier = Modifier.focusProperties { canFocus = false }, columns = GridCells.Fixed(2),  verticalArrangement = Arrangement.Center) {
                        bluetoothDroidList?.forEach { droid ->
                            item {
                                StarWarsButton(
                                    imageResId = getButtonImage(droid),
                                    onClick = {
                                        onDeviceClick(droid)
                                    },
                                )
                            }
                        }
                    }

                }
            }
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 8.dp,
                        top = padding
                            .calculateTopPadding()
                            .plus(8.dp)
                    )
            ) {
                Button(onClick = onAddDeviceClick, modifier = Modifier.align(Alignment.TopEnd)) {
                    Text(text = "Add new Droid")
                }
            }
        }
    }
}

fun getButtonImage(droid: BluetoothDroidObject?): Int {
    return when(droid?.deviceType) {
        DeviceType.CHOPPER ->{
            R.drawable.chopper
        }
        DeviceType.MOUSE->{
            R.drawable.mouse
        }
        null -> {
            R.drawable.chopper_launcher
        }
    }
}


@Composable
fun StarWarsButton(imageResId: Int, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        modifier = modifier
            .width(360.dp)
            .height(140.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent, // Gold color for Star Wars feel
            contentColor = Color.Black
        ),

        ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.button),
                contentDescription = "Chopper",
                contentScale = ContentScale.FillWidth
            ) // Maintain aspect ratio and fit within bounds
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 24.dp),
                contentAlignment = Alignment.Center

            ) {
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = "Chopper",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .height(100.dp)
                ) // Maintain aspect ratio and fit within bounds
            }
        }
    }
}