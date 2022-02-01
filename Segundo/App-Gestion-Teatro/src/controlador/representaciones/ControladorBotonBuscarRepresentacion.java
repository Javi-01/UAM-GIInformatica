package controlador.representaciones;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import modelo.evento.Evento;
import vista.panelAdministrador.pestanaGestionRepresentaciones.PestanaGestionRepresentaciones;

/**
* Clase encargada de accionar el boton para buscar una representacion, se mantendra dicha 
* tabla actualizada continuamente
*
* @author Jaime Diaz, Javier Fraile, Ivan Fernandez
*/
public class ControladorBotonBuscarRepresentacion implements ActionListener{

	private PestanaGestionRepresentaciones vistaGestionRepre;
	private Evento eve;
	
	/**
	* Contructor que acciona y realiza la accion considerada y comprueba que todo se 
	* pueda realizar en cuanto a la gestion
	* 
	* @param vistaGestionRepre pestaña con la vista principal del panel 
	* @author Jaime Diaz, Javier Fraile, Ivan Fernandez
	*/
	public ControladorBotonBuscarRepresentacion(PestanaGestionRepresentaciones vistaGestionRepre) {

		this.vistaGestionRepre = vistaGestionRepre;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		vistaGestionRepre.getPestanaTablaEventos().destruirTablaEventos();
		vistaGestionRepre.getPestanaTablaRepres().destruirTablaRepresentaciones();
		if ((eve = vistaGestionRepre.getPrestanaGestionRepres().getPestanaGestionRepresCenter().getEventoGestionRepre()) != null) {
			vistaGestionRepre.getPestanaTablaRepres().actualizarTablaRepresentaciones(eve);
			vistaGestionRepre.showPestanaTablaGestionRepres();
		}else {
			JOptionPane.showMessageDialog(vistaGestionRepre, "Debes elegir primero evento", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
