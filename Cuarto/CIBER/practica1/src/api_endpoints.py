import requests

BO = "localhost:3000"


def get_drones():
    url = f"http://{BO}/dr"
    headers = {
        "Content-Type": "application/json"
    }
    return requests.get(url=url, headers=headers)


def dr_register(name: str, password: str, public_key: str):
    url = f"http://{BO}/dr/register"
    headers = {
        "Content-Type": "application/json"
    }
    args = {
        "name": name,
        "password": password,
        "public_key": public_key
    }

    return requests.post(url=url, headers=headers, json=args)


def dr_login(name: str, password: str):
    url = f"http://{BO}/dr/login"
    headers = {
        "Content-Type": "application/json"
    }
    args = {
        "name": name,
        "password": password,
    }

    return requests.post(url=url, headers=headers, json=args)


def dr_link_et(dr_name: str, et_name: str):
    url = f"http://{BO}/dr/{dr_name}/link-to/et/{et_name}"
    headers = {
        "Content-Type": "application/json"
    }

    return requests.get(url=url, headers=headers)


def dr_links(dr_name: str):

    url = f"http://{BO}/dr/{dr_name}/linked-ets"
    headers = {
        "Content-Type": "application/json"
    }

    return requests.get(url=url, headers=headers)


def dr_unlink_et(dr_name: str, et_name: str):
    url = f"http://{BO}/dr/{dr_name}/unlink-to/et/{et_name}"
    headers = {
        "Content-Type": "application/json"
    }

    return requests.patch(url=url, headers=headers)


def dr_del_et_connection(dr_name: str, et_name: str):
    url = f"http://{BO}/dr/{dr_name}/disconnect/{et_name}"
    headers = {
        "Content-Type": "application/json"
    }

    return requests.delete(url=url, headers=headers)


def dr_update_status(name: str, fly_status: str, battery_porcentage: str):
    url = f"http://{BO}/dr/{name}/save/status"
    headers = {
        "Content-Type": "application/json"
    }
    args = {
        "fly_status": fly_status,
        "battery_porcentage": battery_porcentage,
    }

    return requests.patch(url=url, headers=headers, json=args)

###### LLAMADAS AL DISCOVERY SERVER PARA ETS ##########


def get_ets():
    url = f"http://{BO}/et"
    headers = {
        "Content-Type": "application/json"
    }
    return requests.get(url=url, headers=headers)


def get_et(et_name: str):
    url = f"http://{BO}/et/{et_name}/info"
    headers = {
        "Content-Type": "application/json"
    }
    return requests.get(url=url, headers=headers)


def et_register(name: str, password: str, public_key: str):
    url = f"http://{BO}/et/register"
    headers = {
        "Content-Type": "application/json"
    }
    args = {
        "name": name,
        "password": password,
        "public_key": public_key
    }

    return requests.post(url=url, headers=headers, json=args)


def et_login(name: str, password: str):
    url = f"http://{BO}/et/login"
    headers = {
        "Content-Type": "application/json"
    }
    args = {
        "name": name,
        "password": password,
    }

    return requests.post(url=url, headers=headers, json=args)


def et_link_bo(et_id: str):
    url = f"http://{BO}/et/{et_id}/link-to/bo"
    headers = {
        "Content-Type": "application/json"
    }

    return requests.patch(url=url, headers=headers)


def bo_unlink_et(et_name: str):
    url = f"http://{BO}/et/{et_name}/unlink"
    headers = {
        "Content-Type": "application/json"
    }

    return requests.patch(url=url, headers=headers)


def et_del_dr_connection(et_id: str, dr_id: str):
    url = f"http://{BO}/et/{et_id}/disconnect/{dr_id}"
    headers = {
        "Content-Type": "application/json"
    }

    return requests.delete(url=url, headers=headers)

###### LLAMADAS AL DISCOVERY SERVER PARA LA BO ##########


def bo_register(name: str, listen_port: int, ip: str, public_key: str):
    url = f"http://{BO}/bo/register"
    headers = {
        "Content-Type": "application/json"
    }
    args = {
        "name": name,
        "listen_port": listen_port,
        "ip": ip,
        "public_key": public_key
    }

    return requests.post(url=url, headers=headers, json=args)


def bo_add_connection(et_name: str, dr_name: str):
    url = f"http://{BO}/bo/{et_name}/connect/{dr_name}"
    headers = {
        "Content-Type": "application/json"
    }

    return requests.post(url=url, headers=headers)


def bo_del_connection(et_name: str, dr_name: str):
    url = f"http://{BO}/bo/{et_name}/disconnect/{dr_name}"
    headers = {
        "Content-Type": "application/json"
    }

    return requests.delete(url=url, headers=headers)


def get_drone(dr_name: str):
    url = f"http://{BO}/dr/{dr_name}/info"
    headers = {
        "Content-Type": "application/json"
    }
    return requests.get(url=url, headers=headers)


def bo_get_dr_connection(dr_id: str):
    url = f"http://{BO}/dr/{dr_id}/connection"
    headers = {
        "Content-Type": "application/json"
    }
    return requests.get(url=url, headers=headers)


def bo_get_connected_drs():
    url = f"http://{BO}/dr/connected"
    headers = {
        "Content-Type": "application/json"
    }

    return requests.get(url=url, headers=headers)


def bo_get_nonconnected_drs():
    url = f"http://{BO}/dr/non-connected"
    headers = {
        "Content-Type": "application/json"
    }

    return requests.get(url=url, headers=headers)


def get_public_key():
    url = f"http://{BO}/bo/public-key"

    headers = {
        "Content-Type": "application/json"
    }

    return requests.get(url=url, headers=headers)


def get_bo_info():
    url = f"http://{BO}/bo/info"

    headers = {
        "Content-Type": "application/json"
    }

    return requests.get(url=url, headers=headers)


def get_port():
    url = f"http://{BO}/bo/port"

    headers = {
        "Content-Type": "application/json"
    }

    return requests.get(url=url, headers=headers)


def regenerate_port():
    url = f"http://{BO}/bo/regenerate/port"

    headers = {
        "Content-Type": "application/json"
    }

    return requests.patch(url=url, headers=headers)


def delete_connections():
    url = f"http://{BO}/bo/delete/connections"

    headers = {
        "Content-Type": "application/json"
    }

    return requests.delete(url=url, headers=headers)
