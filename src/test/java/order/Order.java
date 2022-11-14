package order;

//import org.junit.jupiter.api.Test;

import java.util.Objects;

@SuppressWarnings("unused")
public class Order {
    private int petId;
    private int quantity;
    private int id;
    private String shipDate;
    private boolean complete;
    private String status;

    public int getPetId(){
        return petId;
    }

    public void setPetId(int petId) { this.petId = petId; }

    public int getQuantity(){
        return quantity;
    }

    public void setQuantity(int quantity) {this.quantity = quantity;}

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getShipDate(){
        return shipDate;
    }

    public boolean isComplete(){
        return complete;
    }

    public String getStatus(){
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return petId == order.petId && quantity == order.quantity && id == order.id && complete == order.complete
                && Objects.equals(shipDate, order.shipDate) && Objects.equals(status, order.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(petId, quantity, id, shipDate, complete, status);
    }
}