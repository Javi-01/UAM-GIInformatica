package controlador.estadisticas;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import vista.panelAdministrador.pestanaEstadisticas.PestanaEstadisticasCenter;
import vista.panelAdministrador.pestanaEstadisticas.PestanaEstadisticasWestFiltro;

/**
 * Clase para controlar el desplegable en el que se muestran las opciones
 * con las que se pueden filtrar las estadisticas
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class ControladorFiltrarEstadisticas implements ItemListener{

	private PestanaEstadisticasWestFiltro vistaCboxEstadisticas;
	private PestanaEstadisticasCenter vistaTablaEstadisticas;
	
	public ControladorFiltrarEstadisticas (PestanaEstadisticasWestFiltro vistaCboxEstadisticas, PestanaEstadisticasCenter vistaTablaEstadisticas) {
		
		this.vistaCboxEstadisticas =vistaCboxEstadisticas; 
		this.vistaTablaEstadisticas = vistaTablaEstadisticas;
	}

	@Override
	public void itemStateChanged(ItemEvent e) {

		if (vistaCboxEstadisticas.getComboboxFiltro().getSelectedItem().equals(e.getItem())) {
			if (e.getItem().toString().equals("Ocupacion")) {
				vistaTablaEstadisticas.getEstadisticasCenterGeneralRecaudacion().destruirTablaEstadisticasRecaudacion();
				vistaTablaEstadisticas.getEstadisticasCenterGeneralOcupacion().actualizarTablaEstadisticasGeneralOcupacion();
				vistaTablaEstadisticas.showEstadisticasGeneralOcupacion();
			}else {
				vistaTablaEstadisticas.getEstadisticasCenterGeneralOcupacion().destruirTablaEstadisticasOcupacion();
				vistaTablaEstadisticas.getEstadisticasCenterGeneralRecaudacion().actualizarTablaEstadisticasGeneralRecaudacion();
				vistaTablaEstadisticas.showEstadisticasGeneralRecaudacion();
			}
		}
		
	}
}
