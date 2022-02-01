'''practica1.py
    Muestra el tiempo de llegada de los primeros 50 paquetes a la interfaz especificada
    como argumento y los vuelca a traza nueva con tiempo actual

    Autor: Javier Ramos <javier.ramos@uam.es>
    2020 EPS-UAM
'''

from rc1_pcap import *
import sys
import binascii
import signal
import argparse
from argparse import RawTextHelpFormatter
import time
import logging

ETH_FRAME_MAX = 1514
PROMISC = 1
NO_PROMISC = 0
TO_MS = 10
num_paquete = 0
n_bytes = 0
TIME_OFFSET = 30*60

def signal_handler(nsignal,frame):
	logging.info('Control C pulsado')
	if handle:
		pcap_breakloop(handle)


def procesa_paquete(us,header,data):
	global num_paquete, pdumper
	logging.info('Nuevo paquete de {} bytes capturado en el timestamp UNIX {}.{}'.format(header.len,header.ts.tv_sec,header.ts.tv_usec))
	
	num_paquete += 1

	#TODO imprimir los N primeros bytes
	#En caso de que el tamaño del paquete sea menor al numero de bytes que queremos por paquete,se imprime el paquete entero 
	if header.len > n_bytes:
		for i in range(0, n_bytes):
	        	print('{:02x}'.format(data[i]).upper(), end=' ')
		print("\n")
	else:
		for i in range(0, header.len):
	        	print('{:02x}'.format(data[i]).upper(), end=' ')
		print("\n")

	#Actualizar los campos del paquete de la interfaz
	header.ts.tv_sec += TIME_OFFSET
	header.captlen = n_bytes

	#Escribir el tráfico al fichero de captura con el offset temporal
	if pdumper is not None:
		pcap_dump(pdumper, header, data)
	

if __name__ == "__main__":
	global pdumper,args,handle
	
	parser = argparse.ArgumentParser(description='Captura tráfico de una interfaz ( o lee de fichero) y muestra la longitud y timestamp de los 50 primeros paquetes',
	formatter_class=RawTextHelpFormatter)
	parser.add_argument('--file', dest='tracefile', default=False,help='Fichero pcap a abrir')
	parser.add_argument('--itf', dest='interface', default=False,help='Interfaz a abrir')
	parser.add_argument('--nbytes', dest='nbytes', type=int, default=14,help='Número de bytes a mostrar por paquete')
	parser.add_argument('--debug', dest='debug', default=False, action='store_true',help='Activar Debug messages')
	args = parser.parse_args()

	if args.debug:
		logging.basicConfig(level = logging.DEBUG, format = '[%(asctime)s %(levelname)s]\t%(message)s')
	else:
		logging.basicConfig(level = logging.INFO, format = '[%(asctime)s %(levelname)s]\t%(message)s')

	#Si se ejecuta sin argumentos muestra el manual de ayuda
	if not args:
		parser.print_help()
		sys.exit(-1)
		
	#Control de errores ningun archivo especificado
	if args.tracefile is False and args.interface is False:
		logging.error('No se ha especificado interfaz ni fichero')
		parser.print_help()
		sys.exit(-1)

	#Control de errores donde se intenta abrir una traza e interfaz
	if args.tracefile is True and args.interface is True:
		logging.error('No puedes abrir una traza y una interfaz')
		parser.print_help()
		sys.exit(-1)

	if args.nbytes > ETH_FRAME_MAX:
		logging.error('El tamaño que desea leer excede el limite del paquete')
		parser.print_help()
		sys.exit(-1)
	else: 
		n_bytes = args.nbytes

	signal.signal(signal.SIGINT, signal_handler)

	errbuf = bytearray()
	handle = None
	pdumper = None
	
	#TODO abrir la interfaz especificada para captura o la traza
	if args.interface:
		handle = pcap_open_live(args.interface, ETH_FRAME_MAX, PROMISC, TO_MS, errbuf)
		if handle == None:
			logging.error('Error al abrir la interfaz especificada')
	
	if args.tracefile:
		handle = pcap_open_offline(args.tracefile, errbuf)
		if handle == None:
			logging.error('Error al abrir el fichero de traza especificado')
			

	#TODO abrir un dumper para volcar el tráfico (si se ha especificado interfaz) 
	if args.interface:
		descr = pcap_open_dead(DLT_EN10MB, ETH_FRAME_MAX)
		pdumper = pcap_dump_open(descr, 'captura.{}.{}.pcap'.format(args.interface, time.time()))

	
	ret = pcap_loop(handle, -1, procesa_paquete, None)
	if ret == -1:
		logging.error('Error al capturar un paquete')
	elif ret == -2:
		logging.debug('pcap_breakloop() llamado')
	elif ret == 0:
		logging.debug('No mas paquetes o limite superado')
	logging.info('{} paquetes procesados'.format(num_paquete))
	
	#TODO si se ha creado un dumper cerrarlo
	if pdumper is not None:
			pcap_close(descr)
