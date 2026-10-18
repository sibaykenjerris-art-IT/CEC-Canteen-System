public abstract class MenuItem {
    private final int id;
    private String name;
    private double price;
    public MenuItem(int id, String name, double price) {
        if(name==null || name.isBlank()) throw new IllegalArgumentException("Name is required.");
        if(price<=0) throw new IllegalArgumentException("Price must be greater than zero.");
        this.id=id; this.name=name; this.price=price;
    }
    public int getId(){ return id; }
    public String getName(){ return name; }
    public double getPrice(){ return price; }
    public void setName(String name){ if(name==null||name.isBlank()) throw new IllegalArgumentException("Name is required."); this.name=name; }
    public void setPrice(double price){ if(price<=0) throw new IllegalArgumentException("Price must be greater than zero."); this.price=price; }
    public abstract String getType();
    @Override public String toString(){ return String.format("%s | %s | ₱%.2f", getType(), name, price); }
}
