package controlador.inicioSesionRegistro;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import vista.panelPrincipal.PanelPrincipal;
import vista.panelPrincipal.pestanaIniciarSesion.*;

/**
 * Clase para controlar el boton de elegir registrarse en el sistema en el 
 * panel principal de la aplicacion 
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class ControladorBotonRegistro implements ActionListener{

@SuppressWarnings("unused")
private PestanaIniciarSesionRegistro vistaBtRegistro;

	public  ControladorBotonRegistro(PestanaIniciarSesionRegistro vistaBtRegistro) {
		this.vistaBtRegistro = vistaBtRegistro;
	}

@Override
	public void actionPerformed(ActionEvent e) {
	PanelPrincipal.getVentanaRegistrarUsuario().setVisible(true);
	}
}
