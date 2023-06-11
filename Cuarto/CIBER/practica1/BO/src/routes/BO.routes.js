import { Router } from "express";
import { BO } from "../models/BO.js"
import { ET } from "../models/ET.js"
import { DR } from "../models/DR.js"
import { PORT } from "../models/PORT.js"
import { Connections } from "../models/Connections.js"
import { getNextPort } from "../getNextPort.js"


const router = Router()

router.get("/connections", async (req, res) => {

    await Connections.find({})
        .then((bo) => res.json(bo))
        .catch(err => res.status(400).json({ "error": err }))
})


router.post("/:et_name/connect/:dr_name", async (req, res) => {

    const et = await ET.findOne({ name: req.params.et_name })
    const dr = await DR.findOne({ name: req.params.dr_name })

    const newConn = Connections({
        connection_et: et,
        connection_dr: dr,
    })

    await newConn.save()
        .then((conn) => res.json(conn))
        .catch(err => res.status(400).json({ "error": err }))

})

router.delete("/:et_name/disconnect/:dr_name", async (req, res) => {

    const et = await ET.findOne({ name: req.params.et_name })
    const dr = await DR.findOne({ name: req.params.dr_name })

    const delConn = Connections.findOne({
        connection_et: et,
        connection_dr: dr,
    })

    console.log(delConn)

    await delConn.remove()
        .then((conn) => res.json(conn))
        .catch(err => res.status(400).json({ "error": err }))

})

router.post("/register", async (req, res) => {

    const newBO = new BO({
        name: req.body.name,
        listen_port: await getNextPort(),
        ip: "localhost",
        public_key: req.body.public_key
    })

    await newBO.save()
        .then((bo) => res.json(bo))
        .catch(err => res.status(400).json({ "error": err }))
})

router.get("/public-key", async (req, res) => {

    await BO.find({})
        .then(bo => res.json(bo[0].public_key))
        .catch(err => res.status(400).json({ "error": err }))

})

router.get("/info", async (req, res) => {

    await BO.find({})
        .then(bo => res.json(bo[0]))
        .catch(err => res.status(400).json({ "error": err }))

})

router.get("/port", async (req, res) => {

    await getNextPort()
        .then(port => res.json(port))
        .catch(err => res.status(400).json({ "error": err }))

})

router.patch("/regenerate/port", async (req, res) => {

    const newPort = await getNextPort()

    await BO.updateOne({ name: "bo" }, { listen_port: newPort })

    res.json(newPort)

})

router.delete("/delete/connections", async (req, res) => {

    await Connections.deleteMany({})
    res.json("")

})

export default router
