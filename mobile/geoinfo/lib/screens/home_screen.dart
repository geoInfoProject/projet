import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';

class HomeScreen extends StatefulWidget {
  @override
  _HomeScreenState createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  String? nom;
  String? prenom;
  String? cin;
  String? email;

  @override
  void initState() {
    super.initState();
    _loadUserData();
  }

  // Charger les données de l'utilisateur depuis SharedPreferences
  _loadUserData() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    setState(() {
      nom = prefs.getString('nom');
      prenom = prefs.getString('prenom');
      cin = prefs.getString('cin');
      email = prefs.getString('email');
    });
  }

  // Méthode de déconnexion
  _logout() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    await prefs.clear(); // Effacer toutes les données enregistrées

    // Rediriger vers la page d'authentification
    Navigator.pushReplacementNamed(context, '/auth');
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Bienvenue"),
      ),
      drawer: Drawer(
        child: ListView(
          padding: EdgeInsets.zero,
          children: [
            DrawerHeader(
              decoration: BoxDecoration(
                color: Colors.blue,
              ),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    "Bonjour, ${nom ?? ''} ${prenom ?? ''}",
                    style: TextStyle(
                      color: Colors.white,
                      fontSize: 20,
                    ),
                  ),
                  SizedBox(height: 8),
                  Text(
                    email ?? '',
                    style: TextStyle(
                      color: Colors.white70,
                      fontSize: 14,
                    ),
                  ),
                ],
              ),
            ),
            ListTile(
              leading: Icon(Icons.home),
              title: Text("Accueil"),
              onTap: () {
                Navigator.pop(context); // Fermer le Drawer
              },
            ),
            ListTile(
              leading: Icon(Icons.logout),
              title: Text("Déconnexion"),
              onTap: () {
                _logout(); // Appeler la méthode de déconnexion
              },
            ),
          ],
        ),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text("Nom: ${nom ?? 'Non défini'}"),
            Text("Prénom: ${prenom ?? 'Non défini'}"),
            Text("CIN: ${cin ?? 'Non défini'}"),
            Text("Email: ${email ?? 'Non défini'}"),
          ],
        ),
      ),
    );
  }
}
