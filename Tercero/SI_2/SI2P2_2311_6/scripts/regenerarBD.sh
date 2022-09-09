#!/bin/bash
export J2EE_HOME=/opt/glassfish4/glassfish

cd P2/P1-base
ant unsetup-db; ant setup-db