package controlador.entrada;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.LocalDateTime;

import modelo.evento.Representacion;
import modelo.sistema.Sistema;
import vista.panelPrincipal.pestanaRepresentaciones.*;
import vista.panelUsuarioLogueado.ventanaCompraEntradas.PestanaComprarEntrada;

/**
 * Clase para controlar el evento de raton tras pinchar sobre
 * una de las filas de la tabla de las represetnaciones de las que se pueden 
 * comprar las entradas  
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class ControladorSeleccionarRepresentacion implements MouseListener{

	private PestanaComprarEntrada pestanaCompra;
	private PestanaFiltrarRepresentacionesCenter vistaTablaBuscarRepresentacion;
	
	public ControladorSeleccionarRepresentacion (PestanaComprarEntrada pestanaCompra, PestanaFiltrarRepresentacionesCenter vistaTablaBuscarRepresentacion) {
		
		this.pestanaCompra = pestanaCompra;
		this.vistaTablaBuscarRepresentacion = vistaTablaBuscarRepresentacion;
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		int representacionElegida = vistaTablaBuscarRepresentacion.getTablaRepresentacion().rowAtPoint(e.getPoint());
		Representacion repre;
		
		if (representacionElegida != -1) {
			String horaRepreString = (String) vistaTablaBuscarRepresentacion.getTablaModelo().getValueAt(representacionElegida, 2);
			String fechaRepreString = (String) vistaTablaBuscarRepresentacion.getTablaModelo().getValueAt(representacionElegida, 1);
			String  eventoRepreTitulo = (String) vistaTablaBuscarRepresentacion.getTablaModelo().getValueAt(representacionElegida, 0);
			
			LocalDateTime fechaRepre = Sistema.getInstance().sistemaFormatearFechaRepresentacion(fechaRepreString, horaRepreString);
			repre = Sistema.getInstance().sistemaObtenerRepresentacion(
					Sistema.getInstance().sistemaBuscarEventosCoincidentes(eventoRepreTitulo).get(0), fechaRepre);
			pestanaCompra.getCardZonaEntrada().actualizarTablaZonas(repre);
			pestanaCompra.setRepresentacionVistaEntrada(repre);
			pestanaCompra.showElegirZonaSimple();		
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
