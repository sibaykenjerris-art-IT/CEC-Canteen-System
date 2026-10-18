import java.util.ArrayList;
public final class CanteenData {
    public static final ArrayList<MenuItem> MENU=new ArrayList<>();
    public static final ArrayList<Order> ORDERS=new ArrayList<>();
    private static int nextId=1;
    static { addFood("Chicken Meal",75); addFood("Burger",45); addFood("Fries",35); addBeverage("Iced Tea",20); }
    private CanteenData(){}
    public static void addFood(String name,double price){ MENU.add(new Food(nextId++,name,price)); }
    public static void addBeverage(String name,double price){ MENU.add(new Beverage(nextId++,name,price)); }
}
