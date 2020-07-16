package biyaniparker.com.parker.beans;

/**
 * Created by bt on 08/11/2016.
 */
public class CategoryBean {
    public int categoryId,parentCategoryId;

    public CategoryBean(int categoryId, long changedDate, long changedBy, long createdDate, long createdBy, String deleteStatus, long clientId, String remark, String isLast, String icon, String categoryName, String categoryCode, int parentCategoryId) {
        this.categoryId = categoryId;
        this.changedDate = changedDate;
        this.changedBy = changedBy;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
        this.deleteStatus = deleteStatus;
        this.clientId = clientId;
        this.remark = remark;
        this.isLast = isLast;
        this.icon = icon;
        this.categoryName = categoryName;
        this.categoryCode = categoryCode;
        this.parentCategoryId = parentCategoryId;
    }

    public String categoryCode,categoryName,icon,remark,isLast,deleteStatus;

    public long clientId,createdBy,createdDate,changedBy,changedDate;

    public CategoryBean() {

    }

    public long getChangedDate() {
        return changedDate;
    }

    public void setChangedDate(long changedDate) {
        this.changedDate = changedDate;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public long getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(long changedBy) {
        this.changedBy = changedBy;
    }

    public long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(long createdBy) {
        this.createdBy = createdBy;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public String getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(String deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

    public String getIsLast() {
        return isLast;
    }

    public void setIsLast(String isLast) {
        this.isLast = isLast;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public int getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(int parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

  public  String toString()
  {
      return  categoryName;
  }
}
