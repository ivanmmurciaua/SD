using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Security.Cryptography;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace WindowsFormsApp1{
    public partial class Form2 : Form{
        public Form2(){
            InitializeComponent();
            Contrasenya.PasswordChar = '+';
        }

        private void Form2_Load(object sender, EventArgs e){

        }

        private string encryptSHA1(string str){
            SHA1 sha1 = SHA1Managed.Create();
            ASCIIEncoding encoding = new ASCIIEncoding();
            byte[] stream = null;
            StringBuilder sb = new StringBuilder();
            stream = sha1.ComputeHash(encoding.GetBytes(str));
            for (int i = 0; i < stream.Length; i++) sb.AppendFormat("{0:x2}", stream[i]);
            return sb.ToString();
        }

        private void btnLogin_Click(object sender, EventArgs e){

            String usu = Usuario.Text;
            String pwd = Contrasenya.Text;

            if(usu != "" && pwd != ""){
                try{
                    usu = encryptSHA1(usu);
                    pwd = encryptSHA1(pwd);

                    //Leer fichero de datos
                    String linea = "";
                    System.IO.StreamReader file = new System.IO.StreamReader(@".\login.txt");
                    bool login = false;

                    while((linea = file.ReadLine()) != null){
                        login = (linea.Split(' ')[0] == usu && linea.Split(' ')[1] == pwd);
                    }
                    file.Close();
                    if (login) {
                        Form1 f = new Form1(this, Usuario.Text, usu, pwd);
                        this.Hide();
                        f.ShowDialog();
                        this.Close();
                    }
                    else {
                        salidaLogin.Text = "Datos incorrectos";
                    }
                }
                catch(Exception except) { Console.WriteLine(except); salidaLogin.Text = "ERROR"; }
            }
            else { salidaLogin.Text = "Introduce los datos"; }
        }
    }
}
