package biyaniparker.com.parker.view.reports;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Environment;

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
import java.util.ArrayList;

import biyaniparker.com.parker.beans.DispatchMasterAndDetails;
import biyaniparker.com.parker.beans.ReportProductStockBean;
import biyaniparker.com.parker.utilities.CommonUtilities;
import biyaniparker.com.parker.utilities.DateAndOther;


/**
 * Created by bt18 on 09/22/2016.
 */
public class PrintStock
{
    Activity context;
    ImageLoader imageLoader;
    ArrayList<ReportProductStockBean> stockList;
    private final File pdffile;
    private File imfo;
    String title,fileName;

    DispatchMasterAndDetails source;

    public PrintStock(Activity context, ImageLoader imageLoader, ArrayList<ReportProductStockBean> stockList, int catId, String fileName)
    {
        this.context=context;
        this.fileName=fileName;
        pdffile = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).toString()+"/parkerreport", this.fileName+"_"+catId+".pdf");

       /*
        imfo = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), "l1.jpg");
                */
        this.imageLoader=imageLoader;

        this.stockList=stockList;
       /*
        moduleProduct=new ModuleProduct(context);
        moduleCategory=new ModuleCategory(context);
        */
        try
        {
            imfo = imageLoader.getDiscCache().get(CommonUtilities.URL+"l1.jpg");
        }
        catch (Exception e){}

    }

    public void setFileNameAndTitle(String title, String fileName)
    {
        this.title=title;
        this.fileName=fileName;
    }

    public  void setDataSet(DispatchMasterAndDetails source)
    {
        this.source=source;
    }

    public void call()
    {
        try
        {
             createStockPdf(pdffile.getAbsolutePath().toString());

            AlertDialog.Builder alBuilder=new AlertDialog.Builder(context);
            alBuilder.setTitle(""+title);
            alBuilder.setMessage("\n\n" + "Stock Reports In Location " + pdffile.getAbsolutePath() + "\n\n");
            alBuilder.setPositiveButton("Open Pdf", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    CommonUtilities.openPdf(context, pdffile.getAbsolutePath());
                }
            });
            alBuilder.setNegativeButton("Cancel",null);
            alBuilder.show();
        }
        catch (Exception e)
        {
            String str=e.toString();
            CommonUtilities.alert(context,str);
        }
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
                if(document.getPageNumber()!=1)
                    ColumnText.showTextAligned(writer.getDirectContent(),
                            Element.ALIGN_LEFT, new Phrase(document.getPageNumber()+""),
                            rect.getLeft() , rect.getTop(), 0);

                ColumnText.showTextAligned(writer.getDirectContent(),
                        Element.ALIGN_CENTER, new Phrase(CommonUtilities.GodName),
                        (rect.getLeft() + rect.getRight()) / 2, rect.getTop(), 0);
                        Phrase phrase3 = new Phrase(CommonUtilities.AdminShop, FontFactory.getFont(FontFactory.COURIER, 20, Font.BOLD, new BaseColor(1, 1, 1)));
                ColumnText.showTextAligned(writer.getDirectContent(),
                        Element.ALIGN_CENTER,     //new Phrase("MAHALAXMI APPARELS")
                        phrase3,
                        (rect.getLeft() + rect.getRight()) / 2, rect.getTop()-30, 0);
                ColumnText.showTextAligned(writer.getDirectContent(),
                        Element.ALIGN_CENTER, new Phrase(CommonUtilities.AdminAdress),
                        (rect.getLeft() + rect.getRight()) / 2, rect.getTop()-60, 0);
                ColumnText.showTextAligned(writer.getDirectContent(),
                        Element.ALIGN_RIGHT, new Phrase(CommonUtilities.AdminContact),
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



    public void createStockPdf(String dest) throws IOException, DocumentException
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




        document.add(linebreak);

        document.add(paragraph1);
        document.add(paragraph1);





        Rectangle rect = writer.getBoxSize("bleed");

        ColumnText.showTextAligned(writer.getDirectContent(),
                Element.ALIGN_CENTER, new Phrase(""+title),
                ((rect.getLeft()+rect.getRight())/2), rect.getTop() - 100, 0);





        ColumnText.showTextAligned(writer.getDirectContent(),
                Element.ALIGN_RIGHT, new Phrase("Date : "+ DateAndOther.currentDate()),
                (rect.getRight())-50, rect.getTop() -100, 0);




        PdfPTable table = new PdfPTable(6);

        table.setSpacingBefore(100f);

        table.setWidthPercentage(110);
        table.setSpacingBefore(2f);
        table.setSpacingAfter(2f);

        // first row
        /*
        PdfPCell cell = new PdfPCell(new Phrase("DateRange"));
        cell.setColspan(6);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5.0f);
        cell.setBackgroundColor(new BaseColor(140, 221, 8));
        table.addCell(cell);

        */

        table.addCell("Design");
        table.addCell("Category");
        table.addCell("Product");
        table.addCell("MRP");
        table.addCell("Size");
        table.addCell("Qnty");


        int tot=0;

        for (int i = 0; i < stockList.size(); i++) {

            // createImageCell(image.getAbsolutePath());
            ReportProductStockBean reportBean = stockList.get(i);

           /* for(int j=0;j<master.bagDetails.size();j++)
            {
                int k=0;
                while(k<15) {*/

            try {
                File file = imageLoader.getDiscCache().get(reportBean.IconThumb);
                PdfPCell cell1 = createImageCell(file.getAbsolutePath());
                table.addCell(cell1);
            } catch (Exception e)
            {
                table.addCell("  ");
                e.printStackTrace();
            }



                    //int cid = moduleProduct.getProductBeanByProductId(master.productId).getCategoryId();
                    String CName =reportBean.getCategoryName();
                    PdfPCell c = new PdfPCell(new Phrase(CName));
                    c.setPadding(10);
                    table.addCell(c);
                    table.addCell(reportBean.getProductName()+ "");
                    table.addCell(reportBean.getConsumerPrice() + "");
                    table.addCell(reportBean.getSizeName());
                    table.addCell(reportBean.getQnty()+"");
                    tot=tot+reportBean.getQnty();

               // }
            //}
        }

        table.addCell("Total");
        table.addCell("");
        table.addCell("");
        table.addCell("");
        table.addCell("");
        table.addCell(" "+tot);
        document.add(table);


        document.close();
       // CommonUtilities.alert(context, "Report Saved to : " + pdffile.getAbsolutePath());

    }
}
