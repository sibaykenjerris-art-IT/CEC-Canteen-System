public class Beverage extends MenuItem {
    public Beverage(int id,String name,double price){ super(id,name,price); }
    @Override public String getType(){ return "Beverage"; }
}
