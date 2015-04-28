package jsf;

import javax.faces.bean.ManagedBean;

@ManagedBean
public class ProductDetailView {

	private String productId;

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}
}