package ec.edu.espe.agrosmart.service;

import org.springframework.stereotype.Service;

@Service
public class AgroSmartAIService {

    public String generarPublicidad(String producto, String audiencia) {
        return "Publicidad generada para " + producto + " dirigida a " + audiencia;
    }
}