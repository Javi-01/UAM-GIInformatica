package controlador.abonos;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import modelo.abono.Ciclo;
import modelo.sistema.Sistema;
import vista.panelUsuarioLogueado.pestanaBuscarCiclo.PestanaBuscarCicloCenter;
import vista.panelUsuarioLogueado.pestanaBuscarCiclo.PestanaBuscarCicloWest;

/**
 * Clase para controlar el boton con el que se buscan ciclos
 *
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class ControladorBuscarCiclo implements ActionListener{

	private PestanaBuscarCicloWest vistaTxtBuscarCiclo;
	private PestanaBuscarCicloCenter vistaTablaBuscarCiclo;
	
	/**
	 * constructor del action listener encargado del 
	 * deslizable cuando se seleccion la pestaña ciclo
	 *
	 * @param vistaTablaBuscarCiclo		tabla de buscar ciclos
	 * @param vistaTxtBuscarCiclo		pestaña del deslizable elegido	
	 */
	public ControladorBuscarCiclo (PestanaBuscarCicloWest vistaTxtBuscarCiclo, PestanaBuscarCicloCenter vistaTablaBuscarCiclo) {
		this.vistaTxtBuscarCiclo = vistaTxtBuscarCiclo;
		this.vistaTablaBuscarCiclo = vistaTablaBuscarCiclo;
	}
	
	/**
	 * controlador del evento que ocurre al elegir un la pestaña de ciclos
	 *
	 * @param e		evento de raton que ocurre tras seleccionar la pestaña de ciclos del deslizable	
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		ArrayList <Ciclo> ciclosCoincidencia = Sistema.getInstance().sistemaBuscarCiclos(vistaTxtBuscarCiclo.getPestanaNombreBuscarCiclo());
		vistaTablaBuscarCiclo.destruirTablaCiclos();
		if (ciclosCoincidencia.size() != 0) {
			vistaTablaBuscarCiclo.actualizarTablaCiclos(ciclosCoincidencia);
		}else{
			JOptionPane.showMessageDialog(vistaTxtBuscarCiclo, "No se encontraron coincidencias, intentalo de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
		}
		vistaTxtBuscarCiclo.limpiarPestanaBuscarCiclo();
	}	
}


