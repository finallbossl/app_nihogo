package com.example.nihongomaster.model.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nihongomaster.R
import com.example.nihongomaster.data.remote.ApiClient
import com.example.nihongomaster.model.HomeUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _items = MutableLiveData<List<HomeUiModel>>()
    val items: LiveData<List<HomeUiModel>> = _items
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    init {
        loadHomeData()
    }

    private fun loadHomeData() {
        android.util.Log.d("HomeViewModel", "loadHomeData called")
        
        // Hiển thị dữ liệu tĩnh ngay lập tức
        val staticItems = buildStaticItems()
        android.util.Log.d("HomeViewModel", "Built ${staticItems.size} static items")
        _items.value = staticItems
        
        // Tải dữ liệu từ API trong background
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val homeItems = buildHomeItems()
                _items.postValue(homeItems)
            } catch (e: Exception) {
                // Giữ nguyên dữ liệu tĩnh nếu API lỗi
                android.util.Log.w("HomeViewModel", "API failed: ${e.message}")
            }
        }
    }
    
    private suspend fun buildHomeItems(): List<HomeUiModel> {
        val items = mutableListOf<HomeUiModel>()
        
        // Welcome section
        items.add(HomeUiModel.Welcome(
            title = "Chào mừng đến Nihongo Master!",
            subtitle = "Hành trình học tiếng Nhật của bạn bắt đầu từ đây.",
            imageRes = R.drawable.img_welcome
        ))
        
        items.add(HomeUiModel.SectionHeader("🎌 Bắt đầu học tiếng Nhật"))
        
        // Get data counts from API
        try {
            val vocabResponse = ApiClient.apiService.getVocabulary()
            val vocabCount = if (vocabResponse.isSuccessful) vocabResponse.body()?.size ?: 0 else 0
            
            val readingResponse = ApiClient.apiService.getReading()
            val readingCount = if (readingResponse.isSuccessful) readingResponse.body()?.size ?: 0 else 0
            
            val listeningResponse = ApiClient.apiService.getListening()
            val listeningCount = if (listeningResponse.isSuccessful) listeningResponse.body()?.size ?: 0 else 0
            
            val exerciseResponse = ApiClient.apiService.getExercises()
            val exerciseCount = if (exerciseResponse.isSuccessful) exerciseResponse.body()?.size ?: 0 else 0
            
            items.add(HomeUiModel.FeatureCard("vocab", R.drawable.ic_bulb, "📚 Từ vựng ($vocabCount từ)", "Học từ vựng tiếng Nhật"))
            items.add(HomeUiModel.FeatureCard("reading", R.drawable.ic_read, "📖 Đọc hiểu ($readingCount bài)", "Luyện đọc hiểu tiếng Nhật"))
            items.add(HomeUiModel.FeatureCard("listening", R.drawable.ic_headphones, "🎧 Nghe ($listeningCount bài)", "Luyện nghe tiếng Nhật"))
            items.add(HomeUiModel.FeatureCard("tests", R.drawable.ic_checklist, "🏆 Kiểm tra ($exerciseCount bài)", "Kiểm tra kiến thức"))
            
        } catch (e: Exception) {
            // Fallback to static cards
            items.addAll(buildStaticCards())
        }
        
        items.add(HomeUiModel.SectionHeader("📈 Tiến độ học tập"))
        items.add(HomeUiModel.FeatureCard("progress", R.drawable.ic_chart, "📈 Bảng điều khiển", "Xem tiến độ học tập"))
        
        return items
    }
    
    private fun buildStaticItems(): List<HomeUiModel> = listOf(
        HomeUiModel.Welcome(
            title = "Chào mừng đến Nihongo Master!",
            subtitle = "Hành trình học tiếng Nhật của bạn bắt đầu từ đây.",
            imageRes = R.drawable.img_welcome
        ),
        HomeUiModel.SectionHeader("🎌 Bắt đầu học tiếng Nhật"),
        *buildStaticCards().toTypedArray(),
        HomeUiModel.SectionHeader("📈 Tiến độ học tập"),
        HomeUiModel.FeatureCard("progress", R.drawable.ic_chart, "📈 Bảng điều khiển", "Xem tiến độ học tập")
    )
    
    private fun buildStaticCards(): List<HomeUiModel.FeatureCard> = listOf(
        HomeUiModel.FeatureCard("vocab", R.drawable.ic_bulb, "📚 Từ vựng", "Học từ vựng tiếng Nhật"),
        HomeUiModel.FeatureCard("reading", R.drawable.ic_read, "📖 Đọc hiểu", "Luyện đọc hiểu tiếng Nhật"),
        HomeUiModel.FeatureCard("listening", R.drawable.ic_headphones, "🎧 Nghe", "Luyện nghe tiếng Nhật"),
        HomeUiModel.FeatureCard("tests", R.drawable.ic_checklist, "🏆 Kiểm tra", "Kiểm tra kiến thức")
    )
}