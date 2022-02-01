package controlador.notificaciones;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JOptionPane;

import modelo.notificacion.Notificacion;
import modelo.sistema.Sistema;
import vista.panelUsuarioLogueado.PanelUsuarioLogueado;
import vista.panelUsuarioLogueado.notificaciones.NotificacionesUsuarioCenter;

/**
 * Clase para controlar cuando le pincha sobre una de las notificaciones
 * de la tabla de notificaciones que tiene el usuario  
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class ControladorSeleccionarEntradaLeida implements MouseListener{

	NotificacionesUsuarioCenter vistaCentre;
	
	
	public ControladorSeleccionarEntradaLeida(NotificacionesUsuarioCenter vistaCentre) {
		this.vistaCentre = vistaCentre;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int notificacion = vistaCentre.getTablaNotificaciones().rowAtPoint(e.getPoint());
		
		if (notificacion != -1) {
			
			String identificador = (String) vistaCentre.getTablaModeloNotificaciones().getValueAt(notificacion, 0);
			Notificacion n = Sistema.getInstance().getSistemaUsuarioLogeado().
				getUsuarioNotificacionPorIdentificador(Integer.parseInt(identificador));
			if (n != null) {
				Sistema.getInstance().getSistemaUsuarioLogeado().usuarioLeerNotificacion(n);
				JOptionPane.showMessageDialog(vistaCentre, "Notificacion leida");
				PanelUsuarioLogueado.cleanPestanaNotificaciones();
				PanelUsuarioLogueado.updatePestanaNotificaciones();
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
