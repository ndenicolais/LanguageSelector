package com.denicks21.languageselector

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.denicks21.languageselector.repository.DataStoreRepository
import com.denicks21.languageselector.ui.theme.DarkGrey
import com.denicks21.languageselector.ui.theme.DarkText
import com.denicks21.languageselector.ui.theme.LanguageSelectorTheme
import com.denicks21.languageselector.ui.theme.LightText
import com.denicks21.languageselector.ui.theme.LightYellow
import com.denicks21.languageselector.viewmodels.LanguageViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch
import java.util.Locale

val language = listOf("English", "Italian", "Spanish", "German", "French")

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LanguageSelectorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.surface
                ) {
                    val systemUiController = rememberSystemUiController()
                    val statusBarColor = MaterialTheme.colors.surface
                    val navigationBarColor = MaterialTheme.colors.onSurface
                    val barIcons = isSystemInDarkTheme()

                    SideEffect {
                        systemUiController.setNavigationBarColor(
                            color = navigationBarColor,
                            darkIcons = barIcons
                        )
                        systemUiController.setStatusBarColor(
                            color = statusBarColor,
                            darkIcons = true
                        )
                    }
                    HomePage()
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomePage() {
    var showInfoDialog by remember { mutableStateOf(false) }
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Top
        ) {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        color = if (isSystemInDarkTheme()) DarkText else LightText
                    )
                },
                actions = {
                    IconButton(
                        onClick = { showInfoDialog = true }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = "Back icon",
                            tint = if (isSystemInDarkTheme()) DarkText else LightText
                        )
                    }
                },
                backgroundColor = if (isSystemInDarkTheme()) LightYellow else DarkGrey
            )
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MultiLanguage()
        }
    }
    if (showInfoDialog) {
        val uriHandler = LocalUriHandler.current

        Dialog(
            onDismissRequest = { showInfoDialog = false }
        ) {
            Card(
                modifier = Modifier
                    .wrapContentHeight()
                    .height(470.dp)
                    .width(450.dp),
                shape = RoundedCornerShape(size = 8.dp),
                backgroundColor = MaterialTheme.colors.background
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    IconButton(
                        onClick = { showInfoDialog = false },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Close dialog"
                        )
                    }
                    Card(
                        modifier = Modifier
                            .height(400.dp)
                            .width(450.dp)
                            .padding(start = 15.dp, end = 15.dp, bottom = 15.dp),
                        shape = RoundedCornerShape(8.dp),
                        backgroundColor = MaterialTheme.colors.onBackground,
                        elevation = 10.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = stringResource(id = R.string.app_name),
                                color = if (isSystemInDarkTheme()) LightText else DarkText,
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Image(
                                painter = painterResource(id = R.drawable.logo),
                                contentDescription = "Logo",
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .border(
                                        width = 1.dp,
                                        color = if (isSystemInDarkTheme()) LightText else DarkText,
                                        shape = CircleShape
                                    )
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            Divider(
                                color = if (isSystemInDarkTheme()) LightText else DarkText,
                                thickness = 2.dp
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "#1 of 20 Android app ideas",
                                color = if (isSystemInDarkTheme()) LightText else DarkText,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "This app show a counter",
                                color = if (isSystemInDarkTheme()) LightText else DarkText,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Divider(
                                color = if (isSystemInDarkTheme()) LightText else DarkText,
                                thickness = 2.dp
                            )
                            Spacer(modifier = Modifier.height(15.dp))
                            Text(
                                text = "My GitHub",
                                color = if (isSystemInDarkTheme()) LightText else DarkText,
                                fontWeight = FontWeight.Bold,
                                fontSize = 30.sp
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                IconButton(
                                    onClick = { uriHandler.openUri("https://github.com/ndenicolais") },
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.github_logo),
                                        contentDescription = "Open Github",
                                        colorFilter = ColorFilter.tint(if (isSystemInDarkTheme()) LightText else DarkText)
                                    )
                                }
                            }
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, bottom = 10.dp),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Developed by DeNicks21",
                        color = if (isSystemInDarkTheme()) DarkText else LightText,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MultiLanguage(
    viewModel: LanguageViewModel = viewModel(
        factory = DataStoreViewModelFactory(DataStoreRepository(LocalContext.current))
    ),
) {
    val scope = rememberCoroutineScope()
    val currentLanguage = viewModel.language.observeAsState().value

    SetLanguage(position = currentLanguage!!)
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState(0))
            .fillMaxSize()
    ) {
        LanguagePicker(currentLanguage) { selected ->
            scope.launch {
                viewModel.saveLanguage(selected)
            }
        }
    }
}

@Composable
fun LanguagePicker(
    selectedPosition: Int,
    onLanguageSelected: (Int) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.language_selector),
            color = if (isSystemInDarkTheme()) LightYellow else DarkGrey,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp),
            fontSize = 30.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(50.dp))
        ToggleGroup(
            selectedPosition = selectedPosition,
            onClick = onLanguageSelected
        )
        Spacer(modifier = Modifier.height(50.dp))
        Text(
            text = stringResource(id = R.string.content),
            fontSize = 24.sp,
            color = if (isSystemInDarkTheme()) LightYellow else DarkGrey,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SetLanguage(position: Int) {
    val locale = Locale(
        when (position) {
            0 -> "en"
            1 -> "it"
            2 -> "es"
            3 -> "de"
            4 -> "fr"
            else -> ""
        }
    )
    val configuration = LocalConfiguration.current
    configuration.setLocale(locale)
    val resources = LocalContext.current.resources
    resources.updateConfiguration(configuration, resources.displayMetrics)
}

@Composable
private fun ToggleGroup(
    selectedPosition: Int,
    onClick: (Int) -> Unit,
) {
    Column(
        modifier = Modifier
            .background(
                color = if (isSystemInDarkTheme()) LightYellow else DarkGrey,
            )
            .fillMaxHeight()
            .width(250.dp)
            .clip(RoundedCornerShape(4.dp))
            .border(
                1.dp,
                if (isSystemInDarkTheme()) LightYellow else DarkGrey,
                RoundedCornerShape(4.dp)
            )
    ) {
        language.forEachIndexed { index, element ->
            if (index != selectedPosition) {
                Text(
                    text = element,
                    color = if (isSystemInDarkTheme()) LightYellow else DarkGrey,
                    modifier = Modifier
                        .background(if (isSystemInDarkTheme()) DarkGrey else LightYellow)
                        .fillMaxSize()
                        .clickable(onClick = { onClick(index) }),
                    fontSize = 22.sp,
                    textAlign = TextAlign.Center,
                )
            } else {
                Text(
                    text = element,
                    color = if (isSystemInDarkTheme()) DarkGrey else LightYellow,
                    modifier = Modifier
                        .background(if (isSystemInDarkTheme()) LightYellow else DarkGrey)
                        .fillMaxSize()
                        .clickable(onClick = { onClick(index) }),
                    fontSize = 22.sp,
                    textAlign = TextAlign.Center,
                )
            }
            Divider(
                color = if (isSystemInDarkTheme()) LightYellow else DarkGrey,
                thickness = 1.dp
            )
        }
    }
}

class DataStoreViewModelFactory(private val dataStoreRepository: DataStoreRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LanguageViewModel::class.java)) {
            return LanguageViewModel(dataStoreRepository) as T
        }
        throw IllegalStateException()
    }
}