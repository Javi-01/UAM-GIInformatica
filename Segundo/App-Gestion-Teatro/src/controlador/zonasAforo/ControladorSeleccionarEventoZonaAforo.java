package controlador.zonasAforo;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import modelo.evento.Evento;
import modelo.sistema.Sistema;
import vista.panelAdministrador.pestanaZonasYAforo.PestanaZonasAforo;
import vista.panelAdministrador.pestanaZonasYAforo.PestanaZonasAforoCenter;

/**
* Clase encargada de un evento del que se quiere realizar alguna operacion con el 
* afor de las zonas
* 
* @author Jaime Diaz, Javier Fraile, Ivan Fernandez
*/
public class ControladorSeleccionarEventoZonaAforo implements MouseListener{

	PestanaZonasAforo vistaZonasAforo;
	PestanaZonasAforoCenter vistaCentreZonas;
	
	public ControladorSeleccionarEventoZonaAforo(PestanaZonasAforo vistaZonasAforo,
			PestanaZonasAforoCenter vistaCentreZonas) {

		this.vistaZonasAforo = vistaZonasAforo;
		this.vistaCentreZonas = vistaCentreZonas;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
		int eventoElegido = vistaZonasAforo.getPestanaTablaEventos().getTablaRepres().rowAtPoint(e.getPoint());
		
		if (eventoElegido != -1) {
			String nombreEvento = (String)vistaZonasAforo.getPestanaTablaEventos().getTablaModeloRepres().getValueAt(eventoElegido, 0);
			Evento eve = Sistema.getInstance().sistemaBuscarEventosCoincidentes(nombreEvento).get(0);
			vistaCentreZonas.setEventoZonasAforo(eve);
			vistaCentreZonas.getCardReducirAforo().setTxtEventoAforo(nombreEvento);
			vistaZonasAforo.showPestanaZonasAforoBorder();
			vistaCentreZonas.showCardReducirAforo();
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
