package io.rienel.android.animation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.rienel.android.animation.ui.theme.AndroidAnimationTheme

class MainActivity : ComponentActivity() {

	private val _vm: MainActivityViewModel by viewModels()

	@OptIn(ExperimentalMaterial3Api::class)
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			AndroidAnimationTheme {
				// A surface container using the 'background' color from the theme
				Scaffold(
					modifier = Modifier.fillMaxSize(),
					containerColor = MaterialTheme.colorScheme.background,
					topBar = {
						CenterAlignedTopAppBar(
							title = { Text("Animation Test") },
							colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
								containerColor = MaterialTheme.colorScheme.primary,
								titleContentColor = MaterialTheme.colorScheme.onPrimary
							)
						)
					}
				) {
					Column(
						modifier = Modifier
							.padding(it)
							.padding(10.dp)
							.fillMaxSize()
					) {
						val uiState by _vm.uiState.collectAsState()

						val alpha: Float by animateFloatAsState(
							if (uiState.enabled) 1f else 0.1f,
							label = "",
							finishedListener = {
								Log.i("TAG", "Finished animation")
							}
						)
						Row(
							modifier = Modifier
								.fillMaxWidth(),
							horizontalArrangement = Arrangement.spacedBy(
								10.dp,
								Alignment.CenterHorizontally
							),
						) {
							WordCard(
								modifier = Modifier
									.graphicsLayer(alpha = alpha),
								word = uiState.word
							) {
								_vm.setSelected(Selected.LEFT)
							}
							WordCard(
								modifier = Modifier
									.graphicsLayer(alpha = alpha),
								word = uiState.translation
							) {
								_vm.setSelected(Selected.RIGHT)
							}
						}
					}
				}
			}
		}
	}
}

typealias Action = () -> Unit

@Preview
@Composable
fun WordCard(
	modifier: Modifier = Modifier,
	word: String = "Test",
	onClick: Action? = null
) {
	var modifierCopy = modifier
	onClick?.let {
		modifierCopy = modifier.clickable(onClick = it)
	}
	Box(
		modifier = modifierCopy
			.border(
				width = 0.4.dp,
				color = Color.Black,
				shape = RoundedCornerShape(10.dp)
			)
			.widthIn(max = 200.dp)
	) {
		Text(
			modifier = Modifier
				.padding(
					horizontal = 30.dp,
					vertical = 15.dp
				)
				.align(Alignment.Center),
			textAlign = TextAlign.Center,
			text = word,
		)
	}
}