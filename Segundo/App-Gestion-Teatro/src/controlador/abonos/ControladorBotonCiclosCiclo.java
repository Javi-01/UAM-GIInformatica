package controlador.abonos;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import modelo.abono.Ciclo;
import modelo.sistema.Sistema;
import vista.panelAdministrador.pestanaCrearAbonos.PestanaCrearAbono;
import vista.panelAdministrador.pestanaCrearAbonos.ciclo.PestanaCardCrearCiclo;

/**
 * Clase para controlar el boton con el que se accede a la tabla de ciclos del sistema
 * que se inentan añadir a un ciclo
 *
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class ControladorBotonCiclosCiclo implements ActionListener{

	private PestanaCrearAbono vistaCrearAbono;
	private PestanaCardCrearCiclo vistaCrearCiclo;
	
	/**
	 * constructor del action listener encargado del 
	 * boton que se encarga de mostrar la tabla de ciclos del sistema 
	 *
	 * @param vistaCrearAbono			panel de la creacion de abonos
	 * @param vistaCrearCiclo			panel de la creacion de ciclos	
	 */
	public ControladorBotonCiclosCiclo (PestanaCrearAbono vistaCrearAbono, PestanaCardCrearCiclo vistaCrearCiclo) {
		this.vistaCrearAbono = vistaCrearAbono;
		this.vistaCrearCiclo = vistaCrearCiclo;
	}
	
	/**
	 * controlador del evento que ocurre al pinchar sobre el boton de añadir ciclos
	 *
	 * @param e		evento de raton que ocurre tras pinchar sobre el boton de añadir ciclos	
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		
		vistaCrearAbono.getCardAnadirCompuesto().destruirTablaCiclos();
		
		ArrayList <Ciclo> ciclos = new ArrayList <Ciclo>();
		ciclos.addAll(Sistema.getInstance().sistemaCiclosSinEncontrar(vistaCrearCiclo.getCamposCiclo().getCiclosAnadidosACiclo()));
		if (ciclos.isEmpty()) {
			JOptionPane.showMessageDialog(vistaCrearAbono, "No quedan Ciclos a Escoger", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		vistaCrearAbono.getCardAnadirCompuesto().actualizarTablaCiclos(ciclos);
		vistaCrearAbono.showPestanaAnadirCicloCompuesto();
	}
}
