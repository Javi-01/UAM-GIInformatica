package controlador.abonos;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import modelo.sistema.Sistema;
import vista.panelAdministrador.pestanaCrearAbonos.PestanaCrearAbono;
import vista.panelAdministrador.pestanaCrearAbonos.ciclo.CardCrearCicloEventos;

/**
 * Clase para controlar la manera de añadir eventos a ciclos
 *
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class ControladorAnadirEventoACiclo implements MouseListener{

	private CardCrearCicloEventos vistaTablaEventoCiclo;
	private PestanaCrearAbono vistaCrearAbono;
	
	/**
	 * constructor del action listener encargado de 
	 * escoger un evento al pincharlo sobre la tabla 
	 *
	 * @param vistaTablaEventoCiclo		panel de la tabla de eventos tras darle al boton de añadir eventos
	 * @param vistaCrearAbono			panel donde se crean los abonos	
	 */
	public ControladorAnadirEventoACiclo(CardCrearCicloEventos vistaTablaEventoCiclo, PestanaCrearAbono vistaCrearAbono) {

		this.vistaTablaEventoCiclo = vistaTablaEventoCiclo;
		this.vistaCrearAbono = vistaCrearAbono;
	}

	/**
	 * controlador del evento que ocurre tras pinchar sobre uno de los eventos que
	 * hay en la tabla 
	 *
	 * @param e		evento de raton que ocurre tras pinchar sobre un evento que aparece en la tabla	
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		
		int eventoElegido = vistaTablaEventoCiclo.getTablaEvento().rowAtPoint(e.getPoint());
		vistaCrearAbono.getPestanaCrearAbonoBorder().getPestanaCrearAbonoCenter().getPestanaCrearCiclo().getTablaElementos().destruirTablaCompraCiclo();
		
		if (eventoElegido != -1) {
			String  eventoTitulo = (String) vistaTablaEventoCiclo.getTablaModeloEvento().getValueAt(eventoElegido, 0);
			
			
			vistaCrearAbono.getPestanaCrearAbonoBorder().getPestanaCrearAbonoCenter().
				getPestanaCrearCiclo().getCamposCiclo().anadirEventoACiclo(Sistema.getInstance().sistemaBuscarEventosCoincidentes(eventoTitulo).get(0));
			
			vistaCrearAbono.getPestanaCrearAbonoBorder().getPestanaCrearAbonoCenter().getPestanaCrearCiclo().getTablaElementos().
				actualizarTablaCrearCiclo(vistaCrearAbono.getPestanaCrearAbonoBorder().getPestanaCrearAbonoCenter().getPestanaCrearCiclo().getCamposCiclo().
					getCiclosAnadidosACiclo(), vistaCrearAbono.getPestanaCrearAbonoBorder().getPestanaCrearAbonoCenter().getPestanaCrearCiclo().getCamposCiclo().
						getEventosAnadidosACiclo());
			
			vistaCrearAbono.showPestanaBorderAbono();
			}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
