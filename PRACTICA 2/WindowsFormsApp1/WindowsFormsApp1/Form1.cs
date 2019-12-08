using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace WindowsFormsApp1{
    public partial class Form1 : Form{
        public Form1(){
            InitializeComponent();
        }

        private void button1_Click(object sender, EventArgs e) {
            boom.Sensor sensor = new boom.Sensor();
            if (MetodoSonda.SelectedItem != null && IDSonda.Text != ""){
                switch (MetodoSonda.SelectedItem.ToString()){
                    case "Volumen":
                        { SalidaConsulta.Text = sensor.volumen(IDSonda.Text); }
                        break;
                    case "Ultima Fecha":
                        { SalidaConsulta.Text = sensor.ultimaFecha(IDSonda.Text); }
                        break;
                    case "Led":
                        { SalidaConsulta.Text = sensor.luz(IDSonda.Text); }
                        break;
                    case "Fecha":
                        { SalidaConsulta.Text = sensor.fecha(); }
                        break;
                    default : SalidaConsulta.Text = "No existe ese método";
                    break;
                }
                if (SalidaConsulta.Text == "") { SalidaConsulta.Text = "No existe la sonda " + IDSonda.Text; }
            }
            else {
                SalidaConsulta.Text = "ID o Método vacio/invalido";
            }
        }

        private void button1_Click_1(object sender, EventArgs e){
            boom.Sensor sensor = new boom.Sensor();
            if (MetodoACambiar.SelectedItem != null && IDSonda.Text != ""){
                //setter.Value.ToString();
                switch (MetodoACambiar.SelectedItem.ToString()){
                    case "Volumen":
                        { SalidaCambio.Text = sensor.setvolumen(Int32.Parse(IDSonda.Text), setter.Value.ToString()); }
                        break;
                    case "Led":
                        { SalidaCambio.Text = sensor.setled(Int32.Parse(IDSonda.Text), setter.Value.ToString());}
                        break;
                    default:
                        SalidaCambio.Text = "No existe ese método";
                        break;
                }
                if (SalidaCambio.Text == "") { SalidaCambio.Text = "No existe la sonda " + IDSonda.Text; }
            }
            else{
                SalidaCambio.Text = "ID o Método a cambiar vacio/invalido";
            }
        }
    }
}
