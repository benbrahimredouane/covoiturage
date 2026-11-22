package ma.youcode.covoiturage.service;

import ma.youcode.covoiturage.dto.CreateDriverReq;
import ma.youcode.covoiturage.entity.Driver;
import ma.youcode.covoiturage.mapper.DriverMapper;
import ma.youcode.covoiturage.repository.DriverRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DriverServiceImp implements DriverService {
    private final DriverRepository driverRepository;
    private final DriverMapper driverMapper;

    public DriverServiceImp(DriverRepository driverRepository , DriverMapper driverMapper) {
        this.driverRepository = driverRepository;
        this.driverMapper = driverMapper;
    }

    @Override
    @Transactional
    public Driver createDriver(CreateDriverReq req){
        Driver driver = driverMapper.toEntity(req);
        return driverRepository.save(driver);
    };
}
