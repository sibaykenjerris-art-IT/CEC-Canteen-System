import java.util.ArrayList;
public class Order {
    private static int nextNumber=1;
    private final int number;
    private final ArrayList<OrderItem> items;
    private final String specialRequest;
    private String status="Pending";
    public Order(ArrayList<OrderItem> items,String specialRequest){
        if(items==null||items.isEmpty()) throw new IllegalArgumentException("Order cannot be empty.");
        number=nextNumber++; this.items=new ArrayList<>(items);
        this.specialRequest=specialRequest==null?"":specialRequest.trim();
    }
    public int getNumber(){ return number; }
    public String getStatus(){ return status; }
    public String getSpecialRequest(){ return specialRequest; }
    public double getTotal(){ double total=0; for(OrderItem i:items) total+=i.getSubtotal(); return total; }
    public boolean advanceStatus(){
        switch(status){ case "Pending" -> status="Preparing"; case "Preparing" -> status="Ready"; case "Ready" -> status="Completed"; default -> {return false;} }
        return true;
    }
    @Override public String toString(){
        StringBuilder s=new StringBuilder("Order #"+number+" ["+status+"]\n");
        for(OrderItem i:items) s.append(i).append("\n");
        s.append(String.format("Total: ₱%.2f",getTotal()));
        if(!specialRequest.isBlank()) s.append("\nSpecial request: ").append(specialRequest);
        return s.toString();
    }
}
