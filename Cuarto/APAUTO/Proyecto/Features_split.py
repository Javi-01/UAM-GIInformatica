#####
#
# Modificacion del original
# https://github.com/thmosqueiro/ENose-Decorr_Humdt_Temp
# Para generar un nuevo dataset legible (newresult.dat)
#
#
# In the paper, we have used inductions of July 23rd. Looking at the
# metadata, the id of the presentations are 17 (banana) and 19 (wine).
#
# ####

# # Importing libraries
import numpy as np
import pylab as pl


# # Should create plots of the inductions after subsampling?
createPlot = False

# # Importing metadata and induction information
metadata = np.loadtxt('HT_Sensor_metadata.dat', skiprows=1, dtype=str)
metadata[metadata[:, 2] == "wine", 2] = 2
metadata[metadata[:, 2] == "banana", 2] = 1
metadata[metadata[:, 2] == "background", 2] = 0
metadata = np.array(metadata[:, [0, 2, 3, 4]], dtype=float)

# # Loading the dataset
dataset = np.loadtxt('HT_Sensor_dataset.dat', skiprows=1)


# # Useful definitions

deltaT = 10
window = deltaT*10


def numWindows(tfmin, deltaT):
    """ Evaluates the number of windows that will be used
    given the total time (tot) of a particular induction.
    """
    return int((tfmin - deltaT) + 2)


# # Array to store features
#result = []
#hashtable = []


# # Splitting
newDataset = []
for ind in np.array(metadata[:, 0], dtype=int):

    print(">> Processing induction id %d" % ind)

    # Separating a single induction
    dataset_ = dataset[dataset[:, 0] == ind, 1:]      # obtener todos los atributos (menos id) de filas con id=ind
    dataset_[:, 0] = dataset_[:, 0]*60                # atributo tiempo a minutos
    mins = np.unique(np.array(dataset_[:, 0], dtype=int))  # evitamos filas con la timestamp repetida

    tfmin = metadata[ind, 3]*60  # dt a minutos
    print(" Duration of the induction: %4.2f" % tfmin)

    # Minutes during induction (there may be minutes missing due to data loss)
    mins_ind = mins[np.logical_and(mins >= 0, mins < int(tfmin))]  # Nos quedamos solo con la parte de la muestra entre poner el olor y quitarlo

    # # Checking if the induction has at least one window
    if tfmin < 9 or mins_ind.shape[0] < 9:
        # Vemos si la muestra tiene más del numero de filas que la ventana
        # que hemos definido
        print(" Induction with lower duration than threshold.")

    else:

        # Getting the total number of windows -> grupos (de filas) de tamano deltaT
        nwin = numWindows(tfmin, deltaT)

        # # Finding missing time stamps

        # Fixed list of minutes
        mins_ind_fixtime = np.copy(mins_ind)

        # Checking if there are missing data at the beggining
        # of the induction
        if (mins_ind[0] > 0):  # this means that 0 was missed
            print(" Missed data at the beggining of induction ")
            t0 = mins_ind[0]
            nwin = nwin - mins_ind[0]
        else:
            t0 = 0

        # Checking for missing time stamps during the induction...
        dmins = np.diff(mins_ind)
        locmissing = np.where(dmins > 1)[0] + 1
        if (locmissing.shape[0] > 0):
            print(" Missed time stamps: %d" % np.sum(dmins[locmissing-1]))

        uplocmiss = []  # updated location of the missing points

        # Adding and indexing missing time stamps
        # Genera filas faltantes con una funcion lineal entre los elementos de las filas previa y posterior
        offset = 0
        for j in range(locmissing.shape[0]):
            values = np.arange(
                0, dmins[locmissing[j]-1]-1
                ) + mins_ind_fixtime[locmissing[j]-1+offset] + 1
            mins_ind_fixtime = np.insert(
                mins_ind_fixtime, locmissing[j]+offset, values)

            offset += dmins[locmissing[j]-1] - 1
            uplocmiss.append(values)

        # Preparing an array for the sub-sampled version of the data
        # Preparamos un array para remuestrear los datos (por ventanas de tiempo de tamano deltaT)
        data_subsamp = np.zeros((mins_ind_fixtime.shape[0], 10))  # 10 es el numero de atributos a remuestrear (R1 - ... - Humidity)

        # Constructing the subsampled array without missing points
        for tmin in mins_ind:
            data_subsamp[int(
                tmin) - t0, :] = dataset_[
                    np.abs(dataset_[:, 0] - tmin) < 0.1, 1:].mean(axis=0)

        # Fixing missing points a posteriori
        # Recalcula los puntos faltantes de nuevo con las ventanas ya definidas
        for missed in uplocmiss:
            x0 = data_subsamp[missed[0] - 1 - t0]
            xf = data_subsamp[missed[-1] + 1 - t0]
            step = 1
            alpha = (xf-x0)/float(len(missed))
            for x in missed:
                data_subsamp[x - t0, :] = x0 + alpha*step
                step + 1

        # # Creating plots with the subsampled version of the inductions
        if createPlot:
            pl.figure()
            for j in range(10):
                pl.plot(data_subsamp[:, j])
            pl.savefig('Visualizing_Ind'+str(ind)+'.png', dpi=250)

        # Reshaping in order to facilitate the creation of the moving windows
        induct = np.reshape(
            data_subsamp, (data_subsamp.shape[0] * data_subsamp.shape[1]))

        # # Constructing the moving window
        shape = (induct.shape[0] - window + 1, window)
        strides = induct.strides + (induct.strides[-1],)
        dataSplit = np.lib.stride_tricks.as_strided(
            induct, shape=shape, strides=strides)[0:-1:10]


        # Anadir la clase
        print('anadir clase', metadata[ind])
        newDataSplit = np.zeros((dataSplit.shape[0], 1)) + metadata[ind][1]  # clase
        newDataSplit = np.hstack((dataSplit, newDataSplit))
        print("clase anadida", newDataSplit.shape, metadata[ind][1])


        print(" Finished, now saving.", dataSplit.shape)

        newDataset.extend([r for r in newDataSplit])
        #result.append(dataSplit)
        #hashtable.append(ind)

        print("")

print("-> Guardando inducciones como nuevo dataset")
print(*[r.shape[0] for r in newDataset if r.shape[0] != 101])
newDataset = np.array(newDataset)
print('Shape del nuevo dataset', newDataset.shape)
newDataset = newDataset[~np.isnan(newDataset).any(axis=1)]
print('Shape tras quitar nan', newDataset.shape)

# Crear nueva cabecera a partir de la original
with open('HT_Sensor_dataset.dat', 'r') as f:
    orig = f.readline()
orig = orig.split()[2:]

head = '\t'.join([
    orig[i % 10] + '_' + str(int(i // 10))
    for i in range(newDataset.shape[1])
][:-1] + ['Class', ])

# Guardar nuevo dataset
np.savetxt('newresult.dat', newDataset, header=head)

#print(">> Saving the final dataset")
#np.save("Dataset_Split10min", result)
#np.save("Dataset_Split10min_hashtable", hashtable)

print("The end, my friend.")
