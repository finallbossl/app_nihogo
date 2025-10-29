const express = require('express');
const router = express.Router();
const { Low, JSONFile } = require('lowdb')
const { nanoid } = require('nanoid')
const path = require('path')

const dbFile = path.join(__dirname, '..', 'db.json')
const adapter = new JSONFile(dbFile)
const db = new Low(adapter)

// Middleware để verify JWT (optional - có thể bỏ qua nếu không cần auth)
function verifyToken(req, res, next) {
  // Simple implementation - in production, verify JWT properly
  next() // Skip auth for now
}

// GET /listening/exercises - Lấy danh sách bài nghe
router.get('/exercises', verifyToken, async (req, res) => {
  try {
    await db.read()
    const exercises = db.data.listeningExercises || []
    
    // Trả về danh sách với thông tin cơ bản
    const exerciseList = exercises.map(exercise => ({
      id: exercise.id,
      title: exercise.title,
      level: exercise.level,
      category: exercise.category,
      duration: exercise.duration,
      difficulty: exercise.difficulty,
      thumbnail: exercise.thumbnail,
      description: exercise.description,
      isCompleted: false // TODO: Check user progress
    }))
    
    res.json(exerciseList)
  } catch (error) {
    res.status(500).json({ error: 'Failed to fetch listening exercises' })
  }
})

// GET /listening/exercises/:id - Lấy chi tiết bài nghe + transcript + câu hỏi
router.get('/exercises/:id', verifyToken, async (req, res) => {
  try {
    await db.read()
    const exerciseId = req.params.id
    const exercise = db.data.listeningExercises?.find(e => e.id === exerciseId)
    
    if (!exercise) {
      return res.status(404).json({ error: 'Listening exercise not found' })
    }
    
    // Trả về chi tiết bài nghe với transcript và câu hỏi
    res.json({
      id: exercise.id,
      title: exercise.title,
      audioUrl: exercise.audioUrl,
      transcript: exercise.transcript,
      level: exercise.level,
      category: exercise.category,
      duration: exercise.duration,
      difficulty: exercise.difficulty,
      description: exercise.description,
      instructions: exercise.instructions,
      questions: exercise.questions || []
    })
  } catch (error) {
    res.status(500).json({ error: 'Failed to fetch listening exercise details' })
  }
})

// POST /listening/results - Submit kết quả làm bài nghe
router.post('/results', verifyToken, async (req, res) => {
  try {
    const { exerciseId, answers, timeSpent, score, listeningCount } = req.body
    
    if (!exerciseId || !answers) {
      return res.status(400).json({ error: 'exerciseId and answers are required' })
    }
    
    await db.read()
    
    // Tạo kết quả mới
    const result = {
      id: nanoid(),
      exerciseId,
      userId: req.userId || 'anonymous', // Sẽ lấy từ JWT token
      answers,
      timeSpent: timeSpent || 0,
      score: score || 0,
      listeningCount: listeningCount || 1, // Số lần nghe lại
      completedAt: new Date().toISOString(),
      createdAt: new Date().toISOString()
    }
    
    // Lưu kết quả
    if (!db.data.listeningResults) {
      db.data.listeningResults = []
    }
    db.data.listeningResults.push(result)
    
    await db.write()
    
    // Trả về kết quả đã lưu
    res.status(201).json({
      id: result.id,
      exerciseId: result.exerciseId,
      score: result.score,
      timeSpent: result.timeSpent,
      listeningCount: result.listeningCount,
      completedAt: result.completedAt
    })
  } catch (error) {
    res.status(500).json({ error: 'Failed to save listening result' })
  }
})

// GET /listening/results/:userId - Lấy lịch sử kết quả listening của user
router.get('/results/:userId', verifyToken, async (req, res) => {
  try {
    await db.read()
    const userId = req.params.userId
    const results = db.data.listeningResults?.filter(r => r.userId === userId) || []
    
    // Trả về kết quả với thông tin bài nghe
    const detailedResults = results.map(result => {
      const exercise = db.data.listeningExercises?.find(e => e.id === result.exerciseId)
      return {
        ...result,
        exerciseTitle: exercise?.title || 'Unknown Exercise'
      }
    })
    
    res.json(detailedResults)
  } catch (error) {
    res.status(500).json({ error: 'Failed to fetch user listening results' })
  }
})

// GET /listening/stats/:userId - Thống kê listening của user (bonus)
router.get('/stats/:userId', verifyToken, async (req, res) => {
  try {
    await db.read()
    const userId = req.params.userId
    const results = db.data.listeningResults?.filter(r => r.userId === userId) || []
    
    const stats = {
      totalExercises: results.length,
      averageScore: results.length > 0 ? 
        results.reduce((sum, r) => sum + r.score, 0) / results.length : 0,
      totalTimeSpent: results.reduce((sum, r) => sum + r.timeSpent, 0),
      completedToday: results.filter(r => {
        const today = new Date().toDateString()
        return new Date(r.completedAt).toDateString() === today
      }).length
    }
    
    res.json(stats)
  } catch (error) {
    res.status(500).json({ error: 'Failed to fetch listening stats' })
  }
})

module.exports = router