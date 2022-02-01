package controlador.zonasAforo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import modelo.sistema.Sistema;
import modelo.zona.ZonaCompuesta;
import modelo.zona.ZonaPie;
import modelo.zona.ZonaSentado;
import vista.panelAdministrador.pestanaZonasYAforo.acciones.crearZona.CardCrearZonaTeatroCenter;
import vista.panelAdministrador.pestanaZonasYAforo.acciones.crearZona.CardCrearZonaTeatroWest;

/**
* Clase encargada de controlar el boton al crear una zona, cada clase elige una zona 
* y actua en funcion de el tipo de zona 
*
* @author Jaime Diaz, Javier Fraile, Ivan Fernandez
*/
public class ControladorBotonCrearZona implements ActionListener{

	private CardCrearZonaTeatroCenter vistaZonaTeatro;
	private CardCrearZonaTeatroWest vistaBoxTipoTeatro;
	
	/**
	* Contructor que acciona el boton de crear una zona
	* 
	* @param vistaZonaTeatro pestaña con la vista principal del panel 
	* @param vistaBoxTipoTeatro pestaña con el filtro
	* @author Jaime Diaz, Javier Fraile, Ivan Fernandez
	*/
	public ControladorBotonCrearZona(CardCrearZonaTeatroCenter vistaZonaTeatro, CardCrearZonaTeatroWest vistaBoxTipoTeatro) {
		this.vistaZonaTeatro = vistaZonaTeatro;
		this.vistaBoxTipoTeatro = vistaBoxTipoTeatro;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (Sistema.getInstance().getSistemaEventos().size() != 0) {
			JOptionPane.showMessageDialog(vistaZonaTeatro, "La zona no se ha podido crear porque ya hay eventos creados en el teatro", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if (vistaBoxTipoTeatro.getCombobox().getSelectedItem().toString().equals("Zona Pie")) {
			if (Sistema.getInstance().sistemaBuscarZona(vistaZonaTeatro.getCardZonaPie().getTxtNombreZonaPie()) != null 
					|| vistaZonaTeatro.getCardZonaPie().getTxtNombreZonaPie().equals("")) {
				JOptionPane.showMessageDialog(vistaZonaTeatro, "No se puede crear una zona con ese nombre", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (vistaZonaTeatro.getCardZonaPie().getTxtAforoZona() <= 2) {
				JOptionPane.showMessageDialog(vistaZonaTeatro, "No se puede crear una zona con aforo menor que 2", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			Sistema.getInstance().setContadorZonas(Sistema.getInstance().getContadorZonas() + 1);
			if (!Sistema.getInstance().sistemaCrearZonaTeatro(
					new ZonaPie(Sistema.getInstance().getContadorZonas(), vistaZonaTeatro.getCardZonaPie().getTxtAforoZona(), 
							vistaZonaTeatro.getCardZonaPie().getTxtNombreZonaPie()))) {
				JOptionPane.showMessageDialog(vistaZonaTeatro, "La zona no se ha podido crear", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}else if (vistaBoxTipoTeatro.getCombobox().getSelectedItem().toString().equals("Zona Sentado")) {
			
			if (Sistema.getInstance().sistemaBuscarZona(vistaZonaTeatro.getCardZonaSentado().getTxtNombreZonaSentado()) != null ||
					vistaZonaTeatro.getCardZonaSentado().getTxtNombreZonaSentado().equals("")) {
				JOptionPane.showMessageDialog(vistaZonaTeatro, "No se puede crear una zona con ese nombre", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (vistaZonaTeatro.getCardZonaSentado().getTxtFilasSentado() <= 1) {
				JOptionPane.showMessageDialog(vistaZonaTeatro, "No se puede crear una zona con menos de 1 fila", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			if (vistaZonaTeatro.getCardZonaSentado().getTxtColumnasSentado() <= 1) {
				JOptionPane.showMessageDialog(vistaZonaTeatro, "No se puede crear una zona con menos de 1 columna", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			Sistema.getInstance().setContadorZonas(Sistema.getInstance().getContadorZonas() + 1);
			if (!Sistema.getInstance().sistemaCrearZonaTeatro(
					new ZonaSentado(Sistema.getInstance().getContadorZonas(), vistaZonaTeatro.getCardZonaSentado().getTxtFilasSentado(),
							vistaZonaTeatro.getCardZonaSentado().getTxtColumnasSentado(), 
							vistaZonaTeatro.getCardZonaSentado().getTxtNombreZonaSentado()))) {
				JOptionPane.showMessageDialog(vistaZonaTeatro, "La zona no se ha podido crear", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}else if (vistaBoxTipoTeatro.getCombobox().getSelectedItem().toString().equals("Zona Compuesta")) {
			if (Sistema.getInstance().sistemaBuscarZona(vistaZonaTeatro.getCardZonaCompuesta().getZonaComponentes().getTxtNombreCompuesta()) != null ||
					vistaZonaTeatro.getCardZonaCompuesta().getZonaComponentes().getTxtNombreCompuesta().equals("")) {
				JOptionPane.showMessageDialog(vistaZonaTeatro, "No se puede crear una zona con ese nombre", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			Sistema.getInstance().setContadorZonas(Sistema.getInstance().getContadorZonas() + 1);
			if (vistaZonaTeatro.getCardZonaCompuesta().getZonasCompuesta().isEmpty()) {
				JOptionPane.showMessageDialog(vistaZonaTeatro, "La zona no tiene zonas en su interior", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (!Sistema.getInstance().sistemaCrearZonaTeatro(
					new ZonaCompuesta(Sistema.getInstance().getContadorZonas() ,vistaZonaTeatro.getCardZonaCompuesta().getZonaComponentes().getTxtNombreCompuesta(), 
							vistaZonaTeatro.getCardZonaCompuesta().getZonasCompuesta()))) {
				JOptionPane.showMessageDialog(vistaZonaTeatro, "La zona no se ha podido crear", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
				
		JOptionPane.showMessageDialog(vistaZonaTeatro, "La zona se ha creado correctamente");
		vistaZonaTeatro.getCardZonaPie().limpiarTxtZonaPie();
		vistaZonaTeatro.getCardZonaCompuesta().limpiarZonaCompuesta();
		vistaZonaTeatro.getCardZonaCompuesta().getTablaZonas().destruirTablaZonaCompuesta();
		vistaZonaTeatro.getCardZonaCompuesta().getZonaComponentes().limpiarTxtZonaCompuesta();
		vistaZonaTeatro.getCardZonaSentado().limpiarTxtZonaSentado();
	}

}
