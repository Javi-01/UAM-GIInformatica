
import gc

R = 'r'
W = 'w'

datafile = 'HT_Sensor_dataset.dat'
metafile = 'HT_Sensor_metadata.dat'
resultfile = 'result.dat'
default_class = 'background'

# Eliminar o no la parte previa y posterior a la
# zona de medicion de cada muestra
REMOVE_PRE = True
REMOVE_POST = True

# Anadir o no los atributos originales ademas de los calculados
ADD_RAW_ATRS = True

# Tamano de la ventana de elementos previos
WINDOW_SIZE = 10
# Hacer media de los elementos de la ventana o usar solo su primer elemento
DO_MEAN = False


"""
Metadata
"""
metalist = []
clases = set()
with open(metafile, R) as mf:

    line = mf.readline()  # id	date		class	t0	dt
    line = mf.readline()
    while len(line) > 1:
        elems = line.split()
        elems[-2] = float(elems[-2])
        elems[-1] = float(elems[-1])

        metalist.append(tuple(elems[1:]))
        clases.add(elems[2])

        del line
        line = mf.readline()

gc.collect()
clases = sorted(clases)

print(*metalist[:4], sep='\n')
print(*clases, sep='\n')

"""
Main data
"""
prevlist = []
count = {'wine': 0, 'banana': 0, 'background': 0}

with open(datafile, R) as df:
    with open(resultfile, W) as rf:
        atrs = [f'R{i}' for i in range(1, 9)] + ['Temp.', 'Humidity']
        head = ['id'] + atrs + (
            [a + '_raw' for a in atrs] if ADD_RAW_ATRS else []) + ['Class']
        rf.write('\t'.join(head) + '\n')

        line = df.readline()  # id time R1 R2 R3 R4 R5 R6 R7 R8 Temp. Humidity
        line = df.readline()
        while len(line) > 1:
            data = [float(e) for e in line.split()]
            del line
            metainfo = metalist[int(data[0])]
            clas = metainfo[1]
            dt = metainfo[3]

            if (REMOVE_POST and data[1] > dt) or (REMOVE_PRE and data[1] < 0):
                # No tenemos en cuenta la parte post-medicion
                # Ni pre-medicion, dependiendo de la configuracion
                line = df.readline()
                continue

            """
            Modificable
            """
            if len(prevlist):
                if prevlist[0][0] != data[0]:  # Cambio de muestra
                    print(data[0], clas)
                    while len(prevlist):  # Vaciar la lista
                        e = prevlist.pop()
                        del e

            if len(prevlist) >= WINDOW_SIZE:
                referencia = prevlist[0]  # Elemento mas antiguo de la ventana
                tref = referencia[1]
                # Tiempo desde el principio de la ventana
                tiempoVentana = data[1] - referencia[1]

                # Incluimos las diferencias entre todas las:
                # Rx, Temp. y Humidity
                # Los atributos se dividen entre el tiempo transcurrido
                # para obtener algo parecido a la inclinacion si se
                # tratara de una recta

                if DO_MEAN:
                    medias = [
                        sum([e[i] for e in prevlist]) / len(prevlist)
                        for i in range(2, len(data))
                    ]
                    procesedData = [
                        round((atr - med) / (tiempoVentana / 2), 7)
                        for atr, med in zip(data[2:], medias)
                    ]
                else:
                    procesedData = [
                        round((a - p) / tiempoVentana, 7)
                        for a, p in zip(data[2:], referencia[2:])
                    ]

                prevlist.pop(0)
                del referencia

                # Si la referencia es posterior a 0 y
                # el dato es inferior al limite
                clas = clas if (tref >= 0 and data[1] <= dt) else default_class
                newAtrs = procesedData + (data[2:] if ADD_RAW_ATRS else [])
                newRow = [int(data[0]), *newAtrs, clas]

                newreg = '\t'.join([str(d) for d in newRow]) + '\n'
                del newRow

                count[clas] += 1
                rf.write(newreg)
                del newreg
            else:
                prevlist.append(data)

            line = df.readline()

print('\nRecuento final:')
for k in count:
    print(k, count[k])
