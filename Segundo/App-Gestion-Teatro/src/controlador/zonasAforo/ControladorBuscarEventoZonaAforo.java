package controlador.zonasAforo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import modelo.sistema.Sistema;
import vista.panelAdministrador.pestanaZonasYAforo.PestanaZonasAforo;

/**
* Clase encargada de buscar un evento de una zona , para ello, se busca
* la zona y se destruye y actualiza la tabla
* 
* @author Jaime Diaz, Javier Fraile, Ivan Fernandez
*/
public class ControladorBuscarEventoZonaAforo implements ActionListener{

	private PestanaZonasAforo vistaZonasAforo;
		
	public ControladorBuscarEventoZonaAforo(PestanaZonasAforo vistaZonasAforo) {
		this.vistaZonasAforo = vistaZonasAforo;
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		
		vistaZonasAforo.getPestanaTablaEventos().destruirTablaEventos();
		vistaZonasAforo.getPestanaTablaEventos().actualizarTablaEventos(Sistema.getInstance().getSistemaEventos());
		vistaZonasAforo.showPestanaTablaEventos();
	}

}
