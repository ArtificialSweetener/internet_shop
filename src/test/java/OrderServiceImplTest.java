import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dao.OrderDao;
import models.Order;
import service.OrderService;
import service.impl.OrderServiceImpl;

public class OrderServiceImplTest {

    @Mock
    private OrderDao orderDao;

    private OrderService orderService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        orderService = new OrderServiceImpl(orderDao);
    }

    @Test
    public void testCreate() {
        Order order = new Order();
        when(orderDao.create(order)).thenReturn(order);

        Order returnedOrder = orderService.create(order);
        assertEquals(order, returnedOrder);
    }

    @Test
    public void testGet() {
        Long id = 1L;
        Order order = new Order();
        when(orderDao.get(id)).thenReturn(Optional.of(order));

        Order returnedOrder = orderService.get(id);
        assertEquals(order, returnedOrder);
    }

    @Test
    public void testGetAll() {
        List<Order> orders = new ArrayList<>();
        when(orderDao.getAll()).thenReturn(orders);

        List<Order> returnedOrders = orderService.getAll();
        assertEquals(orders, returnedOrders);
    }

    @Test
    public void testUpdate() {
        Order order = new Order();
        when(orderDao.update(order)).thenReturn(order);

        Order returnedOrder = orderService.update(order);
        assertEquals(order, returnedOrder);
    }

    @Test
    public void testDelete() {
        Long id = 1L;
        when(orderDao.delete(id)).thenReturn(true);

        boolean isDeleted = orderService.delete(id);
        assertTrue(isDeleted);
    }

    @Test
    public void testGetAllOrdersByUserId() {
        Long userId = 1L;
        int offset = 0;
        int noOfRecords = 10;
        List<Order> orders = new ArrayList<>();
        when(orderDao.getAllOrdersByUserId(userId, offset, noOfRecords)).thenReturn(Optional.of(orders));

        Optional<List<Order>> returnedOrders = orderService.getAllOrdersByUserId(userId, offset, noOfRecords);
        assertEquals(orders, returnedOrders.get());
    }
    
    //write test for GetAllWithPagination()

    @Test
    public void testGetNoOfRecords() {
        int expected = 10;
        when(orderDao.getNoOfRecords()).thenReturn(expected);

        int result = orderService.getNoOfRecords();

        assertEquals(expected, result);
    }
}
