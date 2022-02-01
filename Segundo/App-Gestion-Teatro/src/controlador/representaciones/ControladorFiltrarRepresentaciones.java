package controlador.representaciones;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import vista.panelPrincipal.pestanaRepresentaciones.*;

/**
* Clase para controlar mediante un combobox el filtro de las representaciones
* 
* @author Jaime Diaz, Javier Fraile, Ivan Fernandez
*/
public class ControladorFiltrarRepresentaciones implements ItemListener{

	private PestanaFiltrarRepresentacionesWest vistaBoxFiltrarRepresentaciones;
	private PestanaFiltrarRepresentacionesCenter vistaTablaFiltrarRepresentaciones;
	
	/**
	* Contructor que filtra las representaciones mediante un combobox
	* 
	* @param vistaFiltrarRepresentaciones pestaña con la vista principal del panel 
	* @param vistaTablaFiltrarRepresentaciones pestaña con la lateral del panel
	* @author Jaime Diaz, Javier Fraile, Ivan Fernandez
	*/
	public ControladorFiltrarRepresentaciones(PestanaFiltrarRepresentacionesWest vistaFiltrarRepresentaciones, PestanaFiltrarRepresentacionesCenter vistaTablaFiltrarRepresentaciones) {
		this.vistaBoxFiltrarRepresentaciones = vistaFiltrarRepresentaciones;
		this.vistaTablaFiltrarRepresentaciones = vistaTablaFiltrarRepresentaciones;
	}
	
	@Override
	public void itemStateChanged(ItemEvent e) {
		
		if (vistaBoxFiltrarRepresentaciones.getCombobox().getSelectedItem().equals(e.getItem())) {
			vistaTablaFiltrarRepresentaciones.destruirTablaRepresentaciones();
			vistaTablaFiltrarRepresentaciones.actualizarTablaRepresentaciones(e.getItem().toString());
		}
	}
}

