from src import frames as f

DS_SERVER = "vega.ii.uam.es"
DS_PORT = "8000"

# Se crea una instancia de la Interfaz Grafica y se inicia el frame de login
if __name__ == "__main__":
    frame = f.Frame("750x550", DS_SERVER, DS_PORT)
    frame.frame_start()
