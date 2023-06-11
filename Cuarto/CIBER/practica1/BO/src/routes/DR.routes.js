import { Router } from "express";
import { DR } from "../models/DR.js"
import { ET } from "../models/ET.js"
import { Connections } from "../models/Connections.js"
import { getNextPort } from "../getNextPort.js"


const router = Router()
// Rutas de la DR

router.get("/", async (_, res) => {

    await DR.find({})
        .then(drones => res.json(drones))
        .catch(err => res.status(400).json({ "error": err }))
})


router.post("/register", async (req, res) => {

    const newDrone = new DR({
        name: req.body.name,
        password: req.body.password,
        listen_port: await getNextPort(),
        ip: "localhost",
        public_key: req.body.public_key,
        fly_status: "False",
        battery_porcentage: "100.0"
    })

    await newDrone.save()
        .then((drone) => res.json(drone))
        .catch(err => res.status(400).json({ "error": err }))
})

router.post("/login", async (req, res) => {

    await DR.findOne({
        name: req.body.name,
        password: req.body.password
    })
        .then(dr => {
            if (dr == null) {
                res.status(404).json({ "error": "DR no encontrado" })
            } else {
                res.json(dr)
            }
        })
        .catch(err => res.status(400).json({ "error": err }))

})

router.get("/:dr_name/linked-ets", async (req, res) => {

    const ets = await DR.findOne({ name: req.params.dr_name }, { linked_ets: 1 })

    await ET.find({ _id: { $in: ets.linked_ets } })
        .then(l_ets => res.json(l_ets))
        .catch(err => res.status(400).json({ "error": err }))
})


router.get("/:dr_name/link-to/et/:et_name", async (req, res) => {

    const et = await ET.findOne({ name: req.params.et_name })

    await DR.updateOne({ name: req.params.dr_name }, { $push: { linked_ets: et.id } })
        .then(dr => res.json(dr))
        .catch(err => res.status(400).json({ "error": err }))

})

router.patch("/:dr_name/unlink-to/et/:et_name", async (req, res) => {

    const et = await ET.findOne({ name: req.params.et_name })

    await DR.updateOne({ name: req.params.dr_name }, { $pull: { linked_ets: et.id } })
        .then(dr => res.json(dr))
        .catch(err => res.status(400).json({ "error": err }))

})

router.delete("/:dr_id/disconnect/:et_id", async (req, res) => {

    const et = await ET.findOne({ _id: req.params.et_id })
    const dr = await DR.findOne({ _id: req.params.dr_id })

    const delConn = Connections.findOne({
        connection_et: et,
        connection_dr: dr,
    })

    await delConn.remove()
        .then((conn) => res.json(conn))
        .catch(err => res.status(400).json({ "error": err }))

})

router.get("/:dr_name/info", async (req, res) => {

    await DR.findOne({ name: req.params.dr_name })
        .then(dr => {
            if (dr == null) {
                res.status(404).json({ "error": "DR no encontrado" })
            } else {
                res.json(dr)
            }
        })
        .catch(err => res.status(400).json({ "error": err }))
})

router.get("/:dr_id/connection", async (req, res) => {

    const connection = await Connections.findOne({ connection_dr: req.params.dr_id })

    if (connection == null) {
        res.status(404).json({ "error": "DR no encontrado" })
    } else {
        await ET.findOne({ _id: connection.connection_et })
            .then(et => {
                if (et == null) {
                    res.status(404).json({ "error": "DR no encontrado" })
                } else {
                    res.json(et)
                }
            })
            .catch(err => res.status(400).json({ "error": err }))
    }
})

router.get("/linked", async (req, res) => {

    await DR.find({ linked_ets: { $ne: [] } })
        .then(drs => {
            if (drs.length == 0) {
                res.status(400).json({ "error": "No hay drs linkeados a ninguna et" })
            } else {
                res.json(drs)
            }
        })
        .catch(err => res.status(400).json({ "error": err }))

})

router.get("/connected", async (req, res) => {

    const connections = await Connections.find({})
    const connectionDrs = connections.map(conn => conn.connection_dr);

    await DR.find({ _id: { $in: connectionDrs } })
        .then(drs => res.json(drs))
        .catch(err => res.status(400).json({ "error": err }))

})

router.get("/non-connected", async (req, res) => {

    const connections = await Connections.find({})
    const connectionDrs = connections.map(conn => conn.connection_dr);

    await DR.find({
        _id: { $nin: connectionDrs },
        linked_ets: { $exists: true, $not: { $size: 0 } }
    })
        .then(drs => res.json(drs))
        .catch(err => res.status(400).json({ "error": err }))

})

router.get("/:dr_name/public-key", async (req, res) => {

    await DR.find({ name: req.params.dr_name })
        .then(dr => res.json(dr[0].public_key))
        .catch(err => res.status(400).json({ "error": err }))

})

router.patch("/:dr_name/save/status", async (req, res) => {

    await DR.updateOne(
        { name: req.params.dr_name },
        {
            $set: {
                fly_status: req.body.fly_status,
                battery_porcentage: req.body.battery_porcentage
            }
        }).then(dr => res.json(dr))
        .catch(err => res.status(400).json({ "error": err }))

})

export default router
