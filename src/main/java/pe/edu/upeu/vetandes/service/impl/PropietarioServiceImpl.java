package pe.edu.upeu.vetandes.service.impl;
import pe.edu.upeu.vetandes.service.service.PropietarioService;
import pe.edu.upeu.vetandes.dto.*;
import pe.edu.upeu.vetandes.entity.*;
import pe.edu.upeu.vetandes.repository.*;
import pe.edu.upeu.vetandes.mapper.VetAndesMapper;
import pe.edu.upeu.vetandes.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.*;
import java.util.*;
import java.math.*;
import java.time.*;

@Service @RequiredArgsConstructor
public class PropietarioServiceImpl implements PropietarioService {
 private final PropietarioRepository propietarios;
 @Transactional(readOnly=true)
 public List<PropietarioResponseDTO> listar(){return propietarios.findAll().stream().map(VetAndesMapper::propietario).toList();}
}
