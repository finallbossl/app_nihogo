const express = require('express');
const router = express.Router();
const { Low, JSONFile } = require('lowdb')
const { nanoid } = require('nanoid')
const bcrypt = require('bcryptjs')
const jwt = require('jsonwebtoken')
const path = require('path')

const dbFile = path.join(__dirname, '..', 'db.json')
const adapter = new JSONFile(dbFile)
const db = new Low(adapter)

const ACCESS_SECRET = process.env.ACCESS_SECRET || 'access-secret-example'
const REFRESH_SECRET = process.env.REFRESH_SECRET || 'refresh-secret-example'
const ACCESS_EXPIRES_IN = 60 * 15 // 15 minutes
const REFRESH_EXPIRES_IN = 60 * 60 * 24 * 7 // 7 days

async function initDb() {
  await db.read()
  db.data = db.data || { users: [], refreshTokens: [] }
  await db.write()
}

initDb()

router.post('/register', async (req, res) => {
  const { email, password, displayName } = req.body
  if (!email || !password) return res.status(400).json({ error: 'Email and password required' })
  await db.read()
  const exists = db.data.users.find(u => u.email === email)
  if (exists) return res.status(409).json({ error: 'User exists' })
  const hashed = await bcrypt.hash(password, 10)
  const user = { id: nanoid(), email, password: hashed, displayName: displayName || '' }
  db.data.users.push(user)
  await db.write()
  const accessToken = jwt.sign({ sub: user.id, email: user.email }, ACCESS_SECRET, { expiresIn: ACCESS_EXPIRES_IN })
  const refreshToken = jwt.sign({ sub: user.id }, REFRESH_SECRET, { expiresIn: REFRESH_EXPIRES_IN })
  db.data.refreshTokens.push({ token: refreshToken, userId: user.id })
  await db.write()
  res.json({ accessToken, refreshToken, expiresIn: ACCESS_EXPIRES_IN, userId: user.id, email: user.email })
})

router.post('/login', async (req, res) => {
  const { email, password } = req.body
  if (!email || !password) return res.status(400).json({ error: 'Email and password required' })
  await db.read()
  const user = db.data.users.find(u => u.email === email)
  if (!user) return res.status(401).json({ error: 'Invalid credentials' })
  const ok = await bcrypt.compare(password, user.password)
  if (!ok) return res.status(401).json({ error: 'Invalid credentials' })
  const accessToken = jwt.sign({ sub: user.id, email: user.email }, ACCESS_SECRET, { expiresIn: ACCESS_EXPIRES_IN })
  const refreshToken = jwt.sign({ sub: user.id }, REFRESH_SECRET, { expiresIn: REFRESH_EXPIRES_IN })
  db.data.refreshTokens.push({ token: refreshToken, userId: user.id })
  await db.write()
  res.json({ accessToken, refreshToken, expiresIn: ACCESS_EXPIRES_IN, userId: user.id, email: user.email })
})

router.post('/refresh', async (req, res) => {
  const { refreshToken } = req.body
  if (!refreshToken) return res.status(400).json({ error: 'refreshToken required' })
  await db.read()
  const entry = db.data.refreshTokens.find(r => r.token === refreshToken)
  if (!entry) return res.status(401).json({ error: 'Invalid refresh token' })
  try {
    const payload = jwt.verify(refreshToken, REFRESH_SECRET)
    const userId = payload.sub
    const user = db.data.users.find(u => u.id === userId)
    if (!user) return res.status(401).json({ error: 'Invalid token user' })
    const accessToken = jwt.sign({ sub: user.id, email: user.email }, ACCESS_SECRET, { expiresIn: ACCESS_EXPIRES_IN })
    const newRefreshToken = jwt.sign({ sub: user.id }, REFRESH_SECRET, { expiresIn: REFRESH_EXPIRES_IN })
    // replace old
    db.data.refreshTokens = db.data.refreshTokens.filter(r => r.token !== refreshToken)
    db.data.refreshTokens.push({ token: newRefreshToken, userId: user.id })
    await db.write()
    res.json({ accessToken, refreshToken: newRefreshToken, expiresIn: ACCESS_EXPIRES_IN, userId: user.id, email: user.email })
  } catch (e) {
    return res.status(401).json({ error: 'Invalid refresh token' })
  }
})

router.post('/logout', async (req, res) => {
  const { refreshToken } = req.body
  if (!refreshToken) return res.status(400).json({ error: 'refreshToken required' })
  await db.read()
  db.data.refreshTokens = db.data.refreshTokens.filter(r => r.token !== refreshToken)
  await db.write()
  res.status(200).json({})
})

module.exports = router
