import { Router } from "express";
import { ET } from "../models/ET.js"
import { DR } from "../models/DR.js"
import { BO } from "../models/BO.js"
import { Connections } from "../models/Connections.js"
import { getNextPort } from "../getNextPort.js"


const router = Router()
// Rutas de la ET

router.get("/", async (_, res) => {

    await ET.find({})
        .then(ets => res.json(ets))
        .catch(err => res.status(400).json({ "error": err }))
})

router.get("/:et_id/check-linked-dr/:dr_id", async (req, res) => {

    await DR.findOne({ _id: req.params.dr_id }, { linked_ets: 1, _id: 0 })
        .then(l_ets => {
            if (l_ets.linked_ets.indexOf(req.params.et_id) == -1) {
                res.status(404).json({ "error": "El dron no esta linkeado a tu ET" })
            } else {
                res.json({ "ok": "El dron si esta linkeado a tu ET" })
            }
        })
        .catch(err => res.status(400).json({ "error": err }))

})


router.post("/register", async (req, res) => {

    const newET = new ET({
        name: req.body.name,
        password: req.body.password,
        listen_port: await getNextPort(),
        ip: "localhost",
        public_key: req.body.public_key
    })

    await newET.save()
        .then((et) => res.json(et))
        .catch(err => res.status(400).json({ "error": err }))
})

router.post("/login", async (req, res) => {

    await ET.findOne({
        name: req.body.name,
        password: req.body.password
    })
        .then(et => {
            if (et == null) {
                res.status(404).json({ "error": "ET no encontrado" })
            } else {
                res.json(et)
            }
        })
        .catch(err => res.status(400).json({ "error": err }))

})

router.patch("/:et_id/link-to/bo", async (req, res) => {

    const bo = await BO.findOne({})

    await BO.updateOne({ _id: bo._id }, { $push: { linked_ets: req.params.et_id } })
        .then(b => res.json(b))
        .catch(err => res.status(400).json({ "error": err }))

})

router.patch("/:et_name/unlink", async (req, res) => {

    const et = await ET.findOne({ name: req.params.et_name })

    await DR.updateMany(
        { linked_ets: { $in: et._id } },
        { $pull: { linked_ets: et._id } }
    )

    await ET.deleteOne({ name: req.params.et_name })
        .then(res => res.json(res))
        .catch(err => res.json(err))
})


router.get("/:et_name/info", async (req, res) => {

    await ET.findOne({ name: req.params.et_name })
        .then(et => res.json(et))
        .catch(err => res.status(400).json({ "error": err }))

})

router.get("/:et_name/public-key", async (req, res) => {

    await ET.find({ name: req.params.et_name })
        .then(et => res.json(et[0].public_key))
        .catch(err => res.status(400).json({ "error": err }))

})

export default router
