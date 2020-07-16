package biyaniparker.com.parker.beans;

/**
 * Created by bt on 08/19/2016.
 */
public class SizeMaster
{
    public int sizeId, clientId, sequenceNo;
    public long createdDate;
    public long changedBy;
    public long changedDate;
    public long createdBy;
    public String sizeName, deleteStatus;

    public SizeMaster()
    {
    }

    public int getSizeId() {
        return sizeId;
    }

    public void setSizeId(int sizeId) {
        this.sizeId = sizeId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(int sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public long getChangedDate() {
        return changedDate;
    }

    public void setChangedDate(long changedDate) {
        this.changedDate = changedDate;
    }

    public long getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(long changedBy) {
        this.changedBy = changedBy;
    }

    public String getSizeName() {
        return sizeName;
    }

    public void setSizeName(String sizeName) {
        this.sizeName = sizeName;
    }

    public long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(long createdBy) {
        this.createdBy = createdBy;
    }

    public String getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(String deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

    public String toString(){return this.getSizeName();}
}
