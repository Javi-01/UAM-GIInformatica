package controlador.eventos;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import modelo.sistema.Sistema;
import modelo.evento.*;
import vista.panelPrincipal.pestanaBuscarEvento.*;

/**
 * Clase para controlar la busqueda del evento que se introduce en el formulario 
 * del fltro avanzado
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class ControladorTxtBuscarEvento implements ActionListener{

	private PestanaBuscarEventoWest vistaTxtBuscarEvento;
	private PestanaBuscarEventoCenter vistaTablaBuscarEvento;
	
	public ControladorTxtBuscarEvento (PestanaBuscarEventoWest vistaTxtBuscarEvento, PestanaBuscarEventoCenter vistaTablaBuscarEvento) {
		this.vistaTxtBuscarEvento = vistaTxtBuscarEvento;
		this.vistaTablaBuscarEvento = vistaTablaBuscarEvento;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		ArrayList <Evento> eventosCoincidencia = Sistema.getInstance().sistemaBuscarEventosCoincidentes(vistaTxtBuscarEvento.getPestanaNombreBuscarEvento());
		vistaTablaBuscarEvento.destruirTablaEventos();
		if (eventosCoincidencia.size() != 0) {
			vistaTablaBuscarEvento.actualizarTablaEventos(eventosCoincidencia);
		}else{
			JOptionPane.showMessageDialog(vistaTxtBuscarEvento, "No se encontraron coincidencias, intentalo de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
		}
		vistaTxtBuscarEvento.limpiarPestanaBuscarEvento();
	}	
}
