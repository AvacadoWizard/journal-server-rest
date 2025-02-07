const express = require('express');

const app = express();
app.use(express.json());

const PORT = process.env.PORT || 3000;

app.listen(PORT, () => {
  console.log("Server Listening on PORT:", PORT);
});

app.get("/timeline", (request, response)=>
{
    const status = {
        "Status": "Running"
    }
    response.send(status);
});

app.get("/follows", (request, response) => {
    const userId = request.query.userId;
    if (userId) {
        const status = {
            "Follow List": [`hawk 1 for ${userId}`, `hawk 2 for ${userId}`]
        };
        response.send(status);
    } else {
        response.status(400).send({ error: "UserId query parameter is required" });
    }
});
