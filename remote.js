const express = require('express'); // importing the express library
const sqlite3 = require('sqlite3').verbose(); // sqlite import
const app = express(); // initialize express 
const port = 3000; // port 3000 is what i used for connections

// express is the library that I used allowing me to connect to the database
app.use(express.json());

// connects to the sql database with all the posts
const db = new sqlite3.Database('./journal.db', (err) => {
  if (err) {
    console.error(err.message);
  }
  console.log('database connection established.');
});

db.run(`
    CREATE TABLE IF NOT EXISTS entries (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        title TEXT NOT NULL,
        date_published TEXT NOT NULL,
        entry_text TEXT NOT NULL,
        username TEXT NOT NULL,
        is_public INTEGER NOT NULL DEFAULT 1
    );
`);

// responds with all of the entries which are public
app.get('/entries', (req, res) => {
    db.all(`SELECT * FROM entries WHERE is_public = 1`, (err, rows) => {
        if (err) {
            console.error(err.message);
            res.status(500).send('Error retrieving entries');
        } else {
            res.json(rows);
        }
    });
});

// responds with all of the entries that the user has created
app.get('/entries/:username', (req, res) => {
    const username = req.params.username;
    db.all(`SELECT * FROM entries WHERE username = ?`, [username], (err, rows) => {
        if (err) {
            console.error(err.message);
            res.status(500).send('Error retrieving entries');
        } else {
            res.json(rows);
        }
    });
});

// 
app.get('/entries/:username/:title', (req, res) => {
    const username = req.params.username;
    const title = req.params.title.replace(/%20/g, ' ');
    db.get(`SELECT * FROM entries WHERE username = ? AND title = ?`, [username, title], (err, row) => {
        if (err) {
            console.error(err.message);
            res.status(500).send('Error retrieving entry');
        } else {
            if (row) {
                res.status(200).send('Entry exists');
            } else {
                res.status(404).send('Entry does not exist');
            }
        }
    });
});
  
// creates an entry with all the params from the gui
app.post('/entries', (req, res) => {
const { title, datePublished, entryText, username, isPublic } = req.body;
db.run(`
    INSERT INTO entries (title, date_published, entry_text, username, is_public)
    VALUES (?, ?, ?, ?, ?)
`, [title, datePublished, entryText, username, isPublic], (err) => {
    if (err) {
        console.error(err.message);
        res.status(500).send('Internal Server Error');
    } else {
        res.status(200).send('Entry created successfully');
    }
});
});

// update the entries with new body text, title is not editable
app.put('/entries/:username/:title', (req, res) => {
    const username = req.params.username;
    const title = req.params.title.replace(/%20/g, ' ');
    const entryText = req.body.entryText;
    db.run(`
        UPDATE entries
        SET entry_text = ?
        WHERE username = ? AND title = ?
    `, [entryText, username, title], (err) => {
        if (err) {
            console.error(err.message);
            res.status(500).send('Error updating entry');
        } else {
            res.status(200).send('Entry updated successfully');
        }
    });
});

app.listen(port,"0.0.0.0", () => {
  console.log(`Server listening on port ${port}`);
});