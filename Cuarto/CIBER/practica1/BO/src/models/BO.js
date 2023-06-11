import mongoose from "mongoose"

const Schema = mongoose.Schema

const BOSchema = new Schema({
    name: {
        type: String,
        unique: true,
        require: true
    },
    listen_port: {
        type: Number,
        unique: true,
        require: true
    },
    ip: {
        type: String,
        require: true
    },
    public_key: {
        type: String,
        unique: true,
        require: true
    }
})

export const BO = mongoose.model('BO', BOSchema)