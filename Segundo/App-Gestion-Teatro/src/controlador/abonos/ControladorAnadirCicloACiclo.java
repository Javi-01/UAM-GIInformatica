package controlador.abonos;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import modelo.sistema.Sistema;
import vista.panelAdministrador.pestanaCrearAbonos.PestanaCrearAbono;
import vista.panelAdministrador.pestanaCrearAbonos.ciclo.CardCrearCicloCompuesto;

/**
 * Clase para controlar la manera de añadir ciclos a otros ciclos
 *
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class ControladorAnadirCicloACiclo implements MouseListener{

	private CardCrearCicloCompuesto vistaTablaCicloCompuesto;
	private PestanaCrearAbono vistaCrearAbono;
	
	/**
	 * constructor del action listener encargado de 
	 * escoger un ciclo al pincharlo sobre la tabla 
	 *
	 * @param vistaTablaCicloCiclo		panel de la tabla de ciclos tras darle al boton de añadir ciclos
	 * @param vistaCrearAbono			panel donde se crean los abonos	
	 */
	public ControladorAnadirCicloACiclo(CardCrearCicloCompuesto vistaTablaCicloCiclo, PestanaCrearAbono vistaCrearAbono) {

		this.vistaTablaCicloCompuesto = vistaTablaCicloCiclo;
		this.vistaCrearAbono = vistaCrearAbono;
	}

	/**
	 * controlador del evento que ocurre tras pinchar sobre uno de los ciclos que
	 * hay en la tabla 
	 *
	 * @param e		evento de raton que ocurre tras pinchar sobre un ciclo que aparece en la tabla	
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		
		int cicloElegido = vistaTablaCicloCompuesto.getTablaCiclo().rowAtPoint(e.getPoint());
		vistaCrearAbono.getPestanaCrearAbonoBorder().getPestanaCrearAbonoCenter().getPestanaCrearCiclo().getTablaElementos().destruirTablaCompraCiclo();
		
		if (cicloElegido != -1) {
			String  cicloNombre = (String) vistaTablaCicloCompuesto.getTablaModeloCiclo().getValueAt(cicloElegido, 0);
			
			
			vistaCrearAbono.getPestanaCrearAbonoBorder().getPestanaCrearAbonoCenter().
				getPestanaCrearCiclo().getCamposCiclo().anadirCicloACiclo(Sistema.getInstance().sistemaBuscarCiclos(cicloNombre).get(0));
			
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
