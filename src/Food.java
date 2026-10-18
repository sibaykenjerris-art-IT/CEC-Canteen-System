public class Food extends MenuItem {
    public Food(int id,String name,double price){ super(id,name,price); }
    @Override public String getType(){ return "Food"; }
}
