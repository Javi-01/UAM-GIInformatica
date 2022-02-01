package controlador.estadisticas;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import modelo.sistema.Sistema;
import vista.panelAdministrador.pestanaEstadisticas.PestanaEstadisticasCenter;
import vista.panelAdministrador.pestanaEstadisticas.PestanaEstadisticasWestFiltro;
import vista.panelAdministrador.pestanaEstadisticas.PestanaEstadisticasWestTipoBusqueda;

/**
 * Clase para controlar los filtos avanzados por zona y evento
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class ControladorFiltrarEstadisticasAvanzado implements ActionListener{

	private PestanaEstadisticasWestFiltro vistaCBoxFiltroEstadisticas;
	private PestanaEstadisticasWestTipoBusqueda vistaCompBusquedaAvanzada;
	private PestanaEstadisticasCenter vistaTablaEstadisticas;
	
	public ControladorFiltrarEstadisticasAvanzado(PestanaEstadisticasWestFiltro cbVistaFiltroEstadisticas, PestanaEstadisticasWestTipoBusqueda componentesVistaBusquedaAvanzada,
			PestanaEstadisticasCenter vistaTablaEstadisticas) {
		
		this.vistaCBoxFiltroEstadisticas = cbVistaFiltroEstadisticas;
		this.vistaCompBusquedaAvanzada = componentesVistaBusquedaAvanzada;
		this.vistaTablaEstadisticas = vistaTablaEstadisticas;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		vistaTablaEstadisticas.limpiarTablaEstadisticas();
		
		if (vistaCBoxFiltroEstadisticas.getComboboxFiltro().getSelectedItem().toString().equals("Ocupacion")) {
			if (Sistema.getInstance().sistemaContieneEvento(vistaCompBusquedaAvanzada.getNombreFiltroEstadisticas()) != null) {
				vistaTablaEstadisticas.getEstadisticasCenterEventoOcupacion().actualizarTablaEstadisticasEventoOcupacion(
						Sistema.getInstance().sistemaContieneEvento(vistaCompBusquedaAvanzada.getNombreFiltroEstadisticas()));
				vistaTablaEstadisticas.showEstadisticasEventoOcupacion();
			}else if (Sistema.getInstance().sistemaContieneZona(vistaCompBusquedaAvanzada.getNombreFiltroEstadisticas()) != null) {
				vistaTablaEstadisticas.getEstadisticasCenterZonaOcupacion().actualizarTablaEstadisticasZonaOcupacion(
						Sistema.getInstance().sistemaContieneZona(vistaCompBusquedaAvanzada.getNombreFiltroEstadisticas()));
				vistaTablaEstadisticas.showEstadisticasZonaOcupacion();
			}else {
				JOptionPane.showMessageDialog(vistaCompBusquedaAvanzada, "No se ha encotrado ningun Evento/Zona con ese nombre.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		else if (vistaCBoxFiltroEstadisticas.getComboboxFiltro().getSelectedItem().toString().equals("Recaudacion")) {
			if (Sistema.getInstance().sistemaContieneEvento(vistaCompBusquedaAvanzada.getNombreFiltroEstadisticas()) != null) {
				vistaTablaEstadisticas.getEstadisticasCenterEventoRecaudacion().actualizarTablaEstadisticasEventoRecaudacion(
						Sistema.getInstance().sistemaContieneEvento(vistaCompBusquedaAvanzada.getNombreFiltroEstadisticas()));
				vistaTablaEstadisticas.showEstadisticasEventoRecaudacion();
				
			}else if (Sistema.getInstance().sistemaContieneZona(vistaCompBusquedaAvanzada.getNombreFiltroEstadisticas()) != null) {
				vistaTablaEstadisticas.getEstadisticasCenterZonaRecaudacion().actualizarTablaEstadisticasZonaRecaudacion(
						Sistema.getInstance().sistemaContieneZona(vistaCompBusquedaAvanzada.getNombreFiltroEstadisticas()));
				vistaTablaEstadisticas.showEstadisticasZonaRecaudacion();
			}else {
				JOptionPane.showMessageDialog(vistaCompBusquedaAvanzada, "No se ha encotrado ningun Evento/Zona con ese nombre.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	
	
	
}
