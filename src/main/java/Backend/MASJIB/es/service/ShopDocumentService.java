package Backend.MASJIB.es.service;

import Backend.MASJIB.es.document.Shop;
import Backend.MASJIB.es.repository.ShopDocumentRepository;
import Backend.MASJIB.shop.repository.ShopRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopDocumentService {
    private final ShopDocumentRepository shopDocumentRepository;
    private final ShopRepository shopRepository;

    public ShopDocumentService(ShopDocumentRepository shopDocumentRepository, ShopRepository shopRepository) {
        this.shopDocumentRepository = shopDocumentRepository;
        this.shopRepository = shopRepository;
    }

    public void convertToDocument(){
        List<Backend.MASJIB.shop.entity.Shop> shops = shopRepository.findAll();

        for(Backend.MASJIB.shop.entity.Shop shop : shops){
            Shop addShop = Shop.builder()
                    .id(shop.getId())
                    .name(shop.getName())
                    .address(shop.getAddress())
                    .kind(shop.getAddress())
                    .build();
            shopDocumentRepository.save(addShop);
        }
    }

    public List<Shop> SearchShop(String keyword){
        return shopDocumentRepository.findByName(keyword);
    }
}