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
    /// Clase del Form del Login
    /// </summary>
    public partial class Form2 : Form{

        /// <summary>
        /// Inicializar componente con el character de la pwd a +
        /// </summary>
        public Form2(){
            InitializeComponent();
            Contrasenya.PasswordChar = '+';
        }

        /// <summary>
        /// Nada
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void Form2_Load(object sender, EventArgs e){

        }

        /// <summary>
        /// Encriptar en SHA1
        /// </summary>
        /// <param name="str">String a encriptar</param>
        /// <returns>Cadena encriptada</returns>
        private string encryptSHA1(string str){
            SHA1 sha1 = SHA1Managed.Create();
            ASCIIEncoding encoding = new ASCIIEncoding();
            byte[] stream = null;
            StringBuilder sb = new StringBuilder();
            stream = sha1.ComputeHash(encoding.GetBytes(str));
            for (int i = 0; i < stream.Length; i++) sb.AppendFormat("{0:x2}", stream[i]);
            return sb.ToString();
        }

        /// <summary>
        /// Método para escribir en el log
        /// </summary>
        /// <param name="usu">usuario</param>
        /// <param name="pwd">pass</param>
        /// <param name="x">booleano para saber si ha sido sesión correcta o ataque de un hacker</param>
        private void WriteLog(String usu, String pwd, Boolean x){

            String ahora = DateTime.Now.ToString("dd/mm/yyyy h:mm:ss tt");

            //Obtiene la Ip local
            IPHostEntry host;
            string localIP = "";
            host = Dns.GetHostEntry(Dns.GetHostName());
            foreach (IPAddress direccion in host.AddressList){
                if (direccion.AddressFamily.ToString() == "InterNetwork"){
                    localIP = direccion.ToString();
                }
            }

            if (x){
                System.IO.File.AppendAllText(@"./log.txt", "\r\n" + ahora+" = Desde "+localIP+" "+usu+":"+pwd+"---> ACCESO CORRECTO \r\n");
            }
            else{
                System.IO.File.AppendAllText(@"./log.txt", "\r\n" + ahora + " = Desde " + localIP + " " + usu + ":" + pwd + "---> ACCESO NO AUTORIZADO \r\n");
            }

        }

        /// <summary>
        /// Botón para loguearse
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void btnLogin_Click(object sender, EventArgs e){

            String usu = Usuario.Text;
            String pwd = Contrasenya.Text;

            if(usu != "" && pwd != ""){
                try{
                    pwd = encryptSHA1(pwd);

                    //Leer fichero de datos
                    String linea = "";
                    System.IO.StreamReader file = new System.IO.StreamReader(@".\login.txt");
                    bool login = false;

                    while((linea = file.ReadLine()) != null && !login){
                        login = (linea.Split(' ')[0] == usu && linea.Split(' ')[1] == pwd);
                    }
                    file.Close();
                    if (login) {

                        //Obtiene la Ip local
                        IPHostEntry host;
                        string localIP = "";
                        host = Dns.GetHostEntry(Dns.GetHostName());
                        foreach (IPAddress direccion in host.AddressList)
                        {
                            if (direccion.AddressFamily.ToString() == "InterNetwork")
                            {
                                localIP = direccion.ToString();
                            }
                        }

                        WriteLog(usu,pwd,true);
                        Form1 f = new Form1(this, usu, pwd, localIP);
                        this.Hide();
                        f.ShowDialog();
                        this.Close();
                    }
                    else {
                        salidaLogin.Text = "Datos incorrectos";
                        WriteLog(usu,pwd,false);
                    }
                }
                catch(Exception except) { Console.WriteLine(except); salidaLogin.Text = "ERROR"; }
            }
            else { salidaLogin.Text = "Introduce los datos"; }
        }
    }
}
