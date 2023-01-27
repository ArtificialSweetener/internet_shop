import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dao.LocalizationDao;
import service.impl.LocalizationServiceImpl;

public class LocalizationServiceImplTest {
    @Mock
    private LocalizationDao localizationDao;
    private LocalizationServiceImpl localizationService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        localizationService = new LocalizationServiceImpl(localizationDao);
    }

    @Test
    public void testGetMapByLanguage() {
        // Prepare test data
        String language = "en";
        Map<String, String> expectedMap = Map.of("hello", "Hello", "world", "World");
        when(localizationDao.getMapByLanguage(language)).thenReturn(Optional.of(expectedMap));

        // Call the method under test
        Optional<Map<String, String>> actualMap = localizationService.getMapByLanguage(language);

        // Verify the results
        assertTrue(actualMap.isPresent());
        assertEquals(expectedMap, actualMap.get());
        verify(localizationDao).getMapByLanguage(language);
    }
}