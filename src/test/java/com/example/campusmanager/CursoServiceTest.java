package com.example.campusmanager;



import com.example.campusmanager.domain.Curso;
import com.example.campusmanager.repository.CursoRepository;
import com.example.campusmanager.service.CursoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CursoServiceTest {

    @Mock
    private CursoRepository cursoRepository;

    @InjectMocks
    private CursoService cursoService;

    private Curso curso1;
    private Curso curso2;

    @BeforeEach
    void setUp() {
        curso1 = new Curso();
        curso1.setId(1L);
        curso1.setTitulo("Spring Boot");
        curso1.setDescripcion("Curso avanzado de Spring Boot");

        curso2 = new Curso();
        curso2.setId(2L);
        curso2.setTitulo("Java Básico");
        curso2.setDescripcion("Fundamentos del lenguaje Java");
    }

    @Test
    void testListarTodos_OK() {
        // Configuramos el mock: cuando se llame a findAll() devolverá esta lista
        when(cursoRepository.findAll()).thenReturn(Arrays.asList(curso1, curso2));

        List<Curso> resultado = cursoService.listarTodos();

        assertEquals(2, resultado.size());
        assertEquals("Spring Boot", resultado.get(0).getTitulo());
        verify(cursoRepository, times(1)).findAll(); // Verifica que se llamó una vez
    }

    @Test
    void testCrearCurso_OK() {
        // Simulamos que el repositorio guarda correctamente
        when(cursoRepository.save(any(Curso.class))).thenReturn(curso1);

        Curso creado = cursoService.crear(curso1);

        assertNotNull(creado);
        assertEquals("Spring Boot", creado.getTitulo());
        verify(cursoRepository, times(1)).save(any(Curso.class));
    }

    @Test
    void testCrearCurso_TituloVacio_KO() {
        Curso sinTitulo = new Curso();
        sinTitulo.setDescripcion("Curso sin título");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            cursoService.crear(sinTitulo);
        });

        assertEquals("El título es obligatorio.", ex.getMessage());
        verify(cursoRepository, never()).save(any(Curso.class)); // No debe guardar
    }

    @Test
    void testCrearCurso_DescripcionVacia_KO() {
        Curso sinDescripcion = new Curso();
        sinDescripcion.setTitulo("Curso sin descripción");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            cursoService.crear(sinDescripcion);
        });

        assertEquals("La descripción es obligatoria.", ex.getMessage());
        verify(cursoRepository, never()).save(any(Curso.class)); // No debe guardar
    }
}

