import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import dao.ItemDao;
import models.Item;
import service.ItemService;
import service.impl.ItemServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class ItemServiceImplTest {

    @Mock
    private ItemDao itemDao;

    private ItemService itemService;

    @Before
    public void setUp() {
        itemService = new ItemServiceImpl(itemDao);
    }

    @Test
    public void testCreate() {
        Item item = new Item(1, 2, 3, 4, 5.0);
        when(itemDao.create(item)).thenReturn(item);

        Item createdItem = itemService.create(item);

        verify(itemDao).create(item);
        assertEquals(item, createdItem);
    }

    @Test
    public void testGet() {
        Item item = new Item(1, 2, 3, 4, 5.0);
        when(itemDao.get(1L)).thenReturn(Optional.of(item));

        Item returnedItem = itemService.get(1L);

        verify(itemDao).get(1L);
        assertEquals(item, returnedItem);
    }

    @Test
    public void testGetAll() {
        Item item1 = new Item(1, 2, 3, 4, 5.0);
        Item item2 = new Item(2, 3, 4, 5, 6.0);
        when(itemDao.getAll()).thenReturn(Arrays.asList(item1, item2));

        List<Item> returnedItems = itemService.getAll();

        verify(itemDao).getAll();
        assertEquals(Arrays.asList(item1, item2), returnedItems);
    }

    @Test
    public void testUpdate() {
        Item item = new Item(1, 2, 3, 4, 5.0);
        when(itemDao.update(item)).thenReturn(item);

        Item updatedItem = itemService.update(item);

        verify(itemDao).update(item);
        assertEquals(item, updatedItem);
    }
    
    @Test
    public void deleteTest() {
        Long id = 1L;
        when(itemDao.delete(id)).thenReturn(true);
        boolean result = itemService.delete(id);
        verify(itemDao, times(1)).delete(id);
        assertTrue(result);
    }
    @Test
    public void testGetAllByOrderId() {
        Long orderId = 1L;
        ArrayList<Item> items = new ArrayList<>();
        items.add(new Item(1L, 1L, 1L, 1, 1.0));
        when(itemDao.getAllByOrderId(orderId)).thenReturn(Optional.of(items));

        Optional<List<Item>> result = itemService.getAllByOrderId(orderId);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(items, result.get());
        verify(itemDao, times(1)).getAllByOrderId(orderId);
    }

    @Test
    public void testGetAllByOrderIdWithParams() {
        Long orderId = 1L;
        int offset = 0;
        int noOfRecords = 10;
        ArrayList<Item> items = new ArrayList<>();
        items.add(new Item(1L, 1L, 1L, 1, 1.0));
        when(itemDao.getAllByOrderId(orderId, offset, noOfRecords)).thenReturn(items);

        List<Item> result = itemService.getAllByOrderId(orderId, offset, noOfRecords);

        Assertions.assertEquals(items, result);
        verify(itemDao, times(1)).getAllByOrderId(orderId, offset, noOfRecords);
    }

    @Test
    public void testGetNoOfRecords() {
        int noOfRecords = 10;
        when(itemDao.getNoOfRecords()).thenReturn(noOfRecords);

        int result = itemService.getNoOfRecords();

        Assertions.assertEquals(noOfRecords, result);
        verify(itemDao, times(1)).getNoOfRecords();
    }
}