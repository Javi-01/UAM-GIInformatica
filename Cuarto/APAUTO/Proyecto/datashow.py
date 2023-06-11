
from typing import Iterable
import numpy as np
from sys import argv
import matplotlib.pyplot as plt


def main(muestra: int, atributos: Iterable[int]):
    # De la 0 a la 99
    # R1 R2 R3 R4 R5 R6 R7 R8 Temp. Humidity  (0 - 9)
    R = 'r'

    datafile = 'HT_Sensor_dataset.dat'
    metafile = 'HT_Sensor_metadata.dat'

    atributes = set(atributos)

    # Obtener clase de la muestra
    with open(metafile, R) as mf:
        muestra = str(muestra)
        line = mf.readline()  # id	date		class	t0	dt
        line = mf.readline()
        while len(line) > 1:
            if line.startswith(muestra):
                break
            line = mf.readline()

    metad = line.split()
    clase = metad[2]
    t0 = float(metad[3])
    dt = float(metad[4])

    # Obtener datos de la muestra
    data = []
    with open(datafile, R) as df:
        line = df.readline()  # id time R1 R2 R3 R4 R5 R6 R7 R8 Temp. Humidity
        atributeNames = line.split()
        line = df.readline()
        while len(line) > 1:
            if line.startswith(muestra):
                break
            del line
            line = df.readline()

        while line.startswith(muestra):
            data.append([float(e) for e in line.split()])
            del line
            line = df.readline()

    dataArr = np.array(data)
    del data
    data = dataArr

    print(data, len(data))

    plt.title(f'{muestra} {clase} {t0} {dt}')
    ymin, ymax = float('inf'), -float('inf')
    atributes = [a + 2 for a in atributes]
    for atr in atributes:
        plt.plot(data[:, 1], data[:, atr], label=atributeNames[atr])
        ymin = min(ymin, np.min(data[:, atr]))
        ymax = max(ymax, np.max(data[:, atr]))

    ymin *= 1.1
    ymax *= 1.1
    plt.vlines(0, ymin, ymax, colors='m')
    plt.vlines(dt, ymin, ymax, colors='m')

    plt.legend()
    plt.show()


if __name__ == '__main__':
    muestra = 1 if len(argv) <= 1 else int(argv[1])
    if len(argv) > 2:
        atributos = set([int(a) for a in argv[2:] if int(a) <= 9])
    else:
        atributos = {0, 1, 2}

    main(muestra, atributos)
