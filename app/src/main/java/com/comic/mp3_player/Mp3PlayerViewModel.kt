import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class Mp3PlayerViewModel : ViewModel() {

    var isPlaying = mutableStateOf(false)
    private var mediaPlayer: MediaPlayer? = null

    fun setUri(context: Context, uri: Uri) {
        mediaPlayer?.release() // Giải phóng MediaPlayer hiện tại nếu có
        mediaPlayer = MediaPlayer.create(context, uri)
    }

    fun play() {
        mediaPlayer?.start()
        isPlaying.value = true
    }

    fun pause() {
        mediaPlayer?.pause()
        isPlaying.value = false
    }

    fun stop() {
        mediaPlayer?.stop()
        isPlaying.value = false
    }

    override fun onCleared() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
