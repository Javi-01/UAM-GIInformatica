package controlador.inicioSesionRegistro;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import modelo.sistema.Sistema;
import vista.VentanaFrame;
import vista.panelPrincipal.pestanaIniciarSesion.*;
import vista.panelUsuarioLogueado.PanelUsuarioLogueado;

/**
 * Clase para controlar el boton de confirmar el inicio de sesion  
 * de la ventana princiapal de la aplicaion donde se rellenan los campos de inicio de sesion 
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class ControladorIniciarSesion implements ActionListener{

	private PestanaIniciarSesionFormulario vistaIniciarSesion;
	
	public ControladorIniciarSesion(PestanaIniciarSesionFormulario vistaIniciarSesion) {
		this.vistaIniciarSesion = vistaIniciarSesion;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (vistaIniciarSesion.getPestanaNombreInicioSesion().equals("") || vistaIniciarSesion.getPestanaConstrasenaInicio().equals("")) {
			JOptionPane.showMessageDialog(vistaIniciarSesion, "Debe introducir un nombre y una contraseña.", "Error", JOptionPane.ERROR_MESSAGE);
		}else if (Sistema.getInstance().sistemaIniciarSesion(vistaIniciarSesion.getPestanaNombreInicioSesion(), vistaIniciarSesion.getPestanaConstrasenaInicio())) {
			JOptionPane.showMessageDialog(vistaIniciarSesion, "Bienvenido " + vistaIniciarSesion.getPestanaNombreInicioSesion() + "!!");
			if (Sistema.getInstance().getSistemaAdministradorFlag()) {
				VentanaFrame.showPanelAdministrador();
			}else {
				PanelUsuarioLogueado.updatePestanaNotificaciones();
				VentanaFrame.showUsuarioLogeado();
			}
		}else if (!Sistema.getInstance().sistemaIniciarSesion(vistaIniciarSesion.getPestanaNombreInicioSesion(), vistaIniciarSesion.getPestanaConstrasenaInicio())) {
			JOptionPane.showMessageDialog(vistaIniciarSesion, "Nombre de usuario o contraseña incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
		}
		vistaIniciarSesion.limpiarPestanaInicioSesion();
	}
}
