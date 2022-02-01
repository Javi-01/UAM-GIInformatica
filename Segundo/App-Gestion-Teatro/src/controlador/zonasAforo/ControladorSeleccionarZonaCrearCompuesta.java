package controlador.zonasAforo;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import modelo.sistema.Sistema;
import modelo.zona.Zona;
import vista.panelAdministrador.pestanaZonasYAforo.acciones.crearZona.CardCrearZonaTeatroCenter;

/**
* Clase encargada de manteneer a la espera de que el usuario pulse una zona
* 
* @author Jaime Diaz, Javier Fraile, Ivan Fernandez
*/
public class ControladorSeleccionarZonaCrearCompuesta implements MouseListener{

	CardCrearZonaTeatroCenter zonaTeatro;
		
	public ControladorSeleccionarZonaCrearCompuesta(CardCrearZonaTeatroCenter zonaTeatro) {
		this.zonaTeatro = zonaTeatro;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
		int zonaElegida = zonaTeatro.getCardZonaTabla().getTablaZonas().rowAtPoint(e.getPoint());
		
		if (zonaElegida != -1) {
			String nombreZona = null;
			nombreZona = (String)zonaTeatro.getCardZonaTabla().getTablaModeloZonas().getValueAt(zonaElegida, 0);	
	
			Zona zona = Sistema.getInstance().sistemaBuscarZona(nombreZona);
			
			zonaTeatro.getCardZonaCompuesta().addZonasCompuesta(zona);
			
			zonaTeatro.getCardZonaCompuesta().getTablaZonas().actualizarTablaZonas(
					zonaTeatro.getCardZonaCompuesta().getZonasCompuesta());
			zonaTeatro.showCardZonaCompuesta();
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
