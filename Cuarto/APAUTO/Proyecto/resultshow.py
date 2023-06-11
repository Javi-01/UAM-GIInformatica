
from typing import Iterable
import numpy as np
from sys import argv
import matplotlib.pyplot as plt


def main(muestra: int, atributos: Iterable[int]):
    # De la 0 a la 99
    # R1 R2 R3 R4 R5 R6 R7 R8 Temp. Humidity (0 - 9)
    R = 'r'

    datafile = 'result.dat'
    default_class = 'background'
    colors = ['b', 'g', 'r', 'y', 'k']
    colors += colors

    atributes = set(atributos)

    # Obtener datos de la muestra
    data = []
    clases = []
    with open(datafile, R) as df:
        muestra = str(muestra)
        line = df.readline()  # id R1 R2 R3 R4 R5 R6 R7 R8 Temp. Humidity Class
        atributeNames = line.split()[1:]
        line = df.readline()
        while len(line) > 1:
            if line.startswith(muestra):
                break
            del line
            line = df.readline()

        while line.startswith(muestra):
            spl = line.split()
            data.append([float(e) for e in spl[1:-1]])
            clases.append(spl[-1])
            del line
            del spl
            line = df.readline()

    dataArr = np.array(data)
    del data

    fi = 0             # int((9 / 24) * len(dataArr))
    li = len(dataArr)  # int((10 / 24) * len(dataArr))
    data = dataArr[fi:li]  # R1 R2 R3 R4 R5 R6 R7 R8 Temp. Humidity Class
    clases = clases[fi:li]

    start_idx = -1
    for i, c in enumerate(clases):
        if c != default_class:
            start_idx = i
            break

    end_idx = -1
    for i, c in enumerate(clases[::-1]):
        if c != default_class:
            end_idx = len(data) - i
            break

    print(data, len(data))

    plt.title(f'{muestra}')
    ymin, ymax = float('inf'), -float('inf')
    # atributes = [a + 1 for a in atributes]
    times = list(range(len(data)))
    for atr, col in zip(atributes, colors):
        plt.plot(
            times,
            data[:, atr],
            label=atributeNames[atr],
            color=col
            )

        ymin = min(ymin, np.min(data[:, atr]))
        ymax = max(ymax, np.max(data[:, atr]))

    ymin *= 1.1
    ymax *= 1.1
    if start_idx >= 0:
        plt.vlines(start_idx, ymin, ymax, colors='m')
    if end_idx >= 0:
        plt.vlines(end_idx, ymin, ymax, colors='m')

    plt.legend()
    plt.show()


if __name__ == '__main__':
    muestra = 1 if len(argv) <= 1 else int(argv[1])
    if len(argv) > 2:
        atributos = set([int(a) for a in argv[2:] if int(a) <= 9])
    else:
        atributos = {0, 1, 2}

    main(muestra, atributos)
