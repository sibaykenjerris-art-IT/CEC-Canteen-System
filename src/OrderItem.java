public class OrderItem {
    private final MenuItem menuItem;
    private int quantity;
    public OrderItem(MenuItem menuItem,int quantity){
        if(menuItem==null) throw new IllegalArgumentException("Menu item is required.");
        if(quantity<1) throw new IllegalArgumentException("Quantity must be at least 1.");
        this.menuItem=menuItem; this.quantity=quantity;
    }
    public MenuItem getMenuItem(){ return menuItem; }
    public int getQuantity(){ return quantity; }
    public void setQuantity(int quantity){ if(quantity<1) throw new IllegalArgumentException("Quantity must be at least 1."); this.quantity=quantity; }
    public double getSubtotal(){ return menuItem.getPrice()*quantity; }
    @Override public String toString(){ return String.format("%s × %d — ₱%.2f",menuItem.getName(),quantity,getSubtotal()); }
}
