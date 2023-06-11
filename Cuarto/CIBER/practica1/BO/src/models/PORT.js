import mongoose from "mongoose"

const Schema = mongoose.Schema

const BOSchema = new Schema({
    listen_port: {
        type: Number,
        unique: true,
        require: true
    }
})

export const PORT = mongoose.model('PORT', BOSchema)