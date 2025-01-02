import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'authentication_screen.dart';

class LoginScreen extends StatelessWidget {
  final TextEditingController emailController = TextEditingController();
  final TextEditingController passwordController = TextEditingController();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Se connecter")),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          children: [
            TextField(
              controller: emailController,
              decoration: InputDecoration(labelText: "Email"),
            ),
            TextField(
              controller: passwordController,
              obscureText: true,
              decoration: InputDecoration(labelText: "Mot de passe"),
            ),
            SizedBox(height: 20),
            ElevatedButton(
                onPressed: () async {
                  SharedPreferences prefs = await SharedPreferences.getInstance();
                  String? savedEmail = prefs.getString('email');
                  String? savedPassword = prefs.getString('password');

                  if (emailController.text == savedEmail && passwordController.text == savedPassword) {
                    Navigator.pushReplacementNamed(context, '/home');
                  } else {
                    ScaffoldMessenger.of(context).showSnackBar(
                      SnackBar(content: Text("Email ou mot de passe incorrect.")),
                    );
                  }
                },

                child: Text("Se connecter"),
            ),
              SizedBox(height: 20),
              TextButton(
                onPressed: () {
                  Navigator.push(
                    context,
                    MaterialPageRoute(builder: (context) => AuthenticationScreen()),
                  );
                },
                child: Text("Créer un nouveau compte"),
            ),
          ],
        ),
      ),
    );
  }
}
