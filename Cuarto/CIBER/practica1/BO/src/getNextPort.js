import { PORT } from "./models/PORT.js"

export const getNextPort = async () => {
	const port = await PORT.find({})

	if (port.length == 0) {
		const newPORT = new PORT({
			listen_port: 8000
		})

		await newPORT.save()

		return 8000
	} else {
		await PORT.updateOne({ _id: port[0]._id }, { $set: { listen_port: (port[0].listen_port + 1) } })
	}

	return port[0].listen_port + 1
}