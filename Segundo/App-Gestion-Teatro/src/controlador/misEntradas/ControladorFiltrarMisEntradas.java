package controlador.misEntradas;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import modelo.sistema.Sistema;
import vista.panelUsuarioLogueado.misEntradas.PestanaMisEntradas;

/**
* Clase encargada de filtrar las entradas del usuario en base a si han sido compradas 
* o han sido reservadas, una vez pulsado el elemento del combobox, se actualiza la tabla con los registros
* de la misma
*
* @author Jaime Diaz, Javier Fraile, Ivan Fernandez
*/
public class ControladorFiltrarMisEntradas implements ItemListener{

	private PestanaMisEntradas vistaMisEntradas;
	
	/**
	* Contructor de la clase que filtra las entradas en base a si han sido compradas o han sido 
	* reservadas, permitiendo cancelarlas o comprarlas si es el ultimo caso
	* 
	* @param vistaMisEntradas es la pestaña principal dentro del panel
	* @author Jaime Diaz, Javier Fraile, Ivan Fernandez
	*/
	public ControladorFiltrarMisEntradas(PestanaMisEntradas vistaMisEntradas) {
		this.vistaMisEntradas = vistaMisEntradas;
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		
		vistaMisEntradas.getPestanaMisEntradasCentre().getCardEntradasReservadas().destruirTablaMisEntradasReservadas();
		vistaMisEntradas.getPestanaMisEntradasCentre().getCardEntradasCompradas().destruirTablaMisEntradasCompradas();

		if (vistaMisEntradas.getPestanaMisEntradasWest().getCombobox().getSelectedItem().toString().equals("Compradas")) {
			vistaMisEntradas.getPestanaMisEntradasCentre().getCardEntradasCompradas().actualizarTablaMisEntradasCompradas(
						Sistema.getInstance().getSistemaUsuarioLogeado().getUsuarioEntradasCompradas());
			vistaMisEntradas.getPestanaMisEntradasCentre().showMisEntradasCompradas();
		}else if (vistaMisEntradas.getPestanaMisEntradasWest().getCombobox().getSelectedItem().toString().equals("Reservadas")) {
			vistaMisEntradas.getPestanaMisEntradasCentre().getCardEntradasReservadas().actualizarTablaMisEntradasReservadas(
					Sistema.getInstance().getSistemaUsuarioLogeado().getUsuarioEntradasReservadas());
			vistaMisEntradas.getPestanaMisEntradasCentre().showMisEntradasReservadas();
		}
	}

	

}
