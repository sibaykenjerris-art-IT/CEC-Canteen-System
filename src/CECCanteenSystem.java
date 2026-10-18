import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.util.ArrayList;

public class CECCanteenSystem extends Application {
    private final Color blue = Color.web("#5266B8");
    private Stage stage;
    private final ObservableList<OrderItem> cartView = FXCollections.observableArrayList();
    private final ArrayList<OrderItem> cart = new ArrayList<>();
    private static final String STAFF_USER = "staff";
    private static final String STAFF_PASSWORD = "CECstaff123!";

    @Override public void start(Stage primaryStage) {
        stage = primaryStage;
        stage.setTitle("CEC Canteen Ordering System");
        showHome();
        stage.show();
    }

    private Button button(String text) {
        Button b = new Button(text);
        b.setMaxWidth(Double.MAX_VALUE);
        b.setStyle("-fx-background-color:#5266B8;-fx-text-fill:white;-fx-font-size:14px;-fx-background-radius:8;-fx-padding:10 15;");
        return b;
    }

    private VBox page(String headingText) {
        Label title = new Label("CEC CANTEEN");
        title.setFont(Font.font("Arial",25));
        title.setTextFill(Color.WHITE);
        Label subtitle = new Label("Canteen Ordering System");
        subtitle.setTextFill(Color.WHITE);
        VBox header = new VBox(5,title,subtitle);
        header.setPadding(new Insets(20));
        header.setStyle("-fx-background-color:linear-gradient(to right,#5266B8,#8174D6);");
        Label heading = new Label(headingText);
        heading.setFont(Font.font("Arial",20));
        heading.setTextFill(blue);
        VBox root = new VBox(15,header,heading);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color:white;");
        return root;
    }

    private void showPage(String title,VBox root) {
        ScrollPane scroll = new ScrollPane(root);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background:white;-fx-background-color:white;");
        stage.setScene(new Scene(scroll,750,650));
        stage.setTitle(title);
    }

