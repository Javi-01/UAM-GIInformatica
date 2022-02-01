package controlador.zonasAforo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import vista.panelAdministrador.pestanaZonasYAforo.PestanaZonasAforoCenter;

/**
* Clase encargada de controlar el aforo que puede disponer una zona, para ello, se observan los campos
* que ha introducido y se comprueba
* 
* @author Jaime Diaz, Javier Fraile, Ivan Fernandez
*/
public class ControladorReducirAforoEvento implements ActionListener{

	private PestanaZonasAforoCenter vistaCenterZonasAforo;
	
	public ControladorReducirAforoEvento(PestanaZonasAforoCenter vistaCenterZonasAforo) {

		this.vistaCenterZonasAforo = vistaCenterZonasAforo;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (vistaCenterZonasAforo.getCardReducirAforo().getTxtEventoAsociado().equals("")) {
			JOptionPane.showMessageDialog(vistaCenterZonasAforo, "Debe seleccionar un evento.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		vistaCenterZonasAforo.getEventoZonaAforo().reducirEventoAforo(
				vistaCenterZonasAforo.getCardReducirAforo().getPorcentajeEvento());
				
		if (vistaCenterZonasAforo.getCardReducirAforo().getPorcentajeEvento() != 0)
			JOptionPane.showMessageDialog(vistaCenterZonasAforo, "EL evento ha reducido su aforo correctamente!!");
			
		vistaCenterZonasAforo.getCardReducirAforo().limpiarCamposAforo();
	}
}

