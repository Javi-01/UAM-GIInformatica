package controlador.crearEvento;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import vista.panelAdministrador.pestanaCrearEvento.rellenarCampos.PestanaCrearEventoCenter;
import vista.panelAdministrador.pestanaCrearEvento.rellenarCampos.PestanaCrearEventoWest;

/**
 * Clase para controlar el combobox de crear evento
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class ControladorCbEventoACrear implements ItemListener{

	private PestanaCrearEventoWest vistaBoxCrearEventoWest;
	private PestanaCrearEventoCenter vistaCardCrearEventoCenter;
	
	public  ControladorCbEventoACrear(PestanaCrearEventoWest vistaBoxCrearEventoWest, PestanaCrearEventoCenter vistaCardCrearEventoCenter) {
		this.vistaCardCrearEventoCenter = vistaCardCrearEventoCenter;
		this.vistaBoxCrearEventoWest = vistaBoxCrearEventoWest;
	}
	@Override
	public void itemStateChanged(ItemEvent e) {
		
		if (vistaBoxCrearEventoWest.getCombobox().getSelectedItem().equals(e.getItem())) {
			if (e.getItem().equals(vistaCardCrearEventoCenter.getCardEventoDanzaTitulo())) {
				vistaCardCrearEventoCenter.showCrearEventoDanza();
			}else if (e.getItem().equals(vistaCardCrearEventoCenter.getCardEventoConciertoTitulo())) {
				vistaCardCrearEventoCenter.showCrearEventoConcierto();
			}else if (e.getItem().equals(vistaCardCrearEventoCenter.getCardEventoTeatroTitulo())) {
				vistaCardCrearEventoCenter.showCrearEventoTeatro();
			}
		}
		
	}

}
