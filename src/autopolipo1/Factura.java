package autopolipo1;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.sun.org.apache.xml.internal.utils.DOMHelper;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import sun.applet.Main;

public class Factura {
	private static Factura instance;
	private Font arial;
	private Font arialSmall;
	private Font arialMini;
	private Font courier;
        private Connection con;
        private Statement st;
        private ResultSet rs;
        private String caminho;
	/**
	 * Private constructor, since this is a singleton. Fonts are initialized in
	 * the constructor.
	 */
         public Connection conexao(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/autopolipo","root","");
          st=con.createStatement();
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,"Erro ao conectar a base de dados","Erro",JOptionPane.ERROR_MESSAGE);
        }return con;
    }
	public Factura() {
		arial = FontFactory.getFont("Arial", 12);
		arial.setStyle(Font.BOLD);
		arialSmall = FontFactory.getFont("Arial", 10);
		arialSmall.setStyle(Font.BOLD);
		courier = FontFactory.getFont("Courier", 12);
		arialMini = FontFactory.getFont("Arial", 10);
	}

	/**
	 * Provides an access to the service instance
	 * 
	 * @return
	 */
	public static Factura getInstance() {
		if (instance == null) {
			instance = new Factura();
		}
		return instance;
	}

	/**
	 * Creates the PDF document.
	 * 
	 * @param domainObject
	 */
	public void createDocument(Object domainObject) throws IOException {
		/**
		 * Assign the domainObject to an instance variable so that it can be
		 * used to populate the fields in the form
		 */

            caminho = new File("Factura.pdf").getCanonicalPath();
            
		try {
			Document document = new Document(PageSize.LETTER);
			document.setMargins(36f, 36f, 36f, 36f);
			FileOutputStream fos;
			fos = new FileOutputStream(caminho);
			PdfWriter.getInstance(document, fos);
                        
			document.open();
			document.newPage();

			/**
			 * Only adding one element to the document. The element contains the
			 * entire form (which is just a table).
			 */
			Element mainElement = mainElement();
			document.add(mainElement);
			
			document.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Builds and returns the main table.
	 * 
	 * @return
	 */
	private Element mainElement() throws IOException, SQLException {

		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(100f);
		table.getDefaultCell().setBorderWidth(0.5f);

		Element element;
		PdfPCell cell;
                
                element = this.fourthLogoElement();
		// You need to pass the image to the constructor.
		// If you add the image, it will be stretched to fill the cell.
		cell = new PdfPCell((Image)element);
		cell.setPadding(2);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_CENTER);
		//cell.setBorderWidth(0.3f);
		table.addCell(cell);
                
                element = this.dadosOficina();
		cell = this.createCell(1, 1);
		cell.addElement(element);
		table.addCell(cell);
                
		element = this.dadosCliente();
		cell = this.createCell(1, 1);
		cell.addElement(element);
		table.addCell(cell);

		// This is a nested table
		PdfPTable acctTable = (PdfPTable) this.dadosViatura();
		cell = new PdfPCell(acctTable);
		cell.setBorderWidth(0.3f);
		table.addCell(cell);

		element = this.seventhElement();
		cell = this.createCell(2, 1);
		cell.addElement(element);
		table.addCell(cell);

		PdfPTable wTable = (PdfPTable) this.eigthElement();
		cell = new PdfPCell(wTable);
		cell.setColspan(2);
		cell.setBorderWidth(0.3f);
		table.addCell(cell);

		element = this.ninethElement();
		cell = this.createCell(2, 1);
		cell.addElement(element);
		table.addCell(cell);

		return table;
	}

	/**
	 * Creates new cells
	 * 
	 * @param colspan
	 * @param rowspan
	 * @return
	 */
	private PdfPCell createCell(int colspan, int rowspan) {
		PdfPCell cell = new PdfPCell();
		//cell.setPadding(0);
		cell.setBorderWidth(0.3f);
		cell.setColspan(colspan);
		cell.setRowspan(rowspan);
		return cell;
	}



	private Element fourthLogoElement() {
		String path = "C:\\Users\\M2K\\Documents\\NetBeansProjects\\HelloWorldExample\\logo.jpg";
		Image image = null;
		try {
			image = Image.getInstance(getClass().getResource("/Icons/logo.jpg"));
		} catch (BadElementException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}

	private Element dadosCliente() throws SQLException {
             conexao();
             JifConfirmarRep viatura = new JifConfirmarRep();
             String nome = viatura.getProprietario(),bi="",morada="";
             int cont = 0;
             try{
            String query ="SELECT * FROM `clientes` WHERE `nome` LIKE '"+nome+"'";
            rs=st.executeQuery(query);
            if(rs.first()){
            cont = (rs.getInt("contacto"));
            bi=(rs.getString("bi"));
            morada = rs.getString("morada");
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Erro"+e);
        }
		float leading = 9f;
		PdfPTable table = new PdfPTable(1);
		table.setWidthPercentage(100f);
		PdfPCell cell;
		cell = new PdfPCell(new Paragraph("Exmo Sr(a)", arialSmall));
		//cell.setPadding(2);
		cell.setGrayFill(0.85f);
		cell.setBorderWidth(0.3f);
		table.addCell(cell);

		cell = new PdfPCell();
		cell.setBorderWidth(0.3f);
		cell.setFixedHeight(50f);
                Paragraph info = new Paragraph(""+nome+"\nBI:"+bi+"\nMorada:"+morada+"\nContacto:"+cont+"", courier);
		//Paragraph info = new Paragraph("AutoPolipo Lda\nAv do c700\nRua da Madeira 670\nautopolipo@gamil.com",courier);
		info.setLeading(leading);
		cell.addElement(info);
		table.addCell(cell);

		return table;
	}

	private Element ninethElement() {
		String legal = "A ENTREGA DA VIATURA SO SERA FEITA MEDIANTE A APRESENTACAO DESTA FACTURA(ORIGINAL)."
				+ " PROCESSADO POR COMPUTADOR";
				
		Paragraph element = new Paragraph(legal, this.arialMini);
		element.setAlignment(Element.ALIGN_CENTER);
		return element;
	}

	private Element dadosOficina() throws IOException, SQLException {
           
             
		float leading = 9f;

		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(100f);
		PdfPCell cell;

		cell = new PdfPCell(new Paragraph("Oficina",
				arialSmall));
		cell.setGrayFill(0.85f);
		cell.setBorderWidth(0.3f);
		cell.setColspan(2);
		table.addCell(cell);

		cell = new PdfPCell();
		cell.setBorderWidth(0.6f);
		cell.setFixedHeight(60f);
		cell.setColspan(2);
                Paragraph agentInfo = new Paragraph("AutoPolipo Lda\nAv do c700\nRua da Madeira 670\nautopolipo@gmail.com",courier);
		//Paragraph agentInfo = new Paragraph(
				//""+nome+"\nBI:"+bi+"\nMorada:"+morada+"\nContacto:"+cont+"", courier);
		agentInfo.setLeading(leading);
		cell.addElement(agentInfo);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("Banco", arialSmall));
		cell.setPadding(2);
		cell.setGrayFill(0.85f);
		cell.setBorderWidth(0.3f);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("Conta e NIB", arialSmall));
		cell.setPadding(2);
		cell.setGrayFill(0.85f);
		cell.setBorderWidth(0.3f);
		table.addCell(cell);

		cell = new PdfPCell();
		cell.setPadding(2);
		cell.setBorderWidth(0.3f);
		Paragraph iataInfo = new Paragraph("Banco Comercial e de Investimentos", courier);
		iataInfo.setLeading(leading);
		cell.addElement(iataInfo);
		table.addCell(cell);

		cell = new PdfPCell();
		//cell.setPadding(2);
		cell.setBorderWidth(0.3f);
		Paragraph acctInfo = new Paragraph("93316329\n 0008000093311632910180", courier);
		acctInfo.setLeading(leading);
		cell.addElement(acctInfo);
		table.addCell(cell);

		return table;
	}

	private Element dadosViatura() throws SQLException {
               conexao();
             JifConfirmarRep viatura = new JifConfirmarRep();
             String matricula = viatura.getMatricula(),marca="",modelo="";
            
             try{
            String query ="SELECT * FROM `oficina` WHERE `matricula` LIKE '"+matricula+"'";
            rs=st.executeQuery(query);
            if(rs.first()){
            marca = (rs.getString("marca"));
            modelo=(rs.getString("modelo"));
            
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Erro"+e);
        }
		float leading = 9f;
		PdfPTable table = new PdfPTable(1);
		table.setWidthPercentage(100f);
		PdfPCell cell;
		cell = new PdfPCell(new Paragraph("Dados da Viatura", arialSmall));
		//cell.setPadding(2);
		cell.setGrayFill(0.85f);
		cell.setBorderWidth(0.3f);
		table.addCell(cell);

		cell = new PdfPCell();
		cell.setBorderWidth(0.3f);
		Paragraph info = new Paragraph(
				""+marca+"\n"+modelo+"\n"+matricula+"",
				courier);
		info.setLeading(leading);
		cell.addElement(info);
		table.addCell(cell);

		return table;
	}


	private Element seventhElement() {
		float leading = 9f;

		PdfPTable table = new PdfPTable(1);
		table.setWidthPercentage(100f);
		PdfPCell cell;

		cell = new PdfPCell(new Paragraph("Detalhes", arialSmall));
		cell.setPadding(2);
		//cell.setBorderWidth(0);
		table.addCell(cell);

		cell = new PdfPCell();
		cell.setPadding(2);
		cell.setBorderWidth(0);
		cell.setFixedHeight(36f);
		Paragraph info = new Paragraph(
				"Factura referente a reparacao Electrica", courier);
		info.setLeading(leading);
		cell.addElement(info);
		table.addCell(cell);

		return table;
	}

	private Element eigthElement() throws SQLException {
            JifConfirmarRep acess = new JifConfirmarRep();
            int nr=0;
		float[] widths = { 3f, 25f, 6f, 6f, 9f };
		PdfPTable table = new PdfPTable(widths);
		table.setWidthPercentage(100f);

		PdfPCell cell;

		cell = new PdfPCell(new Paragraph(" # ", arialSmall));
		cell.setPadding(2);
		cell.setBorderWidth(0.3f);
		cell.setGrayFill(0.85f);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("Descriçao", arialSmall));
		cell.setPadding(2);
		cell.setBorderWidth(0.3f);
		cell.setGrayFill(0.85f);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("Quantidade", arialSmall));
		cell.setPadding(2);
		cell.setBorderWidth(0.3f);
		cell.setGrayFill(0.85f);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("Preco unitario(MT)", arialSmall));
		cell.setPadding(2);
		cell.setBorderWidth(0.3f);
		cell.setGrayFill(0.85f);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("Total(MT)", arialSmall));
		cell.setPadding(2);
		cell.setBorderWidth(0.3f);
		cell.setGrayFill(0.85f);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);
                                        try{
            String query ="SELECT * FROM `aux`";
            rs=st.executeQuery(query);
            while(rs.next()){
            nr=nr+1;
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Erro"+e);
        }           String numeros="";
                  for(int i=1;i<=nr;i++){
            numeros=numeros+i+"\n";
        }
		cell = new PdfPCell(new Paragraph("numeros", this.courier));
		cell.setPadding(2);
		cell.setFixedHeight(350);
		cell.setBorderWidth(0.3f);
		cell.setVerticalAlignment(Element.ALIGN_TOP);
		table.addCell(cell);
                
                float leading = 9f;
                
		
		 conexao();
            // JifConfirmarRep viatura = new JifConfirmarRep();
             //String matricula = viatura.getMatricula(),marca="",modelo="";
            
            
                   
                
		Paragraph info = new Paragraph();
		info.setLeading(leading);
               // for (int i =0;i<acess.getAcessorios().toArray().length;i++){
                    
                     try{
            String query ="SELECT * FROM `aux`";
            rs=st.executeQuery(query);
            while(rs.next()){
            Phrase description = new Phrase(""+rs.getString("acessorio")+"\n",
				courier);
                    info.add(description);
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Erro"+e);
        }
      
		cell = new PdfPCell(info);
		cell.setPadding(2);
		cell.setFixedHeight(350);
		cell.setBorderWidth(0.3f);
		cell.setVerticalAlignment(Element.ALIGN_TOP);
		table.addCell(cell);
                
                ArrayList quants = new ArrayList();
                                   try{
            String query ="SELECT * FROM `aux`";
            rs=st.executeQuery(query);
            while(rs.next()){
            quants.add(rs.getInt("quant"));
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Erro"+e);
        }       String quantss="";
            for(int i=0;i<quants.size();i++){
             quantss =quantss+quants.toArray()[i].toString()+"\n";
        }
                
		cell = new PdfPCell(new Paragraph(""+quantss+"", this.courier));
		cell.setPadding(2);
		cell.setFixedHeight(350);
		cell.setBorderWidth(0.3f);
		cell.setVerticalAlignment(Element.ALIGN_TOP);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);
                
          ArrayList precos = new ArrayList();
                                   try{
            String query ="SELECT * FROM `aux`";
            rs=st.executeQuery(query);
            while(rs.next()){
            precos.add(rs.getInt("preco"));
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Erro"+e);
        }  String precoss="";
            for(int i=0;i<precos.size();i++){
             precoss =precoss+precos.toArray()[i].toString()+"\n";
        }
		cell = new PdfPCell(new Paragraph(""+precoss+"", this.courier));
		cell.setPadding(2);
		cell.setFixedHeight(350);
		cell.setBorderWidth(0.3f);
		cell.setVerticalAlignment(Element.ALIGN_TOP);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table.addCell(cell);
                
          ArrayList pagars = new ArrayList();
                                   try{
            String query ="SELECT * FROM `aux`";
            rs=st.executeQuery(query);
            while(rs.next()){
            pagars.add(rs.getInt("pagar"));
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Erro"+e);
        }  String pagarss="";
            for(int i=0;i<pagars.size();i++){
             pagarss =pagarss+pagars.toArray()[i].toString()+"\n";
        }
                
		cell = new PdfPCell(new Paragraph(""+pagarss+"", this.courier));
		cell.setPadding(2);
		cell.setFixedHeight(350);
		cell.setBorderWidth(0.3f);
		cell.setVerticalAlignment(Element.ALIGN_TOP);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);

		cell = new PdfPCell();
		cell.setBorderWidth(0.3f);
		cell.setGrayFill(0.85f);
		table.addCell(cell);

		cell = new PdfPCell();
		cell.setBorderWidth(0.3f);
		cell.setGrayFill(0.85f);
		table.addCell(cell);

		cell = new PdfPCell();
		cell.setBorderWidth(0.3f);
		cell.setGrayFill(0.85f);
		table.addCell(cell);
		
                cell = new PdfPCell();
		cell.setBorderWidth(0.3f);
		cell.setGrayFill(0.85f);
		table.addCell(cell);
                
          
          JifConfirmarRep viatura = new JifConfirmarRep();
             String matricula = viatura.getMatricula();
              int total=0;
          try{
            String query ="SELECT * FROM `arquivo` WHERE `matricula` LIKE '"+matricula+"'";
            rs=st.executeQuery(query);
            if(rs.first()){
            total = (rs.getInt("preco"));
          
            
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Erro"+e);
        }
            
		cell = new PdfPCell(new Paragraph(""+total+"",
				this.courier));
		cell.setPadding(2);
		cell.setBorderWidth(0.3f);
		table.addCell(cell);
		return table;
	}

   
           
   
}