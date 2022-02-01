package controlador.zonasAforo;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import vista.panelAdministrador.pestanaZonasYAforo.PestanaZonasAforoCenter;
import vista.panelAdministrador.pestanaZonasYAforo.PestanaZonasAforoWest;

/**
* Clase encargada de fitrar las opciones a realizar por las zonas para ello, se selecciona 
* mediante un combobox la opcion seleccionada
* 
* @author Jaime Diaz, Javier Fraile, Ivan Fernandez
*/
public class ControladorFiltroZonasYAforo implements ItemListener{

	private PestanaZonasAforoCenter vistaFiltrarAbonos;
	private PestanaZonasAforoWest vistaBoxFiltrarAbonos;
	

	public ControladorFiltroZonasYAforo(PestanaZonasAforoCenter vistaFiltrarAbonos,
			PestanaZonasAforoWest vistaBoxFiltrarAbonos) {

		this.vistaFiltrarAbonos = vistaFiltrarAbonos;
		this.vistaBoxFiltrarAbonos = vistaBoxFiltrarAbonos;
	}


	@Override
	public void itemStateChanged(ItemEvent e) {
		if (vistaBoxFiltrarAbonos.getCombobox().getSelectedItem().equals(e.getItem())) {
			if (vistaBoxFiltrarAbonos.getCombobox().getSelectedItem().toString().equals("Crear Zonas")) {
				vistaFiltrarAbonos.showCardCrearZonaTeatro();
			}else if (vistaBoxFiltrarAbonos.getCombobox().getSelectedItem().toString().equals("Entradas a comprar")) {
				vistaFiltrarAbonos.showCardCambiarMaxEntradas();
			}else if (vistaBoxFiltrarAbonos.getCombobox().getSelectedItem().toString().equals("Deshabilitar butaca")) {
				vistaFiltrarAbonos.showCardDeshabilitarButacas();
			}else {
				vistaFiltrarAbonos.showCardReducirAforo();
			}
		}
	}	
}
