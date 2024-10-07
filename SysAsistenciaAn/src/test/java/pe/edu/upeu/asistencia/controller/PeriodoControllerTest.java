package pe.edu.upeu.asistencia.controller;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pe.edu.upeu.asistencia.controllers.PeriodoController;
import pe.edu.upeu.asistencia.dtos.PeriodoDto;
import pe.edu.upeu.asistencia.models.Periodo;
import pe.edu.upeu.asistencia.services.PeriodoServiceImp;

import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class PeriodoControllerTest {

    @Mock
    private PeriodoServiceImp periodoService;

    @InjectMocks
    private PeriodoController periodoController;

    Periodo periodo;
    List<Periodo> periodos;
    PeriodoDto.PeriodoCrearDto periodoCrearDto;
    @BeforeEach
    public void setUp() {
        periodo = Periodo.builder()
                .id(1L)
                .nombre("2024-1")
                .estado("Activo")
                .build();
        periodos = List.of(periodo,
                Periodo.builder()
                        .id(2L)
                        .nombre("2024-2")
                        .estado("Desactivo")
                        .build()
        );
    }

    @Test
    public void ListarPeriodos() {
        //given
        BDDMockito.given(periodoService.findAll()).willReturn(periodos);
        //when
        ResponseEntity<List<Periodo>> lp=periodoController.listPeriodo();
        //then
        Assertions.assertEquals(lp.getStatusCode(), HttpStatus.OK);
        for (Periodo p : lp.getBody()) {
            System.out.println(p.getNombre() + " " + p.getEstado());
        }
        Assertions.assertEquals(periodos,lp.getBody());
    }

    @Test
    public void ActualizarPeriodos(){
        //given
        BDDMockito.given(periodoService.update(BDDMockito.any(Periodo.class), BDDMockito.eq(1L)))
                .willReturn(periodo);

        //when
        ResponseEntity<Periodo> response = periodoController.updatePeriodo(1L, periodo);
        //then
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals("2024-1", response.getBody().getNombre());
        Assertions.assertEquals("Activo", response.getBody().getEstado());

        System.out.println(response.getBody().getNombre() + " " + response.getBody().getEstado());
    }

    @Test
    public void EliminarPeriodo() {
        // given
        Long periodoId = 1L;
        BDDMockito.given(periodoService.getPeriodoById(periodoId)).willReturn(periodo);
        BDDMockito.doReturn(Map.of("deleted", true)).when(periodoService).delete(periodoId);

        // when
        ResponseEntity<Map<String, Boolean>> response = periodoController.deletePeriodo(periodoId);

        // then
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertTrue(response.getBody().get("deleted"));
    }
}