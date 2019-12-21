using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Net;
using System.Security.Cryptography;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace WindowsFormsApp1{

    /// <summary>
    /// Esta clase contiene el Form al que se accede poniendo el login correcto
    /// </summary>
    public partial class Form1 : Form {

        Form2 f;
        String usuario;
        String contrasena;
        String clave;
        String ips;
        public int nsonda = 1;

        /// <summary>
        /// Inicializar la clase
        /// </summary>
        /// <param name="f">Form padre</param>
        /// <param name="usuario">Usuario que accede a él</param>
        /// <param name="cwd">Contraseña encriptada</param>
        /// <param name="ips">IP de la cual llega la conexión</param>
        public Form1(Form2 f, String usuario, String cwd, String ips){
            InitializeComponent();
            this.f = f;
            this.usuario = usuario;
            this.contrasena = cwd;
            this.ips = ips;

            idsondapuerto.Text = "localhost:8080";

            //Leemos la clave y la guardamos
            System.IO.StreamReader fichero_clave = new System.IO.StreamReader(@".\clave.txt");
            String linea;
            while ((linea = fichero_clave.ReadLine()) != null) { this.clave = linea; }
            fichero_clave.Close();
        }

        /// <summary>
        /// Este botón se utiliza para realizar consultas
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void button1_Click(object sender, EventArgs e){

            boom.Sensor sensor = new boom.Sensor();
            //Sacamos el numero de la sonda
            String sondac = sondaConsulta.SelectedItem.ToString().Split(' ')[1];
            //Sacamos la ip a la que nos vamos a conectar y formamos la url
            String ip = sondasYPuertos.Text.Split('\n')[Int32.Parse(sondac) - 1].Split('-')[1];
            sensor.Url = "http://" + ip + "/Sensor/services/Sensor.SensorHttpSoap11Endpoint/";

            if (MetodoSonda.SelectedItem != null && sondaConsulta != null){
                switch (MetodoSonda.SelectedItem.ToString()){
                    case "Volumen":
                        {
                            String se = Encrypt(sondac);
                            String ue = Encrypt(this.usuario);
                            String ipe = Encrypt(this.ips);

                            WriteLog(Encrypt(this.ips),Encrypt(this.usuario),Encrypt("volumen"), Encrypt(sondac), false, "ENCRIPTADO -> ");
                            WriteLog(this.ips, this.usuario, "volumen", sondac, false, "SIN ENCRIPTAR -> ");

                            String contestacionse = sensor.volumen(se, ue, this.contrasena, ipe);

                            WriteLog(Encrypt(this.ips), Encrypt(this.usuario), Encrypt("volumen"), Encrypt(sondac), true, "ENCRIPTADO -> ");
                            WriteLog(this.ips, this.usuario, "volumen", sondac, true, "SIN ENCRIPTAR -> ");

                            SalidaConsulta.Text = Decrypt(contestacionse);
                        }
                        break;
                    case "Ultima Fecha":
                        {
                            String se = Encrypt(sondac);
                            String ue = Encrypt(this.usuario);
                            String ipe = Encrypt(this.ips);

                            WriteLog(ipe, ue, Encrypt("ultimaFecha"), se, false, "ENCRIPTADO -> ");
                            WriteLog(this.ips, this.usuario, "ultimaFecha", sondac, false, "SIN ENCRIPTAR -> ");

                            String contestacionse = sensor.ultimaFecha(se, ue, this.contrasena, ipe);

                            WriteLog(ipe, ue, Encrypt("ultimaFecha"), se, true, "ENCRIPTADO -> ");
                            WriteLog(this.ips, this.usuario, "ultimaFecha", sondac, true, "SIN ENCRIPTAR -> ");

                            SalidaConsulta.Text = Decrypt(contestacionse);
                        }
                        break;
                    case "Led":
                        {
                            String se = Encrypt(sondac);
                            String ue = Encrypt(this.usuario);
                            String ipe = Encrypt(this.ips);

                            WriteLog(ipe, ue, Encrypt("luz"), se, false, "ENCRIPTADO -> ");
                            WriteLog(this.ips, this.usuario, "luz", sondac, false, "SIN ENCRIPTAR -> ");

                            String contestacionse = sensor.luz(se, ue, this.contrasena, ipe);

                            WriteLog(ipe, ue, Encrypt("luz"), se, true, "ENCRIPTADO -> ");
                            WriteLog(this.ips, this.usuario, "luz", sondac, true, "SIN ENCRIPTAR -> ");

                            SalidaConsulta.Text = Decrypt(contestacionse);
                        }
                        break;
                    case "Fecha": { SalidaConsulta.Text = sensor.fecha(); }
                        break;

                    default:
                        SalidaConsulta.Text = "No existe ese método";
                        break;
                }
                if (SalidaConsulta.Text == "") { SalidaConsulta.Text = "No existe la sonda " + sondac; }
            }
            else { SalidaConsulta.Text = "ID o Método vacio/invalido"; }
        }

        /// <summary>
        /// Este botón se utilizar para setear valores
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void button1_Click_1(object sender, EventArgs e){

            boom.Sensor sensor = new boom.Sensor();
            //Sacamos el numero de la sonda
            String sondac = sondaCambio.SelectedItem.ToString().Split(' ')[1];
            //Sacamos la ip a la que nos vamos a conectar y construimos la url
            String ip = sondasYPuertos.Text.Split('\n')[Int32.Parse(sondac) - 1].Split('-')[1];
            sensor.Url = "http://" + ip + "/Sensor/services/Sensor.SensorHttpSoap11Endpoint/";

            if (MetodoACambiar.SelectedItem != null && sondaCambio != null){
                switch (MetodoACambiar.SelectedItem.ToString()){
                    case "Volumen":
                        {
                            String se = Encrypt(sondac);
                            String ue = Encrypt(this.usuario);
                            String ipe = Encrypt(this.ips);

                            WriteLog(ipe, ue, Encrypt("setvolumen"), se, false, "ENCRIPTADO -> ", setter.Value.ToString());
                            WriteLog(this.ips, this.usuario, "setvolumen", sondac, false, "SIN ENCRIPTAR -> ", setter.Value.ToString());

                            String contestacionse = sensor.setvolumen(Int32.Parse(sondac), setter.Value.ToString(), ue, this.contrasena ,ipe);

                            WriteLog(ipe, ue, Encrypt("setvolumen"), se, true, "ENCRIPTADO -> ", setter.Value.ToString());
                            WriteLog(this.ips, this.usuario, "setvolumen", sondac, true, "SIN ENCRIPTAR -> ", setter.Value.ToString());

                            SalidaCambio.Text = Decrypt(contestacionse);
                        }
                        break;
                    case "Led":
                        {
                            String se = Encrypt(sondac);
                            String ue = Encrypt(this.usuario);
                            String ipe = Encrypt(this.ips);

                            WriteLog(ipe, ue, Encrypt("setled"), se, false, "ENCRIPTADO -> ", setter.Value.ToString());
                            WriteLog(this.ips, this.usuario, "setled", sondac, false, "SIN ENCRIPTAR -> ", setter.Value.ToString());

                            String contestacionse = sensor.setled(Int32.Parse(sondac), setter.Value.ToString(), ue, this.contrasena, ipe);

                            WriteLog(ipe, ue, Encrypt("setled"), se, true, "ENCRIPTADO -> ", setter.Value.ToString());
                            WriteLog(this.ips, this.usuario, "setled", sondac, true, "SIN ENCRIPTAR -> ", setter.Value.ToString());

                            SalidaCambio.Text = Decrypt(contestacionse);
                        }
                        break;
                    default:
                        SalidaCambio.Text = "No existe ese método";
                        break;
                }
                if (SalidaCambio.Text == "") { SalidaCambio.Text = "No existe la sonda " + sondac; }
            }
            else { SalidaCambio.Text = "ID o Método a cambiar vacio/invalido"; }
        }

        /// <summary>
        /// Añadimos sondas con su IP al rich y a los combos de consulta y setters
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void btnAñadirSonda_Click(object sender, EventArgs e){
            if (!sondasYPuertos.Text.Contains(idsondapuerto.Text)){
                String chonda = "Sonda " + nsonda;
                sondasYPuertos.Text += chonda + "-" + idsondapuerto.Text + "\n";
                nsonda++;
                sondaConsulta.Items.Add(chonda);
                sondaCambio.Items.Add(chonda);
            }
        }

        /// <summary>
        /// Método para encriptar
        /// </summary>
        /// <param name="secretKey">Clave</param>
        /// <returns></returns>
        public RijndaelManaged GetRijndaelManaged(String secretKey){
            var keyBytes = new byte[16];
            var secretKeyBytes = Encoding.UTF8.GetBytes(secretKey);
            Array.Copy(secretKeyBytes, keyBytes, Math.Min(keyBytes.Length, secretKeyBytes.Length));
            return new RijndaelManaged
            {
                Mode = CipherMode.CBC,
                Padding = PaddingMode.PKCS7,
                KeySize = 128,
                BlockSize = 128,
                Key = keyBytes,
                IV = keyBytes
            };
        }

        /// <summary>
        /// Encriptamos
        /// </summary>
        /// <param name="plainBytes"></param>
        /// <param name="rijndaelManaged"></param>
        /// <returns></returns>
        public byte[] Encrypt(byte[] plainBytes, RijndaelManaged rijndaelManaged){
            return rijndaelManaged.CreateEncryptor()
                .TransformFinalBlock(plainBytes, 0, plainBytes.Length);
        }

        /// <summary>
        ///  Desencriptamos
        /// </summary>
        /// <param name="encryptedData"></param>
        /// <param name="rijndaelManaged"></param>
        /// <returns></returns>
        public byte[] Decrypt(byte[] encryptedData, RijndaelManaged rijndaelManaged){
            return rijndaelManaged.CreateDecryptor()
                .TransformFinalBlock(encryptedData, 0, encryptedData.Length);
        }
        
        /// <summary>
        /// Encriptamos texto
        /// </summary>
        /// <param name="plainText">Texto a encriptar</param>
        /// <returns></returns>
        public String Encrypt(String plainText){
            var plainBytes = Encoding.UTF8.GetBytes(plainText);
            return Convert.ToBase64String(Encrypt(plainBytes, GetRijndaelManaged(clave)));
        }

        /// <summary>
        /// Desencriptamos texto
        /// </summary>
        /// <param name="encryptedText">Texto para desencriptar</param>
        /// <returns></returns>
        public String Decrypt(String encryptedText){
            var encryptedBytes = Convert.FromBase64String(encryptedText);
            return Encoding.UTF8.GetString(Decrypt(encryptedBytes, GetRijndaelManaged(clave)));
        }

        /// <summary>
        /// Método para escribir los logs
        /// </summary>
        /// <param name="ip">IP</param>
        /// <param name="usuario">USUARIO</param>
        /// <param name="method">METODO</param>
        /// <param name="sonduy">SONDA</param>
        /// <param name="x">Booleano para saber si es set o get</param>
        /// <param name="Cad">Cadena a escribir</param>
        /// <param name="valor">En caso del set, el valor</param>
        private void WriteLog(String ip, String usuario, String method, String sonduy, Boolean x, String Cad , String valor="-1"){

            String ahora = DateTime.Now.ToString("dd/mm/yyyy h:mm:ss tt");
            Cad += ahora;

            if (x){
                if (valor == "-1"){
                    //GET RECEPCIÓN
                    System.IO.File.AppendAllText(@"./log.txt", Cad + " = Desde " + ip + " el usuario " + usuario + " recibe información sobre "+ method +" de la sonda " + sonduy + "\r\n");
                }
                else{
                    //SET RECEPCION
                    System.IO.File.AppendAllText(@"./log.txt", Cad + " = Desde " + ip + " el usuario " + usuario + " recibe el valor " + valor + " nuevo de la sonda " + sonduy + "\r\n");
                }
            }
            else{
                if(valor == "-1"){
                    //GET ENVIO
                    System.IO.File.AppendAllText(@"./log.txt", Cad + " = Desde " + ip + " el usuario " + usuario + " quiere consultar la sonda " + sonduy + "\r\n");
                }
                else{
                    //SET ENVIO
                    System.IO.File.AppendAllText(@"./log.txt", Cad + " = Desde " + ip + " el usuario " + usuario + " quiere establecer a " + valor + " la sonda "+sonduy+"\r\n");
                }

            }
        }
    }
}
