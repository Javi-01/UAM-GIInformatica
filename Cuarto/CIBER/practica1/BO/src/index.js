import express from "express"
import mongoose from "mongoose"

import DRRoutes from "./routes/DR.routes.js"
import ETRoutes from "./routes/ET.routes.js"
import BORoutes from "./routes/BO.routes.js"

const boServer = express()
const PORT = 3000

boServer.use(express.json())

boServer.use("/dr/", DRRoutes)
boServer.use("/et/", ETRoutes)
boServer.use("/bo/", BORoutes)

const url = "mongodb://localhost:27017/bo"

mongoose.connect(url,
    {
        useNewUrlParser: true,
        useUnifiedTopology: true,
    }
)
const conn = mongoose.connection

conn.once('open', () => {
    console.log("Connection to MongoDB opened")
})
boServer.listen(PORT, () => {
    console.log("boServer server running on PORT: " + PORT)
})
