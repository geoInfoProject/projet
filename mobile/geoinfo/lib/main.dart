import 'package:flutter/material.dart';
import 'package:geoinfo/screens/CreateRequestScreen.dart';
import 'package:geoinfo/screens/ViewRequestsScreen.dart';
import 'package:video_player/video_player.dart';
import 'screens/authentication_screen.dart'; // Importez votre écran d'authentification
import 'screens/home_screen.dart';
import 'screens/login_screen.dart';
import 'screens/contact_screen.dart';
import 'package:geoinfo/screens/needed_documents_screen.dart';


void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'Demande 360',
      initialRoute: '/login', // Page par défaut
      routes: {
        '/signup': (context) => AuthenticationScreen(),
        '/home': (context) => HomeScreen(),
        '/login': (context) => LoginScreen(), // Ajoutez l'écran de connexion ici
        '/contact': (context) => ContactUsScreen(),
        '/create-request': (context) => CreateRequestScreen(), // New Route
        '/view-requests': (context) => ViewRequestsScreen(),
        '/needed_documents': (context) => NeededDocumentsScreen(),
      },
    );
  }
}

class HomeScreen extends StatefulWidget {
  @override
  _HomeScreenState createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  late VideoPlayerController _controller;

  @override
  void initState() {
    super.initState();
    // Initialise le contrôleur avec une vidéo locale
    _controller = VideoPlayerController.asset('assets/videos/demo.mp4')
      ..initialize().then((_) {
        setState(() {}); // Rafraîchir l'interface après l'initialisation
      });
  }

  @override
  void dispose() {
    _controller.dispose(); // Libère les ressources
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Colors.white,
        elevation: 0,
        leading: Padding(
          padding: const EdgeInsets.all(8.0),
          child: Image.asset(
            'assets/logo.png', // Chemin vers le logo
            height: 40,
            width: 40,
          ),
        ),
        actions: [
          Builder(
            builder: (context) => IconButton(
              icon: Icon(Icons.menu, color: Colors.black),
              onPressed: () {
                Scaffold.of(context).openEndDrawer();
              },
            ),
          ),
        ],
      ),
      endDrawer: Drawer(
        child: ListView(
          padding: EdgeInsets.zero,
          children: [
            DrawerHeader(
              decoration: BoxDecoration(color: Colors.purple[100]),
              child: Text(
                'Menu',
                style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
              ),
            ),
            ListTile(
              leading: Icon(Icons.home),
              title: Text('Accueil'),
              onTap: () {
                Navigator.pop(context); // Ferme le menu
                Navigator.pushReplacement(
                  context,
                  MaterialPageRoute(builder: (context) => HomeScreen()),
                ); // Recharge la même page
              },
            ),
            ListTile(
              leading: Icon(Icons.search),
              title: Text('Suivi de demande'),
              onTap: () {
                Navigator.pop(context);
                // Action pour suivi de demande
              },
            ),
            ListTile(
              leading: Icon(Icons.add),
              title: Text('Créer une demande'),
              onTap: () {
                Navigator.pushNamed(context, '/create-request');
              },
            ),
            ListTile(
              leading: Icon(Icons.add),
              title: Text('Consulter les demandes'),
              onTap: () {
                Navigator.pushNamed(context,'/view-requests');
              },
            ),
            ListTile(
              leading: Icon(Icons.contact_support),
              title: Text('Contactez nous'),
              onTap: () {
                Navigator.pop(context);
                Navigator.pushNamed(context, '/contact');
                // Action pour contacter
              },
            ),
            ListTile(
                leading: Icon(Icons.list),
                title: Text('Ce que vous avez besoin'),
                onTap: () {
                  Navigator.pop(context);
                  Navigator.pushNamed(context, '/needed_documents');
                },
            ),
          ],
        ),
      ),
      body: SingleChildScrollView(
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            SizedBox(height: 20),
            // Vidéo intégrée
            Container(
              height: 200,
              width: 300,
              color: Colors.grey[200],
              child: _controller.value.isInitialized
                  ? Stack(
                alignment: Alignment.center,
                children: [
                  AspectRatio(
                    aspectRatio: _controller.value.aspectRatio,
                    child: VideoPlayer(_controller),
                  ),
                  IconButton(
                    icon: Icon(
                      _controller.value.isPlaying
                          ? Icons.pause
                          : Icons.play_arrow,
                      size: 60,
                      color: Colors.white,
                    ),
                    onPressed: () {
                      setState(() {
                        if (_controller.value.isPlaying) {
                          _controller.pause();
                        } else {
                          _controller.play();
                        }
                      });
                    },
                  ),
                ],
              )
                  : Center(child: CircularProgressIndicator()),
            ),
            SizedBox(height: 20),
            // Texte descriptif
            Padding(
              padding: const EdgeInsets.all(16.0),
              child: Text(
                "Bienvenue sur Demande360 – Suivi et Gestion Simplifiés de Vos Demandes d’Autorisation\n\n"
                    "Demande360 est l’outil essentiel pour suivre et gérer toutes vos demandes d’autorisation en un seul endroit. "
                    "Que vous soyez en attente d’une autorisation de construction, d’un permis pour un terrain ou tout autre type de demande, "
                    "notre plateforme vous offre une visibilité complète et instantanée sur chaque étape du processus.\n\n"
                    "Avec Demande360, vous pouvez :\n"
                    "• Consulter l'état actuel de vos demandes en quelques clics\n"
                    "• Recevoir des notifications dès qu'une étape est franchie\n"
                    "• Accéder facilement aux procédures administratives nécessaires\n"
                    "• Rester en contact direct avec les services concernés pour toute question ou remarque\n\n"
                    "Grâce à notre interface intuitive et nos outils de localisation avancés, gérez vos demandes en toute simplicité, où que vous soyez.\n\n"
                    "Demande360 – Une nouvelle vision de la gestion de vos démarches.\n\n"
                    "*** Types de Demandes",
                textAlign: TextAlign.center,
                style: TextStyle(fontSize: 16),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
