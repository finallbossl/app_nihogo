package com.example.nihongomaster.ui.listening

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.nihongomaster.R
import com.example.nihongomaster.databinding.FragmentListeningSessionBinding
import com.example.nihongomaster.model.viewmodel.ListeningSessionViewModel
import com.example.nihongomaster.model.ListeningLesson
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ListeningSessionFragment : Fragment() {

    private var _b: FragmentListeningSessionBinding? = null
    private val b get() = _b!!
    private val vm: ListeningSessionViewModel by viewModels()
    private var mediaPlayer: MediaPlayer? = null
    private var isPlaying = false
    private var updateTimer: android.os.Handler? = null
    private var updateRunnable: Runnable? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View {
        _b = FragmentListeningSessionBinding.inflate(inflater, container, false)
        return b.root
    }

    override fun onViewCreated(view: View, s: Bundle?) {
        // Toolbar
        b.topBar.title = getString(R.string.listening)
        b.topBar.setNavigationIcon(
            com.google.android.material.R.drawable.material_ic_keyboard_arrow_left_black_24dp
        )
        b.topBar.setNavigationOnClickListener { findNavController().navigateUp() }
        b.topBar.inflateMenu(R.menu.menu_home_top)

        // Ưu tiên nhận lessonId để mở đúng bài (không còn nút qua bài)
        val lessonId = arguments?.getString("lessonId").orEmpty()
        val categoryId = arguments?.getString("categoryId").orEmpty() // optional/back-compat
        if (lessonId.isNotBlank()) vm.startWithLesson(lessonId) else vm.start(categoryId.ifBlank { "conv" })

        // Hiển thị thời gian mặc định trước
        b.tvTime.text = "0:00 / 3:00"
        
        // Bind data bài hiện tại
        vm.current.observe(viewLifecycleOwner) { lesson ->
            lesson?.let { 
                updateLessonUI(it)
                // Force re-render questions after lesson is loaded
                android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                    renderQuestions()
                }, 500)
            }
        }


        

        
        vm.isLoading.observe(viewLifecycleOwner) { isLoading ->
            // Show loading state in UI if needed
        }
        
        vm.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                android.widget.Toast.makeText(requireContext(), it, android.widget.Toast.LENGTH_SHORT).show()
            }
        }

        // Audio player thực sự
        b.btnPlay.setOnClickListener { 
            val currentLesson = vm.current.value
            if (currentLesson != null) {
                if (isPlaying) {
                    pauseAudio()
                } else {
                    playAudio(currentLesson.audioUrl, currentLesson.title)
                }
            }
        }
        
        // SeekBar listener
        b.seek.setOnSeekBarChangeListener(object : android.widget.SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: android.widget.SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer?.seekTo(progress)
                }
            }
            override fun onStartTrackingTouch(seekBar: android.widget.SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: android.widget.SeekBar?) {}
        })

        // Nộp bài & chấm điểm
        b.btnSubmit.setOnClickListener {
            val (correct, total) = vm.gradeCurrent()
            val percent = if (total == 0) 0 else (correct * 100 / total)
            
            // Debug info
            val debugInfo = vm.debugQuestions()
            android.util.Log.d("ListeningFragment", "Debug info:\n$debugInfo")
            
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Kết quả")
                .setMessage("Số câu đúng: $correct / $total\nĐiểm: $percent%\n\nDebug:\n$debugInfo")
                .setPositiveButton("OK", null)
                .show()
        }
    }
    
    private fun updateLessonUI(lesson: ListeningLesson) {
        b.tvLessonTitle.text = lesson.title
        b.tvLevel.text = "Cấp độ: ${lesson.level}"
        
        val duration = if (lesson.audioDurationSec > 0) lesson.audioDurationSec else 180
        val minutes = duration / 60
        val seconds = duration % 60
        b.tvTime.text = "0:00 / ${minutes}:${seconds.toString().padStart(2, '0')}"
        
        // Lazy load questions only when needed
        renderQuestions()
    }
    
    private fun playAudio(audioUrl: String?, title: String) {
        if (audioUrl.isNullOrEmpty()) {
            android.widget.Toast.makeText(requireContext(), "Không có file âm thanh", android.widget.Toast.LENGTH_SHORT).show()
            return
        }
        

        
        try {
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer().apply {
                setDataSource(audioUrl)
                prepareAsync()
                setOnPreparedListener {
                    start()
                    this@ListeningSessionFragment.isPlaying = true
                    b.btnPlay.setImageResource(android.R.drawable.ic_media_pause)
                    
                    // Cài đặt SeekBar
                    b.seek.max = duration
                    
                    // Bắt đầu cập nhật thời gian
                    startTimeUpdater()
                    
                    android.widget.Toast.makeText(requireContext(), "Phát: $title", android.widget.Toast.LENGTH_SHORT).show()
                }
                setOnCompletionListener {
                    this@ListeningSessionFragment.isPlaying = false
                    b.btnPlay.setImageResource(android.R.drawable.ic_media_play)
                    stopTimeUpdater()
                }
                setOnErrorListener { _, what, extra ->
                    android.util.Log.e("MediaPlayer", "Error: what=$what, extra=$extra")
                    this@ListeningSessionFragment.isPlaying = false
                    b.btnPlay.setImageResource(android.R.drawable.ic_media_play)
                    stopTimeUpdater()
                    
                    val errorMsg = when (what) {
                        android.media.MediaPlayer.MEDIA_ERROR_IO -> "Lỗi I/O - Emulator không hỗ trợ audio"
                        android.media.MediaPlayer.MEDIA_ERROR_MALFORMED -> "File âm thanh không hợp lệ"
                        android.media.MediaPlayer.MEDIA_ERROR_UNSUPPORTED -> "Định dạng âm thanh không được hỗ trợ"
                        else -> "Lỗi phát âm thanh (Thử trên thiết bị thật)"
                    }
                    android.widget.Toast.makeText(requireContext(), errorMsg, android.widget.Toast.LENGTH_LONG).show()
                    true
                }
            }
        } catch (e: Exception) {
            android.widget.Toast.makeText(requireContext(), "Lỗi: ${e.message}", android.widget.Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun pauseAudio() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
                isPlaying = false
                b.btnPlay.setImageResource(android.R.drawable.ic_media_play)
                stopTimeUpdater()
                android.widget.Toast.makeText(requireContext(), "Tạm dừng", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun startTimeUpdater() {
        updateTimer = android.os.Handler(android.os.Looper.getMainLooper())
        updateRunnable = object : Runnable {
            override fun run() {
                mediaPlayer?.let { mp ->
                    if (mp.isPlaying) {
                        val currentPos = mp.currentPosition
                        val duration = mp.duration
                        
                        b.seek.progress = currentPos
                        
                        val currentMin = currentPos / 60000
                        val currentSec = (currentPos % 60000) / 1000
                        val totalMin = duration / 60000
                        val totalSec = (duration % 60000) / 1000
                        
                        b.tvTime.text = "${currentMin}:${currentSec.toString().padStart(2, '0')} / ${totalMin}:${totalSec.toString().padStart(2, '0')}"
                        
                        updateTimer?.postDelayed(this, 1000)
                    }
                }
            }
        }
        updateRunnable?.let { updateTimer?.post(it) }
    }
    
    private fun stopTimeUpdater() {
        updateRunnable?.let { updateTimer?.removeCallbacks(it) }
    }
    
    private fun renderQuestions() {
        val container = b.questionsContainer
        container.removeAllViews()
        
        val currentLesson = vm.current.value ?: return
        val questions = vm.getQuestionsForCurrent()
        
        android.util.Log.d("ListeningFragment", "Rendering questions for lesson: ${currentLesson.id}")
        android.util.Log.d("ListeningFragment", "Questions count: ${questions.size}")
        
        // Add transcript card using optimized layout
        val transcriptCard = layoutInflater.inflate(R.layout.item_transcript_card, container, false)
        val titleText = transcriptCard.findViewById<TextView>(R.id.tvTranscriptTitle)
        val contentText = transcriptCard.findViewById<TextView>(R.id.tvTranscriptContent)
        
        titleText.text = "📝 Nội dung bài nghe"
        contentText.text = buildTranscriptContent(currentLesson)
        container.addView(transcriptCard)
        
        // Add question cards
        questions.forEachIndexed { index, question ->
            android.util.Log.d("ListeningFragment", "Adding question $index: ${question.text}")
            val card = layoutInflater.inflate(R.layout.item_listening_question, container, false)
            setupQuestionCard(card, question)
            container.addView(card)
        }
        
        if (questions.isEmpty()) {
            android.util.Log.w("ListeningFragment", "No questions to display!")
        }
    }
    
    private fun buildTranscriptContent(lesson: ListeningLesson): String {
        return buildString {
            append("Bài nghe: ${lesson.title}\n\n")
            append("Thời lượng: ${lesson.audioDurationSec / 60} phút ${lesson.audioDurationSec % 60} giây\n\n")
            append("Cấp độ: ${lesson.level}\n\n")
            
            if (!lesson.description.isNullOrEmpty()) {
                append("Mô tả: ${lesson.description}\n\n")
            }
            
            if (!lesson.transcript.isNullOrEmpty()) {
                append("Nội dung bài nghe:\n${lesson.transcript}")
            } else {
                append("Nội dung: Đây là bài luyện nghe tiếng Nhật giúp bạn cải thiện kỹ năng nghe hiểu.")
            }
        }
    }
    
    private fun setupQuestionCard(card: View, question: com.example.nihongomaster.model.ListeningQuestion) {
        val tvQuestion = card.findViewById<TextView>(R.id.tvQuestion)
        val chip = card.findViewById<com.google.android.material.chip.Chip>(R.id.chipDifficulty)
        
        tvQuestion.text = question.text
        chip.text = question.difficulty
        
        val optionCards = listOf(
            card.findViewById<MaterialCardView>(R.id.optA),
            card.findViewById<MaterialCardView>(R.id.optB),
            card.findViewById<MaterialCardView>(R.id.optC),
            card.findViewById<MaterialCardView>(R.id.optD)
        )
        
        val optionTexts = listOf(
            card.findViewById<TextView>(R.id.tvOptA),
            card.findViewById<TextView>(R.id.tvOptB),
            card.findViewById<TextView>(R.id.tvOptC),
            card.findViewById<TextView>(R.id.tvOptD)
        )
        
        // Setup options efficiently
        optionCards.forEachIndexed { index, optCard ->
            val optText = optionTexts[index]
            val text = question.options.getOrNull(index)
            
            if (text.isNullOrBlank()) {
                optCard.visibility = View.GONE
            } else {
                optCard.visibility = View.VISIBLE
                optText.text = text
                optCard.setOnClickListener {
                    selectOption(optionCards, optCard, question, index)
                }
            }
        }
    }
    
    private fun selectOption(allCards: List<MaterialCardView>, selectedCard: MaterialCardView, question: com.example.nihongomaster.model.ListeningQuestion, index: Int) {
        // Reset all cards efficiently
        allCards.forEach { card ->
            card.strokeWidth = resources.getDimensionPixelSize(R.dimen.card_stroke_normal)
            card.strokeColor = resources.getColor(R.color.card_stroke_normal, null)
        }
        
        // Highlight selected card
        selectedCard.strokeWidth = resources.getDimensionPixelSize(R.dimen.card_stroke_selected)
        selectedCard.strokeColor = resources.getColor(R.color.brandPrimary, null)
        
        vm.selectOption(question, index)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stopTimeUpdater()
        mediaPlayer?.release()
        mediaPlayer = null
        updateTimer = null
        updateRunnable = null
        _b = null
    }
}
