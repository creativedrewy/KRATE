package com.creativedrewy.junglegym

import com.moriatsushi.koject.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@Provides
class MainViewModel(
    private val repository: PlatformRepository,
    @Dispatcher(Dispatchers.Main)
    private val dispatcher: CoroutineDispatcher
) {
    private val job = SupervisorJob()

    private val coroutineScope: CoroutineScope
        get() = CoroutineScope(job + dispatcher)

    private val _userString: MutableStateFlow<String> = MutableStateFlow("This is some text")

    val list: StateFlow<String> = _userString.asStateFlow()

    fun doSomething() {
        coroutineScope.launch {
            _userString.update {
                "This came from ViewModel: ${repository.getPlatformString()}"
            }
        }
    }
}