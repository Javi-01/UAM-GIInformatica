package vista.panelAdministrador.pestanaZonasYAforo.acciones;

import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;

import modelo.zona.Butaca;
import modelo.zona.Zona;
/**
 * Clase CardDeshabilitarButacas para la card de deshabilitar butacas
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class CardDeshabilitarButacas extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2212133271955185111L;
	
	private Zona zonaAsociada;
	private Butaca butaca;
	
	private JLabel lblButacaAsociada = new JLabel("Butaca a deshabilitar: ");
	private JTextField txtButacaAsociada = new JTextField(10);
	private JButton btButacaAsociada = new JButton("Buscar Butaca en Zona");
	
	private JLabel lblFechaDes = new JLabel("Introduce fecha de deshabilitacion(yyyy-MM-dd HH:mm): ");
	private SimpleDateFormat formatoFechaDes = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private String strFechaDes;
	private Date fechaDes = new Date();
	private JFormattedTextField txtFechaDeshabilitacion;

	private JLabel lblFechaHab = new JLabel("Introduce fecha de habilitacion (yyyy-MM-dd HH:mm): ");
	private JFormattedTextField txtFechaHabilitacion;
	
	private JLabel lblButacaMotivo = new JLabel("Motivo de deshabilitacion: ");
	private JTextField txtButacaMotivo = new JTextField(10);
	
	private JButton btAceptar = new JButton ("Confirmar");
	private GridLayout layoutButaca = new GridLayout(7, 2, 25, 25);
	/**
	 * Constructor de CardDeshabilitarButacas
	 */
	public CardDeshabilitarButacas () {
		
		setLayout(layoutButaca);
		
		txtButacaAsociada.setEditable(false);
		txtFechaDeshabilitacion = new JFormattedTextField(createFormatter("####-##-## ##:##"));
		strFechaDes = formatoFechaDes.format(fechaDes);
		txtFechaDeshabilitacion.setText(strFechaDes);
		txtFechaHabilitacion = new JFormattedTextField(createFormatter("####-##-## ##:##"));
		
		anadirComponentes();
	}
	/**
	 * Metodo privado para crear un formater para el formato de la fecha
	 * 
	 * @param string String con la cadena a formatear
	 * 
	 * @return MaskFormatter con el formato
	 */
	private MaskFormatter createFormatter(String string) {
		
		MaskFormatter formatter = null;
	    try {
	      formatter = new MaskFormatter(string);
	    } catch (java.text.ParseException exc) {
	      System.err.println("formatter is bad: " + exc.getMessage());
	    }
	    return formatter;
	}
	/**
	 * Metodo para anadir los componentes al card
	 */
	private void anadirComponentes() {
		
		add(lblButacaAsociada);
		add(txtButacaAsociada);
		add(btButacaAsociada);
		add(new JLabel(""));
		add(lblFechaDes);
		add(lblFechaHab);
		add(txtFechaDeshabilitacion);
		add(txtFechaHabilitacion);
		add(lblButacaMotivo);
		add(txtButacaMotivo);
		add(new JLabel(""));
		add(new JLabel(""));
		add(new JLabel(""));
		add(btAceptar);
	}
	/**
	 * Metodo para establecer el controlador del boton para buscar butaca
	 * 
	 * @param e ActionListener con el controlador
	 */
	public void setControladorZona(ActionListener e) {
		btButacaAsociada.addActionListener(e);
	}
	/**
	 * Metodo para establecer el controlador del boton aceptar
	 * 
	 * @param e ActionListener con el controlador
	 */
	public void setControladorConfirmar (ActionListener e) {
		btAceptar.addActionListener(e);
	}
	/**
	 * Metodo para obtener la fecha indicada de deshabilitada en el textField
	 * 
	 * @return JFormattedTextField con la fecha indicada
	 */
	public JFormattedTextField getFechaAsociadaDeshabilitada () {
		return txtFechaDeshabilitacion;
	}
	/**
	 * Metodo para obtener la fecha indicada de habilitada en el textField
	 * 
	 * @return JFormattedTextField con la fecha indicada
	 */
	public JFormattedTextField getFechaAsociadaHabilitada () {
		return txtFechaHabilitacion;
	}
	/**
	 * Metodo para establecer la butaca a deshabilitar
	 * 
	 * @param nombre String la butaca a deshabilitar
	 */
	public void setTxtButacaAsociada(String nombre) {
		txtButacaAsociada.setText(nombre);
	}
	/**
	 * Metodo para limpiar el textField de butaca
	 */
	public void limpiarTxtButaca() {
		txtButacaAsociada.setText("");
		txtButacaMotivo.setText("");
	}
	/**
	 * Metodo para obtener la butaca indicada en el textField
	 * 
	 * @return JFormattedTextField con la fecha indicada
	 */
	public String getTxtButacaAsociada() {
		return txtButacaAsociada.getText();
	}
	/**
	 * Metodo para obtener el motivo indicada en el textField
	 * 
	 * @return JFormattedTextField con el motivo
	 */
	public String getTxtMotivoButaca () {
		return txtButacaMotivo.getText();
	}
	/**
	 * Metodo para obtener la zona
	 * 
	 * @return Zona con la zona
	 */
	public Zona getZonaAsociada() {
		return zonaAsociada;
	}
	/**
	 * Metodo para establecer la zona de la butaca a deshabilitar
	 * 
	 * @param zonaAsociada Zona con la zona 
	 */
	public void setZonaAsociada(Zona zonaAsociada) {
		this.zonaAsociada = zonaAsociada;
	}
	/**
	 * Metodo para establecer la butaca a deshabilitar
	 * 
	 * @param butaca Butaca con la butaca
	 */
	public void setButacaAsociada(Butaca butaca) {
		this.butaca = butaca;
	}
	/**
	 * Metodo para obtener la butaca a deshabilitar
	 * 
	 * @return Butaca con la butaca
	 */
	public Butaca getButacaAsociada () {
		return butaca;
	}
}
