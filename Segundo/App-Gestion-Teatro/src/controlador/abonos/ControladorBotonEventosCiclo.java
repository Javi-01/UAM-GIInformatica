package controlador.abonos;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import modelo.evento.Evento;
import modelo.sistema.Sistema;
import vista.panelAdministrador.pestanaCrearAbonos.PestanaCrearAbono;
import vista.panelAdministrador.pestanaCrearAbonos.ciclo.PestanaCardCrearCiclo;

/**
 * Clase para controlar el boton con el que se buscan los eventos del sistema
 *
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class ControladorBotonEventosCiclo implements ActionListener{

	private PestanaCrearAbono vistaCrearAbono;
	private PestanaCardCrearCiclo vistaCrearCiclo;
	
	/**
	 * constructor del action listener encargado del 
	 * boton que se encarga de cargar llamar a la tabla de eventos del sistema
	 *
	 * @param vistaCrearCiclo			panel de la creacion de ciclos
	 * @param vistaCrearAbono			panel de la creacion de abonos	
	 */
	public ControladorBotonEventosCiclo(PestanaCrearAbono vistaCrearAbono, PestanaCardCrearCiclo vistaCrearCiclo) {
		this.vistaCrearAbono = vistaCrearAbono;
		this.vistaCrearCiclo = vistaCrearCiclo;
	}
	
	/**
	 * controlador del evento que ocurre al pinchar sobre el boton de añadir un evento
	 *
	 * @param e		evento de raton que ocurre tras pinchar sobre el boton de añadir evento
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		
		vistaCrearAbono.getCardAnadirCicloEvento().destruirTablaEventos();
		
		ArrayList <Evento> eventos = new ArrayList <Evento>();
		eventos.addAll(Sistema.getInstance().sistemaEventosSinEncontrar(vistaCrearCiclo.getCamposCiclo().getEventosAnadidosACiclo()));
		if (eventos.isEmpty()) {
			JOptionPane.showMessageDialog(vistaCrearAbono, "No quedan Eventos a escoger", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		vistaCrearAbono.getCardAnadirCicloEvento().actualizarTablaEventos(eventos);
		vistaCrearAbono.showPestanaAnadirCicloEventos();
	}

}
