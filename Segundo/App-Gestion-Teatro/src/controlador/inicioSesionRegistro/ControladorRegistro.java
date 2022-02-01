package controlador.inicioSesionRegistro;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import modelo.sistema.Sistema;
import vista.VentanaFrame;
import vista.panelPrincipal.PanelPrincipal;
import vista.panelPrincipal.VentanaRegistrarUsuario;

/**
 * Clase para controlar el boton de confirmar el registro de la ventana 
 * donde se rellenan los campos de registro 
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class ControladorRegistro implements ActionListener{
	
		private VentanaRegistrarUsuario vistaRegistrarUsuario;
		public ControladorRegistro(VentanaRegistrarUsuario vistaRegistrarUsuario) {
			this.vistaRegistrarUsuario = vistaRegistrarUsuario;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			
			if (vistaRegistrarUsuario.getVentanaNombreRegistro().equals("") || vistaRegistrarUsuario.getVentanaConstrasenaRegistro().equals("")) {
				JOptionPane.showMessageDialog(vistaRegistrarUsuario, "Debe introducir un nombre y una contraseña.", "Error", JOptionPane.ERROR_MESSAGE);
			}else if (Sistema.getInstance().registrarSistemaUsuario(vistaRegistrarUsuario.getVentanaNombreRegistro(), vistaRegistrarUsuario.getVentanaConstrasenaRegistro())) {
				JOptionPane.showMessageDialog(vistaRegistrarUsuario, "Bienvenido " + vistaRegistrarUsuario.getVentanaNombreRegistro() + "!!");
				PanelPrincipal.getVentanaRegistrarUsuario().setVisible(false);
				VentanaFrame.showUsuarioLogeado();
			}else if (!Sistema.getInstance().registrarSistemaUsuario(vistaRegistrarUsuario.getVentanaNombreRegistro(), vistaRegistrarUsuario.getVentanaConstrasenaRegistro())) {
				JOptionPane.showMessageDialog(vistaRegistrarUsuario, "Error, puede que la cuente ya este registrada.", "Error", JOptionPane.ERROR_MESSAGE);
			}
			vistaRegistrarUsuario.limpiarVenatanaRegistro();
		}
}