    private void message(String text) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("CEC Canteen"); a.setHeaderText(null); a.setContentText(text); a.showAndWait();
    }

    private void showHome() {
        VBox root=page("Welcome!");
        Label info=new Label("Order your favorite meals from the CEC canteen.");
        info.setWrapText(true);
        Button student=button("Student / Customer");
        Button staff=button("Canteen Staff");
        student.setOnAction(e->showStudentDetails());
        staff.setOnAction(e->showStaffLogin());
        root.getChildren().addAll(info,student,staff);
        showPage("CEC Canteen",root);
    }

    private void showStudentDetails() {
        VBox root=page("Student Details");
        TextField username=new TextField(); username.setPromptText("Student ID or username");
        TextField fullName=new TextField(); fullName.setPromptText("Full name");
        Button go=button("Continue to Menu"), back=button("Back to Home");
        go.setOnAction(e->{
            if(username.getText().isBlank()||fullName.getText().isBlank()){
                message("Please enter your student ID and full name."); return;
            }
            Student student=new Student(username.getText().trim(),fullName.getText().trim());
            message("Welcome, "+student.getFullName()+"!");
            showMenu();
        });
        back.setOnAction(e->showHome());
        root.getChildren().addAll(new Label("Student ID / username:"),username,new Label("Full name:"),fullName,go,back);
        showPage("Student Details",root);
    }

    private void showMenu() {
        VBox root=page("Available Food and Beverages");
        ListView<MenuItem> menu=new ListView<>();
        menu.getItems().setAll(CanteenData.MENU);
        menu.setPrefHeight(160);
        TextField quantity=new TextField("1"); quantity.setPromptText("Quantity");
        TextArea request=new TextArea(); request.setPromptText("Special requests (optional)"); request.setPrefRowCount(2);
        ListView<OrderItem> cartList=new ListView<>(cartView); cartList.setPrefHeight(120);
        Label total=new Label("Total: ₱0.00");
        Button add=button("Add to Cart"), remove=button("Remove Selected Item"), submit=button("Submit Order"), back=button("Back to Home");

        add.setOnAction(e->{
            MenuItem selected=menu.getSelectionModel().getSelectedItem();
            if(selected==null){message("Please select a menu item first.");return;}
            try {
                int qty=Integer.parseInt(quantity.getText().trim());
                if(qty<1){message("Quantity must be at least 1.");return;}
                OrderItem existing=null;
                for(OrderItem item:cart) if(item.getMenuItem().getId()==selected.getId()){existing=item;break;}
                if(existing==null){existing=new OrderItem(selected,qty);cart.add(existing);cartView.add(existing);}
                else existing.setQuantity(existing.getQuantity()+qty);
                cartList.refresh(); updateTotal(total); quantity.setText("1");
            } catch(NumberFormatException ex){message("Enter a whole-number quantity.");}
              catch(IllegalArgumentException ex){message(ex.getMessage());}
        });
        remove.setOnAction(e->{
            int i=cartList.getSelectionModel().getSelectedIndex();
            if(i<0){message("Select a cart item first.");return;}
            cart.remove(i);cartView.remove(i);updateTotal(total);
        });
        submit.setOnAction(e->{
            if(cart.isEmpty()){message("Your cart is empty.");return;}
            Order order=new Order(cart,request.getText());
            CanteenData.ORDERS.add(order);
            cart.clear();cartView.clear();updateTotal(total);
            message("Order #"+order.getNumber()+" submitted. Status: Pending.");
            showMenu();
        });
        back.setOnAction(e->showHome());
        root.getChildren().addAll(new Label("Select an item:"),menu,new Label("Quantity:"),quantity,add,
                new Label("Your Cart:"),cartList,remove,total,new Label("Special Requests:"),request,submit,back);
        showPage("Student Menu",root);
    }

    private void updateTotal(Label label) {
        double sum=0; for(OrderItem item:cart) sum+=item.getSubtotal();
        label.setText(String.format("Total: ₱%.2f",sum));
    }

    private void showStaffLogin() {
        VBox root=page("Staff Login");
        TextField username=new TextField(); username.setPromptText("Staff username");
        PasswordField password=new PasswordField(); password.setPromptText("Staff password");
        Button login=button("Log In"), back=button("Back to Home");
        login.setOnAction(e->{
            if(STAFF_USER.equals(username.getText().trim())&&STAFF_PASSWORD.equals(password.getText())){
                Staff staff=new Staff(username.getText().trim(),"Canteen Staff");
                message("Welcome, "+staff.getRole()+"!");
                showStaff();
            } else message("Invalid staff username or password.");
        });
        back.setOnAction(e->showHome());
        root.getChildren().addAll(new Label("Username:"),username,new Label("Password:"),password,login,back);
        showPage("Staff Login",root);
    }

    private void showStaff() {
        VBox root=page("Staff Dashboard");
        ListView<Order> orders=new ListView<>();
        orders.getItems().setAll(CanteenData.ORDERS); orders.setPrefHeight(260);
        Button update=button("Advance Order Status"), manage=button("Manage Menu"), logout=button("Log Out");
        update.setOnAction(e->{
            Order selected=orders.getSelectionModel().getSelectedItem();
            if(selected==null){message("Please select an order.");return;}
            if(selected.advanceStatus()){orders.refresh();message("Order status updated to "+selected.getStatus()+".");}
            else message("This order is already completed.");
        });
        manage.setOnAction(e->showManageMenu());
        logout.setOnAction(e->showHome());
        root.getChildren().addAll(new Label("Customer Orders:"),orders,update,manage,logout);
        showPage("Staff Dashboard",root);
    }

    private void showManageMenu() {
        VBox root=page("Manage Menu");
        ListView<MenuItem> menu=new ListView<>();
        menu.getItems().setAll(CanteenData.MENU); menu.setPrefHeight(190);
        TextField name=new TextField(); name.setPromptText("Item name");
        TextField price=new TextField(); price.setPromptText("Price in pesos");
        ComboBox<String> type=new ComboBox<>(); type.getItems().addAll("Food","Beverage");type.setValue("Food");type.setMaxWidth(Double.MAX_VALUE);
        Button add=button("Add Menu Item"), edit=button("Edit Selected Item"), remove=button("Remove Selected Item"), back=button("Back to Dashboard");
        add.setOnAction(e->{
            if(name.getText().isBlank()){message("Enter an item name.");return;}
            try {
                double p=Double.parseDouble(price.getText().trim());
                if("Beverage".equals(type.getValue()))CanteenData.addBeverage(name.getText().trim(),p);
                else CanteenData.addFood(name.getText().trim(),p);
                menu.getItems().setAll(CanteenData.MENU);name.clear();price.clear();message("Menu item added.");
            }catch(NumberFormatException ex){message("Enter a valid price.");}
             catch(IllegalArgumentException ex){message(ex.getMessage());}
        });
        edit.setOnAction(e->{
            MenuItem selected=menu.getSelectionModel().getSelectedItem();
            if(selected==null){message("Select an item to edit.");return;}
            if(name.getText().isBlank()){message("Enter the new item name.");return;}
            try {
                double p=Double.parseDouble(price.getText().trim());
                selected.setPrice(p);selected.setName(name.getText().trim());menu.refresh();message("Menu item updated.");
            }catch(NumberFormatException ex){message("Enter a valid price.");}
             catch(IllegalArgumentException ex){message(ex.getMessage());}
        });
        remove.setOnAction(e->{
            MenuItem selected=menu.getSelectionModel().getSelectedItem();
            if(selected==null){message("Select an item to remove.");return;}
            CanteenData.MENU.remove(selected);menu.getItems().setAll(CanteenData.MENU);
        });
        back.setOnAction(e->showStaff());
        root.getChildren().addAll(new Label("Current menu:"),menu,new Label("Name:"),name,new Label("Price:"),price,
                new Label("Type:"),type,add,edit,remove,back);
        showPage("Manage Menu",root);
    }

    public static void main(String[] args) { launch(args); }
}
