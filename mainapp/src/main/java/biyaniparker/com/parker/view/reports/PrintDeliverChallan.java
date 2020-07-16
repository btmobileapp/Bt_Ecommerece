package biyaniparker.com.parker.view.reports;

import android.app.Activity;
import android.os.Environment;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import biyaniparker.com.parker.beans.BagMasterBean;
import biyaniparker.com.parker.beans.DispatchMasterAndDetails;
import biyaniparker.com.parker.utilities.CommonUtilities;


/**
 * Created by bt18 on 09/22/2016.
 */
public class PrintDeliverChallan
{
    Activity context;
    ImageLoader imageLoader;
    private final File pdffile;
    private File imfo;

    DispatchMasterAndDetails source;

    public PrintDeliverChallan(Activity context, ImageLoader imageLoader)
    {
        this.context=context;

        pdffile = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), "deliverychallan.pdf");
        imfo = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), "l1.jpg");
        this.imageLoader=imageLoader;
       /*
        moduleProduct=new ModuleProduct(context);
        moduleCategory=new ModuleCategory(context);
        */

    }

    public  void setDataSet(DispatchMasterAndDetails source)
    {
        this.source=source;
    }

    public void call()
    {
        try
        {
             createChallanPdf  (pdffile.getAbsolutePath().toString());
        }
        catch (Exception e){}
    }
    public static PdfPCell createImageCell(String path) throws DocumentException, IOException
    {
        Image img = Image.getInstance(path);
        img.setAlignment(Element.ALIGN_CENTER);
        PdfPCell cell = new PdfPCell(img, true);
        cell.setFixedHeight(5);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        return cell;
    }
    class HeaderFooter extends PdfPageEventHelper
    {
        public void onEndPage(PdfWriter writer, Document document)
        {
            try {
                Rectangle rect = writer.getBoxSize("bleed");
                if(document.getPageNumber()!=0)
                    ColumnText.showTextAligned(writer.getDirectContent(),
                            Element.ALIGN_LEFT, new Phrase(document.getPageNumber()+""),
                            rect.getLeft() , rect.getTop(), 0);
                ColumnText.showTextAligned(writer.getDirectContent(),
                        Element.ALIGN_CENTER, new Phrase("|| Shree Babaji Maharaj Namah ||"),
                        (rect.getLeft() + rect.getRight()) / 2, rect.getTop(), 0);
                        Phrase phrase3 = new Phrase("MAHALAXMI APPARELS", FontFactory.getFont(FontFactory.COURIER, 20, Font.BOLD, new BaseColor(1, 1, 1)));
                ColumnText.showTextAligned(writer.getDirectContent(),
                        Element.ALIGN_CENTER,     //new Phrase("MAHALAXMI APPARELS")
                        phrase3,
                        (rect.getLeft() + rect.getRight()) / 2, rect.getTop()-30, 0);
                ColumnText.showTextAligned(writer.getDirectContent(),
                        Element.ALIGN_CENTER, new Phrase("BLOCK NO.443/3C,GAT NO 24/3A, OPP. HOTEL SAPTAGIRI,JAYSINGPUR-416101"),
                        (rect.getLeft() + rect.getRight()) / 2, rect.getTop()-60, 0);
                ColumnText.showTextAligned(writer.getDirectContent(),
                        Element.ALIGN_RIGHT, new Phrase("Ph No. (02322)224744 "),
                        (rect.getRight()) , rect.getTop(), 0);
                try
                {
                    Image image = Image.getInstance(imfo.getAbsolutePath());
                    image .scaleToFit(80, 80);
                    PdfContentByte canvas = writer.getDirectContentUnder();
                    image.setAbsolutePosition(rect.getLeft(), rect.getTop()-50);
                    canvas.addImage(image);
                }
                catch (Exception e){
                   // Toast.makeText(context, "Image Exception" + e.toString(), Toast.LENGTH_LONG).show();
                }
            }
            catch (Exception e)
            {
               // Toast.makeText(context,e.toString(),Toast.LENGTH_LONG).show();
            }
        }
    }



    public void createChallanPdf(String dest) throws IOException, DocumentException
    {
        Document document = new //Document();
                Document   (PageSize.A4, 0, 0, 0, 0);
        // document.getPageSize().getHeight()
        // Rectangle two = new Rectangle(1400,1400);
        //document.setPageSize(two);
        document.setMargins(50, 50, 100, 50);
        PdfWriter writer=   PdfWriter.getInstance(document, new FileOutputStream(dest));
        // bleed is box which will used in PageEvent Class
        writer.setBoxSize("bleed", new Rectangle(30, 30,   //document.getPageSize().getWidth()-18,document.getPageSize().getHeight()-18));
                PageSize.A4.getWidth()-18, PageSize.A4.getHeight()-18));
        //Rectangle(30, 30, 0, 600));
        try {
            writer.setPageEvent(new HeaderFooter());
        }
        catch (Exception e){
           // Toast.makeText(context,"Headr prble"+e.getMessage()+"-"+e.toString(),Toast.LENGTH_LONG).show();
        }


        document.open();



        Chunk linebreak = new Chunk(new DottedLineSeparator());
        Paragraph paragraph1 = new Paragraph("\n");

      /*
       document.add(paragraph1);
        document.add(paragraph1);
        document.add(paragraph1);
        document.add(paragraph1);
        document.add(paragraph1);
        document.add(paragraph1);

        document.add(paragraph1);
        document.add(paragraph1);
        document.add(paragraph1);
        */


        document.add(linebreak);

        document.add(paragraph1);
        document.add(paragraph1);
        document.add(paragraph1);
        document.add(paragraph1);
        document.add(paragraph1);





        Rectangle rect = writer.getBoxSize("bleed");

        ColumnText.showTextAligned(writer.getDirectContent(),
                Element.ALIGN_CENTER, new Phrase("DELIVERY CHALLAN"),
                ((rect.getLeft()+rect.getRight())/2), rect.getTop() - 100, 0);



        ColumnText.showTextAligned(writer.getDirectContent(),
                Element.ALIGN_LEFT, new Phrase("Challan No : "+source.master.getDispatchId()),
                (rect.getLeft()), rect.getTop() - 115, 0);

        ColumnText.showTextAligned(writer.getDirectContent(),
                Element.ALIGN_RIGHT, new Phrase("Order No : "+source.master.getOrderId()),
                (rect.getRight())-50, rect.getTop() -115, 0);

        ColumnText.showTextAligned(writer.getDirectContent(),
                Element.ALIGN_LEFT, new Phrase("Challan Date : "),
                (rect.getLeft() ) , rect.getTop() - 135, 0);

        ColumnText.showTextAligned(writer.getDirectContent(),
                Element.ALIGN_RIGHT, new Phrase("Order Date : "+ CommonUtilities.longToDate(source.master.getOrderDate())),
                (rect.getRight()-50 ) , rect.getTop() - 135, 0);

        ColumnText.showTextAligned(writer.getDirectContent(),
                Element.ALIGN_LEFT, new Phrase("Shop Name : "+source.master.getShopName()),
                (rect.getLeft() ) , rect.getTop() - 155, 0);

        ColumnText.showTextAligned(writer.getDirectContent(),
                Element.ALIGN_RIGHT, new Phrase("City : "+source.master.getAddress()),
                (rect.getRight()+50 ) , rect.getTop() - 155, 0);





        PdfPTable table = new PdfPTable(4);

        table.setSpacingBefore(100f);

        table.setWidthPercentage(110);
        table.setSpacingBefore(2f);
        table.setSpacingAfter(2f);

        // first row
        PdfPCell cell = new PdfPCell(new Phrase("DateRange"));
        cell.setColspan(4);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5.0f);
        cell.setBackgroundColor(new BaseColor(140, 221, 8));
        table.addCell(cell);

        table.addCell("Product Category");
        table.addCell("MRP");
        table.addCell("Size");
        table.addCell("Qnty");


        /*

        for (int i = 0; i < gsonSelectedItem.masterBeans.size(); i++)
        {

            // createImageCell(image.getAbsolutePath());
            BagMasterBean master= gsonSelectedItem.masterBeans.get(i);

            for(int j=0;j<master.bagDetails.size();j++)
            {
                int k=0;
                while(k<15) {
                    File file = imageLoader.getDiscCache().get(master.bagDetails.get(j).iconThmub);
                    PdfPCell cell1 = createImageCell(file.getAbsolutePath());
                    table.addCell(cell1);

                    int cid = moduleProduct.getProductBeanByProductId(master.productId).getCategoryId();
                    String CName = moduleCategory.getCategoryName(cid);
                    PdfPCell c = new PdfPCell(new Phrase(CName));
                    c.setPadding(10);
                    table.addCell(c);
                    table.addCell(master.bagDetails.get(j).productName + "");
                    table.addCell(master.bagDetails.get(j).cPrice + "");
                    table.addCell(master.bagDetails.get(j).sizeName);
                    table.addCell(master.bagDetails.get(j).selQnty + "");
                    k++;
                }
            }
        }
        */
        document.add(table);




        document.close();
    }
}
