import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import order.Endpoints;
import order.Order;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HomeTaskApiTest {
    @BeforeAll
    static void setUp() {
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setBaseUri(Endpoints.host + Endpoints.orderBasePath) // задаём базовый адрес каждого ресурса
                .setAccept(ContentType.JSON) // задаём заголовок accept // приходит
                .setContentType(ContentType.JSON) // задаём заголовок content-type // когда мы отпр
                .log(LogDetail.ALL) // дополнительная инструкция полного логгирования для RestAssured
                .build(); // после этой команды происходит формирование стандартной "шапки" запроса.

        //Здесь задаётся фильтр, позволяющий выводить содержание ответа,
        // также к нему можно задать условия в параметрах конструктора, которым должен удовлетворять ответ (например код ответа)
        RestAssured.filters(new ResponseLoggingFilter());
    }

    private Order dummyOrder() {
        Order order = new Order();
        order.setPetId(new Random().nextInt(10000));
        order.setId(new Random().nextInt(100));
        order.setQuantity(new Random().nextInt(10));
        return order;
    }

    private void createOrder(Order order) {
        // given().spec(requestSpec) - можно использовать для конкретного запроса свою спецификацию
        given()
                .body(order)
                .when()
                .post(Endpoints.order)
                .then()
                .statusCode(200);
    }

    private void deleteOrder(int orderId) {
        // given().spec(requestSpec) - можно использовать для конкретного запроса свою спецификацию
        given()
                .pathParam("id", orderId)
                .when()
                .delete(Endpoints.order + Endpoints.orderId)
                .then()
                .statusCode(200);
        given()
                .pathParam("id", orderId)
                .when()
                .delete(Endpoints.order + Endpoints.orderId)
                .then()
                .statusCode(404);
    }

    private Map getInventory() {
        return given()
                .when()
                .get(Endpoints.inventory)
                .then()
                .statusCode(200).extract().body().as(Map.class);
    }

    private Order getOrder(int id) {
        return given()
                .pathParam("id", id)
                .when()
                .get(Endpoints.order + Endpoints.orderId)
                .then()
                .assertThat()
                .statusCode(200).extract().body().as(Order.class);
    }


    @Test
    void postOrderTest() {
        Order order = dummyOrder();
        // создаем заказ
        createOrder(order);
        // проверяем правильно ли он создался
        Order apiOrder = getOrder(order.getId());
        assertEquals(order, apiOrder);
    }

    @Test
    void getOrderTest() {
        Order order = dummyOrder();
        createOrder(order);
        Order apiOrder = getOrder(order.getId());
        assertEquals(order, apiOrder);
    }

    @Test
    void deleteOrderTest() {
        Order order = dummyOrder();
        createOrder(order);
        deleteOrder(order.getId());
    }

    @Test
    void getInventoryTest() {
        Map inventory = getInventory();
        assertTrue(inventory.containsKey("sold"), "Inventory не содержит статус sold" );
    }
}
