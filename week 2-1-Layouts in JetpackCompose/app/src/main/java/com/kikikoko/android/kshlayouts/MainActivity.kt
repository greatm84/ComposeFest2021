package com.kikikoko.android.kshlayouts

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.kikikoko.android.kshlayouts.ui.theme.KshLayoutsTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KshLayoutsTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    ImageList()
                }
            }
        }
        isPossible()
    }
}

@Preview
@Composable
fun MyOwnColumnTest() {
    MyOwnColumn(modifier = Modifier.padding(8.dp)) {
        Text("MyOwnColmn")
        Text("MyOwnColmn2")
        Text("MyOwnColmn3")
        Text("MyOwnColmn4")
    }
}

fun kotlinTest(
    testfunc: (String) -> String
) {
    Log.e("KSH_TEST","${testfunc.invoke("??")}")
}

fun testFunc(value:String):String{
    return value
}

fun isPossible(){
    kotlinTest {
        testFunc("1")
        testFunc("2")
        testFunc("3")
    }
}

@Composable
fun MyOwnColumn(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->

        val placeables = measurables.map { measurable ->
            measurable.measure(constraints)
        }

        var yPosition = 0

        layout(constraints.maxWidth, constraints.maxHeight) {
            // place children
            placeables.forEach { placeable ->
                placeable.placeRelative(x = 0, y = yPosition)

                yPosition += placeable.height
            }
        }
    }
}

fun Modifier.firstBaselineToTop(
    firstBaselineToTop: Dp
) = this.then(
    layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)

        check(placeable[FirstBaseline] != AlignmentLine.Unspecified)
        val firstBaseline = placeable[FirstBaseline]

        val placeableY = firstBaselineToTop.roundToPx() - firstBaseline
        val height = placeable.height + placeableY
        layout(placeable.width, height) {
            placeable.placeRelative(0, placeableY)
        }
    }
)

@Preview
@Composable
fun TextWithPaddingToBaselinePreview() {
    KshLayoutsTheme {
        Text("Hi there!\n Something for nothing", Modifier.firstBaselineToTop(32.dp))
    }
}

@Preview
@Composable
fun TextWithNormalPaddingPreview() {
    KshLayoutsTheme {
        Text(
            "Hi there!\n" +
                    " Something for nothing", Modifier.padding(top = 32.dp)
        )
    }
}

@Preview
@Composable
fun ListPreview() {
    KshLayoutsTheme {
        ImageList()
    }
}

@Composable
fun SimpleList() {

    val scrollState = rememberScrollState()

    Column(Modifier.verticalScroll(scrollState)) {
        repeat(100) {
            Text("Item #$it")
        }
    }
}

@Composable
fun ImageList() {
    val listSize = 100
    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Column {
        Row {
            Button(onClick = {
                coroutineScope.launch {
                    scrollState.animateScrollToItem(0)
                }
            }) {
                Text("Scroll to the top")
            }

            Button(onClick = {
                coroutineScope.launch {
                    scrollState.animateScrollToItem(listSize - 1)
                }
            }) {
                Text("Scroll to the end")
            }
        }
        LazyColumn(state = scrollState) {
            items(listSize) {
                ImageListItem(index = it)
            }
        }
    }
}

@Composable
fun ImageListItem(index: Int) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxSize()) {
        Image(
            painter = rememberImagePainter(
                data = "https://developer.android.com/images/brand/Android_Robot.png"
            ),
            contentDescription = "Android logo",
            modifier = Modifier.size(50.dp)
        )
        Spacer(Modifier.width(10.dp))
        Text("Item #$index", style = MaterialTheme.typography.subtitle1)
    }
}

@Composable
fun LayoutCodeLab() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    BodyContent()
                },
                actions = {
                    IconButton(onClick = { Log.e("KSH_TEST", "Icon button click") }) {
                        Icon(Icons.Filled.Favorite, contentDescription = null)
                    }
                    OutlinedButton(onClick = { Log.e("KSH_TEST", "outline button") }) {
                        Text(text = "hello")
                    }
                }
            )
        }
    ) { innerPadding ->
        BodyContent(Modifier.padding(innerPadding))
    }
}

@Composable
fun BodyContent(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(text = "Hi there")
        Text(text = "thanks my friends")
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    KshLayoutsTheme {
        LayoutCodeLab()
    }
}

@Composable
fun PhotographerCard() {
    Column {
        Text("Alfred Sisley", fontWeight = FontWeight.Bold)
        // LocalContentAlpha is defining opacity level of its children
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text("3 minutes ago", style = MaterialTheme.typography.body2)
        }
    }
}

@Preview
@Composable
fun PhotographerCardPreview() {
    KshLayoutsTheme {
        PhotographerCard()
    }
}