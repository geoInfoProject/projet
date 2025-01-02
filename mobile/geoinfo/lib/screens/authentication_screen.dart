import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';

class AuthenticationScreen extends StatelessWidget {
  final TextEditingController nomController = TextEditingController();
  final TextEditingController prenomController = TextEditingController();
  final TextEditingController cinController = TextEditingController();
  final TextEditingController emailController = TextEditingController();
  final TextEditingController passwordController = TextEditingController();
  final TextEditingController confirmPasswordController = TextEditingController();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: SingleChildScrollView(
        child: Padding(
          padding: const EdgeInsets.symmetric(horizontal: 24.0, vertical: 40.0),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.center,
            children: [
              // Logo
              Image.asset(
                'assets/logo.png', // Chemin vers votre logo
                height: 120,
              ),
              SizedBox(height: 10),
              Text(
                "Demande 360",
                style: TextStyle(fontSize: 24, fontWeight: FontWeight.bold),
              ),
              Text(
                "Votre autorisation, suivie de A à Z",
                style: TextStyle(fontSize: 16, color: Colors.grey),
              ),
              SizedBox(height: 30),
              // Formulaire d'inscription
              CustomTextField(controller: nomController, label: "Nom*"),
              CustomTextField(controller: prenomController, label: "Prénom*"),
              CustomTextField(controller: cinController, label: "N CIN*"),
              CustomTextField(controller: emailController, label: "Email*"),
              CustomTextField(
                controller: passwordController,
                label: "Choose password*",
                obscureText: true,
              ),
              CustomTextField(
                controller: confirmPasswordController,
                label: "Repeat password*",
                obscureText: true,
              ),
              SizedBox(height: 20),
              // Bouton "Sign Up"
              ElevatedButton(
                onPressed: () async {
                  // Vérifier si tous les champs sont remplis
                  if (nomController.text.isEmpty ||
                      prenomController.text.isEmpty ||
                      cinController.text.isEmpty ||
                      emailController.text.isEmpty ||
                      passwordController.text.isEmpty ||
                      confirmPasswordController.text.isEmpty) {
                    ScaffoldMessenger.of(context).showSnackBar(
                      SnackBar(content: Text("Tous les champs doivent être remplis.")),
                    );
                    return;
                  }

                  // Vérifier si les mots de passe correspondent
                  if (passwordController.text != confirmPasswordController.text) {
                    ScaffoldMessenger.of(context).showSnackBar(
                      SnackBar(content: Text("Les mots de passe ne correspondent pas !")),
                    );
                    return;
                  }

                  // Stocker les données dans SharedPreferences
                  SharedPreferences prefs = await SharedPreferences.getInstance();
                  await prefs.setString('nom', nomController.text);
                  await prefs.setString('prenom', prenomController.text);
                  await prefs.setString('cin', cinController.text);
                  await prefs.setString('email', emailController.text);
                  await prefs.setString('password', passwordController.text); // Enregistrer le mot de passe

                  // Rediriger vers la page d'accueil
                  Navigator.pushReplacementNamed(context, '/home');
                },

                style: ElevatedButton.styleFrom(
                  minimumSize: Size(double.infinity, 50),
                  backgroundColor: Colors.purple[200],
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(8),
                  ),
                ),
                child: Text("Sign up"),
              ),
              SizedBox(height: 20),
              // Lien pour se connecter
              GestureDetector(
                onTap: () {
                  // Rediriger vers la page de login
                  Navigator.pushReplacementNamed(context, '/login');
                },
                child: Text(
                  "Compte Existant ? Se connecter",
                  style: TextStyle(color: Colors.blue, decoration: TextDecoration.underline),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}

class CustomTextField extends StatelessWidget {
  final TextEditingController controller;
  final String label;
  final bool obscureText;

  const CustomTextField({
    required this.controller,
    required this.label,
    this.obscureText = false,
  });

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 8.0),
      child: TextField(
        controller: controller,
        obscureText: obscureText,
        decoration: InputDecoration(
          labelText: label,
          border: OutlineInputBorder(borderRadius: BorderRadius.circular(8)),
        ),
      ),
    );
  }
}
