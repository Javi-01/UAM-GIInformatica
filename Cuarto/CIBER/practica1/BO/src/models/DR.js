import mongoose from "mongoose"

const Schema = mongoose.Schema

const DRSchema = new Schema({
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
    linked_ets: [{
        type: Schema.Types.ObjectId,
        ref: 'ET',
    }],
    public_key: {
        type: String,
        unique: true,
        require: true
    },
    fly_status: {
        type: String,
        require: true
    },
    battery_porcentage: {
        type: String,
        require: true
    }
})

export const DR = mongoose.model('DR', DRSchema)