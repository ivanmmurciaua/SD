namespace WindowsFormsApp1
{
    partial class Form1
    {
        /// <summary>
        /// Variable del diseñador necesaria.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Limpiar los recursos que se estén usando.
        /// </summary>
        /// <param name="disposing">true si los recursos administrados se deben desechar; false en caso contrario.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Código generado por el Diseñador de Windows Forms

        /// <summary>
        /// Método necesario para admitir el Diseñador. No se puede modificar
        /// el contenido de este método con el editor de código.
        /// </summary>
        private void InitializeComponent()
        {
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(Form1));
            this.MetodoSonda = new System.Windows.Forms.ComboBox();
            this.btnConsulta = new System.Windows.Forms.Button();
            this.SalidaConsulta = new System.Windows.Forms.RichTextBox();
            this.MetodoACambiar = new System.Windows.Forms.ComboBox();
            this.btnChange = new System.Windows.Forms.Button();
            this.SalidaCambio = new System.Windows.Forms.RichTextBox();
            this.setter = new System.Windows.Forms.NumericUpDown();
            this.idsondapuerto = new System.Windows.Forms.TextBox();
            this.btnAñadirSonda = new System.Windows.Forms.Button();
            this.sondasYPuertos = new System.Windows.Forms.RichTextBox();
            this.sondaConsulta = new System.Windows.Forms.ComboBox();
            this.sondaCambio = new System.Windows.Forms.ComboBox();
            ((System.ComponentModel.ISupportInitialize)(this.setter)).BeginInit();
            this.SuspendLayout();
            // 
            // MetodoSonda
            // 
            this.MetodoSonda.FormattingEnabled = true;
            this.MetodoSonda.Items.AddRange(new object[] {
            "Volumen",
            "Fecha",
            "Ultima Fecha",
            "Led"});
            this.MetodoSonda.Location = new System.Drawing.Point(171, 257);
            this.MetodoSonda.Name = "MetodoSonda";
            this.MetodoSonda.Size = new System.Drawing.Size(121, 21);
            this.MetodoSonda.TabIndex = 0;
            // 
            // btnConsulta
            // 
            this.btnConsulta.Location = new System.Drawing.Point(346, 248);
            this.btnConsulta.Name = "btnConsulta";
            this.btnConsulta.Size = new System.Drawing.Size(75, 36);
            this.btnConsulta.TabIndex = 2;
            this.btnConsulta.Text = "Consultar";
            this.btnConsulta.UseVisualStyleBackColor = true;
            this.btnConsulta.Click += new System.EventHandler(this.button1_Click);
            // 
            // SalidaConsulta
            // 
            this.SalidaConsulta.Location = new System.Drawing.Point(458, 225);
            this.SalidaConsulta.Name = "SalidaConsulta";
            this.SalidaConsulta.Size = new System.Drawing.Size(229, 82);
            this.SalidaConsulta.TabIndex = 4;
            this.SalidaConsulta.Text = "";
            // 
            // MetodoACambiar
            // 
            this.MetodoACambiar.Items.AddRange(new object[] {
            "Volumen",
            "Led"});
            this.MetodoACambiar.Location = new System.Drawing.Point(171, 420);
            this.MetodoACambiar.Name = "MetodoACambiar";
            this.MetodoACambiar.Size = new System.Drawing.Size(121, 21);
            this.MetodoACambiar.TabIndex = 8;
            // 
            // btnChange
            // 
            this.btnChange.Location = new System.Drawing.Point(346, 443);
            this.btnChange.Name = "btnChange";
            this.btnChange.Size = new System.Drawing.Size(75, 36);
            this.btnChange.TabIndex = 7;
            this.btnChange.Text = "Cambiar";
            this.btnChange.UseVisualStyleBackColor = true;
            this.btnChange.Click += new System.EventHandler(this.button1_Click_1);
            // 
            // SalidaCambio
            // 
            this.SalidaCambio.Location = new System.Drawing.Point(458, 429);
            this.SalidaCambio.Name = "SalidaCambio";
            this.SalidaCambio.Size = new System.Drawing.Size(229, 82);
            this.SalidaCambio.TabIndex = 9;
            this.SalidaCambio.Text = "";
            // 
            // setter
            // 
            this.setter.Location = new System.Drawing.Point(171, 482);
            this.setter.Maximum = new decimal(new int[] {
            37000,
            0,
            0,
            0});
            this.setter.Name = "setter";
            this.setter.Size = new System.Drawing.Size(120, 20);
            this.setter.TabIndex = 10;
            // 
            // idsondapuerto
            // 
            this.idsondapuerto.Location = new System.Drawing.Point(12, 28);
            this.idsondapuerto.Name = "idsondapuerto";
            this.idsondapuerto.Size = new System.Drawing.Size(280, 20);
            this.idsondapuerto.TabIndex = 11;
            // 
            // btnAñadirSonda
            // 
            this.btnAñadirSonda.Location = new System.Drawing.Point(324, 26);
            this.btnAñadirSonda.Name = "btnAñadirSonda";
            this.btnAñadirSonda.Size = new System.Drawing.Size(133, 23);
            this.btnAñadirSonda.TabIndex = 12;
            this.btnAñadirSonda.Text = "Añadir Sonda";
            this.btnAñadirSonda.UseVisualStyleBackColor = true;
            this.btnAñadirSonda.Click += new System.EventHandler(this.btnAñadirSonda_Click);
            // 
            // sondasYPuertos
            // 
            this.sondasYPuertos.Location = new System.Drawing.Point(12, 80);
            this.sondasYPuertos.Name = "sondasYPuertos";
            this.sondasYPuertos.Size = new System.Drawing.Size(675, 100);
            this.sondasYPuertos.TabIndex = 13;
            this.sondasYPuertos.Text = "";
            // 
            // sondaConsulta
            // 
            this.sondaConsulta.FormattingEnabled = true;
            this.sondaConsulta.Location = new System.Drawing.Point(12, 257);
            this.sondaConsulta.Name = "sondaConsulta";
            this.sondaConsulta.Size = new System.Drawing.Size(121, 21);
            this.sondaConsulta.TabIndex = 14;
            // 
            // sondaCambio
            // 
            this.sondaCambio.FormattingEnabled = true;
            this.sondaCambio.Location = new System.Drawing.Point(12, 420);
            this.sondaCambio.Name = "sondaCambio";
            this.sondaCambio.Size = new System.Drawing.Size(121, 21);
            this.sondaCambio.TabIndex = 15;
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(694, 523);
            this.Controls.Add(this.sondaCambio);
            this.Controls.Add(this.sondaConsulta);
            this.Controls.Add(this.sondasYPuertos);
            this.Controls.Add(this.btnAñadirSonda);
            this.Controls.Add(this.idsondapuerto);
            this.Controls.Add(this.setter);
            this.Controls.Add(this.SalidaCambio);
            this.Controls.Add(this.btnChange);
            this.Controls.Add(this.MetodoACambiar);
            this.Controls.Add(this.SalidaConsulta);
            this.Controls.Add(this.btnConsulta);
            this.Controls.Add(this.MetodoSonda);
            this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
            this.Name = "Form1";
            this.Text = " SENSORES";
            ((System.ComponentModel.ISupportInitialize)(this.setter)).EndInit();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.ComboBox MetodoSonda;
        private System.Windows.Forms.Button btnConsulta;
        private System.Windows.Forms.RichTextBox SalidaConsulta;
        private System.Windows.Forms.ComboBox MetodoACambiar;
        private System.Windows.Forms.Button btnChange;
        private System.Windows.Forms.RichTextBox SalidaCambio;
        private System.Windows.Forms.NumericUpDown setter;
        private System.Windows.Forms.TextBox idsondapuerto;
        private System.Windows.Forms.Button btnAñadirSonda;
        private System.Windows.Forms.RichTextBox sondasYPuertos;
        private System.Windows.Forms.ComboBox sondaConsulta;
        private System.Windows.Forms.ComboBox sondaCambio;
    }
}

