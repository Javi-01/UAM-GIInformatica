package controlador.misEntradas;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.LocalDateTime;

import modelo.evento.Evento;
import modelo.evento.Representacion;
import modelo.sistema.Sistema;
import modelo.entrada.*;
import modelo.zona.Zona;
import vista.panelUsuarioLogueado.misEntradas.PestanaMisEntradasCentre;
import vista.panelUsuarioLogueado.misEntradas.VentanaElegirOpcionEnEntrada;

/**
* Clase encargada de mantenerse a la escucha para cuando el usuario pulse un 
* registro de la tabla en la tabla de reservas, en caso de pulsarlo, se rescata la informacion en forma
* de entrasda seleccionada, y se actua sobre la misma
*
* @author Jaime Diaz, Javier Fraile, Ivan Fernandez
*/
public class ControladorSeleccionarMiEntradaReservada implements MouseListener{

	private PestanaMisEntradasCentre vistaEntradasCentre;
	private VentanaElegirOpcionEnEntrada vistaOpcionEnEntrada;
	
	/**
	* Contructor de la clase que se mantiene a la escucha de un evento de la tabla de las 
	* entradas reservadas; rescatando la informacion de la misma, y permitiendo
	* elegir una opcion a realizar
	* 
	* @param vistEntradasCentre pestaña principal de la tabla que contiene las reservas
	* @param vistaOpcionEnEntrada ventana flotante para mostrar las opciones
	* @author Jaime Diaz, Javier Fraile, Ivan Fernandez
	*/
	public ControladorSeleccionarMiEntradaReservada(PestanaMisEntradasCentre vistEntradasCentre, 
			VentanaElegirOpcionEnEntrada vistaOpcionEnEntrada) {
		
		this.vistaOpcionEnEntrada = vistaOpcionEnEntrada;
		this.vistaEntradasCentre = vistEntradasCentre;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
		if (vistaEntradasCentre.getMisEntradasEntradaAsociada()!= null) {
			vistaEntradasCentre.setMisEntradasEntradaAsociada(null);
		}
		int entradaReg = vistaEntradasCentre.getCardEntradasReservadas().getTablaEntradas().rowAtPoint(e.getPoint());
		
		if (entradaReg != -1) {
			String identificador = (String) vistaEntradasCentre.getCardEntradasReservadas().getTablaModeloEntradas().getValueAt(entradaReg, 0);
			String fecha  = (String) vistaEntradasCentre.getCardEntradasReservadas().getTablaModeloEntradas().getValueAt(entradaReg, 1);
			String hora  = (String) vistaEntradasCentre.getCardEntradasReservadas().getTablaModeloEntradas().getValueAt(entradaReg, 2);
			String zona = (String) vistaEntradasCentre.getCardEntradasReservadas().getTablaModeloEntradas().getValueAt(entradaReg, 3);
			
			int id = Integer.parseInt(identificador);
			LocalDateTime fechaRepre = Sistema.getInstance().sistemaFormatearFechaRepresentacion(fecha, hora);
			Zona z = Sistema.getInstance().sistemaBuscarZona(zona);
			
			for (Evento eve : Sistema.getInstance().getSistemaEventos()) {
				Representacion r = Sistema.getInstance().sistemaObtenerRepresentacion(eve, fechaRepre);
				if (r != null) {
					Entrada entrada = r.getRepresentacionEntradaAPartirDeUnaZonaYUnIdentificador(z, id);
					if (entrada != null) {
						vistaEntradasCentre.setMisEntradasEntradaAsociada(entrada);
						vistaOpcionEnEntrada.setVisible(true);
						return;
					}
				}
			}
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
