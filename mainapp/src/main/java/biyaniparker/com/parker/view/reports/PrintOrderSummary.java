package biyaniparker.com.parker.view.reports;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Environment;
import android.util.Log;
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import biyaniparker.com.parker.bal.ModuleCategory;
import biyaniparker.com.parker.bal.ModuleProduct;
import biyaniparker.com.parker.beans.BagMasterBean;
import biyaniparker.com.parker.beans.GsonSelectedItem;
import biyaniparker.com.parker.beans.OrderDetailBean;
import biyaniparker.com.parker.beans.OrderMasterBean;
import biyaniparker.com.parker.utilities.CommonUtilities;

/**
 * Created by bt18 on 09/21/2016.
 */
public class PrintOrderSummary
{

    /*


                PrintUserOrder order=new PrintUserOrder(context,imageLoader,orderDetails,orderDetailsNew);
                order.setTotal(((int)total)+"");
                order.setTotalQnt((bean.getTotalQnty()) + "");
                order.setOrderId(bean.getOrderId()+"");

                order.GenerateUserOrderPDF();



     */



    private  File pdffile;
    private final ModuleProduct moduleProduct;
    ModuleCategory moduleCategory;
    Activity context;
    private File imfo;
    ImageLoader imageLoader;

   public  ArrayList<OrderDetailBean> orderDetails;
    public OrderMasterBean master;

    public PrintOrderSummary(Activity context, ImageLoader imageLoader)
    {
        this.context=context;



        this.imageLoader=imageLoader;

        try
        {
            imfo = imageLoader.getDiscCache().get(CommonUtilities.URL+"l1.jpg");
        }
        catch (Exception e){}


        moduleProduct=new ModuleProduct(context);
        moduleCategory=new ModuleCategory(context);



    }

