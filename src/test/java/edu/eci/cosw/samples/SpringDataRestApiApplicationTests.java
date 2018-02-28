package edu.eci.cosw.samples;

import edu.eci.cosw.jpa.sample.model.Consulta;
import edu.eci.cosw.jpa.sample.model.Paciente;
import edu.eci.cosw.jpa.sample.model.PacienteId;
import edu.eci.cosw.samples.persistence.PatientsRepository;
import edu.eci.cosw.samples.services.PatientServices;
import edu.eci.cosw.samples.services.ServicesException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringDataRestApiApplication.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class SpringDataRestApiApplicationTests {

    @Autowired
    PatientServices ps; //Para hacer las consultas
    @Autowired
    PatientsRepository pr; //Para agregar datos iniciales

    @Test //Consulta a paciente que existe 
    public void getPatientExist(){
        //Registrar un paciente
        Set<Consulta> consultas = new HashSet<>();
        consultas.add(new Consulta(new Date(32), "Revision of ears"));
        Paciente expected = new Paciente(new PacienteId(1234567, "cc"),"Jhordy", new Date(1), consultas);
        pr.save(expected);
        //consultarlo a través de los servcios
        Paciente actual = null;
        try {
            actual = ps.getPatient(1234567, "cc");
        } catch (ServicesException ex) {
            Assert.fail("It should have continued");
        }
        //y rectificar que sea el mismo
        Assert.assertTrue("The expected patient and the consulted patient must be the same",actual.equals(expected));
        //finalmente eliminar para que lo creado aca no intervenga con las otras pruebas
        pr.deleteAll();
    }

    @Test //Consulta a paciente que no existe
    public void getPatientNotExist() {
        //Consultar a través de los servicios un paciente no registrado
        try{
            System.out.println(ps.getPatient(-014567, "cc"));
            Assert.fail("It should not have continued");
        }catch(ServicesException ex){
            //y esperar que se produzca el error
            Assert.assertTrue("Consulting a patient that does not exist produces an error", true);
        }
        //finalmente eliminar para que lo creado aca no intervenga con las otras pruebas
        pr.deleteAll();
    }

    @Test //No existen pacientes con N o más consultas
    public void topPatientsEmptyList(){
        //Registrar un paciente con sólo 1 consulta
        Set<Consulta> consultas = new HashSet<>();
        consultas.add(new Consulta(new Date(32), "Revision of legs"));
        Paciente paciOne = new Paciente(new PacienteId(987654, "cc"),"David", new Date(1), consultas);
        pr.save(paciOne);
        //Probar usando n=2 como parámetro 
        List<Paciente> actual = null;
        try {
            actual = ps.topPatients(2);
            System.err.println("EMPTYYYYYYYYYY"+actual);
        } catch (ServicesException ex) {
            Assert.fail("It should have continued");
        }
        //y esperar una lista vacía.
        Assert.assertTrue("The list must be empty, because there are no patients with at least N consults",actual.isEmpty());
        //finalmente eliminar para que lo creado aca no intervenga con las otras pruebas
        pr.deleteAll();
    }
    
    @Test //Existen pacientes con N o más consultas
    public void topPatientsWithElements(){
        //Registrar 3 pacientes. Uno sin consultas, otro con una, y el último con dos consultas
        
        //0
        Set<Consulta> consultas0 = new HashSet<>();
        Paciente paciente0 = new Paciente(new PacienteId(645743, "cc"),"Juan", new Date(1), consultas0);
        pr.save(paciente0);
        //1
        Set<Consulta> consultas1 = new HashSet<>();
        consultas1.add(new Consulta(new Date(32), "Revision of mouth"));
        Paciente paciente1= new Paciente(new PacienteId(345650, "cc"),"Pablo", new Date(1), consultas1);
        pr.save(paciente1);
        //2
        Set<Consulta> consultas2 = new HashSet<>();
        consultas2.add(new Consulta(new Date(32), "Revision of eyes"));
        consultas2.add(new Consulta(new Date(32), "Revision of nose"));
        Paciente paciente2 = new Paciente(new PacienteId(409127, "cc"),"Pedro", new Date(1), consultas2);
        pr.save(paciente2);
        // Probar usando n=1
        List<Paciente> actual = null;
        try {
            actual = ps.topPatients(1);
        } catch (ServicesException ex) {
            Assert.fail("It should have continued");
        }
        // y esperar una lista con los dos pacientes correspondientes.
        List<Paciente> expected = new ArrayList<>();
        expected.add(paciente1); 
        expected.add(paciente2); 
        Assert.assertTrue("The list of expected patients and the list of patients with more than N consultations should be the same",expected.equals(actual));      
        //finalmente eliminar para que lo creado aca no intervenga con las otras pruebas
        pr.deleteAll();
    }
}
