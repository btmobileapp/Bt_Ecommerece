package biyaniparker.com.parker.beans;



public class RowItem
{

	private String title,Url;
	private int icon;

	public boolean isLocal=false;

	public boolean isStartSection=false;
	public String sectionName;

	public RowItem(String title, int icon) {
		this.title = title;
		this.icon = icon;

	}
	public RowItem(String title, int icon, String Url) {
		this.title = title;
		this.icon = icon;
		this.Url=Url;

	}
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}
	public String getUrl() {
		return Url;
	}
	public void setUrl(String Url) {
		this.Url = Url;
	}

}
