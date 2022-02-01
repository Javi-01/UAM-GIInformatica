package controlador.representaciones;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import vista.panelAdministrador.pestanaGestionRepresentaciones.PestanaGestionRepresentacionesCenter;
import vista.panelAdministrador.pestanaGestionRepresentaciones.PestanaGestionRepresentacionesWest;

/**
* Clase encargada de filtrar mediante un combobox el tipo de gestion que se quiere
* realizar sobre las representaciones
*
* @author Jaime Diaz, Javier Fraile, Ivan Fernandez
*/
public class ControladorFiltrarGestionRepres implements ItemListener{

	private PestanaGestionRepresentacionesCenter vistaCardRepre;
	private PestanaGestionRepresentacionesWest vistaCbRepre;
	
	/**
	* Contructor de la clase que recibe la pestaña principal de mis entradas, 
	* y la opcion a elegir de mis entradas que se corresponde con la ventana flotantes
	* accionando el boton de comprar
	* 
	* @param vistaCardRepre vista central del panel
	* @param vistaCbRepre vista del combobox
	*
	* @author Jaime Diaz, Javier Fraile, Ivan Fernandez
	*/
	public ControladorFiltrarGestionRepres(PestanaGestionRepresentacionesCenter vistaCardRepre,
			PestanaGestionRepresentacionesWest vistaCbRepre) {
		
		this.vistaCardRepre = vistaCardRepre;
		this.vistaCbRepre = vistaCbRepre;
	}


	@Override
	public void itemStateChanged(ItemEvent e) {
		
		vistaCardRepre.setEventoGestionRepre(null);
		vistaCardRepre.setRepresentacionGestion(null);
		
		if (vistaCbRepre.getCombobox().getSelectedItem().toString().equals("Añadir")) {
			vistaCardRepre.getCardPosponerRepre().limpiarTxtEventoAsocido();
			vistaCardRepre.getCardCancelarRepre().limpiarTxtRepreAsocido();
			vistaCardRepre.getCardPosponerRepre().limpiarTxtEventoAsocido();
			vistaCardRepre.getCardCancelarRepre().limpiarTxtRepreAsocido();
			vistaCardRepre.showCardAnadirRepre();
		}else if (vistaCbRepre.getCombobox().getSelectedItem().toString().equals("Cancelar")){
			vistaCardRepre.getCardAnadirRepre().limpiarEventoAsociado();
			vistaCardRepre.getCardPosponerRepre().limpiarTxtEventoAsocido();
			vistaCardRepre.getCardPosponerRepre().limpiarTxtRepreAsocido();
			vistaCardRepre.showCardCancelarRepre();
		}else if (vistaCbRepre.getCombobox().getSelectedItem().toString().equals("Posponer")) {
			vistaCardRepre.getCardAnadirRepre().limpiarEventoAsociado();
			vistaCardRepre.getCardCancelarRepre().limpiarTxtEventoAsocido();
			vistaCardRepre.getCardCancelarRepre().limpiarTxtRepreAsocido();
			vistaCardRepre.showCardPosponerRepre();
		}
	}

}
