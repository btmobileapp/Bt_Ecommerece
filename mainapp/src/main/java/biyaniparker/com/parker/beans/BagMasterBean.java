package biyaniparker.com.parker.beans;

import java.util.ArrayList;

/**
 * Created by bt on 09/08/2016.
 */
public class BagMasterBean
{
    public int productId, userId, clientId, priceId;
    public String transactionType;
    public long EnterDate;
    public ArrayList<BagDetailsBean> bagDetails=new ArrayList<>();

}
