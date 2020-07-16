package biyaniparker.com.parker.beans;

/**
 * Created by bt18 on 08/08/2016.
 */
public class AlbumBean {
    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public AlbumBean(int albumId,String albumName)
    {

        this.albumId = albumId;
        this.albumName = albumName;
    }

    int albumId;
    String albumName;
}
