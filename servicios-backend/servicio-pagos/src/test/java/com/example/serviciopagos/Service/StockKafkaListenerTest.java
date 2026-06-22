package com.example.serviciopagos.Service;

import com.example.serviciopagos.Model.ProductoStockCache;
import com.example.serviciopagos.Repository.ProductoStockCacheRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class StockKafkaListenerTest {

    @Mock
    private ProductoStockCacheRepository stockCacheRepository;

    @InjectMocks
    private StockKafkaListener stockKafkaListener;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void escucharCambiosStock_MensajeValido_GuardaCache() {
        String jsonValido = "{\"productoId\": 10, \"stock\": 50}";

        stockKafkaListener.escucharCambiosStock(jsonValido);

        verify(stockCacheRepository, times(1)).save(any(ProductoStockCache.class));
    }

    @Test
    void escucharCambiosStock_MensajeInvalido_CaeEnCatch() {
        String jsonInvalido = "esto-no-es-json";

        stockKafkaListener.escucharCambiosStock(jsonInvalido);

        verify(stockCacheRepository, never()).save(any(ProductoStockCache.class));
    }
}