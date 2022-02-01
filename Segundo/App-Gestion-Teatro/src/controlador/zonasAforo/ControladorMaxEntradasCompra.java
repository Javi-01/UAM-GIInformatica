package controlador.zonasAforo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import modelo.sistema.Sistema;
import vista.panelAdministrador.pestanaZonasYAforo.PestanaZonasAforoCenter;

/**
* Clase encargada de controlar el mazimo de entradas que el Administrador 
* pueda establecer el maximo de entradas de una comrpa
* 
* @author Jaime Diaz, Javier Fraile, Ivan Fernandez
*/
public class ControladorMaxEntradasCompra implements ChangeListener, ActionListener{

	private Integer maxEntradas = 5;
	private PestanaZonasAforoCenter vistaCenterZonasAforo;
	
	public ControladorMaxEntradasCompra (PestanaZonasAforoCenter vistaCenterZonasAforo) {

		this.vistaCenterZonasAforo = vistaCenterZonasAforo;
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		maxEntradas = vistaCenterZonasAforo.getCardCambiarMaxEntradas().getJSliderNumEntradas().getValue();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Sistema.getInstance().setNumMaxEntradas(maxEntradas);
		JOptionPane.showMessageDialog(vistaCenterZonasAforo, "Se ha actualizado el numero maximo de entradas a poder comprar");
	}

}
