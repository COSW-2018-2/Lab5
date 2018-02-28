/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.cosw.samples.services;

import edu.eci.cosw.jpa.sample.model.Paciente;
import edu.eci.cosw.jpa.sample.model.PacienteId;
import edu.eci.cosw.samples.persistence.PatientsRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Desarrollo
 */
@Service
public class PatientServicesImpl implements PatientServices{
    
    @Autowired
    PatientsRepository pr;
    
    @Override
    public Paciente getPatient(int id, String tipoid) throws ServicesException {
        PacienteId Pid = new PacienteId(id, tipoid);
        if (!pr.exists(Pid)){
            throw new ServicesException("Entity not exist for it ID");
        }
        return pr.findOne(Pid);
    }

    @Override
    public List<Paciente> topPatients(int n) throws ServicesException{
        return pr.topPatients(n);
    }
    
}
