package controlador.zonasAforo;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import vista.panelAdministrador.pestanaZonasYAforo.acciones.crearZona.CardCrearZonaTeatroCenter;
import vista.panelAdministrador.pestanaZonasYAforo.acciones.crearZona.CardCrearZonaTeatroWest;

/**
* Clase encargada de fitrar las pestaña al crear, para ello, se selecciona 
* mediante un combobox la opcion seleccionada
* 
* @author Jaime Diaz, Javier Fraile, Ivan Fernandez
*/
public class ControladorFiltroTipoZonaCrear implements ItemListener{

	private CardCrearZonaTeatroWest vistaBoxZonaTeatro;
	private CardCrearZonaTeatroCenter vistaZonaTeatro;
	
	
	public ControladorFiltroTipoZonaCrear(CardCrearZonaTeatroWest vistaBoxZonaTeatro,
			CardCrearZonaTeatroCenter vistaZonaTeatro) {

		this.vistaBoxZonaTeatro = vistaBoxZonaTeatro;
		this.vistaZonaTeatro = vistaZonaTeatro;
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		
		if (vistaBoxZonaTeatro.getCombobox().getSelectedItem().equals(e.getItem())) {
			if (vistaBoxZonaTeatro.getCombobox().getSelectedItem().toString().equals("Zona Pie")) {
				vistaZonaTeatro.showCardZonaPie();
				vistaZonaTeatro.getCardZonaCompuesta().getTablaZonas().destruirTablaZonaCompuesta();
				vistaZonaTeatro.getCardZonaCompuesta().getZonaComponentes().limpiarTxtZonaCompuesta();
				vistaZonaTeatro.getCardZonaCompuesta().limpiarZonaCompuesta();
			}else if (vistaBoxZonaTeatro.getCombobox().getSelectedItem().toString().equals("Zona Sentado")) {
				vistaZonaTeatro.showCardZonaSentado();
				vistaZonaTeatro.getCardZonaCompuesta().getZonaComponentes().limpiarTxtZonaCompuesta();
				vistaZonaTeatro.getCardZonaCompuesta().getTablaZonas().destruirTablaZonaCompuesta();
				vistaZonaTeatro.getCardZonaCompuesta().limpiarZonaCompuesta();
			}else {
				vistaZonaTeatro.showCardZonaCompuesta();
			}
		}
	}
}
