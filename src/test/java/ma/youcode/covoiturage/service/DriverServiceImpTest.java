package ma.youcode.covoiturage.service;

import ma.youcode.covoiturage.dto.CreateDriverReq;
import ma.youcode.covoiturage.entity.Driver;
import ma.youcode.covoiturage.mapper.DriverMapper;
import ma.youcode.covoiturage.repository.DriverRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DriverServiceImpTest {

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private DriverMapper driverMapper;

    @InjectMocks
    private DriverServiceImp driverService;


    @Test
    void shouldCreateDriverOnlyAfterTheDataAreValid(){
        //arrange
        CreateDriverReq req = new CreateDriverReq(
                "test",
                "Test req",
                "test@test.Com",
                "+9879989998",
                "87798787"
        );
        Driver driver = new Driver();

        when(driverMapper.toEntity(req)).thenReturn(driver);
        when(driverRepository.save(driver)).thenReturn(driver);

        //act
        Driver result = driverService.createDriver(req);

        //assert
        assertThat(result).isEqualTo(driver);

        verify(driverMapper).toEntity(req);
        verify(driverRepository).save(driver);

    }


}