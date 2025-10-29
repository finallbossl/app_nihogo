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

// GET /reading/articles - Lấy danh sách bài đọc
router.get('/articles', verifyToken, async (req, res) => {
  try {
    await db.read()
    const articles = db.data.readingArticles || []
    
    // Trả về danh sách với thông tin cơ bản
    const articleList = articles.map(article => ({
      id: article.id,
      title: article.title,
      level: article.level,
      category: article.category,
      estimatedTime: article.estimatedTime,
      difficulty: article.difficulty,
      thumbnail: article.thumbnail,
      isCompleted: false // TODO: Check user progress
    }))
    
    res.json(articleList)
  } catch (error) {
    res.status(500).json({ error: 'Failed to fetch articles' })
  }
})

// GET /reading/articles/:id - Lấy chi tiết bài đọc + câu hỏi
router.get('/articles/:id', verifyToken, async (req, res) => {
  try {
    await db.read()
    const articleId = req.params.id
    const article = db.data.readingArticles?.find(a => a.id === articleId)
    
    if (!article) {
      return res.status(404).json({ error: 'Article not found' })
    }
    
    // Trả về chi tiết bài đọc với câu hỏi
    res.json({
      id: article.id,
      title: article.title,
      content: article.content,
      level: article.level,
      category: article.category,
      estimatedTime: article.estimatedTime,
      difficulty: article.difficulty,
      questions: article.questions || []
    })
  } catch (error) {
    res.status(500).json({ error: 'Failed to fetch article details' })
  }
})

// POST /reading/results - Submit kết quả làm bài
router.post('/results', verifyToken, async (req, res) => {
  try {
    const { articleId, answers, timeSpent, score } = req.body
    
    if (!articleId || !answers) {
      return res.status(400).json({ error: 'articleId and answers are required' })
    }
    
    await db.read()
    
    // Tạo kết quả mới
    const result = {
      id: nanoid(),
      articleId,
      userId: req.userId || 'anonymous', // Sẽ lấy từ JWT token
      answers,
      timeSpent: timeSpent || 0,
      score: score || 0,
      completedAt: new Date().toISOString(),
      createdAt: new Date().toISOString()
    }
    
    // Lưu kết quả
    if (!db.data.readingResults) {
      db.data.readingResults = []
    }
    db.data.readingResults.push(result)
    
    await db.write()
    
    // Trả về kết quả đã lưu
    res.status(201).json({
      id: result.id,
      articleId: result.articleId,
      score: result.score,
      timeSpent: result.timeSpent,
      completedAt: result.completedAt
    })
  } catch (error) {
    res.status(500).json({ error: 'Failed to save reading result' })
  }
})

// GET /reading/results/:userId - Lấy lịch sử kết quả của user (optional)
router.get('/results/:userId', verifyToken, async (req, res) => {
  try {
    await db.read()
    const userId = req.params.userId
    const results = db.data.readingResults?.filter(r => r.userId === userId) || []
    
    res.json(results)
  } catch (error) {
    res.status(500).json({ error: 'Failed to fetch user results' })
  }
})

module.exports = router