
public class Category {
	private boolean need; 
	private String categoryName;
	
	public Category(boolean need) {
		this.need = need;
	}
	
	
	
	public boolean isNeed() {
		return need;
	}
	public void setNeed(boolean need) {
		this.need = need;
	}

	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

}