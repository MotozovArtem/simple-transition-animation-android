package io.rienel.android.animation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivityViewModel : ViewModel() {

	private var selectedLeft = false
	private var selectedRight = false
	private val _words = listOf(
		"Word",
		"Translation",
		"Hello",
		"Bye",
		"Diablo",
		"Zoo",
		"Candle",
		"Belgrade",
		"Moscow",
		"Wife"
	)

	private val _uiState = MutableStateFlow(
		State(
			enabled = true,
			word = _words.random(Random(System.currentTimeMillis())),
			translation = _words.random(Random(System.currentTimeMillis() + Random.nextInt()))
		)
	)

	val uiState = _uiState.asStateFlow()

	private fun startWordChanging() {
		viewModelScope.launch {
			delay(500L)
			selectedRight = false
			selectedLeft = false
			_uiState.update {
				it.copy(
					enabled = true,
					word = _words.random(Random(System.currentTimeMillis())),
					translation = _words.random(Random(System.currentTimeMillis() + Random.nextInt()))
				)
			}
		}
	}

	fun setSelected(selected: Selected) {
		when(selected) {
			Selected.LEFT -> selectedLeft = true
			Selected.RIGHT -> selectedRight = true
		}
		val allSelected = selectedLeft && selectedRight
		_uiState.update {
			it.copy(
				enabled = !allSelected
			)
		}
		if (allSelected)
			startWordChanging()
	}

}

data class State(
	val enabled: Boolean,
	val word: String,
	val translation: String,
)

enum class Selected {
	LEFT,
	RIGHT
}