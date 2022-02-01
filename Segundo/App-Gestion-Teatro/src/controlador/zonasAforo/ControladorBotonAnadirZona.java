package controlador.zonasAforo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import modelo.sistema.Sistema;
import vista.panelAdministrador.pestanaZonasYAforo.acciones.crearZona.CardCrearZonaTeatroCenter;

/**
* Clase encargada de accionar el boton que añade un azona a la zona compuesta, 
* para ello, se actualiza la tabla dinamicamente añadiendo las zonas y eliminado de la
* tabla auxiliar
*
* @author Jaime Diaz, Javier Fraile, Ivan Fernandez
*/
public class ControladorBotonAnadirZona implements ActionListener{

	CardCrearZonaTeatroCenter zonaTeatro;
	
	/**
	* Contructor de la clase que gestiona el boton de buscar las zonas
	* dentro de la zona
	* 
	* @param zonaTeatro pestaña con la vista principal del panel 
	* @author Jaime Diaz, Javier Fraile, Ivan Fernandez
	*/
	public ControladorBotonAnadirZona(CardCrearZonaTeatroCenter zonaTeatro) {
		this.zonaTeatro = zonaTeatro;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		zonaTeatro.getCardZonaTabla().destruirTablaZonas();
		zonaTeatro.getCardZonaCompuesta().getTablaZonas().destruirTablaZonaCompuesta();
		
		if (Sistema.getInstance().getSistemaZonas().isEmpty()) {
			JOptionPane.showMessageDialog(zonaTeatro, "No puedes crear una zona compuesta porque no hay zonas en el sistema", 
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if (Sistema.getInstance().getSistemaZonasNoContenidasAPartirDeUnaLista(
				zonaTeatro.getCardZonaCompuesta().getZonasCompuesta()).isEmpty()) {
			JOptionPane.showMessageDialog(zonaTeatro, "No puedes crear una zona compuesta porque no hay más zonas en el sistema que esten sin asignar",
				"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		zonaTeatro.getCardZonaTabla().actualizarTablaZonas(Sistema.getInstance().getSistemaZonasNoContenidasAPartirDeUnaLista(
				zonaTeatro.getCardZonaCompuesta().getZonasCompuesta()));
		zonaTeatro.showCardTablaZona();
	}

}
