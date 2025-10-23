const express = require('express');
const bodyParser = require('body-parser');
const cors = require('cors');
const authRoutes = require('./routes/auth');
const readingRoutes = require('./routes/reading');

const app = express();
app.use(cors());
app.use(bodyParser.json());

app.use('/auth', authRoutes);
app.use('/reading', readingRoutes);

const PORT = process.env.PORT || 4000;
app.listen(PORT, () => console.log(`Auth backend listening on ${PORT}`));
