import mongoose from "mongoose"

const Schema = mongoose.Schema

const ETSchema = new Schema({
    name: {
        type: String,
        unique: true,
        require: true
    },
    password: {
        type: String,
        require: true
    },
    ip: {
        type: String,
        require: true
    },
    listen_port: {
        type: Number,
        unique: true,
        require: true
    },
    public_key: {
        type: String,
        unique: true,
        require: true
    }
})

export const ET = mongoose.model('ET', ETSchema)