package controlador.zonasAforo;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JOptionPane;

import modelo.sistema.Sistema;
import modelo.zona.Butaca;
import vista.panelAdministrador.pestanaZonasYAforo.PestanaZonasAforo;
import vista.panelAdministrador.pestanaZonasYAforo.PestanaZonasAforoCenter;

/**
* Clase encargada de seleccionar una butaca que se quiere deshabilitar en la tabla de butacas y se 
* queda a la espera de que sea selecionada
* 
* @author Jaime Diaz, Javier Fraile, Ivan Fernandez
*/
public class ControladorSeleccionarButacaAforo implements MouseListener{

	private PestanaZonasAforo vistaZonasAforo;
	private PestanaZonasAforoCenter vistaCenterZonasAforo;
	private Butaca butaca;
	
	public ControladorSeleccionarButacaAforo(PestanaZonasAforo vistaZonasAforo,
			PestanaZonasAforoCenter vistaCenterZonasAforo) {
		
		this.vistaZonasAforo = vistaZonasAforo;
		this.vistaCenterZonasAforo = vistaCenterZonasAforo;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
		int butacaElegida = vistaZonasAforo.getPestanaTablaButacas().getTablaButacas().rowAtPoint(e.getPoint());
		
		if (butacaElegida != -1) {
			
			String filaStr = (String) vistaZonasAforo.getPestanaTablaButacas().getTablaModelo().getValueAt(butacaElegida, 1);	
			String columnaStr = (String) vistaZonasAforo.getPestanaTablaButacas().getTablaModelo().getValueAt(butacaElegida, 2);	
			
			Integer fila = Integer.parseInt(filaStr);
			Integer columna = Integer.parseInt(columnaStr);
			
			if (vistaCenterZonasAforo.getCardDeshabilitarButacas().getZonaAsociada() == null) {
				JOptionPane.showMessageDialog(vistaZonasAforo, "Zona no Seleccionada", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if ((butaca = Sistema.getInstance().getSistemaButacaEnZona(vistaCenterZonasAforo.getCardDeshabilitarButacas().getZonaAsociada(), fila, columna)) == null)	{
				JOptionPane.showMessageDialog(vistaZonasAforo, "La butaca no se ha encontrado", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			vistaCenterZonasAforo.getCardDeshabilitarButacas().setButacaAsociada(butaca);
			
			vistaCenterZonasAforo.getCardDeshabilitarButacas().setTxtButacaAsociada(
					"Zona: "+ vistaCenterZonasAforo.getCardDeshabilitarButacas().
					getZonaAsociada().getZonaNombre() + " fila:"+ fila + " columna: "+ columna);			
			vistaZonasAforo.showPestanaZonasAforoBorder();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


}
