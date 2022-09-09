#!/bin/bash
export J2EE_HOME=/opt/glassfish4/glassfish

for i in P2/P1-base P2/P1-ejb-servidor-remoto P2/P1-ejb-cliente-remoto; do
	cd $i
	ant replegar; ant delete-pool-local
	cd -
done

cd P2/P1-ws
ant replegar-servicio; ant replegar-cliente
cd -

cd P2/P1-base
ant delete-db
cd -

for i in P2/P1-base P2/P1-ejb-servidor-remoto P2/P1-ejb-cliente-remoto P2/P1-ws; do
	cd $i
	ant limpiar-todo todo
	cd -
done