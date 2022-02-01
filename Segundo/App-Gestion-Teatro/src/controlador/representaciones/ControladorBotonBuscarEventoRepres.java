package controlador.representaciones;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import modelo.sistema.Sistema;
import vista.panelAdministrador.pestanaGestionRepresentaciones.PestanaGestionRepresentaciones;

/**
* Clase encargada de accionar el boton de buscar un evento dentro de la pestaña de 
* gestionar las representaciones, esta accion se realiza para seleccionar el avento al que se quieren
* añadir o eliminar o posponer representaciones
*
* @author Jaime Diaz, Javier Fraile, Ivan Fernandez
*/
public class ControladorBotonBuscarEventoRepres implements ActionListener{

	private PestanaGestionRepresentaciones vistaGestionRepre;
	
	/**
	* Contructor de la clase que gestiona la busqueda del evento accionado el boton de buscar
	* 
	* @param vistaGestionRepre pestaña con la vista principal del panel 
	* @author Jaime Diaz, Javier Fraile, Ivan Fernandez
	*/
	public ControladorBotonBuscarEventoRepres(PestanaGestionRepresentaciones vistaGestionRepre) {

		this.vistaGestionRepre = vistaGestionRepre;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (Sistema.getInstance().getSistemaEventos().isEmpty()) {
			JOptionPane.showMessageDialog(vistaGestionRepre, "No puedes crear una representación porque no hay eventos creados en el sistema",
			"Error", JOptionPane.ERROR_MESSAGE);
			return;
			}
		
		vistaGestionRepre.getPestanaTablaRepres().destruirTablaRepresentaciones();
		vistaGestionRepre.getPestanaTablaEventos().destruirTablaEventos();
		vistaGestionRepre.getPestanaTablaEventos().actualizarTablaEventos(Sistema.getInstance().getSistemaEventos());
		vistaGestionRepre.showPestanaTablaGestionEventos();
	}

}