    public void  call()
    {
        try
        {
            pdffile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString()+"/parkerreport", "Order_"+master.orderId+".pdf");
            try {
                createPdf(pdffile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            context.runOnUiThread(new Runnable() {
                @Override
                public void run()
                {

                    AlertDialog.Builder alBuilder=new AlertDialog.Builder(context);
                    alBuilder.setTitle("Order Summary");
                    alBuilder.setMessage("\n\n" + "File Generted In Location " + pdffile.getAbsolutePath() + "\n\n");
                    alBuilder.setPositiveButton("Open Pdf", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            CommonUtilities.openPdf(context, pdffile.getAbsolutePath());
                        }
                    });
                    alBuilder.setNegativeButton("Cancel",null);
                    alBuilder.show();
                }
            });


        }
        catch (Exception e)
        {

            //CommonUtilities.alert(context,e.toString());
        }
    }



    public static PdfPCell createImageCell(String path) throws DocumentException, IOException {
        Image img = Image.getInstance(path);
        //   img.scaleToFit(0.1f,0.1f);

        // Rectangle two = new Rectangle(5,5);
        //img.  scaleToFit(two);
        img.setAlignment(Element.ALIGN_CENTER);
        PdfPCell cell = new PdfPCell(img, true);
        cell.setFixedHeight(5);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        //  cell.a
        return cell;
    }





    class HeaderFooter extends PdfPageEventHelper
    {
      public void onEndPage(PdfWriter writer, Document document)
      {
        try {
            Rectangle rect =
                    //document.getPageSize();

                    writer.getBoxSize("bleed");
            //"art");
            if(document.getPageNumber()!=1)
                ColumnText.showTextAligned(writer.getDirectContent(),
                        Element.ALIGN_LEFT, new Phrase(document.getPageNumber()+""),
                        rect.getLeft() , rect.getTop(), 0);
            ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_CENTER, new Phrase(CommonUtilities.GodName),
                    (rect.getLeft() + rect.getRight()) / 2, rect.getTop(), 0);


            Phrase phrase3 = new Phrase(CommonUtilities.AdminShop, FontFactory.getFont(FontFactory.COURIER, 20, Font.BOLD, new BaseColor(1,1,1)));


            ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_CENTER,     //new Phrase("MAHALAXMI APPARELS")
                    phrase3,
                    (rect.getLeft() + rect.getRight()) / 2, rect.getTop()-30, 0);
            ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_CENTER, new Phrase(CommonUtilities.AdminAdress),
                    (rect.getLeft() + rect.getRight()) / 2, rect.getTop()-60, 0);
           /* ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_CENTER, new Phrase("Dilip"),
                    //   Phrase(String.format("page %d", )),
                    (rect.getLeft() + rect.getRight()) / 2, rect.getBottom() - 18, 0);
                    */

            ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_RIGHT, new Phrase(CommonUtilities.AdminContact),
                    (rect.getRight()) , rect.getTop(), 0);

            try
            {

                Image image = Image.getInstance(imfo.getAbsolutePath());
                image .scaleToFit(80, 80);
                //image.set
                PdfContentByte canvas = writer.getDirectContentUnder();
                image.setAbsolutePosition(rect.getLeft(), rect.getTop()-50);
                canvas.addImage(image);
            }
            catch (Exception e){
                Toast.makeText(context, "Image Exception" + e.toString(), Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e)
        {

            Toast.makeText(context,e.toString(),Toast.LENGTH_LONG).show();
        }
    }
    }





    public void createPdf(String dest) throws IOException, DocumentException
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
            Toast.makeText(context,"Headr prble"+e.getMessage()+"-"+e.toString(),Toast.LENGTH_LONG).show();
        }


        document.open();



        Chunk linebreak = new Chunk(new DottedLineSeparator());
        Paragraph paragraph1 = new Paragraph("\n");



        document.add(linebreak);

        document.add(paragraph1);
        document.add(paragraph1);
        document.add(paragraph1);
        document.add(paragraph1);




        Rectangle rect = writer.getBoxSize("bleed");

        ColumnText.showTextAligned(writer.getDirectContent(),
                Element.ALIGN_CENTER, new Phrase("Order Summary"),
                ((rect.getLeft()+rect.getRight())/2 ) , rect.getTop() - 100, 0);



        ColumnText.showTextAligned(writer.getDirectContent(),
                Element.ALIGN_LEFT, new Phrase("Shop Name : "+master.getShopName()),
                (rect.getLeft() ) , rect.getTop() - 120, 0);

        ColumnText.showTextAligned(writer.getDirectContent(),
                Element.ALIGN_RIGHT, new Phrase("Order No : "+master.getOrderId()),
                (rect.getRight())-50, rect.getTop() - 120, 0);

        ColumnText.showTextAligned(writer.getDirectContent(),
                Element.ALIGN_LEFT, new Phrase("Address : "+master.getAddress()),
                (rect.getLeft() ) , rect.getTop() - 135, 0);

        ColumnText.showTextAligned(writer.getDirectContent(),
                Element.ALIGN_RIGHT, new Phrase("Date : " + CommonUtilities.longToDate(master.getOrderDate())),
                (rect.getRight() - 50), rect.getTop() - 135, 0);





        PdfPTable table = new PdfPTable(6);

        table.setSpacingBefore(100f);

        table.setWidthPercentage(110);
        table.setSpacingBefore(2f);
        table.setSpacingAfter(2f);

        // first row


        table.addCell("Design");
        table.addCell("Category");
        table.addCell("Product");
        table.addCell("MRP");
        table.addCell("Size");
        table.addCell("Qty");



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

          int totp=0,totq=0;

        for (int i = 0; i < orderDetails.size(); i++)
        {

            try {
                File file = imageLoader.getDiscCache().get(orderDetails.get(i).iconThumb);
                PdfPCell cell1 = createImageCell(file.getAbsolutePath());
                table.addCell(cell1);
            }
            catch (Exception e)
            {
                table.addCell("");
            }


                    int cid = moduleProduct.getProductBeanByProductId(orderDetails.get(i).productId).getCategoryId();
                    String CName = moduleCategory.getCategoryName(cid);
                    PdfPCell c = new PdfPCell(new Phrase(CName));
                    c.setPadding(10);
                    table.addCell(c);
                    table.addCell(orderDetails.get(i).productName + "");
                    table.addCell(orderDetails.get(i).consumerPrice + "");
                    table.addCell(orderDetails.get(i).sizeName);
                    table.addCell(orderDetails.get(i).getQuantity() + "");

               totq=totq+orderDetails.get(i).getQuantity();
               totp=totp+((int)Float.parseFloat(orderDetails.get(i).getDealerPrice())*orderDetails.get(i).quantity);

        }


        table.addCell("Total");
        table.addCell("");
        table.addCell("");
        table.addCell(totp+"");
        table.addCell("");
        table.addCell(totq+"");


        document.add(table);




        document.close();
    }

}
