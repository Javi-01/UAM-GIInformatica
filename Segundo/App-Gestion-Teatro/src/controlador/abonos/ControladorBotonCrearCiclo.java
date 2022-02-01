package controlador.abonos;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import modelo.abono.Ciclo;
import modelo.abono.CicloCompuesto;
import modelo.abono.CicloEvento;
import modelo.evento.Evento;
import modelo.sistema.Sistema;
import modelo.zona.Zona;
import vista.panelAdministrador.pestanaCrearAbonos.ciclo.PestanaCardCrearCiclo;

/**
 * Clase para controlar el boton con el que se confirma la creacion de los ciclos
 *
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class ControladorBotonCrearCiclo implements ActionListener {

	private PestanaCardCrearCiclo vistaCrearCiclo;

	/**
	 * constructor del action listener encargado del 
	 * boton que se encarga de confirmar la creacion de los ciclos
	 *
	 * @param vistaCrearCiclo			panel de la creacion de ciclos	
	 */
	public ControladorBotonCrearCiclo(PestanaCardCrearCiclo vistaCrearCiclo) {
		this.vistaCrearCiclo = vistaCrearCiclo;
	}

	/**
	 * controlador del evento que ocurre al pinchar sobre el boton de crear el ciclos
	 *
	 * @param e		evento de raton que ocurre tras pinchar sobre el boton de crear	
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		ArrayList<Ciclo> ciclos = new ArrayList<>();
		ArrayList<Evento> eventos = new ArrayList<>();
		ciclos.addAll(vistaCrearCiclo.getCamposCiclo().getCiclosAnadidosACiclo());
		eventos.addAll(vistaCrearCiclo.getCamposCiclo().getEventosAnadidosACiclo());

		if (vistaCrearCiclo.getCamposCiclo().getNombreCiclo().equals("") || vistaCrearCiclo.getCamposCiclo().getZonaCiclo().equals("")) {
			JOptionPane.showMessageDialog(vistaCrearCiclo, "Debes rellenar todos los campos",
					"Error", JOptionPane.ERROR_MESSAGE);
			return;	
		}
		if (vistaCrearCiclo.getCamposCiclo().getPrecioCiclo() <= 0.0) {
			JOptionPane.showMessageDialog(vistaCrearCiclo, "Debes indicar in porcentaje de reduccion mayor a 0",
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		Zona z = Sistema.getInstance().sistemaBuscarZona(vistaCrearCiclo.getCamposCiclo().getZonaCiclo());

		if (z == null) {
			JOptionPane.showMessageDialog(vistaCrearCiclo, "La zona no esta registrada en el sistema",
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (ciclos.isEmpty() && eventos.isEmpty()) {
			JOptionPane.showMessageDialog(vistaCrearCiclo, "No puedes crear un ciclo sin elementos", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
			
		}else if (ciclos.isEmpty() && !eventos.isEmpty()) {
			Sistema.getInstance().sistemaCrearCicloTeatro(
					new CicloEvento(vistaCrearCiclo.getCamposCiclo().getPrecioCiclo(), vistaCrearCiclo.getCamposCiclo().getNombreCiclo(), eventos, z));
			
		}else if (!ciclos.isEmpty() && !eventos.isEmpty()) {
			Sistema.getInstance().sistemaCrearCicloTeatro(
					new CicloCompuesto(vistaCrearCiclo.getCamposCiclo().getPrecioCiclo(), vistaCrearCiclo.getCamposCiclo().getNombreCiclo(), eventos, z, ciclos));
		}
		JOptionPane.showMessageDialog(vistaCrearCiclo, "EL ciclo " + 
				Sistema.getInstance().getSistemaCiclos().get(Sistema.getInstance().getSistemaCiclos().size() - 1).getCicloNombre() + " se ha creado correctamente");
		
		vistaCrearCiclo.getCamposCiclo().destruirElementosEnCiclo();
		vistaCrearCiclo.getTablaElementos().destruirTablaCompraCiclo();
		vistaCrearCiclo.getCamposCiclo().limpiarCamposCrearCiclo();
	}
}
