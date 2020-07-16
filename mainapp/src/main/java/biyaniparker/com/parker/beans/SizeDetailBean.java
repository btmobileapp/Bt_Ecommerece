package biyaniparker.com.parker.beans;

/**
 * Created by bt on 08/19/2016.
 */
public class SizeDetailBean
{
    public int sizeDetailId, SizeId, CategoryId;
    public String DeleteStatus;

    public SizeDetailBean()
    {

    }
    public int getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(int categoryId) {
        CategoryId = categoryId;
    }

    public int getSizeId() {
        return SizeId;
    }

    public void setSizeId(int sizeId) {
        SizeId = sizeId;
    }

    public int getSizeDetailId() {
        return sizeDetailId;
    }

    public void setSizeDetailId(int sizeDetailId) {
        this.sizeDetailId = sizeDetailId;
    }

    public String getDeleteStatus() {
        return DeleteStatus;
    }

    public void setDeleteStatus(String deleteStatus) {
        DeleteStatus = deleteStatus;
    }


}
