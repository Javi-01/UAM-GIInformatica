package controlador.zonasAforo;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import modelo.sistema.Sistema;
import modelo.zona.Zona;
import modelo.zona.ZonaSentado;
import vista.panelAdministrador.pestanaZonasYAforo.PestanaZonasAforo;
import vista.panelAdministrador.pestanaZonasYAforo.PestanaZonasAforoCenter;

/**
* Clase encargada seleccionar de la tabla de zonas las zona sque se quieren añadir a la 
* zona compuesta
* 
* @author Jaime Diaz, Javier Fraile, Ivan Fernandez
*/
public class ControladorSeleccionarZonaAforo implements MouseListener{

	private PestanaZonasAforo vistaZonasAforo;
	private PestanaZonasAforoCenter vistaCentreZonas;
		
	public ControladorSeleccionarZonaAforo(PestanaZonasAforo vistaZonasAforo,
			PestanaZonasAforoCenter vistaCentreZonas) {
		
		this.vistaZonasAforo = vistaZonasAforo;
		this.vistaCentreZonas = vistaCentreZonas;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
		int zonaElegida = vistaZonasAforo.getPestanaTablaZonas().getTablaZonas().rowAtPoint(e.getPoint());
		
		if (zonaElegida != -1) {
			String nombreZona = null;
			nombreZona = (String)vistaZonasAforo.getPestanaTablaZonas().getTablaModelo().getValueAt(zonaElegida, 0);	
	
			Zona zona = Sistema.getInstance().sistemaBuscarZona(nombreZona);
			
			vistaCentreZonas.getCardDeshabilitarButacas().setZonaAsociada(zona);
			vistaZonasAforo.getPestanaTablaButacas().actualizarTablaButacas(((ZonaSentado)zona).getZonaSentadoButacas(), zona);
			vistaZonasAforo.showPestanaTablaButacas();
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
