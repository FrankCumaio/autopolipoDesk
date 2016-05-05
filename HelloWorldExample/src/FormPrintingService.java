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
import sun.applet.Main;

public class FormPrintingService {
	private static FormPrintingService instance;
	private Font arial;
	private Font arialSmall;
	private Font arialMini;
	private Font courier;

	/**
	 * Private constructor, since this is a singleton. Fonts are initialized in
	 * the constructor.
	 */
	private FormPrintingService() {
		arial = FontFactory.getFont("Arial", 8);
		arial.setStyle(Font.BOLD);
		arialSmall = FontFactory.getFont("Arial", 6);
		arialSmall.setStyle(Font.BOLD);
		courier = FontFactory.getFont("Courier", 10);
		arialMini = FontFactory.getFont("Arial", 5);
	}

	/**
	 * Provides an access to the service instance
	 * 
	 * @return
	 */
	public static FormPrintingService getInstance() {
		if (instance == null) {
			instance = new FormPrintingService();
		}
		return instance;
	}

	/**
	 * Creates the PDF document.
	 * 
	 * @param domainObject
	 */
	public void createDocument(Object domainObject) {
		/**
		 * Assign the domainObject to an instance variable so that it can be
		 * used to populate the fields in the form
		 */

		try {
			Document document = new Document(PageSize.LETTER);
			document.setMargins(36f, 36f, 36f, 36f);
			FileOutputStream fos;
			fos = new FileOutputStream("C:\\Users\\M2K\\Documents\\NetBeansProjects\\HelloWorldExample\\SampleForm.pdf");
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
	private Element mainElement() {

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
		cell.setBorderWidth(0.3f);
		table.addCell(cell);
                
		element = this.thirdElement();
		cell = this.createCell(1, 1);
		cell.addElement(element);
		table.addCell(cell);

		

		element = this.fifthElement();
		cell = this.createCell(1, 1);
		cell.addElement(element);
		table.addCell(cell);

		// This is a nested table
		PdfPTable acctTable = (PdfPTable) this.sixthElement();
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
		cell.setPadding(0);
		cell.setBorderWidth(0.3f);
		cell.setColspan(colspan);
		cell.setRowspan(rowspan);
		return cell;
	}



	private Element fourthLogoElement() {
		String path = "C:\\Users\\M2K\\Documents\\NetBeansProjects\\HelloWorldExample\\logo.jpg";
		Image image = null;
		try {
			image = Image.getInstance(path);
		} catch (BadElementException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}

	private Element thirdElement() {
		float leading = 9f;
		PdfPTable table = new PdfPTable(1);
		table.setWidthPercentage(100f);
		PdfPCell cell;
		cell = new PdfPCell(new Paragraph("Oficina", arialSmall));
		cell.setPadding(2);
		cell.setGrayFill(0.85f);
		cell.setBorderWidth(0.3f);
		table.addCell(cell);

		cell = new PdfPCell();
		cell.setBorderWidth(0.3f);
		cell.setFixedHeight(72f);
		Paragraph info = new Paragraph("AutoPolipo Lda\nAv do c700\nRua da Madeira 670\nautopolipo@gamil.com",courier);
		info.setLeading(leading);
		cell.addElement(info);
		table.addCell(cell);

		return table;
	}

	private Element ninethElement() {
		String legal = "Legal Statement: Blah blah blah blah blah blah blah blah blah blah blah blah blah."
				+ "  Blah blah blah blah blah blah blah blah blah blah blah blah blah."
				+ "  Blah blah blah blah blah blah blah blah blah blah blah blah blah."
				+ "  Blah blah blah blah blah blah blah blah blah blah blah blah blah."
				+ "  Blah blah blah blah blah blah blah blah blah blah blah blah blah."
				+ "  Blah blah blah blah blah blah blah blah blah blah blah blah blah."
				+ "  Blah blah blah blah blah blah blah blah blah blah blah blah blah."
				+ "  Blah blah blah blah blah blah blah blah blah blah blah blah blah."
				+ "  Blah blah blah blah blah blah blah blah blah blah blah blah blah."
				+ "  Blah blah blah blah blah blah blah blah blah blah blah blah blah."
				+ "  Blah blah blah blah blah blah blah blah blah blah blah blah blah."
				+ "  Blah blah blah blah blah blah blah blah blah blah blah blah blah."
				+ "  Blah blah blah blah blah blah blah blah blah blah blah blah blah."
				+ "  Blah blah blah blah blah blah blah blah blah blah blah blah blah."
				+ "  Blah blah blah blah blah blah blah blah blah blah blah blah blah."
				+ "  Blah blah blah blah blah blah blah blah blah blah blah blah blah.";
		Paragraph element = new Paragraph(legal, this.arialMini);
		element.setAlignment(Element.ALIGN_JUSTIFIED);
		return element;
	}

	private Element fifthElement() {
             Viatura viatura = new Viatura();
		float leading = 9f;

		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(100f);
		PdfPCell cell;

		cell = new PdfPCell(new Paragraph("Exmo Sr(a)",
				arialSmall));
		cell.setGrayFill(0.85f);
		cell.setBorderWidth(0.3f);
		cell.setColspan(2);
		table.addCell(cell);

		cell = new PdfPCell();
		cell.setBorderWidth(0.3f);
		cell.setFixedHeight(36f);
		cell.setColspan(2);
		Paragraph agentInfo = new Paragraph(
				"Frank Salvador Cumaio\nNumero do BI\n842562111", courier);
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
		cell.setPadding(2);
		cell.setBorderWidth(0.3f);
		Paragraph acctInfo = new Paragraph("93316329\n 0008000093311632910180", courier);
		acctInfo.setLeading(leading);
		cell.addElement(acctInfo);
		table.addCell(cell);

		return table;
	}

	private Element sixthElement() {
		float leading = 9f;
		PdfPTable table = new PdfPTable(1);
		table.setWidthPercentage(100f);
		PdfPCell cell;
		cell = new PdfPCell(new Paragraph("Dados da Viatura", arialSmall));
		cell.setPadding(2);
		cell.setGrayFill(0.85f);
		cell.setBorderWidth(0.3f);
		table.addCell(cell);

		cell = new PdfPCell();
		cell.setBorderWidth(0.3f);
		Paragraph info = new Paragraph(
				"Toyota\nAvensis\nAel 519 MC",
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
		cell.setBorderWidth(0);
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

	private Element eigthElement() {
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

		cell = new PdfPCell(new Paragraph("Preco unitario", arialSmall));
		cell.setPadding(2);
		cell.setBorderWidth(0.3f);
		cell.setGrayFill(0.85f);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("Total", arialSmall));
		cell.setPadding(2);
		cell.setBorderWidth(0.3f);
		cell.setGrayFill(0.85f);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("999", this.courier));
		cell.setPadding(2);
		cell.setFixedHeight(126);
		cell.setBorderWidth(0.3f);
		cell.setVerticalAlignment(Element.ALIGN_TOP);
		table.addCell(cell);
                
                float leading = 9f;
		Phrase description = new Phrase("INDUSTRIAL MACHINERY",
				courier);
		Phrase newLine = new Phrase("\n", courier);
		Phrase dims = new Phrase(
				"99@110x155x45 (Carton), 80@50x50x50 (Drum), 10@15x55x55 (Pallet), 3@100x100x80 (Crate)",
				courier);

		Paragraph info = new Paragraph();
		info.setLeading(leading);
		info.add(description);
		info.add(newLine);
		info.add(dims);

		cell = new PdfPCell(info);
		cell.setPadding(2);
		cell.setFixedHeight(126);
		cell.setBorderWidth(0.3f);
		cell.setVerticalAlignment(Element.ALIGN_TOP);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("9999", this.courier));
		cell.setPadding(2);
		cell.setFixedHeight(126);
		cell.setBorderWidth(0.3f);
		cell.setVerticalAlignment(Element.ALIGN_TOP);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("10125", this.courier));
		cell.setPadding(2);
		cell.setFixedHeight(126);
		cell.setBorderWidth(0.3f);
		cell.setVerticalAlignment(Element.ALIGN_TOP);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("99999.99 USD", this.courier));
		cell.setPadding(2);
		cell.setFixedHeight(126);
		cell.setBorderWidth(0.3f);
		cell.setVerticalAlignment(Element.ALIGN_TOP);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
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

		cell = new PdfPCell(new Paragraph("Total aqui",
				this.courier));
		cell.setPadding(2);
		cell.setBorderWidth(0.3f);
		table.addCell(cell);
		return table;
	}

    public static void main(String[] args) {
            FormPrintingService formPrintingService = new FormPrintingService();
            formPrintingService.createDocument(instance);
    }
}