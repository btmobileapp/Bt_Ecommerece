package biyaniparker.com.parker.view.reports;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Environment;
import android.provider.ContactsContract;

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
import com.itextpdf.text.pdf.GrayColor;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import biyaniparker.com.parker.bal.ModuleUser;
import biyaniparker.com.parker.beans.CategoryBean;
import biyaniparker.com.parker.beans.DeliveryChallanBean;
import biyaniparker.com.parker.beans.DispatchMasterAndDetails;
import biyaniparker.com.parker.beans.DispatchMasterBean;
import biyaniparker.com.parker.beans.UserBean;
import biyaniparker.com.parker.beans.UserShopBean;
import biyaniparker.com.parker.utilities.CommonUtilities;
import biyaniparker.com.parker.utilities.DateAndOther;
import biyaniparker.com.parker.view.homeadmin.orderdispatch.Logic;


/**
 * Created by bt18 on 09/22/2016.
 */
public class PrintDeliverChallanReport
{
    Activity context;
    ImageLoader imageLoader;
    private  File pdffile;
    private File imfo;

    DispatchMasterAndDetails source;
    private DispatchMasterBean master;

    public PrintDeliverChallanReport(Activity context, ImageLoader imageLoader)
    {
        this.context=context;
        pdffile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString()+"/parkerreport", "deliverychallan.pdf");

       /* imfo = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), "l1.jpg");
                */
        this.imageLoader=imageLoader;

