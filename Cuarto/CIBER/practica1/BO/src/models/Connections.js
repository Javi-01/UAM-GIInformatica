import mongoose from "mongoose"

const Schema = mongoose.Schema

const ConnSchema = new Schema({
    connection_et: {
        type: Schema.Types.ObjectId,
        ref: 'ET',
        require: true
    },
    connection_dr: {
        type: Schema.Types.ObjectId,
        ref: 'DR',
        unique: true,
        require: true
    },
})

export const Connections = mongoose.model('Connections', ConnSchema)