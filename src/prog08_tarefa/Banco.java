
package prog08_tarefa;

import java.io.*;
//import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Banco {
   
    private ArrayList<CuentaBancaria> cuentas;
     public Banco() {
        this.cuentas = new ArrayList<>();
    }
    
// buscaCuenta: Recibe un IBAN y busca la cuenta correspondiente.    
    
    
    public CuentaBancaria buscaCuenta(String IBAN) {
         
         for (CuentaBancaria cuenta: this.cuentas){
             if (cuenta.getIBAN().equalsIgnoreCase(IBAN)){
                 return cuenta;
             }
         }
          return null;        
        }

    //abrirCuenta: recibe por parámetro un objeto CuentaBancaria
    //y lo almacena en la estructura. 
    //Devuelve true o false indicando si la operación se realizó con éxito.  
        
    public boolean abrirCuenta(CuentaBancaria cuenta) {
        
         CuentaBancaria encontrada = this.buscaCuenta(cuenta.getIBAN());
         if (encontrada!= null){
             System.out.println("Ya existe.");
             return false;
         }
        this.cuentas.add(cuenta);
        //System.out.println("Cuenta creada");
        return true;
    }  
   
//listadoCuentas: no recibe parámetro y devuelve un array 
//donde cada elemento es una cadena que representa la información de una cuenta.
public String[] listadoCuentas() {
    
    String[] infoCuenta = new String[this.cuentas.size()];
        for (int i = 0; i < infoCuenta.length; i++) {
            infoCuenta[i] = this.cuentas.get(i).devolverInfoString();
        }
        return infoCuenta;
}
//informacionCuenta: recibe un iban por parámetro y 
//devuelve una cadena con la información de la cuenta o null si la cuenta no existe.
public String informacionCuenta(String IBAN){
    CuentaBancaria cuenta = this.buscaCuenta(IBAN);
    if (cuenta != null) {
       return cuenta.devolverInfoString();
    }
       return null;     
}

//ingresoCuenta: recibe un iban por parámetro y una cantidad
//e ingresa la cantidad en la cuenta. Devuelve true o false indicando
//si la operación se realizó con éxito.
public boolean ingresoCuenta(String IBAN, double cantidad) {

        CuentaBancaria c = this.buscaCuenta(IBAN);
        if (c != null) {
            c.setSaldo(c.getSaldo() + cantidad);
            return true;
        }
        return false;
    }
 //retiradaCuenta: recibe un iban por parámetro y una cantidad y trata de 
//retirar la cantidad de la cuenta. Devuelve true o false indicando
//si la operación se realizó con éxito.
    public boolean retiradaCuenta(String IBAN, double cantidad) {

        CuentaBancaria cuenta = this.buscaCuenta(IBAN);
        if (cuenta != null) {

            boolean puedeHacerse = false;

            if (cuenta.getSaldo() - cantidad >= 0) {
                puedeHacerse = true;
            }else if(cuenta instanceof CuentaCorrienteEmpresa){
                CuentaCorrienteEmpresa aux = (CuentaCorrienteEmpresa)cuenta;
                if(Math.abs(cuenta.getSaldo() - cantidad) < aux.getMaximoDescubierto()){
                    puedeHacerse = true;
                }
            }

            if(puedeHacerse){
                cuenta.setSaldo(cuenta.getSaldo() - cantidad);
            }

            return puedeHacerse;

        }
        return false;
    }
//obtenerSaldo: Recibe un iban por parámetro y devuelve el saldo
//de la cuenta si existe. En caso contrario devuelve -1.
    public double obtenerSaldo(String IBAN) {
        CuentaBancaria cuenta = this.buscaCuenta(IBAN);
        if (cuenta != null) {
            return cuenta.getSaldo();
        }
        return -1;
    }
//"Eliminar Cuenta Bancaria". A través de esta opción se pedirá el IBAN
//de una cuenta bancaria y se eliminará de la estructura siempre que existe y su saldo sea 0.
    //No se podrán eliminar cuentas con saldo superior a 0.
    
   

    public boolean eliminarCuenta(String IBAN){
        CuentaBancaria cuenta = this.buscaCuenta(IBAN);
        if (cuenta!= null){
            for (CuentaBancaria cuentaElim: this.cuentas){
                if (cuentaElim.getIBAN().equalsIgnoreCase(IBAN) && cuentaElim.getSaldo()==0){
                    this.cuentas.remove(cuentaElim);
                    return true;
                }
                
            }
        }
        return false;
        
    }
   // Guardar datos de cuentas en un binario
    public void guardar(){
        File fichero= new File("datoscuentasbancarias.dat");
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fichero));
            
            for (CuentaBancaria cuenta: cuentas){
                
                oos.writeObject(cuenta);
            }
            oos.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Error:"+ ex.getMessage());
        }catch (IOException ex ){
            System.out.println("Error:"+ ex.getMessage());
        
    }

    
    }
    //Carga los datos al inicio
     public void cargadatos(){
        
         File fichero= new File("datoscuentasbancarias.dat");
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fichero));
            
            while(true){
                CuentaBancaria cuenta;
                try {
                    cuenta = (CuentaBancaria)ois.readObject();
                     this.abrirCuenta(cuenta);
                } catch (ClassNotFoundException ex) {
                    System.out.println("Error1: "+ ex.getMessage());
                }
               
                
            }
        } catch (EOFException ex) {
            System.out.println("Se cargaron los datos.");
        } catch (IOException ex) {
            System.out.println("Error2: "+ex.getMessage());
        }
     }
     
     
     //Nueva opcion para listar clientes en un archivo de texto
     public void listar(){
         
         //FileWriter listado;
        try {
        FileWriter    listado = new FileWriter ("ListadoClientesIBAN.txt");
            BufferedWriter bw = new BufferedWriter(listado);
           String linea;
            for (CuentaBancaria cuenta : cuentas){
                
                linea = cuenta.getTitular().devolverInfoString()+ " :: "+ cuenta.getIBAN()+ "\r\n";
                bw.write(linea);
                        
            }
            bw.write("Número de Cuentas: "+ cuentas.size()); 
        } catch (IOException ex) {
            System.out.println("Error:" + ex.getMessage());
        }
         
         
         
     }
    
    }

 