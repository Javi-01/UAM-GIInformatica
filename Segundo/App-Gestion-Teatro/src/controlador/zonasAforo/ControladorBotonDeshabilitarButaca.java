package controlador.zonasAforo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.swing.JOptionPane;

import modelo.sistema.Sistema;
import vista.panelAdministrador.pestanaZonasYAforo.PestanaZonasAforo;
import vista.panelAdministrador.pestanaZonasYAforo.PestanaZonasAforoCenter;

/**
* Clase encargada de deshabilitar butacas asociadas a un evento y a una zona sentado
* 
* @author Jaime Diaz, Javier Fraile, Ivan Fernandez
*/
public class ControladorBotonDeshabilitarButaca implements ActionListener{

	private PestanaZonasAforo vistaZonasAforo;
	private PestanaZonasAforoCenter vistaCenterZonasAforo;
	private LocalDateTime fechaIni, fechaFin;
	
	public ControladorBotonDeshabilitarButaca(PestanaZonasAforo vistaZonasAforo,
			PestanaZonasAforoCenter vistaCenterZonasAforo) {

		this.vistaZonasAforo = vistaZonasAforo;
		this.vistaCenterZonasAforo = vistaCenterZonasAforo;
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (vistaCenterZonasAforo.getCardDeshabilitarButacas().getButacaAsociada() == null) {
			JOptionPane.showMessageDialog(vistaZonasAforo, "La butaca no ha sido seleccionada", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		String strFechaIni = vistaCenterZonasAforo.getCardDeshabilitarButacas().getFechaAsociadaDeshabilitada().getText().toString();
		DateTimeFormatter formatterIni = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		String strFechaFin = vistaCenterZonasAforo.getCardDeshabilitarButacas().getFechaAsociadaHabilitada().getText().toString();
		DateTimeFormatter formatterFin = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		
		try {
			fechaIni = LocalDateTime.parse(strFechaIni, formatterIni);	
			fechaFin = LocalDateTime.parse(strFechaFin, formatterFin);	
			
		}catch (DateTimeParseException e1) {
			JOptionPane.showMessageDialog(vistaZonasAforo, "No se ha podido establecer dicha fecha", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if (!fechaIni.isBefore(fechaFin)) {
			JOptionPane.showMessageDialog(vistaZonasAforo, "La fecha de fin no puede ser mayor que la de inicio", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if (!Sistema.getInstance().sistemaDeshabilitarButacaTeatro(
				vistaCenterZonasAforo.getCardDeshabilitarButacas().getZonaAsociada(), 
				vistaCenterZonasAforo.getCardDeshabilitarButacas().getButacaAsociada(), fechaIni, fechaFin, 
				vistaCenterZonasAforo.getCardDeshabilitarButacas().getTxtMotivoButaca())) {
			
			JOptionPane.showMessageDialog(vistaZonasAforo, "La butaca no se ha podido deshabilitar", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		vistaCenterZonasAforo.getCardDeshabilitarButacas().limpiarTxtButaca();
		JOptionPane.showMessageDialog(vistaCenterZonasAforo, "La butaca ha sido deshabilitada");
	}

}