        try
        {
            imfo = imageLoader.getDiscCache().get(CommonUtilities.URL+"l1.jpg");
        }
        catch (Exception e){}


    }

    public  void setDataSet(DispatchMasterAndDetails source)
    {
        this.source=source;
    }

    HashMap<CategoryBean,ArrayList<DeliveryChallanBean>> map;
    public  void setDeliverryChallanDataSet(  HashMap<CategoryBean,ArrayList<DeliveryChallanBean>> map)
    {
        this.map=map;
    }

    public void call()
    {
        try
        {
            pdffile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString()+"/parkerreport", "challan_"+master.dispatchId+".pdf");
            createChallanPdf(pdffile.getAbsolutePath().toString());
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        AlertDialog.Builder alBuilder = new AlertDialog.Builder(context);
                        alBuilder.setTitle("Delivery Challan");
                        alBuilder.setMessage("\n\n" + "File Generted In Location " + pdffile.getAbsolutePath() + "\n\n");
                        alBuilder.setPositiveButton("Open Pdf", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                CommonUtilities.openPdf(context, pdffile.getAbsolutePath());
                            }
                        });
                        alBuilder.setNeutralButton("Mail this Document", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // CommonUtilities.openPdf(context, pdffile.getAbsolutePath());
                                UserShopBean user=new ModuleUser(context).getUser((int) master.customerId);
                                CommonUtilities.emailWithAttachement(context,user.user.getEmailId()," Delivery Challan report of Challan No"+master.dispatchId,"Delivery Challan report",pdffile.getAbsolutePath());
                            }
                        });
                        alBuilder.setNegativeButton("Cancel", null);
                        alBuilder.show();
                    } catch (Exception e)
                    {
                    }
                }
            });

        }
        catch (Exception e)
        {
            String str=e.toString();
            //CommonUtilities.alert(context,str);
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

    public void setMaster(DispatchMasterBean master) {
        this.master = master;
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
                Element.ALIGN_LEFT, new Phrase("Challan No : "+master.getDispatchId()),
                (rect.getLeft()), rect.getTop() - 115, 0);

        ColumnText.showTextAligned(writer.getDirectContent(),
                Element.ALIGN_LEFT, new Phrase("Order No : "+master.getOrderId()),
                (rect.getRight())-180, rect.getTop() -115, 0);

        ColumnText.showTextAligned(writer.getDirectContent(),
                Element.ALIGN_LEFT, new Phrase("Challan Date : "+ CommonUtilities.longToDate(master.dispatchDate)  ) , // DateAndOther.convertToDMY(master.dispatchDate)),
                (rect.getLeft() ) , rect.getTop() - 135, 0);

        ColumnText.showTextAligned(writer.getDirectContent(),
                Element.ALIGN_LEFT, new Phrase("Order Date : "+ CommonUtilities.longToDate(master.getOrderDate())),//+ DateAndOther.convertToDMY(master.)),
                (rect.getRight()-180 ) , rect.getTop() - 135, 0);

        ColumnText.showTextAligned(writer.getDirectContent(),
                Element.ALIGN_LEFT, new Phrase("Shop Name : "+master.getShopName()),
                (rect.getLeft() ) , rect.getTop() - 155, 0);

        ColumnText.showTextAligned(writer.getDirectContent(),
                Element.ALIGN_LEFT, new Phrase("City : "+master.getAddress()),
                (rect.getRight()-180 ) , rect.getTop() - 155, 0);



        Iterator it = map.entrySet().iterator();
        while (it.hasNext())
        {

            Map.Entry pair = (Map.Entry) it.next();
            CategoryBean bean=(CategoryBean)pair.getKey();
            ArrayList<DeliveryChallanBean> list=  map.get(pair.getKey());

            Paragraph para = new Paragraph(bean.categoryName+"\n");
            if(list.size()!=0)
            {
               // para.setFont(new Font(Font.BOLD));
              //  para = new Paragraph(bean.categoryName+"   Size "+list.size()+ "\n");
                Font f3 = new Font(Font.FontFamily.TIMES_ROMAN, 22.0f, Font.BOLD, BaseColor.BLACK);
                para.setFont(f3);
                document.add(para);
            }


            ArrayList<String> sizes=new ArrayList<>();
            for(int i=0;i<list.size();i++)
            {
                 DeliveryChallanBean dbean= list.get(i);
                 for(int j=0;j<dbean.sizesandqnts.size();j++)
                 {
                     int flag=0;
                     for(int k=0;k<sizes.size();k++)
                     {
                         if(dbean.sizesandqnts.get(j).SizeName.equals(sizes.get(k)))
                         {
                            flag=1;
                         }
                     }
                     if(flag==0)
                         sizes.add(dbean.sizesandqnts.get(j).SizeName);

                 }
            }
            int columnsize=sizes.size()+2    +1  ;
            //   1 for qauntity
            Paragraph col = new Paragraph(bean.categoryName+":   "+columnsize+"\n");
           // document.add(col);
            PdfPTable table = new PdfPTable(columnsize);

            table.setSpacingBefore(100f);
            table.setWidthPercentage(100);
            table.setSpacingBefore(2f);
            table.setSpacingAfter(2f);
            int rem=  (columnsize*(1/100));


            float firstwt=columnsize*(1/100)*20;
            float remwt=(columnsize-firstwt);

            float ektakka=(float)((float)columnsize/(float)100);

            float distribute=(float)((float)(ektakka*80)/(float)(columnsize-1));





            {
                float[] arr=new float[columnsize];
                for(int q=0;q<arr.length;q++)
                {
                    if(q==0)
                    {
                        arr[0]=ektakka*20;   //firstwt;//20/columnsize;//firstwt;   // 2;//2;//(columnsize*(20/100));
                    }
                    else
                    {
                        arr[q]=distribute ;//remwt; //1/columnsize ;//remwt/(columnsize-1);  //     1;//(8/columnsize);
                    }
                }
                table.setWidths(arr);
            }


            col = new Paragraph(columnsize+ ":    1% ="+ektakka+"    20%= "+(20*ektakka) +  "  distribut%="+distribute);
          //  document.add(col);
            if(list.size()!=0)
            {
                table.addCell("Product Category");

                //************************************
                {
                   // table.addCell("MRP");
                    Font f = new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD, GrayColor.BLACK);

                    PdfPCell cell = new PdfPCell(new Phrase( "MRP", f));
                    // cell.setBackgroundColor(GrayColor.GRAYBLACK);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                }
                //**************************************

                for (int i = 0; i < sizes.size(); i++)
                {
                    // table.addCell(sizes.get(i));
                    // table.addCell("Qnty");
                    Font f = new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD, GrayColor.BLACK);

                    PdfPCell cell = new PdfPCell(new Phrase(sizes.get(i) + "", f));
                   // cell.setBackgroundColor(GrayColor.GRAYBLACK);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                }
                Font f = new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD, GrayColor.BLACK);

                PdfPCell cell = new PdfPCell(new Phrase( "Qty", f));
                // cell.setBackgroundColor(GrayColor.GRAYBLACK);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

            }

            int grantTotal=0;
            for(int i=0;i<list.size();i++)
            {

                DeliveryChallanBean dbean= list.get(i);
                table.addCell(dbean.CategoryName);

                //*********************************
                {
                   // table.addCell(dbean.Price);
                    Font f = new Font(Font.FontFamily.HELVETICA, 13, Font.NORMAL, GrayColor.BLACK);
                    PdfPCell cell = new PdfPCell(new Phrase(dbean.Price + "", f));
                    // cell.setBackgroundColor(GrayColor.GRAYBLACK);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                }
                //*******************************
                int rowqnty=0;
                for(int z=0;z<sizes.size();z++)
                {


                    int flag = 0,jindex=-1;
                    for (int j = 0; j < dbean.sizesandqnts.size(); j++)
                    {

                       if(sizes.get(z).equals(dbean.sizesandqnts.get(j).SizeName))
                       {
                           flag=1;
                           jindex=j;
                       }
                    }
                    if(flag==1)
                    {
                       // table.addCell(dbean.sizesandqnts.get(jindex).Qnt + "");

                        Font f = new Font(Font.FontFamily.HELVETICA, 13, Font.NORMAL, GrayColor.GRAY);
                        PdfPCell cell = new PdfPCell(new Phrase(dbean.sizesandqnts.get(jindex).Qnt==0?"": dbean.sizesandqnts.get(jindex).Qnt+ "", f));
                       // cell.setBackgroundColor(GrayColor.GRAYBLACK);
                        rowqnty=rowqnty+dbean.sizesandqnts.get(jindex).Qnt;
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cell);
                    }
                    else
                    {
                        table.addCell("");
                    }

                }

                grantTotal=grantTotal+rowqnty;
                Font f = new Font(Font.FontFamily.HELVETICA, 13, Font.NORMAL, GrayColor.BLACK);
                PdfPCell cell = new PdfPCell(new Phrase(rowqnty+ "", f));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

            }


            if(grantTotal>0)
            for(int z=0;z<sizes.size()+3;z++)
            {




                if(z==0)
                {
                    table.addCell("Total");
                }
                else if(z==(sizes.size()+3)-1)
                {
                    Font f1 = new Font(Font.FontFamily.HELVETICA, 13, Font.NORMAL, GrayColor.BLACK);
                    PdfPCell cell1 = new PdfPCell(new Phrase(grantTotal+ "", f1));
                    cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell1);;
                }
                else
                {
                    table.addCell("");
                }



            }




            document.add(table);
        }



/*

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


        document.close();
    }
}
