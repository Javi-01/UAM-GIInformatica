package controlador.zonasAforo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import modelo.sistema.Sistema;
import vista.panelAdministrador.pestanaZonasYAforo.PestanaZonasAforo;
import vista.panelAdministrador.pestanaZonasYAforo.tablas.PestanaZonasAforoTablaZonas;

/**
* Clase encargada de buscar un aforo a una zona, actualiza la tabla de zonas 
* 
* @author Jaime Diaz, Javier Fraile, Ivan Fernandez
*/
public class ControladorBuscarZonaAforo implements ActionListener{

	private PestanaZonasAforo vistaZonasAforo;
	private PestanaZonasAforoTablaZonas vistaTablaZonas;
	
	
	public ControladorBuscarZonaAforo(PestanaZonasAforo vistaZonasAforo, PestanaZonasAforoTablaZonas vistaTablaZonas) {
		this.vistaZonasAforo = vistaZonasAforo;
		this.vistaTablaZonas = vistaTablaZonas;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		vistaTablaZonas.destruirTablaZonas();
		vistaTablaZonas.actualizarTablaZonas(Sistema.getInstance().getSistemaZonasSentadas());
		vistaZonasAforo.showPestanaTablaZonas();
	}

}
